package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.CategoryEntity
import com.example.data.model.EventEntity
import com.example.data.model.MemoryEntity
import com.example.data.repository.MinticeRepository
import com.example.data.repository.UserPreferences
import com.example.data.repository.UserPreferencesRepository
import com.example.service.AlarmScheduler
import com.example.ui.components.MinticeTab
import com.example.utils.DeviceMediaItem
import com.example.utils.MediaManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

class MinticeViewModel(
    private val repository: MinticeRepository,
    private val preferencesRepository: UserPreferencesRepository,
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {

    // Real-time clock ticker updating every second for live countdowns & states
    private val _currentTimeMillis = MutableStateFlow(System.currentTimeMillis())
    val currentTimeMillis: StateFlow<Long> = _currentTimeMillis.asStateFlow()

    private val notifiedReminderEvents = mutableSetOf<Long>()

    init {
        viewModelScope.launch {
            while (true) {
                delay(1000L)
                val now = System.currentTimeMillis()
                _currentTimeMillis.value = now

                // Real-time foreground reminder dispatcher:
                // Evaluates exact trigger seconds so notifications fire instantaneously
                val currentEvents = allEvents.value
                for (event in currentEvents) {
                    if (event.reminderEnabled && !event.isCompleted && !notifiedReminderEvents.contains(event.id)) {
                        val trigger = event.startDateTime - (event.reminderMinutesBefore * 60 * 1000L)
                        if (now in trigger..(trigger + 15000L)) {
                            notifiedReminderEvents.add(event.id)
                            alarmScheduler.sendInstantNotification(
                                title = event.title,
                                message = if (event.locationName.isNotBlank()) "Upcoming at ${event.locationName}" else "Upcoming ${event.category} reminder ♡",
                                eventId = event.id
                            )
                        }
                    }
                }
            }
        }
    }

    val allEvents: StateFlow<List<EventEntity>> = repository.getAllEvents()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allMemories: StateFlow<List<MemoryEntity>> = repository.getAllMemories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allCategories: StateFlow<List<CategoryEntity>> = repository.getAllCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userPreferences: StateFlow<UserPreferences> = preferencesRepository.userPreferencesFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserPreferences())

    private val _devicePhotos = MutableStateFlow<List<DeviceMediaItem>>(emptyList())
    val devicePhotos: StateFlow<List<DeviceMediaItem>> = _devicePhotos.asStateFlow()

    private val _currentTab = MutableStateFlow(MinticeTab.HOME)
    val currentTab: StateFlow<MinticeTab> = _currentTab.asStateFlow()

    private val _selectedCalendarDate = MutableStateFlow(System.currentTimeMillis())
    val selectedCalendarDate: StateFlow<Long> = _selectedCalendarDate.asStateFlow()

    private val _isCreateEventOpen = MutableStateFlow(false)
    val isCreateEventOpen: StateFlow<Boolean> = _isCreateEventOpen.asStateFlow()

    private val _editingEvent = MutableStateFlow<EventEntity?>(null)
    val editingEvent: StateFlow<EventEntity?> = _editingEvent.asStateFlow()

    private val _selectedEventDetail = MutableStateFlow<EventEntity?>(null)
    val selectedEventDetail: StateFlow<EventEntity?> = _selectedEventDetail.asStateFlow()

    private val _selectedMemoryDetail = MutableStateFlow<MemoryEntity?>(null)
    val selectedMemoryDetail: StateFlow<MemoryEntity?> = _selectedMemoryDetail.asStateFlow()

    private val _isSearchOpen = MutableStateFlow(false)
    val isSearchOpen: StateFlow<Boolean> = _isSearchOpen.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _memoryFilter = MutableStateFlow("All")
    val memoryFilter: StateFlow<String> = _memoryFilter.asStateFlow()

    // Derived: Next upcoming event (reacts in real-time to currentTimeMillis)
    val nextEvent: StateFlow<EventEntity?> = allEvents.combine(_currentTimeMillis) { events, now ->
        // First look for active / live event, then next upcoming
        events.firstOrNull { it.startDateTime <= now && now <= it.endDateTime && !it.isCompleted }
            ?: events.firstOrNull { it.startDateTime > now && !it.isCompleted }
            ?: events.firstOrNull { !it.isCompleted }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Derived: Upcoming events for Home screen (reacts in real-time)
    val upcomingEvents: StateFlow<List<EventEntity>> = allEvents.combine(_currentTimeMillis) { events, now ->
        events.filter { it.startDateTime >= now - (2 * 60 * 60 * 1000L) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun refreshDevicePhotos(context: android.content.Context) {
        viewModelScope.launch {
            _devicePhotos.value = MediaManager.loadDevicePhotos(context)
        }
    }

    // Derived: Events for the selected date on calendar
    val eventsForSelectedDate: StateFlow<List<EventEntity>> = combine(allEvents, _selectedCalendarDate) { events, dateMillis ->
        val cal = Calendar.getInstance().apply { timeInMillis = dateMillis }
        val targetYear = cal.get(Calendar.YEAR)
        val targetDayOfYear = cal.get(Calendar.DAY_OF_YEAR)

        events.filter { event ->
            val eventCal = Calendar.getInstance().apply { timeInMillis = event.startDateTime }
            eventCal.get(Calendar.YEAR) == targetYear && eventCal.get(Calendar.DAY_OF_YEAR) == targetDayOfYear
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectTab(tab: MinticeTab) {
        _currentTab.value = tab
    }

    fun setSelectedCalendarDate(dateMillis: Long) {
        _selectedCalendarDate.value = dateMillis
    }

    fun openCreateEvent(eventToEdit: EventEntity? = null) {
        _editingEvent.value = eventToEdit
        _isCreateEventOpen.value = true
    }

    fun closeCreateEvent() {
        _isCreateEventOpen.value = false
        _editingEvent.value = null
    }

    fun openEventDetail(event: EventEntity) {
        _selectedEventDetail.value = event
    }

    fun closeEventDetail() {
        _selectedEventDetail.value = null
    }

    fun openMemoryDetail(memory: MemoryEntity) {
        _selectedMemoryDetail.value = memory
    }

    fun closeMemoryDetail() {
        _selectedMemoryDetail.value = null
    }

    fun openSearch() {
        _isSearchOpen.value = true
    }

    fun closeSearch() {
        _isSearchOpen.value = false
        _searchQuery.value = ""
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setMemoryFilter(filter: String) {
        _memoryFilter.value = filter
    }

    fun saveEvent(event: EventEntity) {
        viewModelScope.launch {
            val savedId = repository.insertEvent(event)
            val updatedEvent = if (event.id == 0L) event.copy(id = savedId) else event
            if (updatedEvent.reminderEnabled) {
                alarmScheduler.scheduleReminder(updatedEvent)
            } else {
                alarmScheduler.cancelReminder(updatedEvent.id)
            }
            _isCreateEventOpen.value = false
            _editingEvent.value = null
            // Also update selectedEventDetail if currently viewing it
            if (_selectedEventDetail.value?.id == updatedEvent.id) {
                _selectedEventDetail.value = updatedEvent
            }
        }
    }

    fun deleteEvent(event: EventEntity) {
        viewModelScope.launch {
            alarmScheduler.cancelReminder(event.id)
            repository.deleteEvent(event)
            if (_selectedEventDetail.value?.id == event.id) {
                _selectedEventDetail.value = null
            }
        }
    }

    fun toggleEventComplete(event: EventEntity, isCompleted: Boolean) {
        viewModelScope.launch {
            val updated = event.copy(isCompleted = isCompleted)
            repository.updateEvent(updated)
            if (_selectedEventDetail.value?.id == event.id) {
                _selectedEventDetail.value = updated
            }
        }
    }

    fun saveMemory(memory: MemoryEntity) {
        viewModelScope.launch {
            repository.insertMemory(memory)
        }
    }

    fun deleteMemory(memory: MemoryEntity) {
        viewModelScope.launch {
            repository.deleteMemory(memory)
            if (_selectedMemoryDetail.value?.id == memory.id) {
                _selectedMemoryDetail.value = null
            }
        }
    }

    fun toggleMemoryFavorite(memory: MemoryEntity) {
        viewModelScope.launch {
            val updated = memory.copy(isFavorite = !memory.isFavorite)
            repository.updateMemory(updated)
            if (_selectedMemoryDetail.value?.id == memory.id) {
                _selectedMemoryDetail.value = updated
            }
        }
    }

    fun setTheme(themeName: String) {
        viewModelScope.launch {
            preferencesRepository.setTheme(themeName)
        }
    }

    fun setCompactMode(enabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setCompactMode(enabled)
        }
    }

    fun set24HourFormat(enabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.set24HourFormat(enabled)
        }
    }

    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setDarkMode(enabled)
        }
    }

    fun setThemeMode(mode: String) {
        viewModelScope.launch {
            preferencesRepository.setThemeMode(mode)
        }
    }

    fun sendInstantTestNotification() {
        alarmScheduler.sendInstantNotification(
            title = "Mintice Real-Time Alert",
            message = "Your notification system is working in real time! ♡"
        )
    }

    fun scheduleAlarmTest(seconds: Int = 5) {
        alarmScheduler.scheduleImmediateTest(seconds)
    }

    fun triggerEventReminderNow(event: EventEntity) {
        alarmScheduler.sendInstantNotification(
            title = event.title,
            message = if (event.locationName.isNotBlank()) "Happening at ${event.locationName}" else "Event starting soon ♡",
            eventId = event.id
        )
    }

    fun setMinimalDecorations(enabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setMinimalDecorations(enabled)
        }
    }

    suspend fun exportJson(): String {
        return repository.exportToJson(allEvents.value, allMemories.value)
    }

    suspend fun importJson(json: String): Boolean {
        return repository.importFromJson(json)
    }
}

class MinticeViewModelFactory(
    private val repository: MinticeRepository,
    private val preferencesRepository: UserPreferencesRepository,
    private val alarmScheduler: AlarmScheduler
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MinticeViewModel::class.java)) {
            return MinticeViewModel(repository, preferencesRepository, alarmScheduler) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

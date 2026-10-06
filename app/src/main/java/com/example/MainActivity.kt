package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ui.components.MinticeBottomBar
import com.example.ui.components.MinticeTab
import com.example.ui.screens.CalendarScreen
import com.example.ui.screens.CreateEventScreen
import com.example.ui.screens.EventDetailScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MemoriesScreen
import com.example.ui.screens.MemoryDetailScreen
import com.example.ui.screens.MoreSettingsScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.theme.MinticeTheme
import com.example.ui.viewmodel.MinticeViewModel
import com.example.ui.viewmodel.MinticeViewModelFactory

class MainActivity : ComponentActivity() {

    private val viewModel: MinticeViewModel by viewModels {
        val app = application as MinticeApplication
        MinticeViewModelFactory(
            app.repository,
            app.preferencesRepository,
            app.alarmScheduler
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Handle open from notification
        val openEventId = intent.getLongExtra("OPEN_EVENT_ID", -1L)
        if (openEventId > 0) {
            val app = application as MinticeApplication
            lifecycleScope.launch {
                val event = app.repository.getEventByIdSync(openEventId)
                if (event != null) {
                    viewModel.openEventDetail(event)
                }
            }
        }

        // Auto-request notifications permission on Android 13+ if not yet granted
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            val permissionCheck = androidx.core.content.ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            )
            if (permissionCheck != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                val launcher = registerForActivityResult(
                    androidx.activity.result.contract.ActivityResultContracts.RequestPermission()
                ) { isGranted ->
                    // Notification permission result handled
                }
                launcher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        setContent {
            val preferences by viewModel.userPreferences.collectAsState()
            val isSystemDark = androidx.compose.foundation.isSystemInDarkTheme()
            val isEffectiveDark = when (preferences.themeMode) {
                "DARK" -> true
                "LIGHT" -> false
                "SYSTEM" -> isSystemDark
                else -> preferences.isDarkMode
            }

            MinticeTheme(
                themeName = preferences.themeName,
                isDarkMode = isEffectiveDark
            ) {
                androidx.compose.runtime.CompositionLocalProvider(
                    com.example.ui.theme.LocalCompactMode provides preferences.isCompactMode
                ) {
                    MinticeAppRoot(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun MinticeAppRoot(viewModel: MinticeViewModel) {
    val currentTab by viewModel.currentTab.collectAsState()
    val isCreateEventOpen by viewModel.isCreateEventOpen.collectAsState()
    val editingEvent by viewModel.editingEvent.collectAsState()
    val selectedEventDetail by viewModel.selectedEventDetail.collectAsState()
    val selectedMemoryDetail by viewModel.selectedMemoryDetail.collectAsState()
    val isSearchOpen by viewModel.isSearchOpen.collectAsState()

    // Handle back button for sub-screens
    BackHandler(
        enabled = isCreateEventOpen || selectedEventDetail != null || selectedMemoryDetail != null || isSearchOpen || currentTab != MinticeTab.HOME
    ) {
        when {
            isCreateEventOpen -> viewModel.closeCreateEvent()
            selectedEventDetail != null -> viewModel.closeEventDetail()
            selectedMemoryDetail != null -> viewModel.closeMemoryDetail()
            isSearchOpen -> viewModel.closeSearch()
            currentTab != MinticeTab.HOME -> viewModel.selectTab(MinticeTab.HOME)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            // Hide bottom bar when in CreateEvent, EventDetail, MemoryDetail, or Search
            if (!isCreateEventOpen && selectedEventDetail == null && selectedMemoryDetail == null && !isSearchOpen) {
                MinticeBottomBar(
                    currentTab = currentTab,
                    onTabSelected = { viewModel.selectTab(it) },
                    onFabClick = { viewModel.openCreateEvent(null) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Primary Tabs
            when (currentTab) {
                MinticeTab.HOME -> HomeScreen(
                    viewModel = viewModel,
                    onNavigateTab = { viewModel.selectTab(it) }
                )
                MinticeTab.CALENDAR -> CalendarScreen(
                    viewModel = viewModel
                )
                MinticeTab.MEMORIES -> MemoriesScreen(
                    viewModel = viewModel
                )
                MinticeTab.MORE -> MoreSettingsScreen(
                    viewModel = viewModel
                )
            }

            // Create / Edit Event Overlay (Screen 3)
            AnimatedVisibility(
                visible = isCreateEventOpen,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                CreateEventScreen(
                    viewModel = viewModel,
                    eventToEdit = editingEvent,
                    onClose = { viewModel.closeCreateEvent() }
                )
            }

            // Event Detail Overlay
            AnimatedVisibility(
                visible = selectedEventDetail != null && !isCreateEventOpen,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                selectedEventDetail?.let { event ->
                    EventDetailScreen(
                        event = event,
                        viewModel = viewModel,
                        onBack = { viewModel.closeEventDetail() }
                    )
                }
            }

            // Memory Detail Overlay
            AnimatedVisibility(
                visible = selectedMemoryDetail != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                selectedMemoryDetail?.let { memory ->
                    MemoryDetailScreen(
                        memory = memory,
                        viewModel = viewModel,
                        onBack = { viewModel.closeMemoryDetail() }
                    )
                }
            }

            // Search Overlay
            AnimatedVisibility(
                visible = isSearchOpen,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                SearchScreen(
                    viewModel = viewModel,
                    onBack = { viewModel.closeSearch() }
                )
            }
        }
    }
}

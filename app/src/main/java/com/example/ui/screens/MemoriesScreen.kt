package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MemoryEntity
import com.example.ui.components.PolaroidCard
import com.example.ui.theme.LocalCompactMode
import com.example.ui.theme.ScriptAccentStyle
import com.example.ui.theme.WashiTapeMint
import com.example.ui.theme.WashiTapePink
import com.example.ui.viewmodel.MinticeViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MemoriesScreen(
    viewModel: MinticeViewModel,
    modifier: Modifier = Modifier
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val allMemories by viewModel.allMemories.collectAsState()
    val activeFilter by viewModel.memoryFilter.collectAsState()
    val devicePhotos by viewModel.devicePhotos.collectAsState()
    val isCompact = LocalCompactMode.current

    var showAddDialog by remember { mutableStateOf(false) }
    var selectedPhotoUri by remember { mutableStateOf<String?>(null) }
    var memoryCaption by remember { mutableStateOf("") }
    var memoryLocation by remember { mutableStateOf("") }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedPhotoUri = uri.toString()
            showAddDialog = true
        }
    }

    val galleryPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        val granted = perms.values.any { it }
        if (granted) {
            viewModel.refreshDevicePhotos(context)
        }
    }

    androidx.compose.runtime.LaunchedEffect(Unit) {
        if (com.example.utils.MediaManager.hasGalleryPermission(context)) {
            viewModel.refreshDevicePhotos(context)
        }
    }

    // Filter memories
    val filteredMemories = when (activeFilter) {
        "Events" -> allMemories.filter { it.eventId != null }
        "Places" -> allMemories.filter { it.location.isNotBlank() }
        "People" -> allMemories.filter { it.people.isNotBlank() }
        "Favorites" -> allMemories.filter { it.isFavorite }
        else -> allMemories
    }

    // Group memories by Month and Year
    val monthYearFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
    val groupedMemories = filteredMemories.groupBy { memory ->
        monthYearFormat.format(Date(memory.createdAt))
    }

    val filterOptions = listOf("All", "Events", "Places", "People", "Favorites")
    val cardRadius = if (isCompact) 16.dp else 20.dp
    val sectionSpacing = if (isCompact) 10.dp else 18.dp

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = if (isCompact) PaddingValues(horizontal = 14.dp, vertical = 10.dp) else PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(sectionSpacing)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.PhotoLibrary,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(if (isCompact) 22.dp else 26.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Memories",
                        style = if (isCompact) MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                                else MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = (-0.5).sp
                                ),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { viewModel.openSearch() },
                        modifier = Modifier.size(if (isCompact) 34.dp else 38.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    IconButton(
                        onClick = {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        modifier = Modifier.size(if (isCompact) 34.dp else 38.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddPhotoAlternate,
                            contentDescription = "Add Memory",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        // Filter Pills Row
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filterOptions) { filter ->
                    val isSelected = activeFilter == filter
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                            )
                            .border(
                                1.dp,
                                if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                                RoundedCornerShape(16.dp)
                            )
                            .clickable { viewModel.setMemoryFilter(filter) }
                            .padding(horizontal = if (isCompact) 12.dp else 16.dp, vertical = if (isCompact) 6.dp else 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = filter,
                            fontSize = if (isCompact) 12.sp else 13.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // Real Device Gallery Strip (When permission granted)
        val hasGallery = com.example.utils.MediaManager.hasGalleryPermission(context)
        if (hasGallery && devicePhotos.isNotEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(cardRadius))
                        .background(MaterialTheme.colorScheme.surface)
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(cardRadius))
                        .padding(if (isCompact) 10.dp else 14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "From Your Device Gallery",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${devicePhotos.size} photos",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(devicePhotos) { photoItem ->
                            val thumbSize = if (isCompact) 56.dp else 70.dp
                            Box(
                                modifier = Modifier
                                    .size(thumbSize)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable {
                                        selectedPhotoUri = photoItem.uri.toString()
                                        showAddDialog = true
                                    }
                            ) {
                                coil.compose.AsyncImage(
                                    model = photoItem.uri,
                                    contentDescription = photoItem.name,
                                    contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }
                }
            }
        } else if (!hasGallery) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
                        .clickable {
                            galleryPermissionLauncher.launch(
                                com.example.utils.MediaManager.getRequiredGalleryPermissions()
                            )
                        }
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Full Gallery Access",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Tap to grant permission and browse your device photos here.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = "Allow",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // Empty State or Grouped Memories
        if (filteredMemories.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(cardRadius))
                        .background(MaterialTheme.colorScheme.surface)
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(cardRadius))
                        .padding(if (isCompact) 20.dp else 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        PolaroidCard(
                            photoUri = null,
                            caption = "Your memories",
                            rotationDegrees = -2f,
                            showTape = true,
                            tapeColor = WashiTapePink,
                            modifier = Modifier.width(if (isCompact) 90.dp else 110.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "No memories saved yet",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Tap below to import a photo from your camera roll and preserve your lovely moments.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text("+ Add your first photo", fontSize = 13.sp)
                        }
                    }
                }
            }
        } else {
            // Render each month grouping with Polaroid Grid
            val columns = if (isCompact) 3 else 3
            for ((monthYear, memories) in groupedMemories) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = monthYear,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${memories.size} moments",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                val rows = memories.chunked(columns)
                for (row in rows) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(if (isCompact) 8.dp else 10.dp)
                        ) {
                            for (memory in row) {
                                Box(modifier = Modifier.weight(1f)) {
                                    PolaroidCard(
                                        photoUri = memory.photoUri,
                                        caption = memory.title.ifBlank { null },
                                        rotationDegrees = ((memory.id % 5) - 2).toFloat(),
                                        showTape = true,
                                        tapeColor = if (memory.id % 2L == 0L) WashiTapeMint else WashiTapePink,
                                        outerPadding = if (isCompact) 4.dp else 5.dp,
                                        bottomChinHeight = if (isCompact) 8.dp else 12.dp,
                                        onClick = {
                                            viewModel.openMemoryDetail(memory)
                                        }
                                    )
                                }
                            }
                            for (i in 0 until (columns - row.size)) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(60.dp))
        }
    }

    // Add Memory Dialog
    if (showAddDialog && selectedPhotoUri != null) {
        AlertDialog(
            onDismissRequest = {
                showAddDialog = false
                selectedPhotoUri = null
            },
            title = {
                Text(
                    text = "Save Memory",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    PolaroidCard(
                        photoUri = selectedPhotoUri,
                        caption = memoryCaption.ifBlank { "New Memory" },
                        showTape = true,
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .align(Alignment.CenterHorizontally)
                    )

                    OutlinedTextField(
                        value = memoryCaption,
                        onValueChange = { memoryCaption = it },
                        label = { Text("Caption / Title") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = memoryLocation,
                        onValueChange = { memoryLocation = it },
                        label = { Text("Location / Tag (Optional)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val memory = MemoryEntity(
                            title = memoryCaption.trim(),
                            photoUri = selectedPhotoUri ?: "",
                            location = memoryLocation.trim()
                        )
                        viewModel.saveMemory(memory)
                        showAddDialog = false
                        selectedPhotoUri = null
                        memoryCaption = ""
                        memoryLocation = ""
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showAddDialog = false
                        selectedPhotoUri = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

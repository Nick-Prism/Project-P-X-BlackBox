package com.example.collegeeventplanner.ui.screens.organizer

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.collegeeventplanner.ui.components.FormDateTimePicker
import com.example.collegeeventplanner.ui.components.FormTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEventScreen(
    navController: NavController,
    viewModel: EditEventViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadEventData()
    }

    LaunchedEffect(state.isEventUpdated) {
        if (state.isEventUpdated) {
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Event") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            FormTextField(
                value = state.title,
                onValueChange = viewModel::onTitleChanged,
                label = "Event Title",
                placeholder = "Enter event title",
                isError = state.titleError != null,
                errorMessage = state.titleError
            )

            Spacer(modifier = Modifier.height(16.dp))

            FormTextField(
                value = state.description,
                onValueChange = viewModel::onDescriptionChanged,
                label = "Description",
                placeholder = "Enter event description",
                isError = state.descriptionError != null,
                errorMessage = state.descriptionError,
                singleLine = false,
                maxLines = 4
            )

            Spacer(modifier = Modifier.height(16.dp))

            FormDateTimePicker(
                dateLabel = "Event Date",
                timeLabel = "Event Time",
                selectedDate = state.date,
                selectedTime = state.time,
                onDateSelected = viewModel::onDateChanged,
                onTimeSelected = viewModel::onTimeChanged
            )

            Spacer(modifier = Modifier.height(16.dp))

            FormTextField(
                value = state.location,
                onValueChange = viewModel::onLocationChanged,
                label = "Location",
                placeholder = "Enter event location",
                isError = state.locationError != null,
                errorMessage = state.locationError
            )

            Spacer(modifier = Modifier.height(16.dp))

            FormTextField(
                value = state.maxParticipants?.toString() ?: "",
                onValueChange = { viewModel.onMaxParticipantsChanged(it.toIntOrNull()) },
                label = "Max Participants (optional)",
                placeholder = "Enter maximum number of participants"
            )

            Spacer(modifier = Modifier.height(16.dp))

            FormTextField(
                value = state.registrationDeadline ?: "",
                onValueChange = viewModel::onRegistrationDeadlineChanged,
                label = "Registration Deadline (optional)",
                placeholder = "YYYY-MM-DD"
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.updateEvent() },
                modifier = Modifier.fillMaxWidth(),
                enabled = state.isFormValid && !state.isLoading
            ) {
                Text(if (state.isLoading) "Updating..." else "Update Event")
            }

            if (state.error != null) {
                Text(
                    text = state.error!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
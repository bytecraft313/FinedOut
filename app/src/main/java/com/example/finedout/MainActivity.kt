package com.example.finedout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.finedout.ui.theme.FinedOutTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FinedOutTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FineTrackerApp()
                }
            }
        }
    }
}

data class Person(
    val name: String,
    val fine: Int
)

@Composable
fun FineTrackerApp() {
    var peopleList by remember { mutableStateOf(listOf<Person>()) }
    var showAddFriend by remember { mutableStateOf(false) }
    var nameInput by remember { mutableStateOf("") }

    val totalFine = peopleList.sumOf { it.fine }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        // Title Bar
        Text(
            text = "Monthly Fine Tracker (July 2025)",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Divider()

        Spacer(modifier = Modifier.height(8.dp))

        // List of Friends
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            peopleList.forEachIndexed { index, person ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(person.name, style = MaterialTheme.typography.bodyLarge)

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("${person.fine} AFN", style = MaterialTheme.typography.bodyLarge)
                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                val updatedFine = (person.fine - 5).coerceAtLeast(0)
                                peopleList = peopleList.toMutableList().also {
                                    it[index] = person.copy(fine = updatedFine)
                                }
                            },
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text("-")
                        }

                        Spacer(modifier = Modifier.width(4.dp))

                        Button(
                            onClick = {
                                val updatedFine = person.fine + 5
                                peopleList = peopleList.toMutableList().also {
                                    it[index] = person.copy(fine = updatedFine)
                                }
                            },
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text("+")
                        }
                    }
                }
            }
        }

        Divider()

        Spacer(modifier = Modifier.height(12.dp))

        // Total Fine
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Total:", style = MaterialTheme.typography.titleMedium)
            Text("$totalFine AFN", style = MaterialTheme.typography.titleMedium)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Add Friend Input (conditionally visible)
        if (showAddFriend) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = nameInput,
                    onValueChange = { nameInput = it },
                    label = { Text("Friend Name") },
                    modifier = Modifier.weight(1f)
                )

                Button(
                    onClick = {
                        if (nameInput.isNotBlank()) {
                            peopleList = peopleList + Person(nameInput.trim(), 0)
                            nameInput = ""
                            showAddFriend = false
                        }
                    }
                ) {
                    Text("Add")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Bottom Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = { showAddFriend = !showAddFriend },
                modifier = Modifier.weight(1f)
            ) {
                Text("+ Add Friend")
            }

            Button(
                onClick = {
                    // TODO: Implement View History functionality
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("View History")
            }
        }
    }
}




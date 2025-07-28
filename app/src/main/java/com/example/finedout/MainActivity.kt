package com.example.finedout

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.finedout.ui.theme.FinedOutTheme
import com.example.finedout.util.DateUtils
import com.example.finedout.util.PreferencesHelper


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FinedOutTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FineTrackerApp(context = this)
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
fun FineTrackerApp(context: Context = LocalContext.current) {
    var peopleList by remember { mutableStateOf(listOf<Person>()) }

    var fineStepAmount by remember { mutableStateOf(5) }
    var fineStepInput by remember { mutableStateOf("5") }

    LaunchedEffect(Unit) {
        peopleList = PreferencesHelper.loadPeople(context)
    }

    LaunchedEffect(peopleList) {
        PreferencesHelper.savePeople(context, peopleList)
    }

    var showAddFriend by remember { mutableStateOf(false) }
    var nameInput by remember { mutableStateOf("") }

    val totalFine = peopleList.sumOf { it.fine }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        // Title
        Text(
            text = "Monthly Fine Tracker (${DateUtils.getCurrentMonthYear()})",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Fine Step Input
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = fineStepInput,
                onValueChange = {
                    fineStepInput = it
                    it.toIntOrNull()?.let { value ->
                        fineStepAmount = value
                    }
                },
                label = { Text("Fine Step Amount") },
                modifier = Modifier.weight(1f)
            )
            Text("AFN")
        }

        Divider()
        Spacer(modifier = Modifier.height(8.dp))

        // Friend List
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
                                val updatedFine = (person.fine - fineStepAmount).coerceAtLeast(0)
                                peopleList = peopleList.toMutableList().also {
                                    it[index] = person.copy(fine = updatedFine)
                                }
                            },
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text("-")
                        }

                        Spacer(modifier = Modifier.width(4.dp))

                        Button(
                            onClick = {
                                val updatedFine = person.fine + fineStepAmount
                                peopleList = peopleList.toMutableList().also {
                                    it[index] = person.copy(fine = updatedFine)
                                }
                            },
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text("+")
                        }

                        Spacer(modifier = Modifier.width(4.dp))

                        IconButton(onClick = {
                            peopleList = peopleList.toMutableList().also {
                                it.removeAt(index)
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Remove Friend"
                            )
                        }
                    }
                }
            }
        }

        Divider()
        Spacer(modifier = Modifier.height(12.dp))

        // Total Fine Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Total:", style = MaterialTheme.typography.titleMedium)
            Text("$totalFine AFN", style = MaterialTheme.typography.titleMedium)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Add Friend Input
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
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
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
                        // TODO: View history implementation
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("View History")
                }
            }

            Button(
                onClick = {
                    peopleList = peopleList.map { it.copy(fine = 0) }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                ),
                modifier = Modifier
                    .height(40.dp)
                    .padding(start = 8.dp)
            ) {
                Text("Reset Fines")
            }
        }
    }
}




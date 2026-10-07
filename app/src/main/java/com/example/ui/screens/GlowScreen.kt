package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.SkincareStepEntity
import com.example.ui.components.CheckListItem
import com.example.ui.components.SectionHeader

@Composable
fun GlowScreen(
    skincareSteps: List<SkincareStepEntity>,
    onToggleStep: (SkincareStepEntity) -> Unit,
    onAddStep: (period: String, title: String) -> Unit,
    onDeleteStep: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val morningSteps = skincareSteps.filter { it.period == "MORNING" }
    val eveningSteps = skincareSteps.filter { it.period == "EVENING" }

    var addDialogPeriod by rememberSaveable { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("glow_screen_list"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header & Subtitle
        item {
            Column {
                Text(
                    text = "Glow ✨",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Simple habits. Consistent care.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Visual Banner
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.img_glow_banner_1791381884382),
                        contentDescription = "Calm skincare shelf illustration",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Surface(
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.88f),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(14.dp)
                    ) {
                        val completedTotal = skincareSteps.count { it.completedToday }
                        Text(
                            text = "✨ $completedTotal of ${skincareSteps.size} self-care steps checked today",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        // Morning Routine Section
        item {
            Spacer(modifier = Modifier.height(4.dp))
            SectionHeader(
                title = "MORNING ROUTINE",
                subtitle = "${morningSteps.count { it.completedToday }} of ${morningSteps.size} completed",
                actionText = "+ Add step",
                onActionClick = { addDialogPeriod = "MORNING" }
            )
        }

        items(morningSteps, key = { it.id }) { step ->
            CheckListItem(
                step = step,
                onToggle = { onToggleStep(step) },
                onDelete = if (morningSteps.size > 2) ({ onDeleteStep(step.id) }) else null
            )
        }

        // Evening Routine Section
        item {
            Spacer(modifier = Modifier.height(10.dp))
            SectionHeader(
                title = "EVENING ROUTINE",
                subtitle = "${eveningSteps.count { it.completedToday }} of ${eveningSteps.size} completed",
                actionText = "+ Add step",
                onActionClick = { addDialogPeriod = "EVENING" }
            )
        }

        items(eveningSteps, key = { it.id }) { step ->
            CheckListItem(
                step = step,
                onToggle = { onToggleStep(step) },
                onDelete = if (eveningSteps.size > 2) ({ onDeleteStep(step.id) }) else null
            )
        }

        // Customize Checklist Button
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = { addDialogPeriod = "MORNING" },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("add_morning_glow_step")
                ) {
                    Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Morning Step")
                }
                OutlinedButton(
                    onClick = { addDialogPeriod = "EVENING" },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("add_evening_glow_step")
                ) {
                    Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Evening Step")
                }
            }
        }

        // Gentle Note
        item {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "🌸 Gentle Care Note",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Skincare and grooming are about comfort, hygiene, and feeling fresh in your own skin—never perfection. Keep it gentle and simple.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.9f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }

    if (addDialogPeriod != null) {
        var newStepTitle by rememberSaveable { mutableStateOf("") }
        val periodName = if (addDialogPeriod == "MORNING") "Morning" else "Evening"

        AlertDialog(
            onDismissRequest = { addDialogPeriod = null },
            title = { Text("Add $periodName Step") },
            text = {
                OutlinedTextField(
                    value = newStepTitle,
                    onValueChange = { newStepTitle = it },
                    label = { Text("Step name (e.g., Lip balm)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newStepTitle.isNotBlank()) {
                            onAddStep(addDialogPeriod!!, newStepTitle)
                            addDialogPeriod = null
                        }
                    }
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = { addDialogPeriod = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

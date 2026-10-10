package com.noor.dua.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.noor.dua.data.Dua
import com.noor.dua.data.DuaRepository

@Composable
fun CategoriesScreen(
    topInset: Dp,
    bottomSpace: Dp,
    favorites: Set<String>,
    onOpenCategory: (String) -> Unit,
    onOpenDua: (String) -> Unit,
) {
    var query by remember { mutableStateOf("") }
    val results = remember(query) { DuaRepository.search(query) }

    LazyColumn(
        contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = topInset + 14.dp, bottom = bottomSpace),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            SectionHeader("সব দুআ", "যেকোনো দুআ লিখে খুঁজুন — বাংলা, উচ্চারণ বা রেফারেন্স দিয়ে")
        }

        item {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("দুআ খুঁজুন… যেমন: ঘুম, ঋণ, শিফা", style = MaterialTheme.typography.bodyMedium) },
                leadingIcon = { Icon(Icons.Filled.Search, null, tint = MaterialTheme.colorScheme.primary) },
                trailingIcon = {
                    AnimatedVisibility(visible = query.isNotEmpty(), enter = fadeIn(), exit = fadeOut()) {
                        IconButton(onClick = { query = "" }) {
                            Icon(Icons.Filled.Close, "মুছুন", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.75f),
                ),
            )
        }

        if (query.isBlank()) {
            item {
                CategoryRows(DuaRepository.categories, onOpenCategory = onOpenCategory)
            }
        } else {
            item {
                Text(
                    "“$query” — ${results.size} টি ফলাফল",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            if (results.isEmpty()) {
                item {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 30.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Filled.Search,
                                null,
                                Modifier.size(34.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Spacer(Modifier.height(10.dp))
                            Text("কিছু পাওয়া যায়নি", style = MaterialTheme.typography.titleSmall)
                            Text(
                                "অন্য শব্দ দিয়ে খুঁজে দেখুন",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            } else {
                items(results, key = { it.id }) { dua ->
                    Staggered(index = results.indexOf(dua)) {
                        DuaListRow(dua = dua, isFavorite = favorites.contains(dua.id), onClick = { onOpenDua(dua.id) })
                    }
                }
            }
        }
    }
}

/* ----------------------------------------------------------- ক্যাটাগরি ডিটেইল স্ক্রিন */

@Composable
fun CategoryDetailScreen(
    catId: String,
    topInset: Dp,
    bottomSpace: Dp,
    favorites: Set<String>,
    onBack: () -> Unit,
    onOpenDua: (String) -> Unit,
) {
    val cat = DuaRepository.category(catId)
    val list = remember(catId) { DuaRepository.byCategory(catId) }

    LazyColumn(
        contentPadding = PaddingValues(
            start = 18.dp, end = 18.dp, top = topInset + 8.dp, bottom = bottomSpace,
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            ScreenHeader(
                title = cat?.title ?: "দুআ",
                subtitle = cat?.subtitle,
                topInset = 0.dp,
                onBack = onBack,
            )
        }

        item {
            InfoNote("এই ক্যাটাগরির ${list.size}টি দুআ — প্রতিটির সাথে আরবি, উচ্চারণ, অর্থ, ফজিলত এবং কুরআন/হাদিসের রেফারেন্স দেওয়া আছে।")
        }

        items(list, key = { it.id }) { dua ->
            Staggered(index = list.indexOf(dua)) {
                DuaListRow(dua = dua, isFavorite = favorites.contains(dua.id), onClick = { onOpenDua(dua.id) })
            }
        }
    }
}

/* ------------------------------------------------------------------ তালিকার সারি */

@Composable
fun DuaListRow(dua: Dua, isFavorite: Boolean, onClick: () -> Unit) {
    PressableCard(onClick = onClick, cornerRadius = 20.dp, elevation = 2.dp) {
        Column(Modifier.padding(15.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(30.dp)
                        .background(
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
                            RoundedCornerShape(10.dp),
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        "${dua.title.take(1)}",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                    )
                }
                Spacer(Modifier.width(10.dp))
                Text(
                    dua.title,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.weight(1f),
                    maxLines = 2,
                )
                if (isFavorite) {
                    Icon(
                        Icons.Filled.Favorite,
                        null,
                        Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.secondary,
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                dua.bn,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
            )
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                NoorChip(text = dua.grade.ifBlank { dua.tag })
                Spacer(Modifier.width(8.dp))
                Text(
                    dua.ref,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
fun EmptyFavorites(onExplore: () -> Unit) {
    Box(Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Filled.Favorite,
                null,
                Modifier.size(36.dp),
                tint = MaterialTheme.colorScheme.secondary,
            )
            Spacer(Modifier.height(12.dp))
            Text("এখনো কোনো দুআ সংরক্ষণ করা হয়নি", style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.height(6.dp))
            Text(
                "কোনো দুআ খুলে ♥ চাপ দিলে সেটি এখানে জমা হবে।",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}


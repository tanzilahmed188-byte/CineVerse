package com.noor.dua.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.noor.dua.data.Dua
import com.noor.dua.data.DuaRepository
import com.noor.dua.ui.theme.ArabicTextStyle
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    topInset: Dp,
    bottomSpace: Dp,
    onBrowse: (BrowseMode) -> Unit,
    onOpenCategory: (String) -> Unit,
    onOpenDua: (String) -> Unit,
) {
    val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
    val duaOfDay = DuaRepository.duaOfTheDay(dayOfYear)
    val total = DuaRepository.duas.size
    val categoryCount = DuaRepository.categories.size

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = topInset + 14.dp, bottom = bottomSpace),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item { GreetingHeader(total) }

        // দুআ আর হাদিস — দুটি আলাদা বড় বাটন
        item { BrowseButtons(onBrowse) }

        duaOfDay?.let { dua ->
            item {
                Staggered(index = 1) {
                    DuaOfTheDayCard(dua = dua, onOpen = { onOpenDua(dua.id) })
                }
            }
        }

        item {
            SectionHeader(
                "ক্যাটাগরি অনুযায়ী",
                "$categoryCountটি বিষয় • $totalটি যাচাইকৃত দুআ",
            )
        }

        item { CategoryRows(DuaRepository.categories, startIndex = 2, onOpenCategory = onOpenCategory) }

        item { SectionHeader("দেখে নিন", "নির্বাচিত কয়েকটি দুআ") }

        val featured = listOf("day-4", "salah-8", "hardship-2", "akhirah-1", "quran-6", "sleep-3")
            .mapNotNull { DuaRepository.byId(it) }

        items(featured, key = { it.id }) { dua ->
            Staggered(index = featured.indexOf(dua) + 14) {
                FeaturedRow(dua = dua, onOpen = { onOpenDua(dua.id) })
            }
        }

        item {
            Spacer(Modifier.height(6.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    Icons.Filled.Verified,
                    null,
                    Modifier.size(15.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    "প্রতিটি দুআ কুরআন ও সহীহ হাদিস থেকে যাচাই করা",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
            }
            Spacer(Modifier.height(10.dp))
        }
    }
}

/* -------------------------------------------------------------------- হেডার */

@Composable
private fun GreetingHeader(total: Int) {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val greeting = when (hour) {
        in 4..11 -> "শুভ সকাল"
        in 12..15 -> "শুভ দুপুর"
        in 16..18 -> "শুভ সন্ধ্যা"
        else -> "শুভ রাত্রি"
    }
    val dateText = remember { SimpleDateFormat("EEEE, d MMMM yyyy", Locale("bn", "BD")).format(Date()) }

    Column {
        Text(
            greeting,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(3.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "নূর",
                style = MaterialTheme.typography.displaySmall.copy(fontSize = 34.sp),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(Modifier.width(10.dp))
            NoorChip(
                text = "$total টি দুআ",
                container = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            )
        }
        Spacer(Modifier.height(4.dp))
        Text(
            dateText,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

/* ------------------------------------------------- দুআ / হাদিস — দুটি আলাদা বড় বাটন */

@Composable
private fun BrowseButtons(onBrowse: (BrowseMode) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        BigTile(
            title = "দুআ",
            subtitle = "আরবি, উচ্চারণ ও অর্থ",
            icon = Icons.Filled.MenuBook,
            filled = true,
            modifier = Modifier.weight(1f),
            onClick = { onBrowse(BrowseMode.Dua) },
        )
        BigTile(
            title = "হাদিস",
            subtitle = "ফজিলত, সূত্র ও মান",
            icon = Icons.Filled.Verified,
            filled = false,
            modifier = Modifier.weight(1f),
            onClick = { onBrowse(BrowseMode.Hadith) },
        )
    }
}

@Composable
private fun BigTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    filled: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val container = if (filled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    val content = if (filled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface

    PressableCard(
        onClick = onClick,
        modifier = modifier,
        cornerRadius = 22.dp,
        containerColor = container,
        contentColor = content,
        borderColor = if (filled) container else MaterialTheme.colorScheme.outlineVariant,
        elevation = if (filled) 4.dp else 1.dp,
    ) {
        Column(Modifier.padding(16.dp)) {
            Box(
                Modifier
                    .size(40.dp)
                    .background(content.copy(alpha = 0.12f), RoundedCornerShape(13.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(icon, null, Modifier.size(21.dp), tint = content)
            }
            Spacer(Modifier.height(12.dp))
            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = content,
            )
            Spacer(Modifier.height(2.dp))
            Text(
                subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = content.copy(alpha = 0.75f),
                lineHeight = 15.sp,
            )
        }
    }
}

/* --------------------------------------------------------------- আজকের দুআ কার্ড */

@Composable
private fun DuaOfTheDayCard(dua: Dua, onOpen: () -> Unit) {
    val container = MaterialTheme.colorScheme.primary
    val content = MaterialTheme.colorScheme.onPrimary

    val transition = rememberInfiniteTransition(label = "sheen")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(4200, easing = LinearEasing), RepeatMode.Restart),
        label = "sheenPhase",
    )

    PressableCard(
        onClick = onOpen,
        containerColor = container,
        contentColor = content,
        borderColor = container,
        cornerRadius = 26.dp,
        elevation = 6.dp,
    ) {
        Box {
            // হালকা ঝিলিক
            Box(
                Modifier
                    .matchParentSize()
                    .background(rememberSheenBrush(phase, container)),
            )
            Column(Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.AutoAwesome, null, Modifier.size(16.dp), tint = content)
                    Spacer(Modifier.width(6.dp))
                    Text(
                        "আজকের দুআ",
                        style = MaterialTheme.typography.labelLarge,
                        color = content,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(Modifier.weight(1f))
                    NoorChip(
                        text = dua.tag,
                        container = content.copy(alpha = 0.14f),
                        contentColor = content,
                    )
                }
                Spacer(Modifier.height(14.dp))
                Text(
                    dua.arabic,
                    style = ArabicTextStyle.copy(fontSize = 24.sp, lineHeight = 46.sp),
                    color = content,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    dua.bn,
                    style = MaterialTheme.typography.bodyMedium,
                    color = content.copy(alpha = 0.88f),
                    maxLines = 3,
                )
                Spacer(Modifier.height(14.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.MenuBook, null, Modifier.size(14.dp), tint = content.copy(alpha = 0.8f))
                    Spacer(Modifier.width(6.dp))
                    Text(
                        dua.ref,
                        style = MaterialTheme.typography.labelSmall,
                        color = content.copy(alpha = 0.8f),
                        maxLines = 1,
                        modifier = Modifier.weight(1f),
                    )
                    Icon(Icons.Filled.ChevronRight, null, Modifier.size(20.dp), tint = content)
                }
            }
        }
    }
}

/* ------------------------------------------------------------------- ফিচার্ড রো */

@Composable
private fun FeaturedRow(dua: Dua, onOpen: () -> Unit) {
    PressableCard(onClick = onOpen, cornerRadius = 20.dp) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(42.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    "${dua.title.take(1)}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(dua.title, style = MaterialTheme.typography.titleSmall, maxLines = 1)
                Spacer(Modifier.height(3.dp))
                Text(
                    dua.ref,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                )
            }
            Icon(
                Icons.Filled.ChevronRight,
                null,
                Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.noor.dua.data.Dua
import com.noor.dua.data.DuaRepository
import com.noor.dua.ui.theme.ArabicTextStyle
import com.noor.dua.ui.theme.Gold
import com.noor.dua.ui.theme.GoldLight
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    topInset: Dp,
    bottomSpace: Dp,
    onOpenDua: (String) -> Unit,
    onOpenCategory: (String) -> Unit,
    onSeeAll: () -> Unit,
    onOpenTasbih: () -> Unit,
    onOpenFavorites: () -> Unit,
) {
    val today = Calendar.getInstance()
    val dayOfYear = today.get(Calendar.DAY_OF_YEAR)
    val duaOfDay = DuaRepository.duaOfTheDay(dayOfYear)
    val total = DuaRepository.duas.size

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            start = 18.dp, end = 18.dp, top = topInset + 14.dp, bottom = bottomSpace,
        ),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item { GreetingHeader(total) }

        duaOfDay?.let { dua ->
            item {
                Staggered(index = 0) {
                    DuaOfTheDayCard(dua = dua, onOpen = { onOpenDua(dua.id) })
                }
            }
        }

        item {
            Staggered(index = 1) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    QuickTile(
                        title = "তাসবিহ",
                        subtitle = "যিকির গুনুন",
                        icon = Icons.Filled.AutoAwesome,
                        modifier = Modifier.weight(1f),
                        onClick = onOpenTasbih,
                    )
                    QuickTile(
                        title = "প্রিয় দুআ",
                        subtitle = "আপনার সংরক্ষিত",
                        icon = Icons.Filled.Favorite,
                        modifier = Modifier.weight(1f),
                        onClick = onOpenFavorites,
                    )
                }
            }
        }

        item { SectionHeader("ক্যাটাগরি অনুযায়ী", "১২টি বিষয়, ৯৩টি যাচাইকৃত দুআ") }

        item {
            CategoryRows(DuaRepository.categories, startIndex = 2, onOpenCategory = onOpenCategory)
        }

        item { SectionHeader("দেখে নিন", "নির্বাচিত কয়েকটি দুআ") }

        val featured = listOf("day-4", "salah-8", "hardship-2", "akhirah-1", "quran-6", "sleep-3")
            .mapNotNull { DuaRepository.byId(it) }

        items(featured, key = { it.id }) { dua ->
            val index = featured.indexOf(dua)
            Staggered(index = index + 6) {
                FeaturedRow(dua = dua, onOpen = { onOpenDua(dua.id) })
            }
        }

        item {
            Spacer(Modifier.height(6.dp))
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Verified,
                        null,
                        Modifier.size(15.dp),
                        tint = MaterialTheme.colorScheme.secondary,
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        "প্রতিটি দুআ কুরআন ও সহীহ হাদিস থেকে যাচাই করা",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
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
        Spacer(Modifier.height(2.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "নূর",
                style = MaterialTheme.typography.displaySmall.copy(fontSize = 34.sp),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(Modifier.width(10.dp))
            Box(
                Modifier
                    .clip(RoundedCornerShape(50))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Gold.copy(alpha = 0.85f), GoldLight.copy(alpha = 0.75f)),
                        ),
                    )
                    .padding(horizontal = 10.dp, vertical = 4.dp),
            ) {
                Text(
                    "$total টি দুআ",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF2A2107),
                    fontWeight = FontWeight.Bold,
                )
            }
        }
        Spacer(Modifier.height(3.dp))
        Text(
            dateText,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

/* --------------------------------------------------------------- আজকের দুআ কার্ড */

@Composable
private fun DuaOfTheDayCard(dua: Dua, onOpen: () -> Unit) {
    val transition = rememberInfiniteTransition(label = "sheen")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(4200, easing = LinearEasing), RepeatMode.Restart),
        label = "sheenPhase",
    )

    PressableCard(
        onClick = onOpen,
        containerColor = MaterialTheme.colorScheme.primary,
        borderColor = Gold.copy(alpha = 0.45f),
        cornerRadius = 26.dp,
        elevation = 8.dp,
    ) {
        Box {
            // সোনালি ঝিলিক
            Box(
                Modifier
                    .matchParentSize()
                    .background(rememberSheenBrush(phase)),
            )
            Column(Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.AutoAwesome,
                        null,
                        Modifier.size(16.dp),
                        tint = GoldLight,
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        "আজকের দুআ",
                        style = MaterialTheme.typography.labelLarge,
                        color = GoldLight,
                    )
                    Spacer(Modifier.weight(1f))
                    NoorChip(
                        text = dua.tag,
                        container = Color.White.copy(alpha = 0.14f),
                        contentColor = GoldLight,
                    )
                }
                Spacer(Modifier.height(14.dp))
                Text(
                    dua.arabic,
                    style = ArabicTextStyle.copy(fontSize = 24.sp, lineHeight = 46.sp),
                    color = Color(0xFFFFFBF0),
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    dua.bn,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFF2EEE2),
                    maxLines = 3,
                )
                Spacer(Modifier.height(14.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.MenuBook, null, Modifier.size(14.dp), tint = GoldLight)
                    Spacer(Modifier.width(6.dp))
                    Text(
                        dua.ref,
                        style = MaterialTheme.typography.labelSmall,
                        color = GoldLight,
                        maxLines = 1,
                        modifier = Modifier.weight(1f),
                    )
                    Icon(
                        Icons.Filled.ChevronRight,
                        null,
                        Modifier.size(20.dp),
                        tint = Color.White.copy(alpha = 0.8f),
                    )
                }
            }
        }
    }
}

/* ------------------------------------------------------------------ কুইক টাইল */

@Composable
private fun QuickTile(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    PressableCard(
        onClick = onClick,
        modifier = modifier,
        cornerRadius = 22.dp,
        elevation = 3.dp,
    ) {
        Column(Modifier.padding(16.dp)) {
            Box(
                Modifier
                    .size(38.dp)
                    .background(
                        Brush.linearGradient(
                            listOf(
                                MaterialTheme.colorScheme.primaryContainer,
                                MaterialTheme.colorScheme.secondaryContainer,
                            ),
                        ),
                        RoundedCornerShape(12.dp),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(icon, null, Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary)
            }
            Spacer(Modifier.height(10.dp))
            Text(title, style = MaterialTheme.typography.titleSmall)
            Text(
                subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 16.sp,
            )
        }
    }
}

/* ------------------------------------------------------------------ ক্যাটাগরি টাইল */

@Composable
fun CategoryTile(
    title: String,
    subtitle: String,
    count: Int,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    PressableCard(onClick = onClick, modifier = modifier, cornerRadius = 22.dp, elevation = 3.dp) {
        Column(Modifier.padding(14.dp)) {
            Box(
                Modifier
                    .size(36.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.75f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(icon, null, Modifier.size(19.dp), tint = MaterialTheme.colorScheme.primary)
            }
            Spacer(Modifier.height(10.dp))
            Text(
                title,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 2,
                lineHeight = 20.sp,
            )
            Spacer(Modifier.height(2.dp))
            Text(
                subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
            )
            Spacer(Modifier.height(6.dp))
            NoorChip(text = "$count টি", leading = Icons.Filled.Verified)
        }
    }
}

/* ------------------------------------------------------------------- ফিচার্ড রো */

@Composable
private fun FeaturedRow(dua: Dua, onOpen: () -> Unit) {
    PressableCard(onClick = onOpen, cornerRadius = 20.dp, elevation = 2.dp) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(42.dp)
                    .background(
                        Brush.linearGradient(
                            listOf(Gold.copy(alpha = 0.28f), GoldLight.copy(alpha = 0.12f)),
                        ),
                        RoundedCornerShape(14.dp),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    Icons.Filled.AutoAwesome,
                    null,
                    Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.secondary,
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(dua.title, style = MaterialTheme.typography.titleSmall, maxLines = 1)
                Spacer(Modifier.height(2.dp))
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

/* ------------------------------------------------------- তালিকার আইটেম স্ট্যাগার অ্যানিমেশন */

@Composable
fun Staggered(index: Int, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    var visible by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
    androidx.compose.runtime.LaunchedEffect(index) {
        kotlinx.coroutines.delay((index * 55).toLong().coerceAtMost(700))
        visible = true
    }
    androidx.compose.animation.AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        enter = androidx.compose.animation.fadeIn(tween(420)) +
            androidx.compose.animation.slideInVertically(tween(420)) { it / 5 },
    ) {
        content()
    }
}

/** দুই কলামে ভাগ করে সারি বানায় — লেজি লিস্টের ভেতরে ভাঙা তালিকা দেখানোর নিরাপদ উপায় */
@Composable
fun CategoryRows(
    categories: List<com.noor.dua.data.DuaCategory>,
    startIndex: Int = 0,
    onOpenCategory: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        categories.chunked(2).forEachIndexed { rowIndex, rowItems ->
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                rowItems.forEachIndexed { columnIndex, cat ->
                    Staggered(
                        index = startIndex + rowIndex * 2 + columnIndex,
                        modifier = Modifier.weight(1f),
                    ) {
                        CategoryTile(
                            title = cat.title,
                            subtitle = cat.subtitle,
                            count = DuaRepository.countOf(cat.id),
                            icon = categoryIcon(cat.icon),
                            onClick = { onOpenCategory(cat.id) },
                        )
                    }
                }
                if (rowItems.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}

package com.noor.dua.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Diversity3
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShieldMoon
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.noor.dua.data.DuaCategory
import com.noor.dua.data.DuaRepository
import com.noor.dua.ui.theme.ArabicTextStyle

/* ----------------------------------------------------------------------------
 *  দেখার দুই ধরন — ব্যবহারকারী চাইলে দুআ দেখবেন, চাইলে হাদিস/ফজিলত দেখবেন।
 * -------------------------------------------------------------------------- */

enum class BrowseMode(val title: String, val subtitle: String, val hint: String) {
    Dua(
        title = "দুআ",
        subtitle = "আরবি টেক্সট, উচ্চারণ ও বাংলা অর্থ",
        hint = "আরবি, উচ্চারণ ও অর্থ একসাথে দেখা যাবে",
    ),
    Hadith(
        title = "হাদিস ও ফজিলত",
        subtitle = "কোন হাদিসে আছে, ফজিলত কী, মান কতটা সহীহ",
        hint = "ফজিলত, রেফারেন্স ও হাদিসের মান দেখা যাবে",
    ),
}

/* ------------------------------------------------------------------ আইকন ম্যাপ */

fun categoryIcon(name: String): ImageVector = when (name) {
    "bedtime" -> Icons.Filled.Bedtime
    "wb_twilight" -> Icons.Filled.WbTwilight
    "restaurant" -> Icons.Filled.Restaurant
    "mosque" -> Icons.Filled.Mosque
    "water_drop" -> Icons.Filled.WaterDrop
    "auto_awesome" -> Icons.Filled.AutoAwesome
    "shield_moon" -> Icons.Filled.ShieldMoon
    "healing" -> Icons.Filled.Healing
    "diversity_3" -> Icons.Filled.Diversity3
    "favorite" -> Icons.Filled.Favorite
    "nights_stay" -> Icons.Filled.NightsStay
    "menu_book" -> Icons.Filled.MenuBook
    else -> Icons.Filled.AutoAwesome
}

/* -------------------------------------------------------------- চাপ দিলে ছোট হওয়া কার্ড */

@Composable
fun PressableCard(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    borderColor: Color = MaterialTheme.colorScheme.outlineVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    cornerRadius: androidx.compose.ui.unit.Dp = 24.dp,
    elevation: androidx.compose.ui.unit.Dp = 1.dp,
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.975f else 1f,
        animationSpec = spring(dampingRatio = 0.45f, stiffness = 900f),
        label = "press",
    )
    Surface(
        onClick = onClick,
        modifier = modifier.scale(scale),
        shape = RoundedCornerShape(cornerRadius),
        color = containerColor,
        contentColor = contentColor,
        tonalElevation = 0.dp,
        shadowElevation = elevation,
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
        interactionSource = interaction,
    ) {
        content()
    }
}

/* --------------------------------------------------------------------- সেকশন হেডার */

@Composable
fun SectionHeader(title: String, subtitle: String? = null, modifier: Modifier = Modifier) {
    Column(modifier.padding(top = 10.dp, bottom = 10.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(width = 4.dp, height = 18.dp)
                    .background(
                        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.85f),
                        RoundedCornerShape(50),
                    ),
            )
            Spacer(Modifier.width(10.dp))
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
        if (subtitle != null) {
            Text(
                subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 14.dp, top = 3.dp),
            )
        }
    }
}

/* -------------------------------------------------------------------------- চিপ */

@Composable
fun NoorChip(
    text: String,
    modifier: Modifier = Modifier,
    leading: ImageVector? = null,
    container: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    Row(
        modifier
            .background(container, RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (leading != null) {
            Icon(leading, null, Modifier.size(14.dp), tint = contentColor)
            Spacer(Modifier.width(5.dp))
        }
        Text(text, style = MaterialTheme.typography.labelSmall, color = contentColor)
    }
}

/* -------------------------------------------------------------------- আরবি টেক্সট */

@Composable
fun ArabicText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: Int = 27,
    textAlign: TextAlign = TextAlign.End,
) {
    Text(
        text = text,
        modifier = modifier.fillMaxWidth(),
        style = ArabicTextStyle.copy(fontSize = fontSize.sp),
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = textAlign,
    )
}

/** সূক্ষ্ম সীমানাযুক্ত আরবি ব্লক — দুআর মূল টেক্সটের জন্য */
@Composable
fun ArabicPanel(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: Int = 27,
) {
    Box(
        modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f), RoundedCornerShape(20.dp))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(20.dp))
            .padding(horizontal = 18.dp, vertical = 20.dp),
        contentAlignment = Alignment.CenterEnd,
    ) {
        ArabicText(text, fontSize = fontSize)
    }
}

/* ------------------------------------------------------------------ যাচাইকরণ ব্যাজ */

@Composable
fun VerifiedBadge(text: String, modifier: Modifier = Modifier) {
    NoorChip(
        text = text,
        modifier = modifier,
        leading = Icons.Filled.Verified,
        container = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
    )
}

@Composable
fun InfoNote(text: String, modifier: Modifier = Modifier) {
    Row(
        modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
                RoundedCornerShape(16.dp),
            )
            .padding(14.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Icon(
            Icons.Filled.Lightbulb,
            contentDescription = null,
            Modifier.size(18.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

/* -------------------------------------------------------------- অ্যানিমেটেড ঝিলিক (monochrome) */

@Composable
fun rememberSheenBrush(phase: Float, container: Color): Brush {
    val darkCard = (container.red * 0.299f + container.green * 0.587f + container.blue * 0.114f) < 0.5f
    val shine = if (darkCard) Color.White.copy(alpha = 0.13f) else Color.Black.copy(alpha = 0.05f)
    val clear = Color.Transparent
    val start = -0.6f + 2.2f * phase
    return remember(phase, container) {
        Brush.linearGradient(
            colors = listOf(clear, clear, shine, clear, clear),
            start = androidx.compose.ui.geometry.Offset(start, 0f),
            end = androidx.compose.ui.geometry.Offset(start + 0.5f, 1f),
        )
    }
}

@Composable
fun AnimatedContainerColor(target: Color): Color {
    val c by animateColorAsState(target, tween(400), label = "containerColor")
    return c
}

/* ------------------------------------------------- দুই-অপশনের স্যুইচ (দুআ / হাদিস) */

@Composable
fun ModeSwitch(
    selected: BrowseMode,
    onSelect: (BrowseMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f), RoundedCornerShape(50))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(50))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BrowseMode.entries.forEach { mode ->
            val active = mode == selected
            Surface(
                onClick = { onSelect(mode) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(50),
                color = if (active) MaterialTheme.colorScheme.primary else Color.Transparent,
                contentColor = if (active) MaterialTheme.colorScheme.onPrimary
                else MaterialTheme.colorScheme.onSurfaceVariant,
                shadowElevation = if (active) 2.dp else 0.dp,
            ) {
                Row(
                    Modifier.padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        if (mode == BrowseMode.Dua) Icons.Filled.MenuBook else Icons.Filled.Verified,
                        contentDescription = null,
                        modifier = Modifier.size(17.dp),
                    )
                    Spacer(Modifier.width(7.dp))
                    Text(
                        mode.title,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = if (active) FontWeight.Bold else FontWeight.Normal,
                        maxLines = 1,
                    )
                }
            }
        }
    }
}

/* ------------------------------------------------------- তালিকার আইটেম স্ট্যাগার অ্যানিমেশন */

@Composable
fun Staggered(index: Int, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(index) {
        kotlinx.coroutines.delay((index * 45).toLong().coerceAtMost(600))
        visible = true
    }
    AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        enter = fadeIn(tween(380)) + slideInVertically(tween(380)) { it / 6 },
    ) {
        content()
    }
}

/* ------------------------------------------------------------------ ক্যাটাগরি টাইল */

@Composable
fun CategoryTile(
    title: String,
    subtitle: String,
    count: Int,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    PressableCard(onClick = onClick, modifier = modifier, cornerRadius = 22.dp) {
        Column(Modifier.padding(14.dp)) {
            Box(
                Modifier
                    .size(36.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(icon, null, Modifier.size(19.dp), tint = MaterialTheme.colorScheme.onSurface)
            }
            Spacer(Modifier.height(10.dp))
            Text(
                title,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 2,
                lineHeight = 20.sp,
            )
            Spacer(Modifier.height(3.dp))
            Text(
                subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                lineHeight = 15.sp,
            )
            Spacer(Modifier.height(8.dp))
            NoorChip(text = "$count টি")
        }
    }
}

/** দুই কলামে ভাগ করে সারি বানায় — লেজি লিস্টের ভেতরে ভাঙা তালিকা দেখানোর নিরাপদ উপায় */
@Composable
fun CategoryRows(
    categories: List<DuaCategory>,
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

val HomePadding = PaddingValues(horizontal = 18.dp)
val CardArrangement = Arrangement.spacedBy(12.dp)

package com.noor.dua.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShieldMoon
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.noor.dua.ui.theme.ArabicTextStyle
import com.noor.dua.ui.theme.Gold

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
    cornerRadius: androidx.compose.ui.unit.Dp = 24.dp,
    elevation: androidx.compose.ui.unit.Dp = 2.dp,
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
    Column(modifier.padding(top = 8.dp, bottom = 10.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(width = 4.dp, height = 18.dp)
                    .background(
                        Brush.verticalGradient(listOf(Gold, MaterialTheme.colorScheme.primary)),
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
                modifier = Modifier.padding(start = 14.dp, top = 2.dp),
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
    container: Color = MaterialTheme.colorScheme.secondaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
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

/** সোনালি সীমানাযুক্ত আরবি ব্লক — দুআর মূল টেক্সটের জন্য */
@Composable
fun ArabicPanel(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: Int = 27,
) {
    Box(
        modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f),
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.65f),
                    ),
                ),
                RoundedCornerShape(20.dp),
            )
            .border(1.dp, Gold.copy(alpha = 0.35f), RoundedCornerShape(20.dp))
            .padding(horizontal = 18.dp, vertical = 20.dp),
        contentAlignment = Alignment.CenterEnd,
    ) {
        ArabicText(text, fontSize = fontSize)
    }
}

/* ------------------------------------------------------------------ যাচাইকরণ ব্যাজ */

@Composable
fun VerifiedBadge(text: String, modifier: Modifier = Modifier) {
    Row(
        modifier
            .background(
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
                RoundedCornerShape(50),
            )
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            Icons.Filled.Verified,
            contentDescription = null,
            Modifier.size(14.dp),
            tint = MaterialTheme.colorScheme.primary,
        )
        Spacer(Modifier.width(5.dp))
        Text(
            text,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    }
}

@Composable
fun InfoNote(text: String, modifier: Modifier = Modifier) {
    Row(
        modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                RoundedCornerShape(16.dp),
            )
            .padding(14.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Icon(
            Icons.Filled.Lightbulb,
            contentDescription = null,
            Modifier.size(18.dp),
            tint = MaterialTheme.colorScheme.secondary,
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

/* -------------------------------------------------------------- অ্যানিমেটেড সোনালি ঝিলিক */

@Composable
fun rememberSheenBrush(phase: Float): Brush {
    val gold = Gold.copy(alpha = 0.35f)
    val clear = Color.Transparent
    val start = -0.6f + 2.2f * phase
    return remember(phase) {
        Brush.linearGradient(
            colors = listOf(clear, clear, gold, clear, clear),
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

val HomePadding = PaddingValues(horizontal = 18.dp)
val CardArrangement = Arrangement.spacedBy(12.dp)

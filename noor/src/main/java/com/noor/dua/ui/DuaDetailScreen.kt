package com.noor.dua.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.noor.dua.data.DuaRepository
import com.noor.dua.ui.theme.Gold

@Composable
fun DuaDetailScreen(
    duaId: String,
    topInset: Dp,
    bottomSpace: Dp,
    isFavorite: Boolean,
    onBack: () -> Unit,
    onToggleFavorite: () -> Unit,
    onMessage: (String) -> Unit,
) {
    val dua = DuaRepository.byId(duaId)
    val context = LocalContext.current
    var copied by remember { mutableStateOf(false) }
    var amalDone by remember { mutableStateOf(false) }

    if (dua == null) {
        Box(Modifier.fillMaxWidth().padding(30.dp), contentAlignment = Alignment.Center) {
            Text("দুআটি পাওয়া যায়নি", style = MaterialTheme.typography.titleSmall)
        }
        return
    }

    val shareText = buildString {
        appendLine(dua.title)
        appendLine()
        appendLine(dua.arabic)
        appendLine()
        appendLine("উচ্চারণ: ${dua.pron}")
        appendLine("অর্থ: ${dua.bn}")
        appendLine("রেফারেন্স: ${dua.ref}")
        appendLine()
        append("— নূর অ্যাপ (দুআ ও আমল)")
    }

    LazyColumn(
        contentPadding = PaddingValues(
            start = 18.dp, end = 18.dp, top = topInset + 8.dp, bottom = bottomSpace + 10.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            ScreenHeader(
                title = dua.title,
                subtitle = DuaRepository.category(dua.cat)?.title,
                topInset = 0.dp,
                onBack = onBack,
                trailing = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RoundIconAction(Icons.Filled.Share, "শেয়ার") {
                            val send = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, shareText)
                            }
                            context.startActivity(Intent.createChooser(send, "দুআ শেয়ার করুন"))
                        }
                        Spacer(Modifier.width(8.dp))
                        FavoriteIconButton(isFavorite = isFavorite) { onToggleFavorite() }
                    }
                },
            )
        }

        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                NoorChip(
                    text = dua.grade.ifBlank { dua.tag },
                    leading = Icons.Filled.Verified,
                )
                Spacer(Modifier.width(8.dp))
                NoorChip(
                    text = dua.tag,
                    container = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.75f),
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }

        // আরবি টেক্সট
        item {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "আরবি",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.weight(1f))
                    Surface(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("dua", shareText))
                            copied = true
                            onMessage("দুআটি কপি হয়েছে")
                        },
                        shape = RoundedCornerShape(50),
                        color = if (copied) MaterialTheme.colorScheme.secondaryContainer
                        else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                    ) {
                        Row(
                            Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                if (copied) Icons.Filled.Check else Icons.Filled.ContentCopy,
                                null,
                                Modifier.size(15.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                if (copied) "কপি হয়েছে" else "কপি করুন",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                ArabicPanel(dua.arabic)
            }
        }

        // উচ্চারণ
        item {
            DetailCard(
                label = "উচ্চারণ",
                icon = Icons.Filled.Lightbulb,
                accent = MaterialTheme.colorScheme.tertiary,
            ) {
                Text(
                    dua.pron,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 30.sp,
                )
            }
        }

        // বাংলা অর্থ
        item {
            DetailCard(
                label = "বাংলা অর্থ",
                icon = Icons.Filled.MenuBook,
                accent = MaterialTheme.colorScheme.primary,
            ) {
                Text(
                    dua.bn,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }

        // ফজিলত / উপকারিতা
        item {
            DetailCard(
                label = "ফজিলত ও উপকারিতা",
                icon = Icons.Filled.Verified,
                accent = Gold,
            ) {
                Text(
                    dua.fazilat,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }

        // রেফারেন্স
        item {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surface,
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                shadowElevation = 1.dp,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                    Box(
                        Modifier
                            .size(34.dp)
                            .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(11.dp)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            Icons.Filled.Verified,
                            null,
                            Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("রেফারেন্স", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.height(3.dp))
                        Text(dua.ref, style = MaterialTheme.typography.titleSmall)
                        if (dua.grade.isNotBlank()) {
                            Spacer(Modifier.height(3.dp))
                            Text(
                                "যাচাই: ${dua.grade}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }
        }

        // আমল
        item {
            PressableCard(
                onClick = { amalDone = !amalDone },
                cornerRadius = 20.dp,
                containerColor = if (amalDone) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.85f)
                else MaterialTheme.colorScheme.surface,
                borderColor = if (amalDone) MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                else MaterialTheme.colorScheme.outlineVariant,
            ) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    val scale by animateFloatAsState(
                        targetValue = if (amalDone) 1f else 0.85f,
                        animationSpec = spring(dampingRatio = 0.45f, stiffness = 700f),
                        label = "amalScale",
                    )
                    Box(
                        Modifier
                            .size(34.dp)
                            .scale(scale)
                            .background(
                                if (amalDone) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.surfaceVariant,
                                RoundedCornerShape(11.dp),
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            Icons.Filled.Check,
                            null,
                            Modifier.size(19.dp),
                            tint = if (amalDone) MaterialTheme.colorScheme.onPrimary
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(if (amalDone) "আলহামদুলিল্লাহ — আমল সম্পন্ন" else "আজ এই আমল করেছি", style = MaterialTheme.typography.titleSmall)
                        Spacer(Modifier.height(3.dp))
                        Text(
                            dua.amal,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }

        item {
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(tween(500)) + slideInVertically(tween(500)) { it / 4 } + scaleIn(tween(500), initialScale = 0.96f),
            ) {
                InfoNote(
                    "মুখস্থ থাকলে আরবিটি ধীরে ধীরে পড়ুন। অর্থ বুঝে দুআ করলে হৃদয় বেশি প্রভাবিত হয়। " +
                        "ফজিলতের বর্ণনা রাসূলুল্লাহ ﷺ এর হাদিস থেকে নেওয়া।",
                )
            }
            Spacer(Modifier.height(6.dp))
        }
    }
}

@Composable
private fun RoundIconAction(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f),
        modifier = Modifier.size(40.dp),
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription, Modifier.size(18.dp), tint = MaterialTheme.colorScheme.onSurface)
        }
    }
}

@Composable
private fun DetailCard(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accent: androidx.compose.ui.graphics.Color,
    content: @Composable () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(28.dp)
                        .background(accent.copy(alpha = 0.16f), RoundedCornerShape(9.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(icon, null, Modifier.size(15.dp), tint = accent)
                }
                Spacer(Modifier.width(9.dp))
                Text(
                    label,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.SemiBold,
                )
            }
            Spacer(Modifier.height(10.dp))
            content()
        }
    }
}

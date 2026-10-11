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
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Translate
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.noor.dua.data.DuaRepository
import com.noor.dua.ui.theme.ArabicTextStyle

/**
 * একটি দুআর বিস্তারিত পর্দা — উপরে থেকে নিচে পরিষ্কার ধাপে সাজানো:
 * দুআ (আরবি) → উচ্চারণ → অর্থ → হাদিস ও ফজিলত → আমল।
 */
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
        appendLine("ফজিলত: ${dua.fazilat}")
        appendLine("হাদিস: ${dua.ref} (${dua.grade})")
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

        // ছোট করে বুঝিয়ে দেওয়া: কুরআন না হাদিস, আর হাদিসটি কতটা সহীহ
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                NoorChip(
                    text = dua.tag.ifBlank { "দুআ" },
                    container = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                )
                Spacer(Modifier.width(8.dp))
                NoorChip(text = dua.grade.ifBlank { "যাচাইকৃত" }, leading = Icons.Filled.Verified)
            }
        }

        // ১ — দুআ (আরবি টেক্সট)
        item {
            DetailCard(
                label = "দুআ (আরবি)",
                icon = Icons.Filled.MenuBook,
                trailing = {
                    Surface(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("dua", shareText))
                            copied = true
                            onMessage("দুআটি কপি হয়েছে")
                        },
                        shape = RoundedCornerShape(50),
                        color = if (copied) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surfaceVariant,
                    ) {
                        Row(
                            Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                if (copied) Icons.Filled.Check else Icons.Filled.ContentCopy,
                                null,
                                Modifier.size(15.dp),
                                tint = if (copied) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                if (copied) "কপি হয়েছে" else "কপি",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (copied) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                },
            ) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(18.dp))
                        .padding(horizontal = 16.dp, vertical = 18.dp),
                    contentAlignment = Alignment.CenterEnd,
                ) {
                    Text(
                        dua.arabic,
                        style = ArabicTextStyle,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = androidx.compose.ui.text.style.TextAlign.End,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }

        // ২ — উচ্চারণ
        item {
            DetailCard(label = "উচ্চারণ", icon = Icons.Filled.RecordVoiceOver) {
                Text(
                    dua.pron,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 30.sp,
                )
            }
        }

        // ৩ — বাংলা অর্থ
        item {
            DetailCard(label = "বাংলা অর্থ", icon = Icons.Filled.Translate) {
                Text(
                    dua.bn,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }

        // ৪ — হাদিস, ফজিলত ও রেফারেন্স একসাথে (বুঝতে সহজ)
        item {
            DetailCard(label = "হাদিস ও ফজিলত", icon = Icons.Filled.Verified) {
                Text(
                    dua.fazilat,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(Modifier.height(12.dp))
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant),
                )
                Spacer(Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Verified,
                        null,
                        Modifier.size(15.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.width(7.dp))
                    Text(
                        "সূত্র: ${dua.ref}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f),
                    )
                }
                if (dua.grade.isNotBlank()) {
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "হাদিসের মান: ${dua.grade}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        // ৫ — আমল (কীভাবে করবেন)
        item {
            PressableCard(
                onClick = { amalDone = !amalDone },
                cornerRadius = 20.dp,
                containerColor = if (amalDone) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.surface,
                contentColor = if (amalDone) MaterialTheme.colorScheme.onPrimary
                else MaterialTheme.colorScheme.onSurface,
                borderColor = if (amalDone) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.outlineVariant,
            ) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    val scale by animateFloatAsState(
                        targetValue = if (amalDone) 1f else 0.9f,
                        animationSpec = spring(dampingRatio = 0.45f, stiffness = 700f),
                        label = "amalScale",
                    )
                    val mark = if (amalDone) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurfaceVariant
                    Box(
                        Modifier
                            .size(34.dp)
                            .scale(scale)
                            .background(
                                if (amalDone) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.16f)
                                else MaterialTheme.colorScheme.surfaceVariant,
                                RoundedCornerShape(11.dp),
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(Icons.Filled.Check, null, Modifier.size(19.dp), tint = mark)
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(
                            if (amalDone) "আলহামদুলিল্লাহ — আমল সম্পন্ন" else "আজ এই আমল করেছি",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(Modifier.height(3.dp))
                        Text(dua.amal, style = MaterialTheme.typography.bodySmall)
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
                    "আরবিটা ধীরে ধীরে পড়ুন, অর্থ বুঝে দুআ করলে হৃদয় বেশি প্রভাবিত হয়। " +
                        "চাইলে নিজেই মূল সূত্র মিলিয়ে নিতে পারবেন — সূত্র প্রতিটি দুআর নিচে দেওয়া আছে।",
                )
            }
            Spacer(Modifier.height(6.dp))
        }
    }
}

@Composable
private fun RoundIconAction(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.surfaceVariant,
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
    icon: ImageVector,
    trailing: (@Composable () -> Unit)? = null,
    accent: Color = MaterialTheme.colorScheme.onSurface,
    content: @Composable () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(28.dp)
                        .background(accent.copy(alpha = 0.12f), RoundedCornerShape(9.dp)),
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
                    modifier = Modifier.weight(1f),
                )
                trailing?.invoke()
            }
            Spacer(Modifier.height(12.dp))
            content()
        }
    }
}

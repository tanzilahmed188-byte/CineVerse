package com.noor.dua.ui

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.noor.dua.data.DuaRepository
import com.noor.dua.ui.theme.Gold
import com.noor.dua.ui.theme.GoldLight

/* ------------------------------------------------------------------ "আরও" স্ক্রিন */

@Composable
fun MoreScreen(
    topInset: Dp,
    bottomSpace: Dp,
    onOpenFavorites: () -> Unit,
    onOpenTasbih: () -> Unit,
    onOpenDua: (String) -> Unit,
    onMessage: (String) -> Unit,
) {
    val context = LocalContext.current
    val total = DuaRepository.duas.size
    val categories = DuaRepository.categories.size

    LazyColumn(
        contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = topInset + 14.dp, bottom = bottomSpace),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item { SectionHeader("আরও", "অ্যাপ সম্পর্কে ও অতিরিক্ত ফিচার") }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard("$total", "যাচাইকৃত দুআ", Modifier.weight(1f))
                StatCard("$categories", "ক্যাটাগরি", Modifier.weight(1f))
                StatCard("৬+", "মূল সূত্র", Modifier.weight(1f))
            }
        }

        item {
            MenuRow(
                title = "প্রিয় দুআ",
                subtitle = "আপনার সংরক্ষিত দুআর তালিকা",
                icon = Icons.Filled.Favorite,
                onClick = onOpenFavorites,
            )
        }
        item {
            MenuRow(
                title = "ডিজিটাল তাসবিহ",
                subtitle = "সুবহানাল্লাহ, আলহামদুলিল্লাহ, আল্লাহু আকবার গুনুন",
                icon = Icons.Filled.AutoAwesome,
                onClick = onOpenTasbih,
            )
        }
        item {
            MenuRow(
                title = "সাইয়েদুল ইস্তিগফার",
                subtitle = "সকাল-সন্ধ্যার সর্বশ্রেষ্ঠ দুআ পড়ুন",
                icon = Icons.Filled.Verified,
                onClick = { onOpenDua("day-4") },
            )
        }
        item {
            MenuRow(
                title = "অ্যাপ শেয়ার করুন",
                subtitle = "পরিবার ও বন্ধুদের সাথে নূর শেয়ার করুন",
                icon = Icons.Filled.Share,
                onClick = {
                    val send = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(
                            Intent.EXTRA_TEXT,
                            "নূর — দুআ ও আমল: আরবি টেক্সট, উচ্চারণ, বাংলা অর্থ, ফজিলত ও কুরআন-হাদিস রেফারেন্স সহ $total টি দুআ।",
                        )
                    }
                    context.startActivity(Intent.createChooser(send, "শেয়ার করুন"))
                },
            )
        }

        item { SectionHeader("যাচাইকরণ", "দুআগুলো কোথা থেকে নেওয়া হয়েছে") }

        item {
            SourceCard()
        }

        item {
            InfoNote(
                "এই অ্যাপের সব দুআ কুরআনুল কারীম, সহীহ বুখারী, সহীহ মুসলিম, সুনানে আবু দাউদ, " +
                    "সুনানে তিরমিজী ও অন্যান্য নির্ভরযোগ্য সূত্র থেকে নেওয়া হয়েছে। প্রতিটি দুআর নিচে " +
                    "রেফারেন্স ও হাদিসের মান (সহীহ/হাসান) উল্লেখ করা হয়েছে — যাতে আপনি নিজেই যাচাই করে নিতে পারেন।",
            )
        }

        item {
            InfoNote(
                "মনে রাখবেন: ফজিলতের বর্ণনা হাদিসের অনুবাদ-ভাষ্য; সুনির্দিষ্ট কোনো রোগের চিকিৎসায় " +
                    "দুআর সাথে ডাক্তারের পরামর্শও নিন। দুআ কখনো বিফলে যায় না — হয় কবুল হয়, নয়তো " +
                    "আল্লাহ উত্তম কিছু অপেক্ষা রাখেন।",
            )
        }

        item {
            Column(Modifier.padding(vertical = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "নূর — দুআ ও আমল",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    "সংস্করণ ১.০ • ইন্টারনেট ছাড়াই কাজ করে",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun StatCard(value: String, label: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shadowElevation = 2.dp,
    ) {
        Column(Modifier.padding(14.dp)) {
            Text(
                value,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.height(2.dp))
            Text(
                label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun MenuRow(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
) {
    PressableCard(onClick = onClick, cornerRadius = 20.dp, elevation = 2.dp) {
        Row(Modifier.padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(40.dp)
                    .background(
                        Brush.linearGradient(
                            listOf(
                                MaterialTheme.colorScheme.primaryContainer,
                                MaterialTheme.colorScheme.secondaryContainer,
                            ),
                        ),
                        RoundedCornerShape(13.dp),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(icon, null, Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary)
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleSmall)
                Spacer(Modifier.height(2.dp))
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 17.sp,
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

@Composable
private fun SourceCard() {
    val sources = listOf(
        "আল-কুরআনুল কারীম" to "আয়াতভিত্তিক দুআ",
        "সহীহ বুখারী" to "ইমাম মুহাম্মাদ ইবনে ইসমাইল বুখারী",
        "সহীহ মুসলিম" to "ইমাম মুসলিম ইবনুল হাজ্জাজ",
        "সুনানে আবু দাউদ" to "ইমাম আবু দাউদ",
        "সুনানে তিরমিজী" to "ইমাম আবু ঈসা তিরমিজী",
        "সুনানে নাসায়ী / মুসনাদে আহমাদ" to "অতিরিক্ত যাচাই",
    )
    Surface(
        shape = RoundedCornerShape(22.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, Gold.copy(alpha = 0.30f)),
        shadowElevation = 3.dp,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(16.dp)) {
            sources.forEachIndexed { index, (name, by) ->
                Row(Modifier.padding(vertical = 7.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier
                            .size(26.dp)
                            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f), CircleShape),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            Icons.Filled.MenuBook,
                            null,
                            Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text(name, style = MaterialTheme.typography.titleSmall)
                        Text(
                            by,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
                if (index != sources.lastIndex) {
                    Spacer(Modifier.height(2.dp))
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
                    )
                }
            }
        }
    }
}

/* -------------------------------------------------------------- প্রিয় দুআ স্ক্রিন */

@Composable
fun FavoritesScreen(
    topInset: Dp,
    bottomSpace: Dp,
    favorites: Set<String>,
    onBack: () -> Unit,
    onOpenDua: (String) -> Unit,
) {
    val list = DuaRepository.duas.filter { favorites.contains(it.id) }

    LazyColumn(
        contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = topInset + 8.dp, bottom = bottomSpace),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            ScreenHeader(
                title = "প্রিয় দুআ",
                subtitle = "${list.size}টি সংরক্ষিত",
                topInset = 0.dp,
                onBack = onBack,
            )
        }
        if (list.isEmpty()) {
            item { EmptyFavorites(onExplore = onBack) }
        } else {
            items(list, key = { it.id }) { dua ->
                Staggered(index = list.indexOf(dua)) {
                    DuaListRow(dua = dua, isFavorite = true, onClick = { onOpenDua(dua.id) })
                }
            }
        }
    }
}

/* ---------------------------------------------------------------- তাসবিহ কাউন্টার */

@Composable
fun TasbihScreen(
    topInset: Dp,
    bottomSpace: Dp,
    prefs: NoorPrefs,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    var count by remember { mutableIntStateOf(prefs.tasbih()) }
    var target by remember { mutableStateOf(33) }
    val progress = (count % target).toFloat() / target.toFloat()

    val animatedProgress by animateFloatAsState(
        targetValue = if (count == 0) 0f else progress,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 300f),
        label = "tasbihProgress",
    )
    val buttonScale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(dampingRatio = 0.4f, stiffness = 500f),
        label = "buttonScale",
    )

    val phrases = listOf("সুবহানাল্লাহ", "আলহামদুলিল্লাহ", "আল্লাহু আকবার")
    val phrase = phrases[(count / target) % phrases.size]

    Column(
        Modifier
            .fillMaxSize()
            .padding(top = topInset + 8.dp, bottom = bottomSpace),
    ) {
        ScreenHeader(title = "ডিজিটাল তাসবিহ", subtitle = "প্রতিটি গুনতিতে হালকা কম্পন", topInset = 0.dp, onBack = onBack)

        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                phrase,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(Modifier.height(10.dp))

            Box(
                Modifier
                    .fillMaxWidth(0.78f)
                    .aspectRatio(1f),
                contentAlignment = Alignment.Center,
            ) {
                Canvas(Modifier.fillMaxSize()) {
                    val stroke = 16.dp.toPx()
                    val inset = stroke / 2
                    drawArc(
                        color = Color(0x226D8F7E),
                        startAngle = 0f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(width = stroke, cap = StrokeCap.Round),
                        topLeft = androidx.compose.ui.geometry.Offset(inset, inset),
                        size = androidx.compose.ui.geometry.Size(size.width - stroke, size.height - stroke),
                    )
                    drawArc(
                        brush = Brush.sweepGradient(listOf(Gold, GoldLight, Gold)),
                        startAngle = -90f,
                        sweepAngle = 360f * animatedProgress,
                        useCenter = false,
                        style = Stroke(width = stroke, cap = StrokeCap.Round),
                        topLeft = androidx.compose.ui.geometry.Offset(inset, inset),
                        size = androidx.compose.ui.geometry.Size(size.width - stroke, size.height - stroke),
                    )
                }
                AnimatedContent(
                    targetState = count % target,
                    transitionSpec = {
                        (fadeIn(tween(220)) + androidx.compose.animation.scaleIn(tween(220), initialScale = 0.8f))
                            .togetherWith(fadeOut(tween(150)) + androidx.compose.animation.scaleOut(tween(150), targetScale = 1.2f))
                    },
                    label = "count",
                ) { value ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "$value",
                            style = MaterialTheme.typography.displaySmall.copy(fontSize = 62.sp),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                        Text(
                            "মোট: $count",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Surface(
                onClick = {
                    count += 1
                    prefs.setTasbih(count)
                    vibrate(context)
                },
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary,
                shadowElevation = 10.dp,
                modifier = Modifier
                    .size(126.dp)
                    .scale(buttonScale),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Filled.AutoAwesome,
                            null,
                            Modifier.size(26.dp),
                            tint = MaterialTheme.colorScheme.onPrimary,
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "গুনুন",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                }
            }

            Spacer(Modifier.height(22.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                listOf(33, 100).forEach { t ->
                    val selected = target == t
                    Surface(
                        onClick = { target = t },
                        shape = RoundedCornerShape(50),
                        color = if (selected) MaterialTheme.colorScheme.secondaryContainer
                        else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                    ) {
                        Text(
                            "$t বার",
                            style = MaterialTheme.typography.labelMedium,
                            color = if (selected) MaterialTheme.colorScheme.onSecondaryContainer
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        )
                    }
                }
                Surface(
                    onClick = {
                        count = 0
                        prefs.setTasbih(0)
                    },
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                ) {
                    Row(
                        Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            Icons.Filled.Refresh,
                            null,
                            Modifier.size(15.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            "রিসেট",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))
            Text(
                "টিপস: ফাতিমা (রা.)-এর তাসবীহ — ৩৩ বার সুবহানাল্লাহ, ৩৩ বার আলহামদুলিল্লাহ এবং ৩৪ বার আল্লাহু আকবার।",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}

private fun vibrate(context: Context) {
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator ?: return
    if (!vibrator.hasVibrator()) return
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK))
    } else {
        @Suppress("DEPRECATION")
        vibrator.vibrate(18)
    }
}

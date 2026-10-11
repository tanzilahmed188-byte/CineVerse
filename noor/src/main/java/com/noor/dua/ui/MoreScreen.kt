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
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Favorite
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

/* ------------------------------------------------------------------ "আরও" স্ক্রিন */

@Composable
fun MoreScreen(
    topInset: Dp,
    bottomSpace: Dp,
    onOpenDua: (String) -> Unit,
    onMessage: (String) -> Unit,
) {
    val context = LocalContext.current
    val total = DuaRepository.duas.size
    val categories = DuaRepository.categories.size

    LazyColumn(
        contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = topInset + 8.dp, bottom = bottomSpace),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item { ScreenHeader("আরও", "অ্যাপ সম্পর্কে ও দরকারি তথ্য", topInset = 0.dp) }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard("$total", "যাচাইকৃত দুআ", Modifier.weight(1f))
                StatCard("$categories", "ক্যাটাগরি", Modifier.weight(1f))
                StatCard("৬+", "মূল সূত্র", Modifier.weight(1f))
            }
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
                title = "সব দুআ একসাথে",
                subtitle = "১২টি বিষয়ের সব দুআ ও হাদিসের তালিকা",
                icon = Icons.Filled.MenuBook,
                onClick = { onMessage("নিচের মেনু থেকে “দুআ” চাপুন") },
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
                            "নূর — দুআ ও আমল: আরবি টেক্সট, উচ্চারণ, বাংলা অর্থ, ফজিলত ও কুরআন-হাদিস " +
                                "রেফারেন্স সহ $total টি দুআ। ইন্টারনেট ছাড়াই কাজ করে।",
                        )
                    }
                    context.startActivity(Intent.createChooser(send, "শেয়ার করুন"))
                },
            )
        }

        item { SectionHeader("যাচাইকরণ", "দুআগুলো কোথা থেকে নেওয়া হয়েছে") }

        item { SourceCard() }

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
                    "সংস্করণ ১.১ • ইন্টারনেট ছাড়াই কাজ করে • ফোনের থিম অনুসরণ করে",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
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
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shadowElevation = 1.dp,
    ) {
        Column(Modifier.padding(14.dp)) {
            Text(
                value,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
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
    PressableCard(onClick = onClick, cornerRadius = 20.dp) {
        Row(Modifier.padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(40.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(13.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(icon, null, Modifier.size(20.dp), tint = MaterialTheme.colorScheme.onSurface)
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
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(16.dp)) {
            sources.forEachIndexed { index, (name, note) ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier
                            .size(26.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            "${index + 1}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text(name, style = MaterialTheme.typography.titleSmall)
                        Text(
                            note,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
                if (index != sources.lastIndex) {
                    Spacer(Modifier.height(9.dp))
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(MaterialTheme.colorScheme.outlineVariant),
                    )
                    Spacer(Modifier.height(9.dp))
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
    onExplore: () -> Unit,
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
                subtitle = if (list.isEmpty()) "এখনো কিছু সংরক্ষণ করা হয়নি" else "${list.size}টি সংরক্ষিত",
                topInset = 0.dp,
            )
        }
        if (list.isEmpty()) {
            item { EmptyFavorites(onExplore = onExplore) }
        } else {
            items(list, key = { it.id }) { dua ->
                Staggered(index = list.indexOf(dua)) {
                    DuaListRow(dua = dua, isFavorite = true, onClick = { onOpenDua(dua.id) })
                }
            }
        }
    }
}

@Composable
fun EmptyFavorites(onExplore: () -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            Icons.Filled.Favorite,
            null,
            Modifier.size(36.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(14.dp))
        Text("এখনো কোনো দুআ সংরক্ষণ করা হয়নি", style = MaterialTheme.typography.titleSmall)
        Spacer(Modifier.height(4.dp))
        Text(
            "যেকোনো দুআ খুলে উপরের ♥ চিহ্নে চাপ দিলে সেটি এখানে চলে আসবে।",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(18.dp))
        Surface(
            onClick = onExplore,
            shape = RoundedCornerShape(50),
            color = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ) {
            Text(
                "দুআ দেখুন",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(horizontal = 22.dp, vertical = 11.dp),
            )
        }
    }
}

/* ---------------------------------------------------------------- তাসবিহ কাউন্টার */

@Composable
fun TasbihScreen(
    topInset: Dp,
    bottomSpace: Dp,
    prefs: NoorPrefs,
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

    val phrases = listOf("সুবহানাল্লাহ", "আলহামদুলিল্লাহ", "আল্লাহু আকবার")
    val phrase = phrases[(count / target) % phrases.size]

    // Canvas (@Composable না) — তাই রঙগুলো আগেই বের করে নিতে হয়
    val inkColor = MaterialTheme.colorScheme.onBackground
    val trackColor = MaterialTheme.colorScheme.outlineVariant
    val progressColors = listOf(
        inkColor.copy(alpha = 0.40f),
        inkColor,
        inkColor.copy(alpha = 0.40f),
    )

    Column(
        Modifier
            .fillMaxSize()
            .padding(top = topInset + 8.dp),
    ) {
        ScreenHeader(
            title = "ডিজিটাল তাসবিহ",
            subtitle = "গুনতি সেভ থাকে • প্রতিটি গুনতিতে হালকা কম্পন",
            topInset = 0.dp,
        )

        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(start = 22.dp, end = 22.dp, bottom = bottomSpace),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Spacer(Modifier.height(10.dp))
            Text(
                phrase,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(Modifier.height(12.dp))

            Box(
                Modifier
                    .fillMaxWidth(0.72f)
                    .aspectRatio(1f),
                contentAlignment = Alignment.Center,
            ) {
                Canvas(Modifier.fillMaxSize()) {
                    val stroke = 16.dp.toPx()
                    val inset = stroke / 2
                    drawArc(
                        color = trackColor.copy(alpha = 0.5f),
                        startAngle = 0f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(width = stroke, cap = StrokeCap.Round),
                        topLeft = androidx.compose.ui.geometry.Offset(inset, inset),
                        size = androidx.compose.ui.geometry.Size(size.width - stroke, size.height - stroke),
                    )
                    drawArc(
                        brush = Brush.sweepGradient(progressColors),
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
                            style = MaterialTheme.typography.displaySmall.copy(fontSize = 60.sp),
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

            Spacer(Modifier.height(26.dp))

            Surface(
                onClick = {
                    count += 1
                    prefs.setTasbih(count)
                    vibrate(context)
                },
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shadowElevation = 8.dp,
                modifier = Modifier.size(126.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Filled.AutoAwesome, null, Modifier.size(26.dp))
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "গুনুন",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                listOf(33, 100).forEach { t ->
                    val selected = target == t
                    Surface(
                        onClick = { target = t },
                        shape = RoundedCornerShape(50),
                        color = if (selected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = if (selected) MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                    ) {
                        Text(
                            "$t বার",
                            style = MaterialTheme.typography.labelMedium,
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
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ) {
                    Row(
                        Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(Icons.Filled.Refresh, null, Modifier.size(15.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("রিসেট", style = MaterialTheme.typography.labelMedium)
                    }
                }
            }

            Spacer(Modifier.height(22.dp))
            Text(
                "টিপস: ফাতিমা (রা.)-এর তাসবীহ — ৩৩ বার সুবহানাল্লাহ, ৩৩ বার আলহামদুলিল্লাহ এবং ৩৪ বার আল্লাহু আকবার।",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(16.dp))
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

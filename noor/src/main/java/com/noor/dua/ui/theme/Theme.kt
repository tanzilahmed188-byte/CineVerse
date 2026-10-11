package com.noor.dua.ui.theme

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.noor.dua.R

/* ----------------------------------------------------------------------------
 *  নূর — রঙ (Colours)
 *  শুধু কালো, সাদা ও ধূসর — কোনো রঙিন অ্যাকসেন্ট নেই, তাই লেখা পড়তে সবচেয়ে আরাম।
 *  অ্যাপ ফোনের সেটিং (সিস্টেম ডিফল্ট) অনুযায়ী নিজে থেকেই লাইট বা ডার্ক হয়।
 * -------------------------------------------------------------------------- */

val Ink = Color(0xFF111111)           // মূল কালো (লাইট থিমে প্রধান রঙ)
val InkSoft = Color(0xFF5A5A5A)       // হালকা ধূসর কালি
val Hairline = Color(0xFFE4E4E4)      // সূক্ষ্ম সীমানা/দাগ (লাইট)
val Paper = Color(0xFFFFFFFF)         // সাদা
val Mist = Color(0xFFF1F1F1)          // হালকা ধূসর পৃষ্ঠ
val NightBase = Color(0xFF000000)     // ডার্ক থিমের ব্যাকগ্রাউন্ড
val NightSurface = Color(0xFF121212)  // ডার্ক থিমের কার্ড
val NightMist = Color(0xFF1E1E1E)
val NightHairline = Color(0xFF2E2E2E)
val Snow = Color(0xFFF4F4F4)          // ডার্ক থিমে প্রধান (প্রায় সাদা)

private val LightScheme = lightColorScheme(
    primary = Ink,
    onPrimary = Paper,
    primaryContainer = Color(0xFFEDEDED),
    onPrimaryContainer = Color(0xFF141414),
    secondary = InkSoft,
    onSecondary = Paper,
    secondaryContainer = Color(0xFFE8E8E8),
    onSecondaryContainer = Color(0xFF1A1A1A),
    tertiary = Color(0xFF6B6B6B),
    onTertiary = Paper,
    background = Color(0xFFFAFAFA),
    onBackground = Color(0xFF101010),
    surface = Paper,
    onSurface = Color(0xFF101010),
    surfaceVariant = Mist,
    onSurfaceVariant = Color(0xFF565656),
    outline = Color(0xFFC4C4C4),
    outlineVariant = Hairline,
    error = Color(0xFFB3261E),
    onError = Paper,
)

private val DarkScheme = darkColorScheme(
    primary = Snow,
    onPrimary = Color(0xFF101010),
    primaryContainer = Color(0xFF262626),
    onPrimaryContainer = Color(0xFFF0F0F0),
    secondary = Color(0xFFCFCFCF),
    onSecondary = Color(0xFF141414),
    secondaryContainer = Color(0xFF2B2B2B),
    onSecondaryContainer = Color(0xFFECECEC),
    tertiary = Color(0xFFABABAB),
    onTertiary = Color(0xFF101010),
    background = NightBase,
    onBackground = Color(0xFFEDEDED),
    surface = NightSurface,
    onSurface = Color(0xFFEDEDED),
    surfaceVariant = NightMist,
    onSurfaceVariant = Color(0xFFB2B2B2),
    outline = Color(0xFF3C3C3C),
    outlineVariant = NightHairline,
    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410),
)

/* ----------------------------------------------------------------------------
 *  ফন্ট — বাংলার জন্য Hind Siliguri, আরবির জন্য Amiri (নসখ ক্যালিগ্রাফি)
 *  Google Fonts (SIL Open Font License)
 * -------------------------------------------------------------------------- */

val BengaliFont = FontFamily(
    Font(R.font.hind_regular, FontWeight.Normal),
    Font(R.font.hind_semibold, FontWeight.SemiBold),
    Font(R.font.hind_bold, FontWeight.Bold),
)

val ArabicFont = FontFamily(
    Font(R.font.amiri_regular, FontWeight.Normal),
    Font(R.font.amiri_bold, FontWeight.Bold),
)

/** আরবি টেক্সটের স্টাইল — লম্বা লাইন হাইট ক্যালিগ্রাফিক নসখের জন্য জরুরি */
val ArabicTextStyle: TextStyle
    get() = TextStyle(
        fontFamily = ArabicFont,
        fontWeight = FontWeight.Normal,
        fontSize = 27.sp,
        lineHeight = 56.sp,
        letterSpacing = 0.sp,
    )

private val NoorTypography = Typography().run {
    copy(
        displaySmall = displaySmall.copy(fontFamily = BengaliFont, fontWeight = FontWeight.Bold),
        headlineLarge = headlineLarge.copy(fontFamily = BengaliFont, fontWeight = FontWeight.Bold),
        headlineMedium = headlineMedium.copy(fontFamily = BengaliFont, fontWeight = FontWeight.Bold),
        headlineSmall = headlineSmall.copy(fontFamily = BengaliFont, fontWeight = FontWeight.Bold),
        titleLarge = titleLarge.copy(fontFamily = BengaliFont, fontWeight = FontWeight.Bold),
        titleMedium = titleMedium.copy(fontFamily = BengaliFont, fontWeight = FontWeight.SemiBold),
        titleSmall = titleSmall.copy(fontFamily = BengaliFont, fontWeight = FontWeight.SemiBold),
        bodyLarge = bodyLarge.copy(fontFamily = BengaliFont, lineHeight = 29.sp),
        bodyMedium = bodyMedium.copy(fontFamily = BengaliFont, lineHeight = 26.sp),
        bodySmall = bodySmall.copy(fontFamily = BengaliFont, lineHeight = 21.sp),
        labelLarge = labelLarge.copy(fontFamily = BengaliFont, fontWeight = FontWeight.SemiBold),
        labelMedium = labelMedium.copy(fontFamily = BengaliFont),
        labelSmall = labelSmall.copy(fontFamily = BengaliFont),
    )
}

/**
 * অ্যাপের থিম — ডিফল্টভাবে ফোনের সিস্টেম সেটিং অনুসরণ করে
 * (ফোন ডার্ক মোডে থাকলে অ্যাপও ডার্ক, নাহলে লাইট)।
 */
@Composable
fun NoorTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkScheme else LightScheme,
        typography = NoorTypography,
        content = content,
    )
}

/* ----------------------------------------------------------------------------
 *  ব্যাকড্রপ — খুব হালকা ধূসর গ্রেডিয়েন্ট আর চোখে পড়ার মতো নয় এমন ইসলামি নকশা।
 *  ব্যাটারি বাঁচাতে এখানে আর কোনো অ্যানিমেশন চলে না।
 * -------------------------------------------------------------------------- */

@Composable
fun NoorBackdrop(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    val dark = MaterialTheme.colorScheme.background.luminanceLow()

    Box(modifier = modifier.fillMaxSize()) {
        Canvas(Modifier.fillMaxSize()) {
            drawBackdrop(dark)
        }
        content()
    }
}

private fun Color.luminanceLow(): Boolean = (red * 0.299f + green * 0.587f + blue * 0.114f) < 0.5f

private fun DrawScope.drawBackdrop(dark: Boolean) {
    val w = size.width
    val h = size.height

    val top = if (dark) Color(0xFF0C0C0C) else Color(0xFFFFFFFF)
    val mid = if (dark) Color(0xFF010101) else Color(0xFFF9F9F9)
    val bottom = if (dark) Color(0xFF0A0A0A) else Color(0xFFEFEFEF)

    drawRect(
        brush = Brush.linearGradient(
            colors = listOf(top, mid, bottom),
            start = Offset(0f, 0f),
            end = Offset(w * 0.35f, h),
        ),
        size = size,
    )

    // দুই কোণায় খুব হালকা আলো-ছায়া, যাতে স্ক্রিনটা সমান না দেখায়
    val glowA = if (dark) Color.White.copy(alpha = 0.045f) else Color.Black.copy(alpha = 0.030f)
    val glowB = if (dark) Color.White.copy(alpha = 0.030f) else Color.Black.copy(alpha = 0.020f)

    drawCircle(
        brush = Brush.radialGradient(
            listOf(glowA, Color.Transparent),
            center = Offset(w * 0.15f, h * 0.10f),
            radius = w * 0.62f,
        ),
        radius = w * 0.62f,
        center = Offset(w * 0.15f, h * 0.10f),
    )
    drawCircle(
        brush = Brush.radialGradient(
            listOf(glowB, Color.Transparent),
            center = Offset(w * 0.88f, h * 0.78f),
            radius = w * 0.66f,
        ),
        radius = w * 0.66f,
        center = Offset(w * 0.88f, h * 0.78f),
    )

    // হালকা ইসলামি জ্যামিতিক নকশা (গাঢ় নয়, শুধু যেন পেছনে গঠন থাকে)
    val line = if (dark) Color.White.copy(alpha = 0.020f) else Color.Black.copy(alpha = 0.024f)
    val cell = w / 5.2f
    var row = 0
    while (row * cell < h + cell) {
        var col = 0
        while (col * cell < w + cell) {
            val cx = col * cell + cell / 2f
            val cy = row * cell + cell / 2f
            rotate(45f, Offset(cx, cy)) {
                drawRect(
                    color = line,
                    topLeft = Offset(cx - cell * 0.30f, cy - cell * 0.30f),
                    size = androidx.compose.ui.geometry.Size(cell * 0.60f, cell * 0.60f),
                    style = Stroke(width = 1.1f),
                )
            }
            col++
        }
        row++
    }
}

/** কার্ডের কোণা ও ছায়ার একেক জায়গায় ব্যবহারের জন্য */
object NoorShapes {
    val card = androidx.compose.foundation.shape.RoundedCornerShape(24.dp)
    val chip = androidx.compose.foundation.shape.RoundedCornerShape(50)
    val sheet = androidx.compose.foundation.shape.RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
}

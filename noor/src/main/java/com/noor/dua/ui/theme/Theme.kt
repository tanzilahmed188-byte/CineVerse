package com.noor.dua.ui.theme

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import kotlin.math.cos
import kotlin.math.sin

/* ----------------------------------------------------------------------------
 *  নূর — রঙ (Colours)
 *  গাঢ় পান্না-সবুজ (emerald) + সোনালি (gold) প্যালেট, আরবি-ইসলামি নকশার সাথে মানানসই
 * -------------------------------------------------------------------------- */

val EmeraldDeep = Color(0xFF0E4A38)
val EmeraldInk = Color(0xFF0A2A20)
val EmeraldSoft = Color(0xFFD9EBE1)
val Gold = Color(0xFFD4AF37)
val GoldLight = Color(0xFFEBD08A)
val GoldDeep = Color(0xFF9A7B2E)
val SandLight = Color(0xFFF7F2E7)
val InkNight = Color(0xFF061411)
val NightSurface = Color(0xFF0D1D18)

private val LightScheme = lightColorScheme(
    primary = EmeraldDeep,
    onPrimary = Color(0xFFFFFBF0),
    primaryContainer = EmeraldSoft,
    onPrimaryContainer = EmeraldInk,
    secondary = GoldDeep,
    onSecondary = Color(0xFFFFFBF0),
    secondaryContainer = Color(0xFFF3E4C2),
    onSecondaryContainer = Color(0xFF2E2408),
    tertiary = Color(0xFF3F6B52),
    onTertiary = Color(0xFFFFFFFF),
    background = SandLight,
    onBackground = Color(0xFF1B1C18),
    surface = Color(0xFFFFFDF7),
    onSurface = Color(0xFF1B1C18),
    surfaceVariant = Color(0xFFEDE6D6),
    onSurfaceVariant = Color(0xFF4A4638),
    outline = Color(0xFFC9BFA8),
    outlineVariant = Color(0xFFE2DAC7),
    error = Color(0xFFB3261E),
    onError = Color(0xFFFFFFFF),
)

private val DarkScheme = darkColorScheme(
    primary = GoldLight,
    onPrimary = Color(0xFF21190A),
    primaryContainer = Color(0xFF17352A),
    onPrimaryContainer = Color(0xFFDDF1E6),
    secondary = Color(0xFFD6BC84),
    onSecondary = Color(0xFF231B08),
    secondaryContainer = Color(0xFF2E2711),
    onSecondaryContainer = Color(0xFFF6E4BE),
    tertiary = Color(0xFF9CC7AC),
    onTertiary = Color(0xFF0B2117),
    background = InkNight,
    onBackground = Color(0xFFE8E6DC),
    surface = NightSurface,
    onSurface = Color(0xFFEAE8DF),
    surfaceVariant = Color(0xFF1C2D26),
    onSurfaceVariant = Color(0xFFC6C8BB),
    outline = Color(0xFF40544A),
    outlineVariant = Color(0xFF243831),
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
        bodyLarge = bodyLarge.copy(fontFamily = BengaliFont, lineHeight = 28.sp),
        bodyMedium = bodyMedium.copy(fontFamily = BengaliFont, lineHeight = 25.sp),
        bodySmall = bodySmall.copy(fontFamily = BengaliFont, lineHeight = 20.sp),
        labelLarge = labelLarge.copy(fontFamily = BengaliFont, fontWeight = FontWeight.SemiBold),
        labelMedium = labelMedium.copy(fontFamily = BengaliFont),
        labelSmall = labelSmall.copy(fontFamily = BengaliFont),
    )
}

@Composable
fun NoorTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkScheme else LightScheme,
        typography = NoorTypography,
        content = content,
    )
}

/* ----------------------------------------------------------------------------
 *  অ্যানিমেটেড ব্যাকড্রপ — ধীরে বদলানো গ্রেডিয়েন্ট, গ্লো আর জ্বলজ্বলে তারা
 * -------------------------------------------------------------------------- */

@Composable
fun NoorBackdrop(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    val dark = MaterialTheme.colorScheme.background.luminanceLow()
    val transition = rememberInfiniteTransition(label = "backdrop")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(26000, easing = LinearEasing), RepeatMode.Restart),
        label = "phase",
    )

    Box(modifier = modifier.fillMaxSize()) {
        Canvas(Modifier.fillMaxSize()) {
            drawBackdrop(phase, dark)
        }
        content()
    }
}

private fun Color.luminanceLow(): Boolean = (red * 0.299f + green * 0.587f + blue * 0.114f) < 0.5f

private fun DrawScope.drawBackdrop(phase: Float, dark: Boolean) {
    val w = size.width
    val h = size.height

    val top = if (dark) Color(0xFF0A1F19) else Color(0xFFFDF9F0)
    val mid = if (dark) Color(0xFF061411) else Color(0xFFF6F0E4)
    val bottom = if (dark) Color(0xFF030C09) else Color(0xFFEFE7D6)

    drawRect(
        brush = Brush.linearGradient(
            colors = listOf(top, mid, bottom),
            start = Offset(0f, 0f),
            end = Offset(w * 0.35f, h),
        ),
        size = size,
    )

    // ধীরে ভেসে বেড়ানো আলোর বল (glow blobs)
    val glowA = if (dark) GoldLight.copy(alpha = 0.10f) else Gold.copy(alpha = 0.13f)
    val glowB = if (dark) Color(0xFF2E8B6A).copy(alpha = 0.16f) else Color(0xFF4F9C79).copy(alpha = 0.13f)

    val aX = w * (0.18f + 0.10f * sin(phase * 6.2832f))
    val aY = h * (0.12f + 0.06f * cos(phase * 6.2832f))
    drawCircle(
        brush = Brush.radialGradient(listOf(glowA, Color.Transparent), center = Offset(aX, aY), radius = w * 0.55f),
        radius = w * 0.55f,
        center = Offset(aX, aY),
    )

    val bX = w * (0.85f + 0.08f * cos(phase * 5.1f))
    val bY = h * (0.72f + 0.09f * sin(phase * 5.1f))
    drawCircle(
        brush = Brush.radialGradient(listOf(glowB, Color.Transparent), center = Offset(bX, bY), radius = w * 0.60f),
        radius = w * 0.60f,
        center = Offset(bX, bY),
    )

    // হালকা ইসলামি জ্যামিতিক নকশা
    val line = if (dark) Color.White.copy(alpha = 0.030f) else EmeraldInk.copy(alpha = 0.035f)
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

    // জ্বলজ্বলে তারা (twinkle)
    val sparkle = if (dark) GoldLight.copy(alpha = 0.55f) else GoldDeep.copy(alpha = 0.28f)
    val spots = listOf(
        Triple(0.14f, 0.20f, 0.9f), Triple(0.86f, 0.14f, 1.4f), Triple(0.72f, 0.44f, 2.2f),
        Triple(0.28f, 0.62f, 1.1f), Triple(0.52f, 0.86f, 1.8f), Triple(0.92f, 0.72f, 0.6f),
    )
    spots.forEachIndexed { i, (fx, fy, speed) ->
        val tw = 0.35f + 0.65f * ((sin((phase * speed + i * 0.37f) * 6.2832f) + 1f) / 2f)
        val r = w * 0.006f * (0.7f + tw * 0.9f)
        val c = Offset(w * fx, h * fy)
        drawCircle(color = sparkle.copy(alpha = sparkle.alpha * tw), radius = r, center = c)
        drawLine(
            color = sparkle.copy(alpha = sparkle.alpha * tw * 0.5f),
            start = Offset(c.x - r * 3.2f, c.y),
            end = Offset(c.x + r * 3.2f, c.y),
            strokeWidth = 1f,
        )
        drawLine(
            color = sparkle.copy(alpha = sparkle.alpha * tw * 0.5f),
            start = Offset(c.x, c.y - r * 3.2f),
            end = Offset(c.x, c.y + r * 3.2f),
            strokeWidth = 1f,
        )
    }
}

/** কার্ডের কোণা ও ছায়ার একেক জায়গায় ব্যবহারের জন্য */
object NoorShapes {
    val card = androidx.compose.foundation.shape.RoundedCornerShape(24.dp)
    val chip = androidx.compose.foundation.shape.RoundedCornerShape(50)
    val sheet = androidx.compose.foundation.shape.RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
}

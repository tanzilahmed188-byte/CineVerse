package com.noor.dua.ui

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.noor.dua.ui.theme.NoorBackdrop
import kotlinx.coroutines.launch

/* ---------------------------------------------------------------------- রুট */

/**
 * অ্যাপের পর্দাগুলো। depth = 0 মানে বটম বারের মূল ট্যাব,
 * depth 1-2 মানে ট্যাবের উপরে খোলা বিস্তারিত পর্দা।
 */
sealed interface Route {
    val depth: Int
    val tabKey: String

    data object Home : Route {
        override val depth = 0
        override val tabKey = "home"
    }

    data class Browse(val mode: BrowseMode) : Route {
        override val depth = 0
        override val tabKey = "browse"
    }

    data object Tasbih : Route {
        override val depth = 0
        override val tabKey = "tasbih"
    }

    data object Favorites : Route {
        override val depth = 0
        override val tabKey = "favorites"
    }

    data object More : Route {
        override val depth = 0
        override val tabKey = "more"
    }

    data class CategoryDetail(val catId: String, val mode: BrowseMode = BrowseMode.Dua) : Route {
        override val depth = 1
        override val tabKey = "browse"
    }

    data class DuaDetail(val duaId: String) : Route {
        override val depth = 2
        override val tabKey = "browse"
    }
}

/* -------------------------------------------------------------------- প্রেফারেন্স */

class NoorPrefs(context: Context) {
    private val sp = context.getSharedPreferences("noor_prefs", Context.MODE_PRIVATE)

    fun favorites(): Set<String> = sp.getStringSet("favorites", emptySet()) ?: emptySet()

    fun isFavorite(id: String): Boolean = favorites().contains(id)

    fun toggleFavorite(id: String): Boolean {
        val current = favorites().toMutableSet()
        val added = if (current.contains(id)) current.remove(id) else current.add(id)
        sp.edit().putStringSet("favorites", current).apply()
        return added
    }

    fun tasbih(): Int = sp.getInt("tasbih", 0)
    fun setTasbih(value: Int) = sp.edit().putInt("tasbih", value).apply()
}

@Composable
fun rememberPrefs(): NoorPrefs {
    val context = LocalContext.current
    return remember { NoorPrefs(context) }
}

/* ------------------------------------------------------------------ মূল অ্যাপ */

@Composable
fun NoorApp() {
    val prefs = rememberPrefs()
    val stack = remember { mutableStateListOf<Route>(Route.Home) }
    val current = stack.last()
    var forward by remember { mutableStateOf(true) }
    var favoriteIds by remember { mutableStateOf(prefs.favorites()) }
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    fun push(route: Route) {
        forward = route.depth >= current.depth
        if (route.depth == 0) {
            stack.clear()
            stack.add(route)
        } else {
            stack.add(route)
        }
        favoriteIds = prefs.favorites()
    }

    fun pop() {
        forward = false
        if (stack.size > 1) stack.removeAt(stack.lastIndex)
    }

    fun openTab(route: Route) {
        forward = false
        stack.clear()
        stack.add(route)
    }

    BackHandler(enabled = stack.size > 1) { pop() }

    val bottomInset = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val topInset = WindowInsets.systemBars.asPaddingValues().calculateTopPadding()
    val showBottomBar = current.depth == 0

    NoorBackdrop {
        Box(Modifier.fillMaxSize()) {
            AnimatedContent(
                targetState = current,
                transitionSpec = {
                    val spec = if (forward) {
                        (slideInHorizontally(tween(340)) { it / 6 } + fadeIn(tween(280)))
                            .togetherWith(slideOutHorizontally(tween(340)) { -it / 8 } + fadeOut(tween(200)))
                    } else {
                        (slideInHorizontally(tween(340)) { -it / 6 } + fadeIn(tween(280)))
                            .togetherWith(slideOutHorizontally(tween(340)) { it / 8 } + fadeOut(tween(200)))
                    }
                    spec
                },
                label = "screen",
                modifier = Modifier.fillMaxSize(),
            ) { route ->
                val bottomSpace = if (showBottomBar) 98.dp + bottomInset else 24.dp + bottomInset
                when (route) {
                    Route.Home -> HomeScreen(
                        topInset = topInset,
                        bottomSpace = bottomSpace,
                        onBrowse = { mode -> push(Route.Browse(mode)) },
                        onOpenCategory = { catId -> push(Route.CategoryDetail(catId, BrowseMode.Dua)) },
                        onOpenDua = { push(Route.DuaDetail(it)) },
                    )

                    is Route.Browse -> BrowseScreen(
                        initialMode = route.mode,
                        topInset = topInset,
                        bottomSpace = bottomSpace,
                        favorites = favoriteIds,
                        onOpenCategory = { catId -> push(Route.CategoryDetail(catId, route.mode)) },
                        onOpenDua = { push(Route.DuaDetail(it)) },
                    )

                    Route.Tasbih -> TasbihScreen(
                        topInset = topInset,
                        bottomSpace = bottomSpace,
                        prefs = prefs,
                    )

                    Route.Favorites -> FavoritesScreen(
                        topInset = topInset,
                        bottomSpace = bottomSpace,
                        favorites = favoriteIds,
                        onExplore = { openTab(Route.Browse(BrowseMode.Dua)) },
                        onOpenDua = { push(Route.DuaDetail(it)) },
                    )

                    Route.More -> MoreScreen(
                        topInset = topInset,
                        bottomSpace = bottomSpace,
                        onOpenDua = { push(Route.DuaDetail(it)) },
                        onMessage = { msg -> scope.launch { snackbar.showSnackbar(msg) } },
                    )

                    is Route.CategoryDetail -> CategoryDetailScreen(
                        catId = route.catId,
                        mode = route.mode,
                        topInset = topInset,
                        bottomSpace = bottomSpace,
                        favorites = favoriteIds,
                        onBack = { pop() },
                        onOpenDua = { push(Route.DuaDetail(it)) },
                    )

                    is Route.DuaDetail -> DuaDetailScreen(
                        duaId = route.duaId,
                        topInset = topInset,
                        bottomSpace = bottomSpace,
                        isFavorite = favoriteIds.contains(route.duaId),
                        onBack = { pop() },
                        onToggleFavorite = {
                            prefs.toggleFavorite(route.duaId)
                            favoriteIds = prefs.favorites()
                        },
                        onMessage = { msg -> scope.launch { snackbar.showSnackbar(msg) } },
                    )
                }
            }

            // ভাসমান বটম নেভিগেশন — ৫টি আলাদা বাটন
            AnimatedVisibility(
                visible = showBottomBar,
                enter = fadeIn(tween(360)),
                exit = fadeOut(tween(180)),
                modifier = Modifier.align(Alignment.BottomCenter),
            ) {
                BottomBar(
                    current = current,
                    bottomInset = bottomInset,
                    onSelect = { route -> if (route.tabKey != current.tabKey) openTab(route) },
                )
            }

            SnackbarHost(
                hostState = snackbar,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = if (showBottomBar) 106.dp + bottomInset else 16.dp),
            )
        }
    }

    // প্রিয় তালিকা অন্য স্ক্রিন থেকে বদলালেও সিঙ্ক রাখা
    LaunchedEffect(stack.size) { favoriteIds = prefs.favorites() }
}

/* ------------------------------------------------------------- বটম নেভিগেশন বার */

private data class NavItem(val route: Route, val label: String, val icon: ImageVector)

@Composable
private fun BottomBar(current: Route, bottomInset: Dp, onSelect: (Route) -> Unit) {
    val items = listOf(
        NavItem(Route.Home, "হোম", Icons.Filled.Home),
        NavItem(Route.Browse(BrowseMode.Dua), "দুআ", Icons.Filled.MenuBook),
        NavItem(Route.Tasbih, "তাসবিহ", Icons.Filled.AutoAwesome),
        NavItem(Route.Favorites, "প্রিয়", Icons.Filled.Favorite),
        NavItem(Route.More, "আরও", Icons.Filled.MoreHoriz),
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 14.dp, end = 14.dp, bottom = 14.dp + bottomInset),
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.97f),
        shadowElevation = 12.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 7.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            items.forEach { item ->
                val selected = current.tabKey == item.route.tabKey
                val pillColor by animateColorAsState(
                    targetValue = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent,
                    animationSpec = tween(300),
                    label = "pill",
                )
                val contentColor = if (selected) MaterialTheme.colorScheme.onPrimary
                else MaterialTheme.colorScheme.onSurfaceVariant

                Column(
                    Modifier
                        .weight(1f)
                        .selectable(selected = selected, onClick = { onSelect(item.route) })
                        .padding(vertical = 3.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box(
                        Modifier
                            .background(pillColor, RoundedCornerShape(50))
                            .padding(horizontal = 14.dp, vertical = 5.dp),
                    ) {
                        Icon(
                            item.icon,
                            contentDescription = item.label,
                            tint = contentColor,
                            modifier = Modifier.size(21.dp),
                        )
                    }
                    Spacer(Modifier.height(3.dp))
                    Text(
                        item.label,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                        color = contentColor,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                    )
                }
            }
        }
    }
}

/** সব স্ক্রিনে ব্যবহারের জন্য ভাসমান হেডার */
@Composable
fun ScreenHeader(
    title: String,
    subtitle: String? = null,
    topInset: Dp,
    onBack: (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(top = topInset + 10.dp, start = 18.dp, end = 18.dp, bottom = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (onBack != null) {
            Surface(
                onClick = onBack,
                shape = RoundedCornerShape(50),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.size(38.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "ফিরে যান",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(19.dp),
                    )
                }
            }
            Spacer(Modifier.width(12.dp))
        }
        Column(Modifier.weight(1f)) {
            Text(
                title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
            )
            if (subtitle != null) {
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                )
            }
        }
        trailing?.invoke()
    }
}

@Composable
fun FavoriteIconButton(isFavorite: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        color = if (isFavorite) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.size(40.dp),
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                Icons.Filled.Favorite,
                contentDescription = if (isFavorite) "প্রিয় তালিকা থেকে সরান" else "প্রিয় তালিকায় রাখুন",
                tint = if (isFavorite) MaterialTheme.colorScheme.onPrimary
                else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(19.dp),
            )
        }
    }
}

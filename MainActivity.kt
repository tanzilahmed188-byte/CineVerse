package com.cineverse.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

private val Bg = Color(0xFF080B12)
private val Panel = Color(0xFF121925)
private val Red = Color(0xFFFF3048)
private val Muted = Color(0xFF9AA7BA)

data class Movie(
    val title: String, val year: Int, val genre: String, val rating: String,
    val synopsis: String, val poster: String, val backdrop: String, val trailerQuery: String
)

private val movies = listOf(
    Movie("Interstellar",2014,"Sci-Fi","8.7","A team travels through a wormhole in space in a search for humanity's future.","/gEU2QniE6E77NI6lCU6Mxe7gXIi.jpg","/xJHokMbljvjADYdit5fK5VQsXEG.jpg","Interstellar official trailer"),
    Movie("Dune: Part Two",2024,"Sci-Fi","8.5","Paul Atreides unites with Chani and the Fremen while seeking revenge.","/1pdfLvkbY9ohJlCjQH2CZjjYVvJ.jpg","/zViRwl3ySscZnbXZJ2Q9wq3SeUG.jpg","Dune Part Two official trailer"),
    Movie("The Dark Knight",2008,"Action","9.0","Batman faces a criminal mastermind who pushes Gotham into chaos.","/qJ2tW6WMUDux911r6m7haRef0WH.jpg","/hkBaDkMWbLaf8B1lsWsKX7Ew3Xq.jpg","The Dark Knight official trailer"),
    Movie("Spider-Man: Across the Spider-Verse",2023,"Animation","8.7","Miles Morales journeys across the multiverse and meets a team of Spider-People.","/8Vt6mWEReuy4Of61Lnj5Xj704m8.jpg","/4HodYYKEIsGOdinkGi2Ucz6X9i0.jpg","Spider-Man Across the Spider-Verse official trailer"),
    Movie("Joker: Folie à Deux",2024,"Drama","5.8","A troubled performer finds an unexpected connection amid public scrutiny.","/aciP8Km0waTLXEYf5YKQ96lEVaJ.jpg","/u2eA9pqi1q3DvevT7RuDuJHxxBT.jpg","Joker Folie a Deux official trailer"),
    Movie("John Wick",2014,"Action","7.4","A retired hitman returns to the underworld after a devastating loss.","/fZPSd91yGE9fCfRTHOpVvN6V0h.jpg","/umC04Cozevu8nn3JTDJ1pc7PVTn.jpg","John Wick official trailer"),
    Movie("Inception",2010,"Sci-Fi","8.8","A skilled thief enters dreams to attempt an impossible act.","/oYuLEt3zVCKq57qu2F8dT7NIa6f.jpg","/s3TBrRGB1iav7gFOCNx3H31MoES.jpg","Inception official trailer"),
    Movie("Gladiator",2000,"Action","8.5","A betrayed Roman general fights his way toward justice.","/ty8TGRuvJLPUmAR1H1nRIsgwvim.jpg","/hND7xAaxiBusrq2l9itbq3bW0g.jpg","Gladiator 2000 official trailer"),
    Movie("The Conjuring",2013,"Horror","7.5","Paranormal investigators help a family facing a terrifying presence.","/wVYREutTvI2tmhv8DfihzP7d2B.jpg","/5aXGEB7zYx5QkRjJ5l1V7Z6Yj7.jpg","The Conjuring official trailer"),
    Movie("Parasite",2019,"Thriller","8.5","Two families become entangled in a story of class and deception.","/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg","/TU9NIjwzjoKPwQHoHshkFcQUCG.jpg","Parasite 2019 official trailer"),
    Movie("Breaking Bad",2008,"Crime","9.5","A chemistry teacher enters the criminal world after a life-changing diagnosis.","/3xnWaLQengkXEhG5M7r9zO9nFdx.jpg","/eSzpy96DwBartyQuXmoE6orugug.jpg","Breaking Bad official trailer"),
    Movie("Stranger Things",2016,"Sci-Fi","8.7","A group of friends uncover strange events in their small town.","/49WJfeN0moxb9IPf Gn8AIqMGskD.jpg".replace(" ",""),"/56v2KjBlU4XaOv9rVYEQypROD7P.jpg","Stranger Things official trailer")
)
private fun img(path: String) = "https://image.tmdb.org/t/p/w500$path"
private fun backdrop(path: String) = "https://image.tmdb.org/t/p/w780$path"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val prefs = remember { getSharedPreferences("cineverse", MODE_PRIVATE) }
            var tab by remember { mutableStateOf("Home") }
            var selected by remember { mutableStateOf<Movie?>(null) }
            var query by remember { mutableStateOf("") }
            var genre by remember { mutableStateOf("All") }
            var saved by remember { mutableStateOf(prefs.getStringSet("watchlist", emptySet())?.toSet() ?: emptySet()) }
            fun toggle(title: String) {
                saved = if (saved.contains(title)) saved - title else saved + title
                prefs.edit().putStringSet("watchlist", saved.toSet()).apply()
            }
            MaterialTheme(colorScheme = darkColorScheme(primary=Red, background=Bg, surface=Panel)) {
                Surface(Modifier.fillMaxSize(), color=Bg) {
                    Column {
                        AnimatedContent(targetState = selected, transitionSpec = { fadeIn() togetherWith fadeOut() }, label="screen") { movie ->
                            if (movie != null) DetailScreen(movie, saved.contains(movie.title),
                                onBack={selected=null}, onToggle={toggle(movie.title)}, onTrailer={openTrailer(movie.trailerQuery)})
                            else when(tab) {
                                "Search" -> SearchScreen(query,{query=it},genre,{genre=it},movies.filter { (it.title.contains(query,true)||it.genre.contains(query,true)) && (genre=="All"||it.genre==genre) },{selected=it})
                                "My List" -> MovieListScreen("My List",movies.filter { saved.contains(it.title) },{selected=it})
                                "Categories" -> SearchScreen(query,{query=it},genre,{genre=it},movies.filter { genre=="All"||it.genre==genre },{selected=it},showSearch=false)
                                else -> HomeScreen(movies,{selected=it},saved)
                            }
                        }
                        if(selected==null) BottomBar(tab,{tab=it; if(it!="Search") query=""})
                    }
                }
            }
        }
    }
    private fun openTrailer(q: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/results?search_query="+Uri.encode(q)))
        startActivity(intent)
    }
}

@Composable
fun BottomBar(tab:String,onSelect:(String)->Unit) {
    NavigationBar(containerColor=Color(0xFF0D121B)) {
        listOf("Home","Search","Categories","My List").forEach { item ->
            val icon=when(item) { "Home"->Icons.Default.Home; "Search"->Icons.Default.Search; "Categories"->Icons.Default.GridView; else->Icons.Default.Bookmark }
            NavigationBarItem(selected=tab==item,onClick={onSelect(item)},icon={Icon(icon,null)},label={Text(item,fontSize=10.sp)},colors=NavigationBarItemDefaults.colors(selectedIconColor=Red,selectedTextColor=Red,indicatorColor=Red.copy(alpha=.12f),unselectedIconColor=Muted,unselectedTextColor=Muted))
        }
    }
}

@Composable
fun HomeScreen(list:List<Movie>,open:(Movie)->Unit,saved:Set<String>) {
    LazyColumn(contentPadding=PaddingValues(bottom=18.dp),verticalArrangement=Arrangement.spacedBy(18.dp)) {
        item { Row(Modifier.fillMaxWidth().padding(horizontal=20.dp,vertical=18.dp),verticalAlignment=Alignment.CenterVertically) {
            Text("Cine",color=Color.White,fontSize=27.sp,fontWeight=FontWeight.Black); Text("Verse",color=Red,fontSize=27.sp,fontWeight=FontWeight.Black)
            Spacer(Modifier.weight(1f)); Icon(Icons.Default.LocalMovies,null,tint=Red,modifier=Modifier.size(28.dp))
        } }
        item {
            val hero=list[1]
            Box(Modifier.padding(horizontal=16.dp).fillMaxWidth().height(310.dp).clip(RoundedCornerShape(26.dp)).clickable { open(hero) }) {
                AsyncImage(backdrop(hero.backdrop),null,Modifier.fillMaxSize(),contentScale=ContentScale.Crop)
                Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent,Color(0xF0080B12)))))
                Column(Modifier.align(Alignment.BottomStart).padding(20.dp)) {
                    Text("C I N E M A T I C   P I C K",color=Color(0xFFFFB4A7),fontSize=10.sp,fontWeight=FontWeight.Bold)
                    Text(hero.title,color=Color.White,fontSize=28.sp,fontWeight=FontWeight.ExtraBold)
                    Text("${hero.year} • ${hero.genre} • ★ ${hero.rating}",color=Color.LightGray,fontSize=12.sp)
                    Spacer(Modifier.height(12.dp))
                    Button(onClick={open(hero)},colors=ButtonDefaults.buttonColors(containerColor=Red),shape=RoundedCornerShape(14.dp)) { Icon(Icons.Default.Info,null); Spacer(Modifier.width(6.dp)); Text("Explore") }
                }
            }
        }
        item { SectionTitle("Trending Now") }; item { MovieStrip(list.take(6),open) }
        item { SectionTitle("Popular Movies") }; item { MovieStrip(list.drop(2).take(6),open) }
        item { SectionTitle("Your Watchlist") }; item { MovieStrip(list.filter { saved.contains(it.title) },open) }
    }
}
@Composable fun SectionTitle(text:String) { Text(text,color=Color.White,fontSize=19.sp,fontWeight=FontWeight.Bold,modifier=Modifier.padding(horizontal=18.dp)) }

@Composable
fun MovieStrip(list:List<Movie>,open:(Movie)->Unit) {
    Row(Modifier.horizontalScroll(rememberScrollState()).padding(horizontal=16.dp),horizontalArrangement=Arrangement.spacedBy(12.dp)) {
        list.forEach { m -> Column(Modifier.width(132.dp).clickable { open(m) }) {
            Box(Modifier.fillMaxWidth().height(190.dp).clip(RoundedCornerShape(18.dp)).background(Panel)) {
                AsyncImage(img(m.poster),m.title,Modifier.fillMaxSize(),contentScale=ContentScale.Crop)
                Box(Modifier.align(Alignment.BottomStart).padding(8.dp).clip(RoundedCornerShape(8.dp)).background(Color.Black.copy(alpha=.72f)).padding(horizontal=7.dp,vertical=4.dp)) { Text("★ ${m.rating}",color=Color(0xFFFFD166),fontSize=11.sp,fontWeight=FontWeight.Bold) }
            }
            Spacer(Modifier.height(7.dp)); Text(m.title,color=Color.White,fontSize=13.sp,fontWeight=FontWeight.SemiBold,maxLines=1,overflow=TextOverflow.Ellipsis)
            Text("${m.year} • ${m.genre}",color=Muted,fontSize=11.sp,maxLines=1)
        } }
    }
}

@Composable
fun SearchScreen(query:String,onQuery:(String)->Unit,genre:String,onGenre:(String)->Unit,list:List<Movie>,open:(Movie)->Unit,showSearch:Boolean=true) {
    Column(Modifier.fillMaxSize().padding(horizontal=16.dp)) {
        Text(if(showSearch) "Discover" else "Categories",color=Color.White,fontSize=28.sp,fontWeight=FontWeight.ExtraBold,modifier=Modifier.padding(top=22.dp,bottom=14.dp))
        if(showSearch) OutlinedTextField(value=query,onValueChange=onQuery,modifier=Modifier.fillMaxWidth(),placeholder={Text("Search movies, genres...")},leadingIcon={Icon(Icons.Default.Search,null)},singleLine=true,shape=RoundedCornerShape(16.dp),colors=OutlinedTextFieldDefaults.colors(focusedBorderColor=Red,unfocusedBorderColor=Panel,focusedTextColor=Color.White,unfocusedTextColor=Color.White))
        Row(Modifier.horizontalScroll(rememberScrollState()).padding(vertical=14.dp),horizontalArrangement=Arrangement.spacedBy(8.dp)) {
            listOf("All","Action","Sci-Fi","Drama","Animation","Horror","Thriller","Crime").forEach { g -> FilterChip(selected=genre==g,onClick={onGenre(g)},label={Text(g)},colors=FilterChipDefaults.filterChipColors(selectedContainerColor=Red,selectedLabelColor=Color.White)) }
        }
        if(list.isEmpty()) Box(Modifier.fillMaxSize(),contentAlignment=Alignment.Center) { Text("No movies found",color=Muted) }
        else LazyColumn(verticalArrangement=Arrangement.spacedBy(10.dp),contentPadding=PaddingValues(bottom=18.dp)) { items(list) { MovieRow(it,open) } }
    }
}

@Composable
fun MovieListScreen(title:String,list:List<Movie>,open:(Movie)->Unit) {
    Column(Modifier.fillMaxSize().padding(horizontal=16.dp)) {
        Text(title,color=Color.White,fontSize=28.sp,fontWeight=FontWeight.ExtraBold,modifier=Modifier.padding(top=22.dp,bottom=16.dp))
        if(list.isEmpty()) Box(Modifier.fillMaxSize(),contentAlignment=Alignment.Center) { Column(horizontalAlignment=Alignment.CenterHorizontally) { Icon(Icons.Default.BookmarkBorder,null,tint=Muted,modifier=Modifier.size(42.dp)); Text("Your list is empty",color=Muted,modifier=Modifier.padding(top=10.dp)) } }
        else LazyColumn(verticalArrangement=Arrangement.spacedBy(10.dp)) { items(list) { MovieRow(it,open) } }
    }
}

@Composable
fun MovieRow(m:Movie,open:(Movie)->Unit) {
    Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(Panel).clickable { open(m) }.padding(10.dp),verticalAlignment=Alignment.CenterVertically) {
        AsyncImage(img(m.poster),m.title,Modifier.size(74.dp,96.dp).clip(RoundedCornerShape(12.dp)),contentScale=ContentScale.Crop)
        Column(Modifier.weight(1f).padding(start=12.dp)) {
            Text(m.title,color=Color.White,fontWeight=FontWeight.Bold,fontSize=15.sp,maxLines=1,overflow=TextOverflow.Ellipsis)
            Text("${m.year} • ${m.genre} • ★ ${m.rating}",color=Muted,fontSize=11.sp,modifier=Modifier.padding(top=4.dp))
            Text(m.synopsis,color=Color(0xFFCFD6E0),fontSize=12.sp,maxLines=2,overflow=TextOverflow.Ellipsis,modifier=Modifier.padding(top=5.dp))
        }
        Icon(Icons.Default.ChevronRight,null,tint=Muted)
    }
}

@Composable
fun DetailScreen(m:Movie,saved:Boolean,onBack:()->Unit,onToggle:()->Unit,onTrailer:()->Unit) {
    Column(Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxWidth().height(330.dp)) {
            AsyncImage(backdrop(m.backdrop),m.title,Modifier.fillMaxSize(),contentScale=ContentScale.Crop)
            Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Black.copy(alpha=.15f),Bg))))
            IconButton(onClick=onBack,modifier=Modifier.align(Alignment.TopStart).padding(10.dp).background(Color.Black.copy(alpha=.45f),RoundedCornerShape(50))) { Icon(Icons.Default.ArrowBack,null,tint=Color.White) }
            Text(m.title,color=Color.White,fontSize=28.sp,fontWeight=FontWeight.ExtraBold,modifier=Modifier.align(Alignment.BottomStart).padding(20.dp))
        }
        Column(Modifier.verticalScroll(rememberScrollState()).padding(20.dp)) {
            Text("${m.year} • ${m.genre} • ★ ${m.rating}",color=Color(0xFFFFD166),fontWeight=FontWeight.Bold,fontSize=13.sp)
            Spacer(Modifier.height(16.dp)); Text("Overview",color=Color.White,fontSize=20.sp,fontWeight=FontWeight.Bold)
            Spacer(Modifier.height(8.dp)); Text(m.synopsis,color=Color(0xFFCAD2DE),fontSize=15.sp,lineHeight=23.sp)
            Spacer(Modifier.height(22.dp))
            Button(onClick=onTrailer,modifier=Modifier.fillMaxWidth().height(52.dp),colors=ButtonDefaults.buttonColors(containerColor=Red),shape=RoundedCornerShape(16.dp)) { Icon(Icons.Default.PlayArrow,null); Spacer(Modifier.width(8.dp)); Text("Find Official Trailer") }
            Spacer(Modifier.height(10.dp))
            OutlinedButton(onClick=onToggle,modifier=Modifier.fillMaxWidth().height(50.dp),shape=RoundedCornerShape(16.dp)) { Icon(if(saved) Icons.Default.BookmarkAdded else Icons.Default.BookmarkAdd,null); Spacer(Modifier.width(8.dp)); Text(if(saved) "Saved to My List" else "Add to My List") }
            Spacer(Modifier.height(22.dp)); Text("More like this",color=Color.White,fontSize=18.sp,fontWeight=FontWeight.Bold)
            Text("Explore more ${m.genre} titles in Search.",color=Muted,modifier=Modifier.padding(top=7.dp))
        }
    }
}

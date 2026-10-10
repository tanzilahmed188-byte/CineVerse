package com.noor.dua.data

import android.content.Context
import org.json.JSONObject

data class DuaCategory(
    val id: String,
    val order: Int,
    val icon: String,
    val title: String,
    val subtitle: String,
)

data class Dua(
    val id: String,
    val cat: String,
    val title: String,
    val arabic: String,
    val pron: String,
    val bn: String,
    val fazilat: String,
    val ref: String,
    val grade: String,
    val tag: String,
    val amal: String,
)

/**
 * সব দুআ অ্যাপের ভেতরেই (assets/data/duas_1.json, duas_2.json) রাখা হয়েছে —
 * ইন্টারনেট ছাড়াই কাজ করে এবং প্রতিটি দুআর সাথে কুরআন/হাদিস রেফারেন্স দেওয়া আছে।
 */
object DuaRepository {

    private const val FILES = "data"

    private val _categories = mutableListOf<DuaCategory>()
    private val _duas = mutableListOf<Dua>()
    private var loaded = false

    val categories: List<DuaCategory> get() = _categories
    val duas: List<Dua> get() = _duas

    fun load(context: Context) {
        if (loaded) return
        loaded = true
        val assets = context.assets
        val names = assets.list(FILES)?.sorted().orEmpty()
        names.filter { it.endsWith(".json") }.forEach { name ->
            val text = assets.open("$FILES/$name").bufferedReader().use { it.readText() }
            parse(text)
        }
        _categories.sortBy { it.order }
    }

    private fun parse(text: String) {
        val root = JSONObject(text)
        root.optJSONArray("categories")?.let { cats ->
            for (i in 0 until cats.length()) {
                val c = cats.getJSONObject(i)
                _categories += DuaCategory(
                    id = c.getString("id"),
                    order = c.optInt("order", 99),
                    icon = c.optString("icon", "auto_awesome"),
                    title = c.getString("title"),
                    subtitle = c.optString("subtitle"),
                )
            }
        }
        root.optJSONArray("duas")?.let { duas ->
            for (i in 0 until duas.length()) {
                val d = duas.getJSONObject(i)
                _duas += Dua(
                    id = d.getString("id"),
                    cat = d.getString("cat"),
                    title = d.getString("title"),
                    arabic = d.optString("arabic"),
                    pron = d.optString("pron"),
                    bn = d.optString("bn"),
                    fazilat = d.optString("fazilat"),
                    ref = d.optString("ref"),
                    grade = d.optString("grade"),
                    tag = d.optString("tag"),
                    amal = d.optString("amal"),
                )
            }
        }
    }

    fun category(id: String): DuaCategory? = _categories.firstOrNull { it.id == id }

    fun byCategory(catId: String): List<Dua> = _duas.filter { it.cat == catId }

    fun byId(id: String): Dua? = _duas.firstOrNull { it.id == id }

    fun countOf(catId: String): Int = _duas.count { it.cat == catId }

    fun search(query: String): List<Dua> {
        val q = query.trim()
        if (q.isEmpty()) return emptyList()
        return _duas.filter {
            it.title.contains(q, true) || it.bn.contains(q, true) ||
                it.pron.contains(q, true) || it.ref.contains(q, true) ||
                it.fazilat.contains(q, true) || it.arabic.contains(q)
        }
    }

    /** দিন অনুযায়ী স্থির "আজকের দুআ" — প্রতিদিন বদলাবে */
    fun duaOfTheDay(dayOfYear: Int): Dua? =
        if (_duas.isEmpty()) null else _duas[dayOfYear % _duas.size]
}

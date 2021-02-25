package com.ndk.server.manager

import com.ndk.server.model.MusicOnline
import org.jsoup.Jsoup
import org.springframework.stereotype.Component

@Suppress("NAME_SHADOWING")
@Component
open class SongManager {
    fun searchSong(songName: String?, pageNumber: Int): Any {
        return if (songName == null || songName == "") {
            searchMusic("", pageNumber, "https://chiasenhac.vn/mp3/us-uk.html?tab=bai-hat-moi&page={1}")
        } else {
            searchMusic(songName, pageNumber, "https://chiasenhac.vn/tim-kiem?q={0}&page_music={1}&filter=")
        }
    }



    private fun searchMusic(songName: String, page: Int = 1, linkOrigin: String): MutableList<MusicOnline> {
        var newName = songName
        while (newName.contains("  ")) newName = newName.replace("  ", " ").trim()
        val link = linkOrigin
            .replace("{0}", newName)
            .replace("{1}", page.toString())
            .replace(" ", "+")
        val listMusic = mutableListOf<MusicOnline>()
        val doc = Jsoup.connect(link).get()
        for (element in doc.selectFirst("div.tab-content").select("li.media")) {
            try {
                var linkHtml = element.selectFirst("div.media-left").selectFirst("a").attr("href")
                if (!linkHtml.startsWith("http")) {
                    linkHtml = "https://vi.chiasenhac.vn$linkHtml"
                }
                val title = element.selectFirst("div.media-left").selectFirst("a").attr("title")
                val linkImage2 =
                    element.selectFirst("div.media-left").selectFirst("a").selectFirst("img")
                        .attr("src")
                val linkImage = linkImage2.replace("cover_thumb", "cover")
                val artist = element.selectFirst("div.author").text()
                val linkMusic: String? = null
                val lyric = getLyrics(linkHtml)
                val id = (0..5).random()
                listMusic.add(
                    MusicOnline(
                        title, artist, linkHtml, linkImage, id = id.toString(), linkMusic = linkMusic, lyric = lyric
                    )
                )
            } catch (e: Exception) {
            }
        }
        return listMusic
    }

    private fun getLyrics(linkHtml: String): String {
        val doc = Jsoup.connect(linkHtml).get()
        var link = ""
        val els = doc.select("div.tab-content")
        for (e in els.first().select("div.tab-pane")
            .first().select("article")) {
            link = e.select("div#fulllyric").text()
        }
        var s = ""
        val array: List<String> = link.split(Regex("(?=\\p{Upper})"))
        for (element in array) {
            s += if (element.length <= 5) {
                element
            } else {
                element + "\n"
            }
        }
        return s
    }


}
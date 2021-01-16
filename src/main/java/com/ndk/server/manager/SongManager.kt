package com.ndk.server.manager

import com.ndk.server.model.MusicOnline
import org.jsoup.Jsoup
import org.springframework.stereotype.Component

@Suppress("NAME_SHADOWING")
@Component
open class SongManager {
    fun searchSong(songName:String?,pageNumber:Int):Any{
        return if (songName == null|| songName == "") {
            searchMusic("",pageNumber,"https://chiasenhac.vn/mp3/vietnam.html?tab=bai-hat-moi&page={1}")
        }else{
            searchMusic(songName,pageNumber,"https://chiasenhac.vn/tim-kiem?q={0}&page_music={1}&filter=")
        }
    }

    fun searchSongs(songName:String?):Any{
        return if (songName == null|| songName == "") {

            val listMusic = mutableListOf<MusicOnline>()
            var k = 1
            for(i in 0..3){
                listMusic.addAll(searchMusic("",k,"https://chiasenhac.vn/mp3/vietnam.html?tab=bai-hat-moi&page={1}"))
                k++
            }
            listMusic
        }else{
            val listMusic = mutableListOf<MusicOnline>()
            var k = 1
            for(i in 0..3){
                listMusic.addAll(searchMusic(songName, k,"https://chiasenhac.vn/tim-kiem?q={0}&page_music={1}&filter="))
                k++
            }
            listMusic

        }
    }

    private fun searchMusic(songName: String, page: Int = 1, linkOrigin:String): MutableList<MusicOnline> {
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
                val linkImage =
                    element.selectFirst("div.media-left").selectFirst("a").selectFirst("img")
                        .attr("src")
                val artist = element.selectFirst("div.author").text()

                var linkMusic:String? = null
                var lyric:String? = null
                val doc2 = Jsoup.connect(linkHtml).get()
                val els = doc2.select("div.tab-content")
                var link2 :String? = null
                val list = mutableListOf<String>()
                for (e in els.first().select("ul.list-unstyled")
                    .first().select("a.download_item")) {
                     link2 = e.attr("href")
//                    if (link2.contains(".mp3")) {
                        list.add(link2)

//                    }

                }
                linkMusic = list[1]
                for (e in els.first().select("div.tab-pane")
                    .first().select("article")) {
                    lyric = e.select("div#fulllyric").text()
                }

                listMusic.add(
                    MusicOnline(
                        title, artist, linkHtml, linkImage,id = linkHtml,linkMusic = linkMusic,lyric = lyric
                    )
                )
            } catch (e: Exception) {

            }
        }

        return listMusic

    }

    fun getLinkMusic(linkHtml: String): String? {
        val doc = Jsoup.connect(linkHtml).get()
        val els = doc.select("div.tab-content")
        for (e in els.first().select("ul.list-unstyled")
            .first().select("a.download_item")) {
            val link = e.attr("href")
            if (link.contains(".mp3")) {
                return link
            }
        }

        return null
    }




}
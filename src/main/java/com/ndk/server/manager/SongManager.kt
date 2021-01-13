package com.ndk.server.manager

import com.ndk.server.model.MusicOnline
import org.jsoup.Jsoup
import org.springframework.stereotype.Component

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
            for(i in 0..30){
                listMusic.addAll(searchMusic("",k,"https://chiasenhac.vn/mp3/vietnam.html?tab=bai-hat-moi&page={1}"))
                k++
            }
            listMusic
        }else{
            val listMusic = mutableListOf<MusicOnline>()
            var k = 1
            for(i in 0..30){
                listMusic.addAll(searchMusic(songName, k,"https://chiasenhac.vn/tim-kiem?q={0}&page_music={1}&filter="))
                k++
            }
            listMusic

        }
    }

    private fun searchMusic(songName: String, page: Int = 1, linkOrigin:String): MutableList<MusicOnline> {
        var newName = songName
        while (newName.contains("  ")) {
            newName = newName.replace("  ", " ")
        }
        newName = newName.trim()
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
                listMusic.add(
                    MusicOnline(
                        title, artist, linkHtml, linkImage,id = linkHtml
                    )
                )
            } catch (e: Exception) {

            }
        }

        return listMusic

    }


}
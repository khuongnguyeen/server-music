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
                val lyric = getLyrics(linkHtml)

//                var link2 :String? = null
//                for (e in els.first().select("ul.list-unStyled".toLowerCase())
//                    .first().select("a.download_item")) {
//                     link2 = e.attr("href")
//                    if (link2.contains(".mp3")) {
//                        linkMusic = "-----------------------------------------------"
//                    }
//                }


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

//    private fun getLyrics(lyric :String?):String{
//        if(lyric == null) return "Không có lời bài hát !!!!!!!!!"
//        var s = ""
//        val array: List<String> = lyric.split(Regex("(?=\\p{Upper})"))
//        for(i in 0..array.size){
//            s += array[i] +"\n"
//        }
//        return s
//    }

    private fun getLyrics(linkHtml: String): String {
        val doc = Jsoup.connect(linkHtml).get()
        var link =""
        val els = doc.select("div.tab-content")
        for (e in els.first().select("div.tab-pane")
            .first().select("article")) {
             link  = e.select("div#fulllyric").text()
        }
        val array: List<String> = link.split(Regex("(?=\\p{Upper})"))
        link =""
        for(element in array){
            link += element +"\n"
        }
                return link

    }




}
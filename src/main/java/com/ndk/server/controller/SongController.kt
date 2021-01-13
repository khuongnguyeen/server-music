package com.ndk.server.controller

import com.ndk.server.manager.SongManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
open class SongController {
    @Autowired
    private lateinit var songManager: SongManager

    @GetMapping("/api/searchSong")
    fun searchSong(
        @RequestParam("songName", required = false) songName: String?,
        @RequestParam("page", required = false, defaultValue = "1") pageNumber: Int
    ): Any {
        return songManager.searchSong(songName,pageNumber)
    }


    @GetMapping("/api/searchSongs")
    fun searchSongs(
        @RequestParam("songName", required = false) songName: String?
    ): Any {
        return songManager.searchSongs(songName)
    }






}
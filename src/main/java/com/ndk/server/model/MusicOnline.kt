package com.ndk.server.model


data class MusicOnline(
    val songName:String,
    val artistName:String,
    val linkSong:String,
    var linkImage:String?=null,
    var linkMusic:String?=null,
    var lyric:String?=null,
    val id:String=""
)
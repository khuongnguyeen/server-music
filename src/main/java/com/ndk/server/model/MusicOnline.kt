package com.ndk.server.model


data class MusicOnline(
    val songName:String,
    val artistName:String,
    val linkSong:String,
    var linkImage:String?=null,
    var linkMusic:String?=null,
    val id:String=""
)
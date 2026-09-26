package com.example.netcloudmusic

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
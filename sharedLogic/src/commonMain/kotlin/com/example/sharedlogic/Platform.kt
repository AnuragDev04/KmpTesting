package com.example.sharedlogic

interface Platform {
  val name: String
}

expect fun getPlatform(): Platform
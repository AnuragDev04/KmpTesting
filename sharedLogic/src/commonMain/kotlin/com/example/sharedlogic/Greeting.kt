package com.example.sharedlogic

class Greeting {
  fun greet(): String = "CareHome on ${getPlatform().name}"
}
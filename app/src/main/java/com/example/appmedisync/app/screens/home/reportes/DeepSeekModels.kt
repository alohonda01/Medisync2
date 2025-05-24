package com.example.appmedisync.app.screens.home.reportes

data class DeepSeekRequest(
    val model: String = "deepseek-chat",
    val messages: List<Message>
)

data class Message(
    val role: String,
    val content: String
)

data class DeepSeekResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: Message
)
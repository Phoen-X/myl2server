package com.l2server.network.communication


data class SessionId(private val id: Int) {
    fun toInt() = id
}
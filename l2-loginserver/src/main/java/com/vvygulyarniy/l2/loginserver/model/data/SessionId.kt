package com.vvygulyarniy.l2.loginserver.model.data


data class SessionId(private val id: Int) {
    fun toInt() = id
}
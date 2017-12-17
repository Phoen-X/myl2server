package com.vvygulyarniy.l2.loginserver.netty

import com.l2server.crypt.LoginCrypt
import com.vvygulyarniy.l2.loginserver.model.data.SessionId
import io.netty.channel.ChannelHandlerContext
import io.netty.util.AttributeKey


private val cryptAttributeName = "crypt"

fun ChannelHandlerContext.setCrypt(value: LoginCrypt) = this.setAttribute(cryptAttributeName, value)
fun ChannelHandlerContext.getCrypt(): LoginCrypt = this.getAttribute(cryptAttributeName) ?: throw RuntimeException("Cannot find crypt for the given connection")

fun ChannelHandlerContext.getSessionId(): SessionId = this.getAttribute("sessionId") ?: throw RuntimeException("Cannot find session Id for the connection")
fun ChannelHandlerContext.setSessionId(value: SessionId) = this.setAttribute("sessionId", value)

fun <T> ChannelHandlerContext.getAttribute(attributeName: String): T? {
    return this.channel().attr(AttributeKey.valueOf<T>(attributeName)).get()
}

fun <T> ChannelHandlerContext.setAttribute(attributeName: String, value: T) {
    this.channel().attr(AttributeKey.valueOf<T>(attributeName)).set(value)
}
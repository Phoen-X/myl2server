package com.vvygulyarniy.l2.gameserver.crypt

import com.l2server.crypt.GameCrypt
import com.l2server.network.communication.SessionId
import java.nio.ByteBuffer

class CryptService {
    private val sessionCrypts = hashMapOf<SessionId, GameCrypt?>()

    fun initSession(sessionId: SessionId) {
        sessionCrypts[sessionId] = GameCrypt()
    }

    fun closeSession(sessionId: SessionId) {
        sessionCrypts.remove(sessionId)
    }

    fun decrypt(sessionId: SessionId, byteBuffer: ByteBuffer, length: Int): Boolean {
        sessionCrypts[sessionId]?.let {
            it.decrypt(byteBuffer.array(), byteBuffer.position(), length)
            return true
        }
        return false
    }

    fun encrypt(sessionId: SessionId, buf: ByteBuffer, size: Int): Boolean {
        sessionCrypts[sessionId]?.let {
            it.encrypt(buf.array(), buf.position(), size)
            buf.position(buf.position() + size)
            return true
        }

        return false
    }

    fun getKey(sessionId: SessionId) = sessionCrypts[sessionId]?.getKey()
}

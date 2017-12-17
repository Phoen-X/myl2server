package com.vvygulyarniy.l2.loginserver.netty.login

import com.vvygulyarniy.l2.loginserver.communication.packet.client.L2LoginClientPacket
import com.vvygulyarniy.l2.loginserver.model.data.SessionId

/**
 * Created by Phoen-X on 16.02.2017.
 */
interface ClientPacketProcessor {
    fun process(sessionId: SessionId, packet: L2LoginClientPacket)
}

package com.l2server.network.serverpackets.login

import java.nio.ByteBuffer

class AccountKicked(private val reason: AccountKickedReason) : L2LoginServerPacket() {

    override fun write(buffer: ByteBuffer) {
        writeC(buffer, 0x02)
        writeD(buffer, reason.code)
    }

    enum class AccountKickedReason(val code: Int) {
        REASON_DATA_STEALER(0x01),
        REASON_GENERIC_VIOLATION(0x08),
        REASON_7_DAYS_SUSPENDED(0x10),
        REASON_PERMANENTLY_BANNED(0x20)
    }
}

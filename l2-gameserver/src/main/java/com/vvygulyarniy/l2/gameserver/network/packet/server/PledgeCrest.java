package com.vvygulyarniy.l2.gameserver.network.packet.server;

import lombok.AllArgsConstructor;
import lombok.ToString;

import java.nio.ByteBuffer;

@AllArgsConstructor
@ToString
public final class PledgeCrest extends L2GameServerPacket {
    private final int crestId;

    @Override
    protected final void writeImpl(ByteBuffer buffer) {
        writeC(buffer, 0x6A);
        writeD(buffer, crestId);
        writeD(buffer, 0); // there should be byte array with crest data
    }
}
/*
 * Copyright (C) 2004-2015 L2J Server
 * 
 * This file is part of L2J Server.
 * 
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vvygulyarniy.l2.gameserver.network;

import com.l2server.crypt.BlowFishKeygen;
import com.l2server.crypt.GameCrypt;
import com.l2server.network.SessionKey;
import com.vvygulyarniy.l2.gameserver.network.packet.server.L2GameServerPacket;
import com.vvygulyarniy.l2.gameserver.network.packet.server.ServerClose;
import com.vvygulyarniy.l2.gameserver.world.character.L2Player;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static com.vvygulyarniy.l2.gameserver.network.L2GameClient.GameClientState.CONNECTED;

/**
 * Represents a client connected on Game Server.
 *
 * @author KenM
 */
@Slf4j
public final class L2GameClient {
    // Crypt
    private final GameCrypt _crypt;
    @Getter
    private final ChannelHandlerContext networkContext;
    private GameClientState state;
    private String _accountName;
    private SessionKey _sessionId;
    private boolean _isAuthedGG;
    private List<L2Player> accountCharacters = new ArrayList<>();
    private L2Player activeCharacter = null;

    public L2GameClient(ChannelHandlerContext networkContext) {
        this.networkContext = networkContext;
        state = CONNECTED;
        _crypt = new GameCrypt();
    }

    public byte[] enableCrypt() {
        byte[] key = BlowFishKeygen.INSTANCE.getRandomKey();
        _crypt.setKey(key);
        return key;
    }

    public GameClientState getState() {
        return state;
    }

    public void setState(GameClientState pState) {
        if (state != pState) {
            state = pState;
        }
    }

    public boolean decrypt(ByteBuffer buf, int size) {
        _crypt.decrypt(buf.array(), buf.position(), size);
        return true;
    }

    public boolean encrypt(final ByteBuffer buf, final int size) {
        _crypt.encrypt(buf.array(), buf.position(), size);
        buf.position(buf.position() + size);
        return true;
    }

    public List<L2Player> getAccountCharacters() {
        return new ArrayList<>(accountCharacters);
    }

    public void setAccountCharacters(List<L2Player> accountCharacters) {
        this.accountCharacters = accountCharacters;
    }

    public void addCharacter(L2Player l2Player) {
        this.accountCharacters.add(l2Player);
    }

    public L2Player getActiveCharacter() {
        return activeCharacter;
    }

    public void selectCharacter(int slotId) {
        this.activeCharacter = accountCharacters.get(slotId);
    }

    public void setGameGuardOk(boolean val) {
        _isAuthedGG = val;
    }

    public boolean isAuthedGG() {
        return _isAuthedGG;
    }

    public String getAccountName() {
        return _accountName;
    }

    public void setAccountName(String pAccountName) {
        _accountName = pAccountName;
    }

    public SessionKey getSessionId() {
        return _sessionId;
    }

    public void setSessionId(SessionKey sk) {
        _sessionId = sk;
    }

    public void send(L2GameServerPacket gsp) {
        log.info("Sending packet {}", gsp);

        if (gsp != null) {
            networkContext.channel().writeAndFlush(gsp);
        }
    }

    public void close(L2GameServerPacket... packetsToSendBefore) {
        for (L2GameServerPacket packet : packetsToSendBefore) {
            if (packet != null) {
                networkContext.channel().write(packet);
            }
        }
        networkContext.channel().close();
    }

    public void closeNow() {
        close(ServerClose.STATIC_PACKET);
    }

    /**
     * @author KenM
     */
    public enum GameClientState {
        CONNECTED, // Client has just connected .
        IN_LOBBY, // Client has authed but doesn't has character attached to it yet.
        IN_GAME // Client has selected a char and is in game.
    }
}

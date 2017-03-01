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
package com.l2server.network;

import com.l2server.network.serverpackets.game.L2GameServerPacket;
import com.l2server.network.serverpackets.game.ServerClose;
import com.l2server.network.util.crypt.BlowFishKeygen;
import com.l2server.network.util.crypt.GameCrypt;
import com.vvygulyarniy.l2.domain.character.L2Character;
import io.netty.channel.ChannelHandlerContext;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.l2server.network.L2GameClient.GameClientState.CONNECTED;

/**
 * Represents a client connected on Game Server.
 *
 * @author KenM
 */
@Slf4j
public final class L2GameClient {
    private final LocalDateTime connectionTime;
    // Crypt
    private final GameCrypt _crypt;
    private final ChannelHandlerContext networkContext;
    private GameClientState state;
    private String _accountName;
    private SessionKey _sessionId;
    private boolean _isAuthedGG;
    private boolean _isDetached = false;
    @Setter
    private int protocolVersion;
    private List<L2Character> accountCharacters = new ArrayList<>();
    private L2Character activeCharacter = null;
    private int[][] trace;

    public L2GameClient(ChannelHandlerContext networkContext) {
        this.networkContext = networkContext;
        state = CONNECTED;
        connectionTime = LocalDateTime.now();
        _crypt = new GameCrypt();
    }

    public byte[] enableCrypt() {
        byte[] key = BlowFishKeygen.getRandomKey();
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

    public List<L2Character> getAccountCharacters() {
        return new ArrayList<>(accountCharacters);
    }

    public void setAccountCharacters(List<L2Character> accountCharacters) {
        this.accountCharacters = accountCharacters;
    }

    public void addCharacter(L2Character l2Character) {
        this.accountCharacters.add(l2Character);
    }

    public L2Character getActiveCharacter() {
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

    public void sendPacket(L2GameServerPacket gsp) {
        log.info("Sending packet {}", gsp);
        if (_isDetached || (gsp == null)) {
            return;
        }

        /*// Packets from invisible chars sends only to GMs
        if (gsp.isInvisible() && (getActiveChar() != null) && !getActiveChar().canOverrideCond(PcCondOverride.SEE_ALL_PLAYERS)) {
            return;
        }*/

        networkContext.channel().writeAndFlush(gsp);
    }

    public void close(L2GameServerPacket... packetsToSendBefore) {
        for (L2GameServerPacket packet : packetsToSendBefore) {
            if (packet != null) {
                networkContext.channel().write(packet);
            }
        }
        networkContext.channel().close();
    }

    /**
     * True if detached, or flood detected, or queue overflow detected and queue still not empty.
     *
     * @return false if client can receive packets.
     */
    public boolean dropPacket() {
        return false;
    }

    /**
     * Counts unknown packets
     */
    public void onUnknownPacket() {
        closeNow();
    }


    public void closeNow() {
        _isDetached = true; // prevents more packets execution
        close(ServerClose.STATIC_PACKET);
    }

    /**
     * @author KenM
     */
    public enum GameClientState {
        /**
         * Client has just connected .
         */
        CONNECTED,
        /**
         * Client has authed but doesn't has character attached to it yet.
         */
        AUTHED,
        /**
         * Client has selected a char and is in game.
         */
        IN_GAME
    }
}

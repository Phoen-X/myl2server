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
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Represents a client connected on Game Server.
 *
 * @author KenM
 */
@Slf4j
public final class L2GameClient extends MMOClient<MMOConnection<L2GameClient>> implements Runnable {
    // Info
    private final InetAddress _addr;
    private final ReentrantLock _activeCharLock = new ReentrantLock();
    private final long _connectionStartTime;
    // Crypt
    private final GameCrypt _crypt;
    private final ClientStats _stats;
    private final ArrayBlockingQueue<ReceivablePacket> _packetQueue;
    private final ReentrantLock _queueLock = new ReentrantLock();
    private final ChannelHandlerContext channelContext;
    // Task
    protected ScheduledFuture<?> _cleanupTask = null;
    private GameClientState _state;
    private String _accountName;
    private SessionKey _sessionId;
    private boolean _isAuthedGG;
    private boolean _isDetached = false;
    private boolean _protocol;
    private List<L2Character> accountCharacters = new ArrayList<>();
    private L2Character activeCharacter = null;
    private int[][] trace;

    public L2GameClient(MMOConnection<L2GameClient> con, ChannelHandlerContext channelContext) {
        super(con);
        this.channelContext = channelContext;
        _state = GameClientState.CONNECTED;
        _connectionStartTime = System.currentTimeMillis();
        _crypt = new GameCrypt();
        _stats = new ClientStats();

        _packetQueue = new ArrayBlockingQueue<>(20);


        try {
            _addr = con != null ? con.getInetAddress() : InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new Error("Unable to determine localhost address.");
        }
    }

    public byte[] enableCrypt() {
        byte[] key = BlowFishKeygen.getRandomKey();
        _crypt.setKey(key);
        return key;
    }

    public GameClientState getState() {
        return _state;
    }

    public void setState(GameClientState pState) {
        if (_state != pState) {
            _state = pState;
        }
    }

    public ClientStats getStats() {
        return _stats;
    }

    /**
     * For loaded offline traders returns localhost address.
     *
     * @return cached connection IP address, for checking detached clients.
     */
    public InetAddress getConnectionAddress() {
        return _addr;
    }

    public long getConnectionStartTime() {
        return _connectionStartTime;
    }

    @Override
    public boolean decrypt(ByteBuffer buf, int size) {
        _crypt.decrypt(buf.array(), buf.position(), size);
        return true;
    }

    @Override
    public boolean encrypt(final ByteBuffer buf, final int size) {
        _crypt.encrypt(buf.array(), buf.position(), size);
        buf.position(buf.position() + size);
        return true;
    }

    public List<L2Character> getAccountCharacters() {
        return new ArrayList<>(accountCharacters);
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

    @Override
    public void onDisconnection() {
        log.info("disconnected: {}", getAccountName());
    }

    public ReentrantLock getActiveCharLock() {
        return _activeCharLock;
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

        if (channelContext != null) {
            channelContext.channel().writeAndFlush(gsp);
        } else {
            getConnection().sendPacket(gsp);
            gsp.runImpl();
        }
    }

    public boolean isDetached() {
        return _isDetached;
    }

    public void setDetached(boolean b) {
        _isDetached = b;
    }

    public void close(L2GameServerPacket gsp) {
        if (channelContext != null) {
            if (gsp != null) {
                channelContext.channel().writeAndFlush(gsp);
                channelContext.channel().close();
            }
        } else {
            getConnection().close(gsp);
        }
    }

    public void close(L2GameServerPacket[] gspArray) {
        if (getConnection() == null) {
            return; // ofline shop
        }
        getConnection().close(gspArray);
    }

    @Override
    public void onForcedDisconnection() {
        LogRecord record = new LogRecord(Level.WARNING, "Disconnected abnormally");
        record.setParameters(new Object[]{this});
    }

    public boolean isProtocolOk() {
        return _protocol;
    }



    /*protected class DisconnectTask implements Runnable {
        @Override
        public void run() {
            boolean fast = true;
            try {
                if ((getActiveChar() != null) && !isDetached()) {
                    setDetached(true);
                    if (offlineMode(getActiveChar())) {
                        getActiveChar().leaveParty();
                        OlympiadManager.getInstance().unRegisterNoble(getActiveChar());

                        // If the L2PcInstance has Pet, unsummon it
                        if (getActiveChar().hasSummon()) {
                            getActiveChar().getSummon().setRestoreSummon(true);

                            getActiveChar().getSummon().unSummon(getActiveChar());
                            // Dead pet wasn't unsummoned, broadcast npcinfo changes (pet will be without owner name - means owner offline)
                            if (getActiveChar().getSummon() != null) {
                                getActiveChar().getSummon().broadcastNpcInfo(0);
                            }
                        }

                        if (Config.OFFLINE_SET_NAME_COLOR) {
                            getActiveChar().getAppearance().setNameColor(Config.OFFLINE_NAME_COLOR);
                            getActiveChar().broadcastUserInfo();
                        }

                        if (getActiveChar().getOfflineStartTime() == 0) {
                            getActiveChar().setOfflineStartTime(System.currentTimeMillis());
                        }

                        final LogRecord record = new LogRecord(Level.INFO, "Entering offline mode");
                        record.setParameters(new Object[]
                                {
                                        L2GameClient.this
                                });
                        _logAccounting.log(record);
                        return;
                    }
                    fast = !getActiveChar().isInCombat() && !getActiveChar().isLocked();
                }
                cleanMe(fast);
            } catch (Exception e1) {
                _log.log(Level.WARNING, "Error while disconnecting client.", e1);
            }
        }
    }*/

    public void setProtocolOk(boolean b) {
        _protocol = b;
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

    @Override
    public void run() {
        if (!_queueLock.tryLock()) {
            return;
        }

        try {
            int count = 0;
            ReceivablePacket packet;
            while (true) {
                packet = _packetQueue.poll();
                if (packet == null) {
                    return;
                }

                if (_isDetached) // clear queue immediately after detach
                {
                    _packetQueue.clear();
                    return;
                }

                try {
                    //packet.run();
                    log.info("Packet received {}", packet);
                } catch (Exception ignored) {
                }

                count++;
                /*if (getStats().countBurst(count)) {
                    return;
                }*/
            }
        } finally {
            _queueLock.unlock();
        }
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

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


import com.l2server.network.util.crypt.LoginCrypt;
import com.l2server.network.util.crypt.ScrambledKeyPair;
import lombok.Getter;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Represents a client connected into the LoginServer
 *
 * @author KenM
 */
public final class L2LoginClient extends MMOClient<MMOConnection<L2LoginClient>> {
    private static final Random rnd = new Random();
    // Crypt
    @Getter
    private final LoginCrypt loginCrypt;
    private final ScrambledKeyPair _scrambledPair;
    private final byte[] _blowfishKey;
    private final int _sessionId;
    private final long _connectionStartTime;
    private LoginClientState _state;
    private String _account;
    private int _accessLevel;
    private int _lastServer;
    private SessionKey _sessionKey;
    private boolean _joinedGS;
    private Map<Integer, Integer> _charsOnServers;
    private Map<Integer, long[]> _charsToDelete;

    /**
     * @param con
     */
    public L2LoginClient(MMOConnection<L2LoginClient> con, ScrambledKeyPair scrambledKeyPair, byte[] blowfishKey) {
        super(con);
        _state = LoginClientState.CONNECTED;
        this._scrambledPair = scrambledKeyPair;
        this._blowfishKey = blowfishKey;
        this._sessionId = rnd.nextInt();
        this._connectionStartTime = System.currentTimeMillis();
        this.loginCrypt = new LoginCrypt();
        this.loginCrypt.setKey(blowfishKey);
    }

    @Override
    public boolean decrypt(ByteBuffer buf, int size) {
        boolean isChecksumValid = false;
        try {
            isChecksumValid = loginCrypt.decrypt(buf.array(), buf.position(), size);
            if (!isChecksumValid) {

                super.getConnection().close((SendablePacket) null);
                return false;
            }
            return true;
        } catch (IOException e) {

            super.getConnection().close((SendablePacket) null);
            return false;
        }
    }

    @Override
    public boolean encrypt(ByteBuffer buf, int size) {
        /*final int offset = buf.position();
        try {
            size = loginCrypt.encrypt(buf.array(), offset, size);
        } catch (IOException e) {

            return false;
        }
        buf.position(offset + size);
        return true;*/
        try {
            loginCrypt.encrypt(buf, size);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public LoginClientState getState() {
        return _state;
    }

    public void setState(LoginClientState state) {
        _state = state;
    }

    public byte[] getBlowfishKey() {
        return _blowfishKey;
    }

    public byte[] getScrambledModulus() {
        return _scrambledPair._scrambledModulus;
    }

    public RSAPrivateKey getRSAPrivateKey() {
        return (RSAPrivateKey) _scrambledPair._pair.getPrivate();
    }

    public String getAccount() {
        return _account;
    }

    public void setAccount(String account) {
        _account = account;
    }

    public int getAccessLevel() {
        return _accessLevel;
    }

    public void setAccessLevel(int accessLevel) {
        _accessLevel = accessLevel;
    }

    public int getLastServer() {
        return _lastServer;
    }

    public void setLastServer(int lastServer) {
        _lastServer = lastServer;
    }

    public int getSessionId() {
        return _sessionId;
    }

    public boolean hasJoinedGS() {
        return _joinedGS;
    }

    public void setJoinedGS(boolean val) {
        _joinedGS = val;
    }

    public SessionKey getSessionKey() {
        return _sessionKey;
    }

    public void setSessionKey(SessionKey sessionKey) {
        _sessionKey = sessionKey;
    }

    public long getConnectionStartTime() {
        return _connectionStartTime;
    }

    public void sendPacket(L2LoginServerPacket lsp) {
        getConnection().sendPacket(lsp);
    }

    public void close(LoginFail.LoginFailReason reason) {
        getConnection().close(new LoginFail(reason));
    }

    public void close(PlayFail.PlayFailReason reason) {
        getConnection().close(new PlayFail(reason));
    }

    public void close(L2LoginServerPacket lsp) {
        getConnection().close(lsp);
    }

    public void setCharsOnServ(int servId, int chars) {
        if (_charsOnServers == null) {
            _charsOnServers = new HashMap<>();
        }
        _charsOnServers.put(servId, chars);
    }

    public Map<Integer, Integer> getCharsOnServ() {
        return _charsOnServers;
    }

    public void serCharsWaitingDelOnServ(int servId, long[] charsToDel) {
        if (_charsToDelete == null) {
            _charsToDelete = new HashMap<>();
        }
        _charsToDelete.put(servId, charsToDel);
    }

    public Map<Integer, long[]> getCharsWaitingDelOnServ() {
        return _charsToDelete;
    }

    @Override
    public void onDisconnection() {
        /*if (!hasJoinedGS() || ((getConnectionStartTime() + LoginController.LOGIN_TIMEOUT) < System.currentTimeMillis())) {
            LoginController.getInstance().removeAuthedLoginClient(getAccount());
        }*/
    }

    @Override
    public String toString() {
        InetAddress address = getConnection().getInetAddress();
        if (getState() == LoginClientState.AUTHED_LOGIN) {
            return "[" + getAccount() + " (" + (address == null ? "disconnected" : address.getHostAddress()) + ")]";
        }
        return "[" + (address == null ? "disconnected" : address.getHostAddress()) + "]";
    }

    @Override
    public void onForcedDisconnection() {
        // Empty
    }

    public static enum LoginClientState {
        CONNECTED,
        AUTHED_GG,
        AUTHED_LOGIN
    }
}

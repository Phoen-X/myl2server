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
package com.vvygulyarniy.l2.loginserver;


import com.l2server.network.gameserverpackets.ServerStatus;
import com.vvygulyarniy.l2.loginserver.util.Rnd;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.HashMap;
import java.util.Map;

import static com.l2server.network.gameserverpackets.ServerStatus.STATUS_GOOD;

/**
 * The Class GameServerTable loads the game server names and initialize the game server tables.
 *
 * @author KenM, Zoey76
 */
public final class GameServerTable {
    // Server Names
    private static final Map<Integer, String> SERVER_NAMES = new HashMap<>();
    // Game Server Table
    private static final Map<Integer, GameServerInfo> GAME_SERVER_TABLE = new HashMap<>();
    // RSA Config
    private static final int KEYS_SIZE = 10;

    static {
        GameServerInfo server1 = new GameServerInfo(1, new byte[]{1, 1});
        GameServerInfo server2 = new GameServerInfo(2, new byte[]{2, 2});
        server1.setStatus(STATUS_GOOD);
        server2.setStatus(STATUS_GOOD);

        GAME_SERVER_TABLE.put(1, server1);
        GAME_SERVER_TABLE.put(2, server2);
    }

    private KeyPair[] _keyPairs;

    /**
     * Instantiates a new game server table.
     */
    public GameServerTable() {
        initRSAKeys();
    }

    /**
     * Gets the single instance of GameServerTable.
     *
     * @return single instance of GameServerTable
     */
    public static GameServerTable getInstance() {
        return SingletonHolder._instance;
    }

    /**
     * Inits the RSA keys.
     */
    private void initRSAKeys() {
        try {
            final KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(new RSAKeyGenParameterSpec(512, RSAKeyGenParameterSpec.F4));
            _keyPairs = new KeyPair[KEYS_SIZE];
            for (int i = 0; i < KEYS_SIZE; i++) {
                _keyPairs[i] = keyGen.genKeyPair();
            }
        } catch (Exception e) {

        }
    }

    /**
     * Gets the registered game servers.
     *
     * @return the registered game servers
     */
    public Map<Integer, GameServerInfo> getRegisteredGameServers() {
        return GAME_SERVER_TABLE;
    }

    /**
     * Gets the registered game server by id.
     *
     * @param id the game server Id
     * @return the registered game server by id
     */
    public GameServerInfo getRegisteredGameServerById(int id) {
        return GAME_SERVER_TABLE.get(id);
    }

    /**
     * Checks for registered game server on id.
     *
     * @param id the id
     * @return true, if successful
     */
    public boolean hasRegisteredGameServerOnId(int id) {
        return GAME_SERVER_TABLE.containsKey(id);
    }

    /**
     * Register with first available id.
     *
     * @param gsi the game server information DTO
     * @return true, if successful
     */
    public boolean registerWithFirstAvailableId(GameServerInfo gsi) {
        // avoid two servers registering with the same "free" id
        synchronized (GAME_SERVER_TABLE) {
            for (Integer serverId : SERVER_NAMES.keySet()) {
                if (!GAME_SERVER_TABLE.containsKey(serverId)) {
                    GAME_SERVER_TABLE.put(serverId, gsi);
                    gsi.setId(serverId);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Register a game server.
     *
     * @param id  the id
     * @param gsi the gsi
     * @return true, if successful
     */
    public boolean register(int id, GameServerInfo gsi) {
        // avoid two servers registering with the same id
        synchronized (GAME_SERVER_TABLE) {
            if (!GAME_SERVER_TABLE.containsKey(id)) {
                GAME_SERVER_TABLE.put(id, gsi);
                return true;
            }
        }
        return false;
    }


    /**
     * Gets the server name by id.
     *
     * @param id the id
     * @return the server name by id
     */
    public String getServerNameById(int id) {
        return SERVER_NAMES.get(id);
    }

    /**
     * Gets the server names.
     *
     * @return the game server names map.
     */
    public Map<Integer, String> getServerNames() {
        return SERVER_NAMES;
    }

    /**
     * Gets the key pair.
     *
     * @return a random key pair.
     */
    public KeyPair getKeyPair() {
        return _keyPairs[Rnd.nextInt(10)];
    }

    /**
     * String to hex.
     *
     * @param string the string to convert.
     * @return return the hex representation.
     */
    private byte[] stringToHex(String string) {
        return new BigInteger(string, 16).toByteArray();
    }

    /**
     * Hex to string.
     *
     * @param hex the hex value to convert.
     * @return the string representation.
     */
    private String hexToString(byte[] hex) {
        if (hex == null) {
            return "null";
        }
        return new BigInteger(hex).toString(16);
    }

    /**
     * The Class GameServerInfo.
     */
    public static class GameServerInfo {
        private final byte[] _hexId;
        // config
        private final boolean _isPvp = true;
        // auth
        private int _id;
        private boolean _isAuthed;
        // status
        private int _status;
        private int _port;
        private int _serverType;
        private int _ageLimit;
        private boolean _isShowingBrackets;
        private int _maxPlayers;

        /**
         * Instantiates a new game server info.
         *
         * @param id    the id
         * @param hexId the hex id
         */
        public GameServerInfo(int id, byte[] hexId) {
            _id = id;
            _hexId = hexId;
            _status = ServerStatus.STATUS_DOWN;
        }

        /**
         * Gets the id.
         *
         * @return the id
         */
        public int getId() {
            return _id;
        }

        /**
         * Sets the id.
         *
         * @param id the new id
         */
        public void setId(int id) {
            _id = id;
        }

        /**
         * Gets the hex id.
         *
         * @return the hex id
         */
        public byte[] getHexId() {
            return _hexId;
        }

        public String getName() {
            // this value can't be stored in a private variable because the ID can be changed by setId()
            return GameServerTable.getInstance().getServerNameById(_id);
        }

        /**
         * Checks if is authed.
         *
         * @return true, if is authed
         */
        public boolean isAuthed() {
            return _isAuthed;
        }

        /**
         * Sets the authed.
         *
         * @param isAuthed the new authed
         */
        public void setAuthed(boolean isAuthed) {
            _isAuthed = isAuthed;
        }

        /**
         * Gets the status.
         *
         * @return the status
         */
        public int getStatus() {
            return _status;
        }

        /**
         * Sets the status.
         *
         * @param status the new status
         */
        public void setStatus(int status) {
            _status = status;
        }

        public String getStatusName() {
            switch (_status) {
                case 0:
                    return "Auto";
                case 1:
                    return "Good";
                case 2:
                    return "Normal";
                case 3:
                    return "Full";
                case 4:
                    return "Down";
                case 5:
                    return "GM Only";
                default:
                    return "Unknown";
            }
        }


        /**
         * Gets the port.
         *
         * @return the port
         */
        public int getPort() {
            return _port;
        }

        /**
         * Sets the port.
         *
         * @param port the new port
         */
        public void setPort(int port) {
            _port = port;
        }

        /**
         * Gets the max players.
         *
         * @return the max players
         */
        public int getMaxPlayers() {
            return _maxPlayers;
        }

        /**
         * Sets the max players.
         *
         * @param maxPlayers the new max players
         */
        public void setMaxPlayers(int maxPlayers) {
            _maxPlayers = maxPlayers;
        }

        /**
         * Checks if is pvp.
         *
         * @return true, if is pvp
         */
        public boolean isPvp() {
            return _isPvp;
        }

        /**
         * Gets the age limit.
         *
         * @return the age limit
         */
        public int getAgeLimit() {
            return _ageLimit;
        }

        /**
         * Sets the age limit.
         *
         * @param val the new age limit
         */
        public void setAgeLimit(int val) {
            _ageLimit = val;
        }

        /**
         * Gets the server type.
         *
         * @return the server type
         */
        public int getServerType() {
            return _serverType;
        }

        /**
         * Sets the server type.
         *
         * @param val the new server type
         */
        public void setServerType(int val) {
            _serverType = val;
        }

        /**
         * Checks if is showing brackets.
         *
         * @return true, if is showing brackets
         */
        public boolean isShowingBrackets() {
            return _isShowingBrackets;
        }

        /**
         * Sets the showing brackets.
         *
         * @param val the new showing brackets
         */
        public void setShowingBrackets(boolean val) {
            _isShowingBrackets = val;
        }
    }

    /**
     * The Class SingletonHolder.
     */
    private static class SingletonHolder {
        protected static final GameServerTable _instance = new GameServerTable();
    }
}

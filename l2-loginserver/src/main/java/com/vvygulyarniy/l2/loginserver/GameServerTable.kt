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
package com.vvygulyarniy.l2.loginserver


import com.vvygulyarniy.l2.loginserver.netty.login.ServerStatus
import com.vvygulyarniy.l2.loginserver.util.Rnd
import java.math.BigInteger
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.spec.RSAKeyGenParameterSpec
import java.util.*

/**
 * The Class GameServerTable loads the game server names and initialize the game server tables.

 * @author KenM, Zoey76
 */
class GameServerTable {

    private var _keyPairs: Array<KeyPair>? = null

    /**
     * Instantiates a new game server table.
     */
    init {
        initRSAKeys()
    }

    /**
     * Inits the RSA keys.
     */
    private fun initRSAKeys() {
        try {
            val keyGen = KeyPairGenerator.getInstance("RSA")
            keyGen.initialize(RSAKeyGenParameterSpec(512, RSAKeyGenParameterSpec.F4))
            _keyPairs = arrayOfNulls<KeyPair>(KEYS_SIZE).map { keyGen.genKeyPair() }
                    .toTypedArray()
        } catch (e: Exception) {

        }

    }


    /**
     * Gets the server name by id.

     * @param id the id
     * *
     * @return the server name by id
     */
    fun getServerNameById(id: Int): String? {
        return SERVER_NAMES[id]
    }

    /**
     * Gets the server names.

     * @return the game server names map.
     */
    val serverNames: Map<Int, String>
        get() = SERVER_NAMES

    /**
     * Gets the key pair.

     * @return a random key pair.
     */
    val keyPair: KeyPair
        get() = _keyPairs!![Rnd.nextInt(10)]

    /**
     * String to hex.

     * @param string the string to convert.
     * *
     * @return return the hex representation.
     */
    private fun stringToHex(string: String): ByteArray {
        return BigInteger(string, 16).toByteArray()
    }

    /**
     * Hex to string.

     * @param hex the hex value to convert.
     * *
     * @return the string representation.
     */
    private fun hexToString(hex: ByteArray?): String {
        if (hex == null) {
            return "null"
        }
        return BigInteger(hex).toString(16)
    }

    /**
     * The Class GameServerInfo.
     */
    class GameServerInfo
    /**
     * Instantiates a new game server info.

     * @param id    the id
     * *
     * @param hexId the hex id
     */
    (// auth
            /**
             * Gets the id.

             * @return the id
             */
            /**
             * Sets the id.

             * @param id the new id
             */
            var id: Int,
            /**
             * Gets the hex id.

             * @return the hex id
             */
            val hexId: ByteArray) {
        // config
        /**
         * Checks if is pvp.

         * @return true, if is pvp
         */
        val isPvp = true
        /**
         * Checks if is authed.

         * @return true, if is authed
         */
        /**
         * Sets the authed.

         * @param isAuthed the new authed
         */
        var isAuthed: Boolean = false
        // status
        /**
         * Gets the status.

         * @return the status
         */
        /**
         * Sets the status.

         * @param status the new status
         */
        var status: Int = 0
        /**
         * Gets the port.

         * @return the port
         */
        /**
         * Sets the port.

         * @param port the new port
         */
        var port: Int = 0
        /**
         * Gets the server type.

         * @return the server type
         */
        /**
         * Sets the server type.

         * @param val the new server type
         */
        var serverType: Int = 0
        /**
         * Gets the age limit.

         * @return the age limit
         */
        /**
         * Sets the age limit.

         * @param val the new age limit
         */
        var ageLimit: Int = 0
        /**
         * Checks if is showing brackets.

         * @return true, if is showing brackets
         */
        /**
         * Sets the showing brackets.

         * @param val the new showing brackets
         */
        var isShowingBrackets: Boolean = false
        /**
         * Gets the max players.

         * @return the max players
         */
        /**
         * Sets the max players.

         * @param maxPlayers the new max players
         */
        var maxPlayers: Int = 0

        init {
            status = ServerStatus.DOWN.code
        }

        // this value can't be stored in a private variable because the ID can be changed by setId()
        val name: String?
            get() = GameServerTable.instance.getServerNameById(id)

        val statusName: String
            get() {
                when (status) {
                    0 -> return "Auto"
                    1 -> return "Good"
                    2 -> return "Normal"
                    3 -> return "Full"
                    4 -> return "Down"
                    5 -> return "GM Only"
                    else -> return "Unknown"
                }
            }
    }

    /**
     * The Class SingletonHolder.
     */
    private object SingletonHolder {
        val _instance = GameServerTable()
    }

    companion object {
        // Server Names
        private val SERVER_NAMES = HashMap<Int, String>()
        // Game Server Table
        private val GAME_SERVER_TABLE = HashMap<Int, GameServerInfo>()
        // RSA Config
        private val KEYS_SIZE = 10

        init {
            val server1 = GameServerInfo(1, byteArrayOf(1, 1))
            val server2 = GameServerInfo(2, byteArrayOf(2, 2))
            server1.status = ServerStatus.GOOD.code
            server2.status = ServerStatus.GOOD.code

            GAME_SERVER_TABLE.put(1, server1)
            GAME_SERVER_TABLE.put(2, server2)
        }

        /**
         * Gets the single instance of GameServerTable.

         * @return single instance of GameServerTable
         */
        val instance: GameServerTable
            get() = SingletonHolder._instance
    }
}

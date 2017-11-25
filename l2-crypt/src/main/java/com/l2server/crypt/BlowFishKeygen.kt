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
package com.l2server.crypt


import java.util.*

/**
 * Blowfish keygen for GameServer client connections.

 * @author KenM
 */
object BlowFishKeygen {
    private val rnd = Random()
    private val CRYPT_KEYS_SIZE = 20
    private val CRYPT_KEYS = Array(CRYPT_KEYS_SIZE) { ByteArray(16) }

    init {
        // init the GS encryption keys on class load

        for (i in 0..CRYPT_KEYS_SIZE - 1) {
            // randomize the 8 first bytes
            for (j in 0..CRYPT_KEYS[i].size - 1) {
                CRYPT_KEYS[i][j] = rnd.nextInt(255).toByte()
            }

            // the last 8 bytes are static
            CRYPT_KEYS[i][8] = 0xc8.toByte()
            CRYPT_KEYS[i][9] = 0x27.toByte()
            CRYPT_KEYS[i][10] = 0x93.toByte()
            CRYPT_KEYS[i][11] = 0x01.toByte()
            CRYPT_KEYS[i][12] = 0xa1.toByte()
            CRYPT_KEYS[i][13] = 0x6c.toByte()
            CRYPT_KEYS[i][14] = 0x31.toByte()
            CRYPT_KEYS[i][15] = 0x97.toByte()
        }
    }

    /**
     * Returns a key from this keygen pool, the logical ownership is retained by this keygen.<BR></BR>
     * Thus when getting a key with interests other then read-only a copy must be performed.<BR></BR>

     * @return A key from this keygen pool.
     */
    val randomKey: ByteArray
        get() = CRYPT_KEYS[rnd.nextInt(CRYPT_KEYS_SIZE)]
}// block instantiation

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
package com.l2server.network.util.crypt

import java.math.BigInteger
import java.security.KeyPair
import java.security.interfaces.RSAPublicKey
import kotlin.experimental.xor

/**

 */
class ScrambledKeyPair(var _pair: KeyPair) {
    var _scrambledModulus: ByteArray

    init {
        _scrambledModulus = scrambleModulus((_pair.public as RSAPublicKey).modulus)
    }

    private fun scrambleModulus(modulus: BigInteger): ByteArray {
        var scrambledMod = modulus.toByteArray()

        if (scrambledMod.size == 0x81 && scrambledMod[0].toInt() == 0x00) {
            val temp = ByteArray(0x80)
            System.arraycopy(scrambledMod, 1, temp, 0, 0x80)
            scrambledMod = temp
        }
        // step 1 : 0x4d-0x50 <-> 0x00-0x04
        for (i in 0..3) {
            val temp = scrambledMod[i]
            scrambledMod[i] = scrambledMod[0x4d + i]
            scrambledMod[0x4d + i] = temp
        }
        // step 2 : xor first 0x40 bytes with last 0x40 bytes
        for (i in 0..63) {
            scrambledMod[i] = (scrambledMod[i] xor scrambledMod[0x40 + i]).toByte()
        }
        // step 3 : xor bytes 0x0d-0x10 with bytes 0x34-0x38
        for (i in 0..3) {
            scrambledMod[0x0d + i] = (scrambledMod[0x0d + i] xor scrambledMod[0x34 + i]).toByte()
        }
        // step 4 : xor last 0x40 bytes with first 0x40 bytes
        for (i in 0..63) {
            scrambledMod[0x40 + i] = (scrambledMod[0x40 + i] xor scrambledMod[i]).toByte()
        }


        return scrambledMod
    }
}

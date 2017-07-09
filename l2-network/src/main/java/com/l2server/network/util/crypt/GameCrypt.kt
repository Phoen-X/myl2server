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

import com.l2server.network.and
import com.l2server.network.shl
import java.util.*

/**
 * @author KenM
 */
class GameCrypt {
    private val _inKey = ByteArray(16)
    private val _outKey = ByteArray(16)
    private var _isEnabled: Boolean = false

    fun setKey(key: ByteArray) {
        log.debug("Setting crypt key: {}", Arrays.toString(key))
        System.arraycopy(key, 0, _inKey, 0, 16)
        System.arraycopy(key, 0, _outKey, 0, 16)
    }

    fun decrypt(raw: ByteArray, offset: Int, size: Int) {
        if (!_isEnabled) {
            return
        }

        var temp = 0
        for (i in 0..size - 1) {
            val temp2 = raw[offset + i] and 0xFF
            raw[offset + i] = (temp2 xor _inKey[i and 15].toInt() xor temp).toByte()
            temp = temp2
        }

        var old = _inKey[8] and 0xff
        old = old or (_inKey[9] shl 8 and 0xff00)
        old = old or (_inKey[10] shl 0x10 and 0xff0000)
        old = old or (_inKey[11] shl 0x18 and 0xff000000.toInt())

        old += size

        _inKey[8] = (old and 0xff).toByte()
        _inKey[9] = (old shr 0x08 and 0xff).toByte()
        _inKey[10] = (old shr 0x10 and 0xff).toByte()
        _inKey[11] = (old shr 0x18 and 0xff).toByte()
    }

    fun encrypt(raw: ByteArray, offset: Int, size: Int) {
        if (!_isEnabled) {
            _isEnabled = true
            return
        }

        var temp = 0
        for (i in 0..size - 1) {
            val temp2 = raw[offset + i] and 0xFF
            temp = temp2 xor _outKey[i and 15].toInt() xor temp
            raw[offset + i] = temp.toByte()
        }

        var old = _outKey[8] and 0xff
        old = old or (_outKey[9] shl 8 and 0xff00)
        old = old or (_outKey[10] shl 0x10 and 0xff0000)
        old = old or (_outKey[11] shl 0x18 and 0xff000000.toInt())

        old += size

        _outKey[8] = (old and 0xff).toByte()
        _outKey[9] = (old shr 0x08 and 0xff).toByte()
        _outKey[10] = (old shr 0x10 and 0xff).toByte()
        _outKey[11] = (old shr 0x18 and 0xff).toByte()
    }

    companion object {
        private val log = org.slf4j.LoggerFactory.getLogger(GameCrypt::class.java)
    }
}

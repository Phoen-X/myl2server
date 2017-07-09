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


import java.io.IOException
import java.nio.ByteBuffer
import java.util.*

/**
 * @author KenM
 */
class LoginCrypt {
    private var _crypt: NewCrypt? = null
    private var _static = true

    /**
     * Method to initialize the the blowfish cipher with dynamic key.

     * @param key the blowfish key to initialize the dynamic blowfish cipher with
     */
    fun setKey(key: ByteArray) {
        _crypt = NewCrypt(key)
    }

    /**
     * Method to decrypt an incoming login client packet.

     * @param raw    array with encrypted data
     * *
     * @param offset offset where the encrypted data is located
     * *
     * @param size   number of bytes of encrypted data
     * *
     * @return true when checksum could be verified, false otherwise
     * *
     * @throws IOException the size is not multiple of blowfishs block size or the raw array can't hold size bytes starting at offset due to it's size
     */
    @Throws(IOException::class)
    fun decrypt(raw: ByteArray, offset: Int, size: Int): Boolean {
        if (size % 8 != 0) {
            throw IOException("size have to be multiple of 8")
        }
        if (offset + size > raw.size) {
            throw IOException("raw array too short for size starting from offset")
        }

        _crypt!!.decrypt(raw, offset, size)
        return NewCrypt.verifyChecksum(raw, offset, size)
    }

    /**
     * Method to encrypt an outgoing packet to login client.<br></br>
     * Performs padding and resizing of data array.

     * @param raw    array with plain data
     * *
     * @param offset offset where the plain data is located
     * *
     * @param size   number of bytes of plain data
     * *
     * @return the new array size
     * *
     * @throws IOException packet is too long to make padding and add verification data
     */
    @Throws(IOException::class)
    fun encrypt(raw: ByteArray, offset: Int, size: Int): Int {
        var size = size
        // reserve checksum
        size += 4

        if (_static) {
            // reserve for XOR "key"
            size += 4

            // padding
            size += 8 - size % 8
            /*if ((offset + size) > raw.length) {
                throw new IOException("packet too long");
            }*/
            NewCrypt.encXORPass(raw, offset, size, 1)
            _STATIC_CRYPT.crypt(raw, offset, size)
            _static = false
        } else {
            // padding
            size += 8 - size % 8
            if (offset + size > raw.size) {
                throw IOException("packet too long")
            }
            NewCrypt.appendChecksum(raw, offset, size)
            _crypt!!.crypt(raw, offset, size)
        }
        return size
    }

    fun encrypt(buffer: ByteBuffer, size: Int): ByteBuffer {
        var size = size
        val offset = buffer.position()
        try {
            size = encrypt(buffer.array(), offset, size)
            buffer.position(offset + size)
            return buffer
        } catch (e: IOException) {
            throw RuntimeException("Parse exception")
        }

    }

    companion object {
        private val rnd = Random()
        private val STATIC_BLOWFISH_KEY = byteArrayOf(0x6b.toByte(),
                0x60.toByte(), 0xcb.toByte(), 0x5b.toByte(), 0x82.toByte(), 0xce.toByte(), 0x90.toByte(), 0xb1.toByte(), 0xcc.toByte(), 0x2b.toByte(), 0x6c.toByte(), 0x55.toByte(), 0x6c.toByte(), 0x6c.toByte(), 0x6c.toByte(), 0x6c.toByte())

        private val _STATIC_CRYPT = NewCrypt(STATIC_BLOWFISH_KEY)
    }
}

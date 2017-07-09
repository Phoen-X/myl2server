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
import kotlin.experimental.and

/**
 * Class to use a blowfish cipher with ECB processing.<br></br>
 * Static methods are present to append/check the checksum of<br></br>
 * packets exchanged between the following partners:<br></br>
 * Login Server <-> Game Client<br></br>
 * Login Server <-> Game Server<br></br>
 * Also a static method is provided for the initial xor encryption between Login Server <-> Game Client.
 */
class NewCrypt(blowfishKey: ByteArray) {
    private val _cipher: BlowfishEngine = BlowfishEngine()

    init {
        _cipher.init(blowfishKey)
    }

    /**
     * Method to decrypt using Blowfish-Blockcipher in ECB mode.<br></br>
     * The results will be directly placed inside `raw` array.<br></br>
     * This method does not do any error checking, since the calling code<br></br>
     * should ensure sizes.

     * @param raw    the data array to be decrypted
     * *
     * @param offset the offset at which to start decrypting
     * *
     * @param size   the number of bytes to be decrypted
     */
    fun decrypt(raw: ByteArray, offset: Int, size: Int) {
        var i = offset
        while (i < offset + size) {
            _cipher.decryptBlock(raw, i)
            i += 8
        }
    }

    /**
     * Method to encrypt using Blowfish-Blockcipher in ECB mode.<br></br>
     * The results will be directly placed inside `raw` array.<br></br>
     * This method does not do any error checking, since the calling code should ensure sizes.

     * @param raw    the data array to be decrypted
     * *
     * @param offset the offset at which to start decrypting
     * *
     * @param size   the number of bytes to be decrypted
     */
    fun crypt(raw: ByteArray, offset: Int, size: Int) {
        var i = offset
        while (i < offset + size) {
            _cipher.encryptBlock(raw, i)
            i += 8
        }
    }

    companion object {

        /**
         * Equivalent to calling [.verifyChecksum] with parameters (raw, 0, raw.length)

         * @param raw data array to be verified
         * *
         * @return true when the checksum of the data is valid, false otherwise
         */
        fun verifyChecksum(raw: ByteArray): Boolean {
            return NewCrypt.verifyChecksum(raw, 0, raw.size)
        }

        /**
         * Method to verify the checksum of a packet received by login server from game client.<br></br>
         * This is also used for game server <-> login server communication.

         * @param raw    data array to be verified
         * *
         * @param offset at which offset to start verifying
         * *
         * @param size   number of bytes to verify
         * *
         * @return true if the checksum of the data is valid, false otherwise
         */
        fun verifyChecksum(raw: ByteArray, offset: Int, size: Int): Boolean {
            // check if size is multiple of 4 and if there is more then only the checksum
            if (size and 3 != 0 || size <= 4) {
                return false
            }

            var chksum: Long = 0
            val count = size - 4
            var check: Long = -1
            var i: Int

            i = offset
            while (i < count) {
                check = (raw[i] and 0xff.toByte()).toLong()
                check = check or (raw[i + 1] shl 8 and 0xff00).toLong()
                check = check or (raw[i + 2] shl 0x10 and 0xff0000).toLong()
                check = check or (raw[i + 3] shl 0x18 and 0xff000000.toInt()).toLong()

                chksum = chksum xor check
                i += 4
            }

            check = (raw[i] and 0xff).toLong()
            check = check or (raw[i + 1] shl 8 and 0xff00).toLong()
            check = check or (raw[i + 2] shl 0x10 and 0xff0000).toLong()
            check = check or (raw[i + 3] shl 0x18 and 0xff000000.toInt()).toLong()

            return check == chksum
        }

        /**
         * Equivalent to calling [.appendChecksum] with parameters (raw, 0, raw.length)

         * @param raw data array to compute the checksum from
         */
        fun appendChecksum(raw: ByteArray) {
            NewCrypt.appendChecksum(raw, 0, raw.size)
        }

        /**
         * Method to append packet checksum at the end of the packet.

         * @param raw    data array to compute the checksum from
         * *
         * @param offset offset where to start in the data array
         * *
         * @param size   number of bytes to compute the checksum from
         */
        fun appendChecksum(raw: ByteArray, offset: Int, size: Int) {
            var chksum: Long = 0
            val count = size - 4
            var ecx: Long
            var i: Int

            i = offset
            while (i < count) {
                ecx = (raw[i] and 0xff).toLong()
                ecx = ecx or (raw[i + 1] shl 8 and 0xff00).toLong()
                ecx = ecx or (raw[i + 2] shl 0x10 and 0xff0000).toLong()
                ecx = ecx or (raw[i + 3] shl 0x18 and 0xff000000.toInt()).toLong()

                chksum = chksum xor ecx
                i += 4
            }

            ecx = (raw[i] and 0xff).toLong()
            ecx = ecx or (raw[i + 1] shl 8 and 0xff00).toLong()
            ecx = ecx or (raw[i + 2] shl 0x10 and 0xff0000).toLong()
            ecx = ecx or (raw[i + 3] shl 0x18 and 0xff000000.toInt()).toLong()

            raw[i] = (chksum and 0xff).toByte()
            raw[i + 1] = (chksum shr 0x08 and 0xff).toByte()
            raw[i + 2] = (chksum shr 0x10 and 0xff).toByte()
            raw[i + 3] = (chksum shr 0x18 and 0xff).toByte()
        }

        /**
         * Packet is first XOR encoded with `key` then, the last 4 bytes are overwritten with the the XOR "key".<br></br>
         * Thus this assume that there is enough room for the key to fit without overwriting data.

         * @param raw The raw bytes to be encrypted
         * *
         * @param key The 4 bytes (int) XOR key
         */
        fun encXORPass(raw: ByteArray, key: Int) {
            NewCrypt.encXORPass(raw, 0, raw.size, key)
        }

        /**
         * Packet is first XOR encoded with `key` then, the last 4 bytes are overwritten with the the XOR "key".<br></br>
         * Thus this assume that there is enough room for the key to fit without overwriting data.

         * @param raw    The raw bytes to be encrypted
         * *
         * @param offset The beginning of the data to be encrypted
         * *
         * @param size   Length of the data to be encrypted
         * *
         * @param key    The 4 bytes (int) XOR key
         */
        fun encXORPass(raw: ByteArray, offset: Int, size: Int, key: Int) {
            val stop = size - 8
            var pos = 4 + offset
            var edx: Int
            var ecx = key // Initial xor key

            while (pos < stop) {
                edx = raw[pos] and 0xFF
                edx = edx or (raw[pos + 1] and 0xFF shl 8)
                edx = edx or (raw[pos + 2] and 0xFF shl 16)
                edx = edx or (raw[pos + 3] and 0xFF shl 24)

                ecx += edx

                edx = edx xor ecx

                raw[pos++] = (edx and 0xFF).toByte()
                raw[pos++] = (edx shr 8 and 0xFF).toByte()
                raw[pos++] = (edx shr 16 and 0xFF).toByte()
                raw[pos++] = (edx shr 24 and 0xFF).toByte()
            }

            raw[pos++] = (ecx and 0xFF).toByte()
            raw[pos++] = (ecx shr 8 and 0xFF).toByte()
            raw[pos++] = (ecx shr 16 and 0xFF).toByte()
            raw[pos++] = (ecx shr 24 and 0xFF).toByte()
        }
    }
}

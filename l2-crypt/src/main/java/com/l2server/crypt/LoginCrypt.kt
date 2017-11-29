package com.l2server.crypt


import java.io.IOException
import java.nio.ByteBuffer

/**
 * @author KenM
 */
class LoginCrypt(private val _crypt: NewCrypt) {
    private var _static = true

    /**
     * Method to initialize the the blowfish cipher with dynamic key.

     * @param key the blowfish key to initialize the dynamic blowfish cipher with
     */
    fun setKey(key: ByteArray) {
        _crypt.setKey(key)
    }

    /**
     * Method to decrypt an incoming login client packet.

     * @param raw    array with encrypted data
     * @param offset offset where the encrypted data is located
     * @param size   number of bytes of encrypted data
     * @return true when checksum could be verified, false otherwise
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

        _crypt.decrypt(raw, offset, size)
        return NewCrypt.verifyChecksum(raw, offset, size)
    }

    /**
     * Method to encrypt an outgoing packet to login client.<br></br>
     * Performs padding and resizing of data array.

     * @param raw    array with plain data
     * @param offset offset where the plain data is located
     * @param size   number of bytes of plain data
     * @return the new array size
     * @throws IOException packet is too long to make padding and add verification data
     */
    @Throws(IOException::class)
    fun encrypt(raw: ByteArray, offset: Int, size: Int): Int {
        var index = size
        // reserve checksum
        index += 4

        if (_static) {
            // reserve for XOR "key"
            index += 4

            // padding
            index += 8 - index % 8
            /*if ((offset + index) > raw.length) {
                throw new IOException("packet too long");
            }*/
            NewCrypt.encXORPass(raw, offset, index, 1)
            _STATIC_CRYPT.crypt(raw, offset, index)
            _static = false
        } else {
            // padding
            index += 8 - index % 8
            if (offset + index > raw.size) {
                throw IOException("packet too long")
            }
            NewCrypt.appendChecksum(raw, offset, index)
            _crypt.crypt(raw, offset, index)
        }
        return index
    }

    fun encrypt(buffer: ByteBuffer, size: Int): ByteBuffer {
        var index = size
        val offset = buffer.position()
        try {
            index = encrypt(buffer.array(), offset, index)
            buffer.position(offset + index)
            return buffer
        } catch (e: IOException) {
            throw RuntimeException("Parse exception")
        }

    }

    companion object {
        private val STATIC_BLOWFISH_KEY = byteArrayOf(0x6b.toByte(),
                0x60.toByte(), 0xcb.toByte(), 0x5b.toByte(), 0x82.toByte(), 0xce.toByte(), 0x90.toByte(),
                0xb1.toByte(), 0xcc.toByte(), 0x2b.toByte(), 0x6c.toByte(),
                0x55.toByte(), 0x6c.toByte(), 0x6c.toByte(), 0x6c.toByte(), 0x6c.toByte())

        private val _STATIC_CRYPT = NewCrypt(STATIC_BLOWFISH_KEY)
    }
}

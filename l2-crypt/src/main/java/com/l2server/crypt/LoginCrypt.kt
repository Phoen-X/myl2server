package com.l2server.crypt


import java.io.IOException
import java.nio.ByteBuffer
import java.security.GeneralSecurityException
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.spec.RSAKeyGenParameterSpec
import java.util.*
import javax.crypto.Cipher

/**
 * @author KenM
 */
class LoginCrypt {
    private var _static = true

    val blowfishKey = cachedBlowfishKeys[(Math.random() * BLOWFISH_KEYS).toInt()]
    val scrambledKeyPair = cachedScrambledKeyPairs[Random().nextInt(SCRAMBLED_PAIRS_COUNT)]
    val crypt = NewCrypt(blowfishKey)

    /**
     * Method to initialize the the blowfish cipher with dynamic key.

     * @param key the blowfish key to initialize the dynamic blowfish cipher with
     */
    fun setKey(key: ByteArray) {
        crypt.setKey(key)
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

        crypt.decrypt(raw, offset, size)
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
            STATIC_CRYPT.crypt(raw, offset, index)
            _static = false
        } else {
            // padding
            index += 8 - index % 8
            if (offset + index > raw.size) {
                throw IOException("packet too long")
            }
            NewCrypt.appendChecksum(raw, offset, index)
            crypt.crypt(raw, offset, index)
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
        private val STATIC_BLOWFISH_KEY = byteArrayOf(0x6b.toByte(), 0x60.toByte(), 0xcb.toByte(), 0x5b.toByte(),
                                                      0x82.toByte(), 0xce.toByte(), 0x90.toByte(), 0xb1.toByte(),
                                                      0xcc.toByte(), 0x2b.toByte(), 0x6c.toByte(), 0x55.toByte(),
                                                      0x6c.toByte(), 0x6c.toByte(), 0x6c.toByte(), 0x6c.toByte())
        private val BLOWFISH_KEYS = 20

        private val STATIC_CRYPT = NewCrypt(STATIC_BLOWFISH_KEY)

        private val keygen = KeyPairGenerator.getInstance("RSA")
        private val cachedScrambledKeyPairs: Array<ScrambledKeyPair>
        private var cachedBlowfishKeys: Array<ByteArray>
        private val SCRAMBLED_PAIRS_COUNT = 10

        init {
            val spec = RSAKeyGenParameterSpec(1024, RSAKeyGenParameterSpec.F4)
            keygen.initialize(spec)

            cachedScrambledKeyPairs = arrayOfNulls<Unit>(SCRAMBLED_PAIRS_COUNT)
                    .map { ScrambledKeyPair(keygen.generateKeyPair()) }
                    .toTypedArray()

            cachedScrambledKeyPairs.forEach { testCipher(it.pair.private as RSAPrivateKey) }

            cachedBlowfishKeys = generateBlowFishKeys()
        }

        private fun generateBlowFishKeys(): Array<ByteArray> {
            val blowfishKeys = Array(BLOWFISH_KEYS) { ByteArray(16) }
            val rnd = Random()
            for (i in 0 until BLOWFISH_KEYS) {
                for (j in 0 until blowfishKeys[i].size) {
                    blowfishKeys[i][j] = (rnd.nextInt(255) + 1).toByte()
                }
            }
            return blowfishKeys
        }

        /**
         * This is mostly to force the initialization of the Crypto Implementation, avoiding it being done on runtime when its first needed.<BR></BR>
         * In short it avoids the worst-case execution time on runtime by doing it on loading.

         * @param key Any private RSA Key just for testing purposes.
         * *
         * @throws GeneralSecurityException if a underlying exception was thrown by the Cipher
         */
        @Throws(GeneralSecurityException::class)
        private fun testCipher(key: RSAPrivateKey) {
            // avoid worst-case execution, KenM
            val rsaCipher = Cipher.getInstance("RSA/ECB/nopadding")
            rsaCipher.init(Cipher.DECRYPT_MODE, key)
        }
    }
}

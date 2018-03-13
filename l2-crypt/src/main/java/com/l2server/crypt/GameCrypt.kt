package com.l2server.crypt

/**
 * @author KenM
 */
class GameCrypt {
    private val inKey = ByteArray(16)
    private val outKey = ByteArray(16)
    private var enabled: Boolean = false

    init {
        setKey(BlowFishKeygen.randomKey)
    }

    fun setKey(key: ByteArray) {
        System.arraycopy(key, 0, inKey, 0, 16)
        System.arraycopy(key, 0, outKey, 0, 16)
    }

    fun getKey() = inKey

    fun decrypt(encoded: ByteArray, offset: Int, size: Int) {
        if (!enabled) {
            return
        }

        var temp = 0
        for (i in 0 until size) {
            val temp2 = encoded[offset + i].toInt() and 0xFF
            encoded[offset + i] = (temp2 xor inKey[i and 15].toInt() xor temp).toByte()
            temp = temp2
        }

        var old = inKey[8].toLong() and 0xff
        old = old or (inKey[9].toLong() shl 8 and 0xff00)
        old = old or (inKey[10].toLong() shl 0x10 and 0xff0000)
        old = old or (inKey[11].toLong() shl 0x18 and 0xff000000)

        old += size

        inKey[8] = (old and 0xff).toByte()
        inKey[9] = (old shr 0x08 and 0xff).toByte()
        inKey[10] = (old shr 0x10 and 0xff).toByte()
        inKey[11] = (old shr 0x18 and 0xff).toByte()
    }

    fun encrypt(raw: ByteArray, offset: Int, size: Int) {
        if (!enabled) {
            enabled = true
            return
        }

        var temp = 0
        for (i in 0 until size) {
            val temp2 = raw[offset + i].toInt() and 0xFF
            temp = temp2 xor outKey[i and 15].toInt() xor temp
            raw[offset + i] = temp.toByte()
        }

        var old = outKey[8].toLong() and 0xff
        old = old or (outKey[9].toLong() shl 8 and 0xff00)
        old = old or (outKey[10].toLong() shl 0x10 and 0xff0000)
        old = old or (outKey[11].toLong() shl 0x18 and 0xff000000)

        old += size

        outKey[8] = (old and 0xff).toByte()
        outKey[9] = (old shr 0x08 and 0xff).toByte()
        outKey[10] = (old shr 0x10 and 0xff).toByte()
        outKey[11] = (old shr 0x18 and 0xff).toByte()
    }
}

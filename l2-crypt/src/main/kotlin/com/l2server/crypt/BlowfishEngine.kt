/*
 * This file is based on the Blowfish Engine that is part of the BouncyCastle JCE.
 * Copyright (c) 2000 The Legion Of The Bouncy Castle (http://www.bouncycastle.org)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.l2server.crypt

import java.io.IOException

class BlowfishEngine {
    private val S0: IntArray
    private val S1: IntArray
    private val S2: IntArray
    private val S3: IntArray // the s-boxes
    private val P: IntArray // the p-array
    private var workingKey: ByteArray? = null

    init {
        S0 = IntArray(SBOX_SK)
        S1 = IntArray(SBOX_SK)
        S2 = IntArray(SBOX_SK)
        S3 = IntArray(SBOX_SK)
        P = IntArray(P_SZ)
    }

    /**
     * Initialize a Blowfish cipher.

     * @param key the key used to set up the cipher
     */
    fun init(key: ByteArray) {
        workingKey = key
        setKey(key)
    }

    val algorithmName: String
        get() = "Blowfish"

    val blockSize: Int
        get() = BLOCK_SIZE

    private fun func(x: Int): Int {
        return (S0[x.ushr(24)] + S1[x.ushr(16) and 0xff] xor S2[x.ushr(8) and 0xff]) + S3[x and 0xff]
    }

    /**
     * apply the encryption cycle to each value pair in the table.

     * @param left
     * *
     * @param right
     * *
     * @param table
     */
    private fun processTable(left: Int, right: Int, table: IntArray) {
        var xl = left
        var xr = right
        val size = table.size
        var s = 0
        while (s < size) {
            xl = xl xor P[0]
            xr = xr xor (func(xl) xor P[1])
            xl = xl xor (func(xr) xor P[2])
            xr = xr xor (func(xl) xor P[3])
            xl = xl xor (func(xr) xor P[4])
            xr = xr xor (func(xl) xor P[5])
            xl = xl xor (func(xr) xor P[6])
            xr = xr xor (func(xl) xor P[7])
            xl = xl xor (func(xr) xor P[8])
            xr = xr xor (func(xl) xor P[9])
            xl = xl xor (func(xr) xor P[10])
            xr = xr xor (func(xl) xor P[11])
            xl = xl xor (func(xr) xor P[12])
            xr = xr xor (func(xl) xor P[13])
            xl = xl xor (func(xr) xor P[14])
            xr = xr xor (func(xl) xor P[15])
            xl = xl xor (func(xr) xor P[16])
            xr = xr xor P[17]

            table[s] = xr
            table[s + 1] = xl
            xr = xl // end of cycle swap
            xl = table[s]
            s += 2
        }
    }

    private fun setKey(key: ByteArray) {
        /**
         * - comments are from _Applied Crypto_, Schneier, p338.<br></br>
         * Please be careful comparing the two, AC numbers the arrays from 1, the enclosed code from 0.<br></br>
         * (1) Initialize the S-boxes and the P-array, with a fixed string This string contains the hexadecimal digits of pi (3.141...)
         */
        System.arraycopy(KS0, 0, S0, 0, SBOX_SK)
        System.arraycopy(KS1, 0, S1, 0, SBOX_SK)
        System.arraycopy(KS2, 0, S2, 0, SBOX_SK)
        System.arraycopy(KS3, 0, S3, 0, SBOX_SK)
        System.arraycopy(KP, 0, P, 0, P_SZ)
        /**
         * (2) Now, XOR P[0] with the first 32 bits of the key, XOR P[1] with the second 32-bits of the key, and so on for all bits of the key (up to P[17]).<br></br>
         * Repeatedly cycle through the key bits until the entire P-array has been XOR-ed with the key bits
         */
        val keyLength = key.size
        var keyIndex = 0
        for (i in 0..P_SZ - 1) {
            // get the 32 bits of the key, in 4 * 8 bit chunks
            var data = 0x0000000
            for (j in 0..3) {
                // create a 32 bit block
                data = data shl 8 or (key[keyIndex++].toInt() and 0xff)
                // wrap when we get to the end of the key
                if (keyIndex >= keyLength) {
                    keyIndex = 0
                }
            }
            // XOR the newly created 32 bit chunk onto the P-array
            P[i] = P[i] xor data
        }
        /**
         * (3) Encrypt the all-zero string with the Blowfish algorithm, using the subkeys described in (1) and (2)<br></br>
         * (4) Replace P1 and P2 with the output of step (3)<br></br>
         * (5) Encrypt the output of step(3) using the Blowfish algorithm, with the modified subkeys.<br></br>
         * (6) Replace P3 and P4 with the output of step (5)<br></br>
         * (7) Continue the process, replacing all elements of the P-array and then all four S-boxes in order, with the output of the continuously changing Blowfish algorithm
         */
        processTable(0, 0, P)
        processTable(P[P_SZ - 2], P[P_SZ - 1], S0)
        processTable(S0[SBOX_SK - 2], S0[SBOX_SK - 1], S1)
        processTable(S1[SBOX_SK - 2], S1[SBOX_SK - 1], S2)
        processTable(S2[SBOX_SK - 2], S2[SBOX_SK - 1], S3)
    }

    /**
     * Method to encrypt the block at the given index.<br></br>
     * The encrypted block goes directly to the source array at the given index.

     * @param src      source array with the plain data
     * *
     * @param srcIndex index where the block to encrypt is located
     * *
     * @throws IllegalStateException The cipher was not yet initialized
     * *
     * @throws IOException           The source array is too small to hold a block at the given index
     */
    @Throws(IOException::class)
    fun tryEncryptBlock(src: ByteArray, srcIndex: Int) {
        if (workingKey == null) {
            throw IllegalStateException("Blowfish not initialized")
        }
        if (srcIndex + BLOCK_SIZE > src.size) {
            throw IOException("input buffer too short")
        }
        encryptBlock(src, srcIndex)
    }

    /**
     * Method to encrypt the block at the given index.<br></br>
     * The encrypted block goes to the destination array at the given index.<br></br>
     * <br></br>

     * @param src      source array with the plain data
     * *
     * @param srcIndex index where the block to encrypt is located
     * *
     * @param dst      destination array the encryption will go to
     * *
     * @param dstIndex index where the encrypted block is to be stored
     * *
     * @throws IllegalStateException The cipher was not yet initialized
     * *
     * @throws IOException           The source or destination array is too small to hold a block at the given index
     */
    @Throws(IOException::class)
    fun tryEncryptBlock(src: ByteArray, srcIndex: Int, dst: ByteArray, dstIndex: Int) {
        if (workingKey == null) {
            throw IllegalStateException("Blowfish not initialized")
        }
        if (srcIndex + BLOCK_SIZE > src.size) {
            throw IOException("input buffer too short")
        }
        if (dstIndex + BLOCK_SIZE > dst.size) {
            throw IOException("output buffer too short")
        }
        encryptBlock(src, srcIndex, dst, dstIndex)
    }

    /**
     * Method to encrypt the block at the given index.<br></br>
     * The encrypted block goes to the destination array at the given index.<br></br>
     * <br></br>
     * This method does not perform any error checking. This could be<br></br>
     * usefull when code calling this method performs size checks already or<br></br>
     * perfroming steps to ensure nothing can go wrong.<br></br>
     * <br></br>
     * If you want error checking use [.tryEncryptBlock].

     * @param src      source array with the plain data
     * *
     * @param srcIndex index where the block to encrypt is located
     * *
     * @param dst      destination array the encryption will go to
     * *
     * @param dstIndex index where the encrypted block is to be stored
     */
    @JvmOverloads fun encryptBlock(src: ByteArray, srcIndex: Int, dst: ByteArray = src, dstIndex: Int = srcIndex) {
        var xl = bytesTo32bits(src, srcIndex)
        var xr = bytesTo32bits(src, srcIndex + 4)

        xl = xl xor P[0]
        xr = xr xor (func(xl) xor P[1])
        xl = xl xor (func(xr) xor P[2])
        xr = xr xor (func(xl) xor P[3])
        xl = xl xor (func(xr) xor P[4])
        xr = xr xor (func(xl) xor P[5])
        xl = xl xor (func(xr) xor P[6])
        xr = xr xor (func(xl) xor P[7])
        xl = xl xor (func(xr) xor P[8])
        xr = xr xor (func(xl) xor P[9])
        xl = xl xor (func(xr) xor P[10])
        xr = xr xor (func(xl) xor P[11])
        xl = xl xor (func(xr) xor P[12])
        xr = xr xor (func(xl) xor P[13])
        xl = xl xor (func(xr) xor P[14])
        xr = xr xor (func(xl) xor P[15])
        xl = xl xor (func(xr) xor P[16])
        xr = xr xor P[17]

        bits32ToBytes(xr, dst, dstIndex)
        bits32ToBytes(xl, dst, dstIndex + 4)
    }

    /**
     * Method to decrypt the block at the given index.<br></br>
     * The decrypted block goes directly to the source array at the given<br></br>
     * index.

     * @param src      source array with the encrypted data
     * *
     * @param srcIndex index where the block to decrypt is located
     * *
     * @throws IllegalStateException The cipher was not yet initialized
     * *
     * @throws IOException           The source array is too small to hold a block at the given index
     */
    @Throws(IOException::class)
    fun tryDecryptBlock(src: ByteArray, srcIndex: Int) {
        if (workingKey == null) {
            throw IllegalStateException("Blowfish not initialized")
        }
        if (srcIndex + BLOCK_SIZE > src.size) {
            throw IOException("input buffer too short")
        }
        decryptBlock(src, srcIndex)
    }

    /**
     * Method to decrypt the block at the given index.<br></br>
     * The decrypted block goes to the destination array at the given index.<br></br>

     * @param src      source array with the plain data
     * *
     * @param srcIndex index where the block to decrypt is located
     * *
     * @param dst      destination array the decryption will go to
     * *
     * @param dstIndex index where the decrypted block is to be stored
     * *
     * @throws IllegalStateException The cipher was not yet initialized
     * *
     * @throws IOException           The source or destination array is too small to hold a block at the given index
     */
    @Throws(IOException::class)
    fun tryDecryptBlock(src: ByteArray, srcIndex: Int, dst: ByteArray, dstIndex: Int) {
        if (workingKey == null) {
            throw IllegalStateException("Blowfish not initialized")
        }
        if (srcIndex + BLOCK_SIZE > src.size) {
            throw IOException("input buffer too short")
        }
        if (dstIndex + BLOCK_SIZE > src.size) {
            throw IOException("output buffer too short")
        }
        decryptBlock(src, srcIndex, dst, dstIndex)
    }

    /**
     * Method to decrypt the block at the given index.<br></br>
     * The decrypted block goes to the destination array at the given index.<br></br>
     * <br></br>
     * This method does not perform any error checking. This could be<br></br>
     * usefull when code calling this method performs size checks already or<br></br>
     * perfroming steps to ensure nothing can go wrong.<br></br>
     * <br></br>
     * If you want error checking use [.tryDecryptBlock].

     * @param src      source array with the plain data
     * *
     * @param srcIndex index where the block to decrypt is located
     * *
     * @param dst      destination array the decryption will go to
     * *
     * @param dstIndex index where the decrypted block is to be stored
     * *
     * @throws IllegalStateException The cipher was not yet initialized
     */
    @JvmOverloads fun decryptBlock(src: ByteArray, srcIndex: Int, dst: ByteArray = src, dstIndex: Int = srcIndex) {
        var xl = bytesTo32bits(src, srcIndex)
        var xr = bytesTo32bits(src, srcIndex + 4)

        xl = xl xor P[17]
        xr = xr xor (func(xl) xor P[16])
        xl = xl xor (func(xr) xor P[15])
        xr = xr xor (func(xl) xor P[14])
        xl = xl xor (func(xr) xor P[13])
        xr = xr xor (func(xl) xor P[12])
        xl = xl xor (func(xr) xor P[11])
        xr = xr xor (func(xl) xor P[10])
        xl = xl xor (func(xr) xor P[9])
        xr = xr xor (func(xl) xor P[8])
        xl = xl xor (func(xr) xor P[7])
        xr = xr xor (func(xl) xor P[6])
        xl = xl xor (func(xr) xor P[5])
        xr = xr xor (func(xl) xor P[4])
        xl = xl xor (func(xr) xor P[3])
        xr = xr xor (func(xl) xor P[2])
        xl = xl xor (func(xr) xor P[1])
        xr = xr xor P[0]

        bits32ToBytes(xr, dst, dstIndex)
        bits32ToBytes(xl, dst, dstIndex + 4)
    }

    /**
     * Method to construct an int from the source array.<br></br>
     * 4 bytes are used from the given index.<br></br>
     * <br></br>
     * This method does not do any error checking.

     * @param src      source array with the bytes
     * *
     * @param srcIndex the index to extract the int from
     * *
     * @return the extracted integer
     */
    private fun bytesTo32bits(src: ByteArray, srcIndex: Int): Int {
        return src[srcIndex + 3].toInt() and 0xff shl 24 or
                (src[srcIndex + 2].toInt() and 0xff shl 16) or
                (src[srcIndex + 1].toInt() and 0xff shl 8) or
                (src[srcIndex].toInt() and 0xff)
    }

    /**
     * Method to decompose an int into a byte array.<br></br>
     * <br></br>
     * This method does not do any error checking.

     * @param in       the integer to decompose into bytes
     * *
     * @param dst      the destination array the decomposed int goes to
     * *
     * @param dstIndex the index in the destination array the decomposed int will be stored at
     */
    private fun bits32ToBytes(`in`: Int, dst: ByteArray, dstIndex: Int) {
        dst[dstIndex] = `in`.toByte()
        dst[dstIndex + 1] = (`in` shr 8).toByte()
        dst[dstIndex + 2] = (`in` shr 16).toByte()
        dst[dstIndex + 3] = (`in` shr 24).toByte()
    }

    companion object {
        private val KP = intArrayOf(0x243F6A88, 0x85A308D3.toInt(), 0x13198A2E, 0x03707344, 0xA4093822.toInt(), 0x299F31D0, 0x082EFA98, 0xEC4E6C89.toInt(), 0x452821E6, 0x38D01377, 0xBE5466CF.toInt(), 0x34E90C6C, 0xC0AC29B7.toInt(), 0xC97C50DD.toInt(), 0x3F84D5B5, 0xB5470917.toInt(), 0x9216D5D9.toInt(), 0x8979FB1B.toInt())
        private val KS0 = intArrayOf(0xD1310BA6.toInt(), 0x98DFB5AC.toInt(), 0x2FFD72DB, 0xD01ADFB7.toInt(), 0xB8E1AFED.toInt(), 0x6A267E96, 0xBA7C9045.toInt(), 0xF12C7F99.toInt(), 0x24A19947, 0xB3916CF7.toInt(), 0x0801F2E2, 0x858EFC16.toInt(), 0x636920D8, 0x71574E69, 0xA458FEA3.toInt(), 0xF4933D7E.toInt(), 0x0D95748F, 0x728EB658, 0x718BCD58, 0x82154AEE.toInt(), 0x7B54A41D, 0xC25A59B5.toInt(), 0x9C30D539.toInt(), 0x2AF26013, 0xC5D1B023.toInt(), 0x286085F0, 0xCA417918.toInt(), 0xB8DB38EF.toInt(), 0x8E79DCB0.toInt(), 0x603A180E, 0x6C9E0E8B, 0xB01E8A3E.toInt(), 0xD71577C1.toInt(), 0xBD314B27.toInt(), 0x78AF2FDA, 0x55605C60, 0xE65525F3.toInt(), 0xAA55AB94.toInt(), 0x57489862, 0x63E81440, 0x55CA396A, 0x2AAB10B6, 0xB4CC5C34.toInt(), 0x1141E8CE, 0xA15486AF.toInt(), 0x7C72E993, 0xB3EE1411.toInt(), 0x636FBC2A, 0x2BA9C55D, 0x741831F6, 0xCE5C3E16.toInt(), 0x9B87931E.toInt(), 0xAFD6BA33.toInt(), 0x6C24CF5C, 0x7A325381, 0x28958677, 0x3B8F4898, 0x6B4BB9AF, 0xC4BFE81B.toInt(), 0x66282193, 0x61D809CC, 0xFB21A991.toInt(), 0x487CAC60, 0x5DEC8032, 0xEF845D5D.toInt(), 0xE98575B1.toInt(), 0xDC262302.toInt(), 0xEB651B88.toInt(), 0x23893E81, 0xD396ACC5.toInt(), 0x0F6D6FF3, 0x83F44239.toInt(), 0x2E0B4482, 0xA4842004.toInt(), 0x69C8F04A, 0x9E1F9B5E.toInt(), 0x21C66842, 0xF6E96C9A.toInt(), 0x670C9C61, 0xABD388F0.toInt(), 0x6A51A0D2, 0xD8542F68.toInt(), 0x960FA728.toInt(), 0xAB5133A3.toInt(), 0x6EEF0B6C, 0x137A3BE4, 0xBA3BF050.toInt(), 0x7EFB2A98, 0xA1F1651D.toInt(), 0x39AF0176, 0x66CA593E, 0x82430E88.toInt(), 0x8CEE8619.toInt(), 0x456F9FB4, 0x7D84A5C3, 0x3B8B5EBE, 0xE06F75D8.toInt(), 0x85C12073.toInt(), 0x401A449F, 0x56C16AA6, 0x4ED3AA62, 0x363F7706, 0x1BFEDF72, 0x429B023D, 0x37D0D724, 0xD00A1248.toInt(), 0xDB0FEAD3.toInt(), 0x49F1C09B, 0x075372C9, 0x80991B7B.toInt(), 0x25D479D8, 0xF6E8DEF7.toInt(), 0xE3FE501A.toInt(), 0xB6794C3B.toInt(), 0x976CE0BD.toInt(), 0x04C006BA, 0xC1A94FB6.toInt(), 0x409F60C4, 0x5E5C9EC2, 0x196A2463, 0x68FB6FAF, 0x3E6C53B5, 0x1339B2EB, 0x3B52EC6F, 0x6DFC511F, 0x9B30952C.toInt(), 0xCC814544.toInt(), 0xAF5EBD09.toInt(), 0xBEE3D004.toInt(), 0xDE334AFD.toInt(), 0x660F2807, 0x192E4BB3, 0xC0CBA857.toInt(), 0x45C8740F, 0xD20B5F39.toInt(), 0xB9D3FBDB.toInt(), 0x5579C0BD, 0x1A60320A, 0xD6A100C6.toInt(), 0x402C7279, 0x679F25FE, 0xFB1FA3CC.toInt(), 0x8EA5E9F8.toInt(), 0xDB3222F8.toInt(), 0x3C7516DF, 0xFD616B15.toInt(), 0x2F501EC8, 0xAD0552AB.toInt(), 0x323DB5FA, 0xFD238760.toInt(), 0x53317B48, 0x3E00DF82, 0x9E5C57BB.toInt(), 0xCA6F8CA0.toInt(), 0x1A87562E, 0xDF1769DB.toInt(), 0xD542A8F6.toInt(), 0x287EFFC3, 0xAC6732C6.toInt(), 0x8C4F5573.toInt(), 0x695B27B0, 0xBBCA58C8.toInt(), 0xE1FFA35D.toInt(), 0xB8F011A0.toInt(), 0x10FA3D98, 0xFD2183B8.toInt(), 0x4AFCB56C, 0x2DD1D35B, 0x9A53E479.toInt(), 0xB6F84565.toInt(), 0xD28E49BC.toInt(), 0x4BFB9790, 0xE1DDF2DA.toInt(), 0xA4CB7E33.toInt(), 0x62FB1341, 0xCEE4C6E8.toInt(), 0xEF20CADA.toInt(), 0x36774C01, 0xD07E9EFE.toInt(), 0x2BF11FB4, 0x95DBDA4D.toInt(), 0xAE909198.toInt(), 0xEAAD8E71.toInt(), 0x6B93D5A0, 0xD08ED1D0.toInt(), 0xAFC725E0.toInt(), 0x8E3C5B2F.toInt(), 0x8E7594B7.toInt(), 0x8FF6E2FB.toInt(), 0xF2122B64.toInt(), 0x8888B812.toInt(), 0x900DF01C.toInt(), 0x4FAD5EA0, 0x688FC31C, 0xD1CFF191.toInt(), 0xB3A8C1AD.toInt(), 0x2F2F2218, 0xBE0E1777.toInt(), 0xEA752DFE.toInt(), 0x8B021FA1.toInt(), 0xE5A0CC0F.toInt(), 0xB56F74E8.toInt(), 0x18ACF3D6, 0xCE89E299.toInt(), 0xB4A84FE0.toInt(), 0xFD13E0B7.toInt(), 0x7CC43B81, 0xD2ADA8D9.toInt(), 0x165FA266, 0x80957705.toInt(), 0x93CC7314.toInt(), 0x211A1477, 0xE6AD2065.toInt(), 0x77B5FA86, 0xC75442F5.toInt(), 0xFB9D35CF.toInt(), 0xEBCDAF0C.toInt(), 0x7B3E89A0, 0xD6411BD3.toInt(), 0xAE1E7E49.toInt(), 0x00250E2D, 0x2071B35E, 0x226800BB, 0x57B8E0AF, 0x2464369B, 0xF009B91E.toInt(), 0x5563911D, 0x59DFA6AA, 0x78C14389, 0xD95A537F.toInt(), 0x207D5BA2, 0x02E5B9C5, 0x83260376.toInt(), 0x6295CFA9, 0x11C81968, 0x4E734A41, 0xB3472DCA.toInt(), 0x7B14A94A, 0x1B510052, 0x9A532915.toInt(), 0xD60F573F.toInt(), 0xBC9BC6E4.toInt(), 0x2B60A476, 0x81E67400.toInt(), 0x08BA6FB5, 0x571BE91F, 0xF296EC6B.toInt(), 0x2A0DD915, 0xB6636521.toInt(), 0xE7B9F9B6.toInt(), 0xFF34052E.toInt(), 0xC5855664.toInt(), 0x53B02D5D, 0xA99F8FA1.toInt(), 0x08BA4799, 0x6E85076A)
        private val KS1 = intArrayOf(0x4B7A70E9, 0xB5B32944.toInt(), 0xDB75092E.toInt(), 0xC4192623.toInt(), 0xAD6EA6B0.toInt(), 0x49A7DF7D, 0x9CEE60B8.toInt(), 0x8FEDB266.toInt(), 0xECAA8C71.toInt(), 0x699A17FF, 0x5664526C, 0xC2B19EE1.toInt(), 0x193602A5, 0x75094C29, 0xA0591340.toInt(), 0xE4183A3E.toInt(), 0x3F54989A, 0x5B429D65, 0x6B8FE4D6, 0x99F73FD6.toInt(), 0xA1D29C07.toInt(), 0xEFE830F5.toInt(), 0x4D2D38E6, 0xF0255DC1.toInt(), 0x4CDD2086, 0x8470EB26.toInt(), 0x6382E9C6, 0x021ECC5E, 0x09686B3F, 0x3EBAEFC9, 0x3C971814, 0x6B6A70A1, 0x687F3584, 0x52A0E286, 0xB79C5305.toInt(), 0xAA500737.toInt(), 0x3E07841C, 0x7FDEAE5C, 0x8E7D44EC.toInt(), 0x5716F2B8, 0xB03ADA37.toInt(), 0xF0500C0D.toInt(), 0xF01C1F04.toInt(), 0x0200B3FF, 0xAE0CF51A.toInt(), 0x3CB574B2, 0x25837A58, 0xDC0921BD.toInt(), 0xD19113F9.toInt(), 0x7CA92FF6, 0x94324773.toInt(), 0x22F54701, 0x3AE5E581, 0x37C2DADC, 0xC8B57634.toInt(), 0x9AF3DDA7.toInt(), 0xA9446146.toInt(), 0x0FD0030E, 0xECC8C73E.toInt(), 0xA4751E41.toInt(), 0xE238CD99.toInt(), 0x3BEA0E2F, 0x3280BBA1, 0x183EB331, 0x4E548B38, 0x4F6DB908, 0x6F420D03, 0xF60A04BF.toInt(), 0x2CB81290, 0x24977C79, 0x5679B072, 0xBCAF89AF.toInt(), 0xDE9A771F.toInt(), 0xD9930810.toInt(), 0xB38BAE12.toInt(), 0xDCCF3F2E.toInt(), 0x5512721F, 0x2E6B7124, 0x501ADDE6, 0x9F84CD87.toInt(), 0x7A584718, 0x7408DA17, 0xBC9F9ABC.toInt(), 0xE94B7D8C.toInt(), 0xEC7AEC3A.toInt(), 0xDB851DFA.toInt(), 0x63094366, 0xC464C3D2.toInt(), 0xEF1C1847.toInt(), 0x3215D908, 0xDD433B37.toInt(), 0x24C2BA16, 0x12A14D43, 0x2A65C451, 0x50940002, 0x133AE4DD, 0x71DFF89E, 0x10314E55, 0x81AC77D6.toInt(), 0x5F11199B, 0x043556F1, 0xD7A3C76B.toInt(), 0x3C11183B, 0x5924A509, 0xF28FE6ED.toInt(), 0x97F1FBFA.toInt(), 0x9EBABF2C.toInt(), 0x1E153C6E, 0x86E34570.toInt(), 0xEAE96FB1.toInt(), 0x860E5E0A.toInt(), 0x5A3E2AB3, 0x771FE71C, 0x4E3D06FA, 0x2965DCB9, 0x99E71D0F.toInt(), 0x803E89D6.toInt(), 0x5266C825, 0x2E4CC978, 0x9C10B36A.toInt(), 0xC6150EBA.toInt(), 0x94E2EA78.toInt(), 0xA5FC3C53.toInt(), 0x1E0A2DF4, 0xF2F74EA7.toInt(), 0x361D2B3D, 0x1939260F, 0x19C27960, 0x5223A708, 0xF71312B6.toInt(), 0xEBADFE6E.toInt(), 0xEAC31F66.toInt(), 0xE3BC4595.toInt(), 0xA67BC883.toInt(), 0xB17F37D1.toInt(), 0x018CFF28, 0xC332DDEF.toInt(), 0xBE6C5AA5.toInt(), 0x65582185, 0x68AB9802, 0xEECEA50F.toInt(), 0xDB2F953B.toInt(), 0x2AEF7DAD, 0x5B6E2F84, 0x1521B628, 0x29076170, 0xECDD4775.toInt(), 0x619F1510, 0x13CCA830, 0xEB61BD96.toInt(), 0x0334FE1E, 0xAA0363CF.toInt(), 0xB5735C90.toInt(), 0x4C70A239, 0xD59E9E0B.toInt(), 0xCBAADE14.toInt(), 0xEECC86BC.toInt(), 0x60622CA7, 0x9CAB5CAB.toInt(), 0xB2F3846E.toInt(), 0x648B1EAF, 0x19BDF0CA, 0xA02369B9.toInt(), 0x655ABB50, 0x40685A32, 0x3C2AB4B3, 0x319EE9D5, 0xC021B8F7.toInt(), 0x9B540B19.toInt(), 0x875FA099.toInt(), 0x95F7997E.toInt(), 0x623D7DA8, 0xF837889A.toInt(), 0x97E32D77.toInt(), 0x11ED935F, 0x16681281, 0x0E358829, 0xC7E61FD6.toInt(), 0x96DEDFA1.toInt(), 0x7858BA99, 0x57F584A5, 0x1B227263, 0x9B83C3FF.toInt(), 0x1AC24696, 0xCDB30AEB.toInt(), 0x532E3054, 0x8FD948E4.toInt(), 0x6DBC3128, 0x58EBF2EF, 0x34C6FFEA, 0xFE28ED61.toInt(), 0xEE7C3C73.toInt(), 0x5D4A14D9, 0xE864B7E3.toInt(), 0x42105D14, 0x203E13E0, 0x45EEE2B6, 0xA3AAABEA.toInt(), 0xDB6C4F15.toInt(), 0xFACB4FD0.toInt(), 0xC742F442.toInt(), 0xEF6ABBB5.toInt(), 0x654F3B1D, 0x41CD2105, 0xD81E799E.toInt(), 0x86854DC7.toInt(), 0xE44B476A.toInt(), 0x3D816250, 0xCF62A1F2.toInt(), 0x5B8D2646, 0xFC8883A0.toInt(), 0xC1C7B6A3.toInt(), 0x7F1524C3, 0x69CB7492, 0x47848A0B, 0x5692B285, 0x095BBF00, 0xAD19489D.toInt(), 0x1462B174, 0x23820E00, 0x58428D2A, 0x0C55F5EA, 0x1DADF43E, 0x233F7061, 0x3372F092, 0x8D937E41.toInt(), 0xD65FECF1.toInt(), 0x6C223BDB, 0x7CDE3759, 0xCBEE7460.toInt(), 0x4085F2A7, 0xCE77326E.toInt(), 0xA6078084.toInt(), 0x19F8509E, 0xE8EFD855.toInt(), 0x61D99735, 0xA969A7AA.toInt(), 0xC50C06C2.toInt(), 0x5A04ABFC, 0x800BCADC.toInt(), 0x9E447A2E.toInt(), 0xC3453484.toInt(), 0xFDD56705.toInt(), 0x0E1E9EC9, 0xDB73DBD3.toInt(), 0x105588CD, 0x675FDA79, 0xE3674340.toInt(), 0xC5C43465.toInt(), 0x713E38D8, 0x3D28F89E, 0xF16DFF20.toInt(), 0x153E21E7, 0x8FB03D4A.toInt(), 0xE6E39F2B.toInt(), 0xDB83ADF7.toInt())
        private val KS2 = intArrayOf(0xE93D5A68.toInt(), 0x948140F7.toInt(), 0xF64C261C.toInt(), 0x94692934.toInt(), 0x411520F7, 0x7602D4F7, 0xBCF46B2E.toInt(), 0xD4A20068.toInt(), 0xD4082471.toInt(), 0x3320F46A, 0x43B7D4B7, 0x500061AF, 0x1E39F62E, 0x97244546.toInt(), 0x14214F74, 0xBF8B8840.toInt(), 0x4D95FC1D, 0x96B591AF.toInt(), 0x70F4DDD3, 0x66A02F45, 0xBFBC09EC.toInt(), 0x03BD9785, 0x7FAC6DD0, 0x31CB8504, 0x96EB27B3.toInt(), 0x55FD3941, 0xDA2547E6.toInt(), 0xABCA0A9A.toInt(), 0x28507825, 0x530429F4, 0x0A2C86DA, 0xE9B66DFB.toInt(), 0x68DC1462, 0xD7486900.toInt(), 0x680EC0A4, 0x27A18DEE, 0x4F3FFEA2, 0xE887AD8C.toInt(), 0xB58CE006.toInt(), 0x7AF4D6B6, 0xAACE1E7C.toInt(), 0xD3375FEC.toInt(), 0xCE78A399.toInt(), 0x406B2A42, 0x20FE9E35, 0xD9F385B9.toInt(), 0xEE39D7AB.toInt(), 0x3B124E8B, 0x1DC9FAF7, 0x4B6D1856, 0x26A36631, 0xEAE397B2.toInt(), 0x3A6EFA74, 0xDD5B4332.toInt(), 0x6841E7F7, 0xCA7820FB.toInt(), 0xFB0AF54E.toInt(), 0xD8FEB397.toInt(), 0x454056AC, 0xBA489527.toInt(), 0x55533A3A, 0x20838D87, 0xFE6BA9B7.toInt(), 0xD096954B.toInt(), 0x55A867BC, 0xA1159A58.toInt(), 0xCCA92963.toInt(), 0x99E1DB33.toInt(), 0xA62A4A56.toInt(), 0x3F3125F9, 0x5EF47E1C, 0x9029317C.toInt(), 0xFDF8E802.toInt(), 0x04272F70, 0x80BB155C.toInt(), 0x05282CE3, 0x95C11548.toInt(), 0xE4C66D22.toInt(), 0x48C1133F, 0xC70F86DC.toInt(), 0x07F9C9EE, 0x41041F0F, 0x404779A4, 0x5D886E17, 0x325F51EB, 0xD59BC0D1.toInt(), 0xF2BCC18F.toInt(), 0x41113564, 0x257B7834, 0x602A9C60, 0xDFF8E8A3.toInt(), 0x1F636C1B, 0x0E12B4C2, 0x02E1329E, 0xAF664FD1.toInt(), 0xCAD18115.toInt(), 0x6B2395E0, 0x333E92E1, 0x3B240B62, 0xEEBEB922.toInt(), 0x85B2A20E.toInt(), 0xE6BA0D99.toInt(), 0xDE720C8C.toInt(), 0x2DA2F728, 0xD0127845.toInt(), 0x95B794FD.toInt(), 0x647D0862, 0xE7CCF5F0.toInt(), 0x5449A36F, 0x877D48FA.toInt(), 0xC39DFD27.toInt(), 0xF33E8D1E.toInt(), 0x0A476341, 0x992EFF74.toInt(), 0x3A6F6EAB, 0xF4F8FD37.toInt(), 0xA812DC60.toInt(), 0xA1EBDDF8.toInt(), 0x991BE14C.toInt(), 0xDB6E6B0D.toInt(), 0xC67B5510.toInt(), 0x6D672C37, 0x2765D43B, 0xDCD0E804.toInt(), 0xF1290DC7.toInt(), 0xCC00FFA3.toInt(), 0xB5390F92.toInt(), 0x690FED0B, 0x667B9FFB, 0xCEDB7D9C.toInt(), 0xA091CF0B.toInt(), 0xD9155EA3.toInt(), 0xBB132F88.toInt(), 0x515BAD24, 0x7B9479BF, 0x763BD6EB, 0x37392EB3, 0xCC115979.toInt(), 0x8026E297.toInt(), 0xF42E312D.toInt(), 0x6842ADA7, 0xC66A2B3B.toInt(), 0x12754CCC, 0x782EF11C, 0x6A124237, 0xB79251E7.toInt(), 0x06A1BBE6, 0x4BFB6350, 0x1A6B1018, 0x11CAEDFA, 0x3D25BDD8, 0xE2E1C3C9.toInt(), 0x44421659, 0x0A121386, 0xD90CEC6E.toInt(), 0xD5ABEA2A.toInt(), 0x64AF674E, 0xDA86A85F.toInt(), 0xBEBFE988.toInt(), 0x64E4C3FE, 0x9DBC8057.toInt(), 0xF0F7C086.toInt(), 0x60787BF8, 0x6003604D, 0xD1FD8346.toInt(), 0xF6381FB0.toInt(), 0x7745AE04, 0xD736FCCC.toInt(), 0x83426B33.toInt(), 0xF01EAB71.toInt(), 0xB0804187.toInt(), 0x3C005E5F, 0x77A057BE, 0xBDE8AE24.toInt(), 0x55464299, 0xBF582E61.toInt(), 0x4E58F48F, 0xF2DDFDA2.toInt(), 0xF474EF38.toInt(), 0x8789BDC2.toInt(), 0x5366F9C3, 0xC8B38E74.toInt(), 0xB475F255.toInt(), 0x46FCD9B9, 0x7AEB2661, 0x8B1DDF84.toInt(), 0x846A0E79.toInt(), 0x915F95E2.toInt(), 0x466E598E, 0x20B45770, 0x8CD55591.toInt(), 0xC902DE4C.toInt(), 0xB90BACE1.toInt(), 0xBB8205D0.toInt(), 0x11A86248, 0x7574A99E, 0xB77F19B6.toInt(), 0xE0A9DC09.toInt(), 0x662D09A1, 0xC4324633.toInt(), 0xE85A1F02.toInt(), 0x09F0BE8C, 0x4A99A025, 0x1D6EFE10, 0x1AB93D1D, 0x0BA5A4DF, 0xA186F20F.toInt(), 0x2868F169, 0xDCB7DA83.toInt(), 0x573906FE, 0xA1E2CE9B.toInt(), 0x4FCD7F52, 0x50115E01, 0xA70683FA.toInt(), 0xA002B5C4.toInt(), 0x0DE6D027, 0x9AF88C27.toInt(), 0x773F8641, 0xC3604C06.toInt(), 0x61A806B5, 0xF0177A28.toInt(), 0xC0F586E0.toInt(), 0x006058AA, 0x30DC7D62, 0x11E69ED7, 0x2338EA63, 0x53C2DD94, 0xC2C21634.toInt(), 0xBBCBEE56.toInt(), 0x90BCB6DE.toInt(), 0xEBFC7DA1.toInt(), 0xCE591D76.toInt(), 0x6F05E409, 0x4B7C0188, 0x39720A3D, 0x7C927C24, 0x86E3725F.toInt(), 0x724D9DB9, 0x1AC15BB4, 0xD39EB8FC.toInt(), 0xED545578.toInt(), 0x08FCA5B5, 0xD83D7CD3.toInt(), 0x4DAD0FC4, 0x1E50EF5E, 0xB161E6F8.toInt(), 0xA28514D9.toInt(), 0x6C51133C, 0x6FD5C7E7, 0x56E14EC4, 0x362ABFCE, 0xDDC6C837.toInt(), 0xD79A3234.toInt(), 0x92638212.toInt(), 0x670EFA8E, 0x406000E0)
        private val KS3 = intArrayOf(0x3A39CE37, 0xD3FAF5CF.toInt(), 0xABC27737.toInt(), 0x5AC52D1B, 0x5CB0679E, 0x4FA33742, 0xD3822740.toInt(), 0x99BC9BBE.toInt(), 0xD5118E9D.toInt(), 0xBF0F7315.toInt(), 0xD62D1C7E.toInt(), 0xC700C47B.toInt(), 0xB78C1B6B.toInt(), 0x21A19045, 0xB26EB1BE.toInt(), 0x6A366EB4, 0x5748AB2F, 0xBC946E79.toInt(), 0xC6A376D2.toInt(), 0x6549C2C8, 0x530FF8EE, 0x468DDE7D, 0xD5730A1D.toInt(), 0x4CD04DC6, 0x2939BBDB, 0xA9BA4650.toInt(), 0xAC9526E8.toInt(), 0xBE5EE304.toInt(), 0xA1FAD5F0.toInt(), 0x6A2D519A, 0x63EF8CE2, 0x9A86EE22.toInt(), 0xC089C2B8.toInt(), 0x43242EF6, 0xA51E03AA.toInt(), 0x9CF2D0A4.toInt(), 0x83C061BA.toInt(), 0x9BE96A4D.toInt(), 0x8FE51550.toInt(), 0xBA645BD6.toInt(), 0x2826A2F9, 0xA73A3AE1.toInt(), 0x4BA99586, 0xEF5562E9.toInt(), 0xC72FEFD3.toInt(), 0xF752F7DA.toInt(), 0x3F046F69, 0x77FA0A59, 0x80E4A915.toInt(), 0x87B08601.toInt(), 0x9B09E6AD.toInt(), 0x3B3EE593, 0xE990FD5A.toInt(), 0x9E34D797.toInt(), 0x2CF0B7D9, 0x022B8B51, 0x96D5AC3A.toInt(), 0x017DA67D, 0xD1CF3ED6.toInt(), 0x7C7D2D28, 0x1F9F25CF, 0xADF2B89B.toInt(), 0x5AD6B472, 0x5A88F54C, 0xE029AC71.toInt(), 0xE019A5E6.toInt(), 0x47B0ACFD, 0xED93FA9B.toInt(), 0xE8D3C48D.toInt(), 0x283B57CC, 0xF8D56629.toInt(), 0x79132E28, 0x785F0191, 0xED756055.toInt(), 0xF7960E44.toInt(), 0xE3D35E8C.toInt(), 0x15056DD4, 0x88F46DBA.toInt(), 0x03A16125, 0x0564F0BD, 0xC3EB9E15.toInt(), 0x3C9057A2, 0x97271AEC.toInt(), 0xA93A072A.toInt(), 0x1B3F6D9B, 0x1E6321F5, 0xF59C66FB.toInt(), 0x26DCF319, 0x7533D928, 0xB155FDF5.toInt(), 0x03563482, 0x8ABA3CBB.toInt(), 0x28517711, 0xC20AD9F8.toInt(), 0xABCC5167.toInt(), 0xCCAD925F.toInt(), 0x4DE81751, 0x3830DC8E, 0x379D5862, 0x9320F991.toInt(), 0xEA7A90C2.toInt(), 0xFB3E7BCE.toInt(), 0x5121CE64, 0x774FBE32, 0xA8B6E37E.toInt(), 0xC3293D46.toInt(), 0x48DE5369, 0x6413E680, 0xA2AE0810.toInt(), 0xDD6DB224.toInt(), 0x69852DFD, 0x09072166, 0xB39A460A.toInt(), 0x6445C0DD, 0x586CDECF, 0x1C20C8AE, 0x5BBEF7DD, 0x1B588D40, 0xCCD2017F.toInt(), 0x6BB4E3BB, 0xDDA26A7E.toInt(), 0x3A59FF45, 0x3E350A44, 0xBCB4CDD5.toInt(), 0x72EACEA8, 0xFA6484BB.toInt(), 0x8D6612AE.toInt(), 0xBF3C6F47.toInt(), 0xD29BE463.toInt(), 0x542F5D9E, 0xAEC2771B.toInt(), 0xF64E6370.toInt(), 0x740E0D8D, 0xE75B1357.toInt(), 0xF8721671.toInt(), 0xAF537D5D.toInt(), 0x4040CB08, 0x4EB4E2CC, 0x34D2466A, 0x0115AF84, 0xE1B00428.toInt(), 0x95983A1D.toInt(), 0x06B89FB4, 0xCE6EA048.toInt(), 0x6F3F3B82, 0x3520AB82, 0x011A1D4B, 0x277227F8, 0x611560B1, 0xE7933FDC.toInt(), 0xBB3A792B.toInt(), 0x344525BD, 0xA08839E1.toInt(), 0x51CE794B, 0x2F32C9B7, 0xA01FBAC9.toInt(), 0xE01CC87E.toInt(), 0xBCC7D1F6.toInt(), 0xCF0111C3.toInt(), 0xA1E8AAC7.toInt(), 0x1A908749, 0xD44FBD9A.toInt(), 0xD0DADECB.toInt(), 0xD50ADA38.toInt(), 0x0339C32A, 0xC6913667.toInt(), 0x8DF9317C.toInt(), 0xE0B12B4F.toInt(), 0xF79E59B7.toInt(), 0x43F5BB3A, 0xF2D519FF.toInt(), 0x27D9459C, 0xBF97222C.toInt(), 0x15E6FC2A, 0x0F91FC71, 0x9B941525.toInt(), 0xFAE59361.toInt(), 0xCEB69CEB.toInt(), 0xC2A86459.toInt(), 0x12BAA8D1, 0xB6C1075E.toInt(), 0xE3056A0C.toInt(), 0x10D25065, 0xCB03A442.toInt(), 0xE0EC6E0E.toInt(), 0x1698DB3B, 0x4C98A0BE, 0x3278E964, 0x9F1F9532.toInt(), 0xE0D392DF.toInt(), 0xD3A0342B.toInt(), 0x8971F21E.toInt(), 0x1B0A7441, 0x4BA3348C, 0xC5BE7120.toInt(), 0xC37632D8.toInt(), 0xDF359F8D.toInt(), 0x9B992F2E.toInt(), 0xE60B6F47.toInt(), 0x0FE3F11D, 0xE54CDA54.toInt(), 0x1EDAD891, 0xCE6279CF.toInt(), 0xCD3E7E6F.toInt(), 0x1618B166, 0xFD2C1D05.toInt(), 0x848FD2C5.toInt(), 0xF6FB2299.toInt(), 0xF523F357.toInt(), 0xA6327623.toInt(), 0x93A83531.toInt(), 0x56CCCD02, 0xACF08162.toInt(), 0x5A75EBB5, 0x6E163697, 0x88D273CC.toInt(), 0xDE966292.toInt(), 0x81B949D0.toInt(), 0x4C50901B, 0x71C65614, 0xE6C6C7BD.toInt(), 0x327A140A, 0x45E1D006, 0xC3F27B9A.toInt(), 0xC9AA53FD.toInt(), 0x62A80F00, 0xBB25BFE2.toInt(), 0x35BDD2F6, 0x71126905, 0xB2040222.toInt(), 0xB6CBCF7C.toInt(), 0xCD769C2B.toInt(), 0x53113EC0, 0x1640E3D3, 0x38ABBD60, 0x2547ADF0, 0xBA38209C.toInt(), 0xF746CE76.toInt(), 0x77AFA1C5, 0x20756060, 0x85CBFE4E.toInt(), 0x8AE88DD8.toInt(), 0x7AAAF9B0, 0x4CF9AA7E, 0x1948C25C, 0x02FB8A8C, 0x01C36AE4, 0xD6EBE1F9.toInt(), 0x90D4F869.toInt(), 0xA65CDEA0.toInt(), 0x3F09252D, 0xC208E69F.toInt(), 0xB74E6132.toInt(), 0xCE77E25B.toInt(), 0x578FDFE3, 0x3AC372E6)

        private val ROUNDS = 16
        private val BLOCK_SIZE = 8 // bytes = 64 bits
        private val SBOX_SK = 256
        private val P_SZ = ROUNDS + 2
    }
}
/**
 * Method to encrypt the block at the given index.<br></br>
 * The encrypted block goes directly to the source array at the given<br></br>
 * index.<br></br>
 * <br></br>
 * This method does not perform any error checking. This could be<br></br>
 * usefull when code calling this method performs size checks already or<br></br>
 * perfroming steps to ensure nothing can go wrong.<br></br>
 * <br></br>
 * If you want error checking use [.tryEncryptBlock].

 * @param src      source array with the plain data
 * *
 * @param srcIndex index where the block to encrypt is located
 */
/**
 * Method to decrypt the block at the given index.<br></br>
 * The decrypted block goes directly to the source array at the given<br></br>
 * index.<br></br>
 * <br></br>
 * This method does not perform any error checking. This could be<br></br>
 * usefull when code calling this method performs size checks already or<br></br>
 * perfroming steps to ensure nothing can go wrong.<br></br>
 * <br></br>
 * If you want error checking use [.tryDecryptBlock].

 * @param src      source array with the encrypted data
 * *
 * @param srcIndex index where the block to decrypt is located
 */

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
package com.vvygulyarniy.l2.loginserver.util

import com.l2server.network.and
import java.util.*

/**
 * @author HorridoJoho
 */
object HexUtils {
    // lookup table for hex characters
    private val _NIBBLE_CHAR_LOOKUP = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')
    private val _NEW_LINE_CHARS = System.getProperty("line.separator").toCharArray()
    private val _HEX_ED_BPL = 16
    private val _HEX_ED_CPB = 2

    /**
     * Method to generate the hexadecimal character presentation of a byte

     * @param data        byte to generate the hexadecimal character presentation from
     * *
     * @param dstHexChars the char array the hexadecimal character presentation should be copied to, if this is null, dstOffset is ignored and a new char array with 2 elements is created
     * *
     * @param dstOffset   offset at which the hexadecimal character presentation is copied to dstHexChars
     * *
     * @return the char array the hexadecimal character presentation was copied to
     */
    @JvmOverloads fun b2HexChars(data: Byte, dstHexChars: CharArray? = null, dstOffset: Int = 0): CharArray {
        var chars = dstHexChars
        var offset = dstOffset
        if (chars == null) {
            chars = CharArray(2)
            offset = 0
        }

        // /////////////////////////////
        // NIBBLE LOOKUP
        chars[offset] = _NIBBLE_CHAR_LOOKUP[data and 0xF0 shr 4]
        chars[offset + 1] = _NIBBLE_CHAR_LOOKUP[data and 0x0F]

        return chars
    }

    /**
     * Method to generate the hexadecimal character presentation of an integer

     * @param data        integer to generate the hexadecimal character presentation from
     * *
     * @param dstHexChars the char array the hexadecimal character presentation should be copied to, if this is null, dstOffset is ignored and a new char array with 8 elements is created
     * *
     * @param dstOffset   offset at which the hexadecimal character presentation is copied to dstHexChars
     * *
     * @return the char array the hexadecimal character presentation was copied to
     */
    @JvmOverloads fun int2HexChars(data: Int, dstHexChars: CharArray? = CharArray(8), dstOffset: Int = 0): CharArray {
        var dstHexChars = dstHexChars
        var dstOffset = dstOffset
        if (dstHexChars == null) {
            dstHexChars = CharArray(8)
            dstOffset = 0
        }

        b2HexChars((data and 0xFF000000.toInt() shr 24).toByte(), dstHexChars, dstOffset)
        b2HexChars((data and 0x00FF0000 shr 16).toByte(), dstHexChars, dstOffset + 2)
        b2HexChars((data and 0x0000FF00 shr 8).toByte(), dstHexChars, dstOffset + 4)
        b2HexChars((data and 0x000000FF).toByte(), dstHexChars, dstOffset + 6)
        return dstHexChars
    }

    /**
     * Method to generate the hexadecimal character presentation of a byte array

     * @param data        byte array to generate the hexadecimal character presentation from
     * *
     * @param offset      offset where to start in data array
     * *
     * @param len         number of bytes to generate the hexadecimal character presentation from
     * *
     * @param dstHexChars the char array the hexadecimal character presentation should be copied to, if this is null, dstOffset is ignored and a new char array with len*2 elements is created
     * *
     * @param dstOffset   offset at which the hexadecimal character presentation is copied to dstHexChars
     * *
     * @return the char array the hexadecimal character presentation was copied to
     */
    @JvmOverloads fun bArr2HexChars(data: ByteArray, offset: Int, len: Int, dstHexChars: CharArray? = null, dstOffset: Int = 0): CharArray {
        var dstHexChars = dstHexChars
        var dstOffset = dstOffset
        if (dstHexChars == null) {
            dstHexChars = CharArray(len * 2)
            dstOffset = 0
        }

        var dataIdx = offset
        var charsIdx = dstOffset
        while (dataIdx < len + offset) {
            // /////////////////////////////
            // NIBBLE LOOKUP, we duplicate the code from b2HexChars here, we want to save a few cycles(for charsIdx increment)
            dstHexChars[charsIdx] = _NIBBLE_CHAR_LOOKUP[data[dataIdx] and 0xF0 shr 4]
            dstHexChars[++charsIdx] = _NIBBLE_CHAR_LOOKUP[data[dataIdx] and 0x0F]
            ++dataIdx
            ++charsIdx
        }

        return dstHexChars
    }

    @JvmOverloads fun bArr2AsciiChars(data: ByteArray, offset: Int, len: Int, dstAsciiChars: CharArray? = CharArray(len), dstOffset: Int = 0): CharArray {
        var dstAsciiChars = dstAsciiChars
        var dstOffset = dstOffset
        if (dstAsciiChars == null) {
            dstAsciiChars = CharArray(len)
            dstOffset = 0
        }

        var dataIdx = offset
        var charsIdx = dstOffset
        while (dataIdx < len + offset) {
            if (data[dataIdx] > 0x1f && data[dataIdx] < 0x80) {
                dstAsciiChars[charsIdx] = data[dataIdx].toChar()
            } else {
                dstAsciiChars[charsIdx] = '.'
            }
            ++dataIdx
            ++charsIdx
        }

        return dstAsciiChars
    }

    /**
     * Method to generate the hexadecimal character representation of a byte array like in a hex editor<br></br>
     * Line Format: {OFFSET} {HEXADECIMAL} {ASCII}({NEWLINE})<br></br>
     * {OFFSET} = offset of the first byte in line(8 chars)<br></br>
     * {HEXADECIMAL} = hexadecimal character representation([._HEX_ED_BPL]*2 chars)<br></br>
     * {ASCII} = ascii character presentation([._HEX_ED_BPL] chars)

     * @param data byte array to generate the hexadecimal character representation
     * *
     * @param len  the number of bytes to generate the hexadecimal character representation from
     * *
     * @return byte array which contains the hexadecimal character representation of the given byte array
     */
    fun bArr2HexEdChars(data: ByteArray, len: Int): CharArray {
        // {OFFSET} {HEXADECIMAL} {ASCII}{NEWLINE}
        val lineLength = 9 + _HEX_ED_BPL * _HEX_ED_CPB + 1 + _HEX_ED_BPL + _NEW_LINE_CHARS.size
        val lenBplMod = len % _HEX_ED_BPL
        // create text buffer
        // 1. don't allocate a full last line if not _HEX_ED_BPL bytes are shown in last line
        // 2. no new line at end of buffer
        // BUG: when the length is multiple of _HEX_ED_BPL we erase the whole ascii space with this
        // char[] textData = new char[lineLength * numLines - (_HEX_ED_BPL - (len % _HEX_ED_BPL)) - _NEW_LINE_CHARS.length];
        // FIXED HERE
        val numLines: Int
        val textData: CharArray
        if (lenBplMod == 0) {
            numLines = len / _HEX_ED_BPL
            textData = CharArray(lineLength * numLines - _NEW_LINE_CHARS.size)
        } else {
            numLines = len / _HEX_ED_BPL + 1
            textData = CharArray(lineLength * numLines - (_HEX_ED_BPL - lenBplMod) - _NEW_LINE_CHARS.size)
        }

        // performance penalty, only doing space filling in the loop is faster
        // Arrays.fill(textData, ' ');

        var dataOffset: Int
        var dataLen: Int
        var lineStart: Int
        var lineHexDataStart: Int
        var lineAsciiDataStart: Int
        for (i in 0..numLines - 1) {
            dataOffset = i * _HEX_ED_BPL
            dataLen = Math.min(len - dataOffset, _HEX_ED_BPL)
            lineStart = i * lineLength
            lineHexDataStart = lineStart + 9
            lineAsciiDataStart = lineHexDataStart + _HEX_ED_BPL * _HEX_ED_CPB + 1

            int2HexChars(dataOffset, textData, lineStart) // the offset of this line
            textData[lineHexDataStart - 1] = ' ' // separate
            bArr2HexChars(data, dataOffset, dataLen, textData, lineHexDataStart) // the data in hex
            bArr2AsciiChars(data, dataOffset, dataLen, textData, lineAsciiDataStart) // the data in ascii

            if (i < numLines - 1) {
                textData[lineAsciiDataStart - 1] = ' ' // separate
                System.arraycopy(_NEW_LINE_CHARS, 0, textData, lineAsciiDataStart + _HEX_ED_BPL, _NEW_LINE_CHARS.size) // the new line
            } else if (dataLen < _HEX_ED_BPL) {
                // last line which shows less than _HEX_ED_BPL bytes
                val lineHexDataEnd = lineHexDataStart + dataLen * _HEX_ED_CPB
                Arrays.fill(textData, lineHexDataEnd, lineHexDataEnd + (_HEX_ED_BPL - dataLen) * _HEX_ED_CPB + 1, ' ') // spaces, for the last line if there are not _HEX_ED_BPL bytes
            } else {
                // last line which shows _HEX_ED_BPL bytes
                textData[lineAsciiDataStart - 1] = ' ' // separate
            }
        }
        return textData
    }
}
/**
 * Method to generate the hexadecimal character presentation of a byte<br></br>
 * This call is equivalent to [HexUtils.b2HexChars] with parameters (data, null, 0)

 * @param data byte to generate the hexadecimal character presentation from
 * *
 * @return a new char array with exactly 2 elements
 */
/**
 * Method to generate the hexadecimal character presentation of an integer This call is equivalent to [HexUtils.int2HexChars] with parameters (data, null, 0)

 * @param data integer to generate the hexadecimal character presentation from
 * *
 * @return new char array with 8 elements
 */
/**
 * Method to generate the hexadecimal character presentation of a byte array<br></br>
 * This call is equivalent to [HexUtils.bArr2HexChars] with parameters (data, offset, len, null, 0)

 * @param data   byte array to generate the hexadecimal character presentation from
 * *
 * @param offset offset where to start in data array
 * *
 * @param len    number of bytes to generate the hexadecimal character presentation from
 * *
 * @return a new char array with len*2 elements
 */

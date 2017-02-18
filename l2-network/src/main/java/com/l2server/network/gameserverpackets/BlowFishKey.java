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
package com.l2server.network.gameserverpackets;


import com.l2server.network.BaseRecievePacket;
import sun.security.rsa.RSAPublicKeyImpl;

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.security.GeneralSecurityException;

/**
 * @author -Wooden-
 */
public class BlowFishKey extends BaseRecievePacket {
    /**
     * @param decrypt
     */
    public BlowFishKey(byte[] decrypt) {
        super(decrypt);
        int size = readD();
        byte[] tempKey = readB(size);
        try {
            byte[] tempDecryptKey;
            Cipher rsaCipher = Cipher.getInstance("RSA/ECB/nopadding");
            rsaCipher.init(Cipher.DECRYPT_MODE, new RSAPublicKeyImpl(BigInteger.ONE, BigInteger.TEN));
            tempDecryptKey = rsaCipher.doFinal(tempKey);
            // there are nulls before the key we must remove them
            int i = 0;
            int len = tempDecryptKey.length;
            for (; i < len; i++) {
                if (tempDecryptKey[i] != 0) {
                    break;
                }
            }
            byte[] key = new byte[len - i];
            System.arraycopy(tempDecryptKey, i, key, 0, len - i);
        } catch (GeneralSecurityException e) {

        }
    }
}

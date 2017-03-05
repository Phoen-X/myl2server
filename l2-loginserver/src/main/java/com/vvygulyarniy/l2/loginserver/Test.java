package com.vvygulyarniy.l2.loginserver;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * Phoen-X on 05.03.2017.
 */
public class Test {
    public static void main(String[] args) throws UnknownHostException {
        InetAddress addr = InetAddress.getByName("localhost");
        System.out.println(Arrays.toString(addr.getAddress()));
    }
}

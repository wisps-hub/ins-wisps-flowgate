package com.wisps.flowgate.common.utils;

import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class RemotingHelper {

    public static String exceptionSimpleDesc(final Throwable e){
        StringBuilder builder = new StringBuilder();
        if (e != null){
            builder.append(e.toString());
            StackTraceElement[] stackTrace = e.getStackTrace();
            if (stackTrace != null && stackTrace.length > 0) {
                StackTraceElement element = stackTrace[0];
                builder.append(", ");
                builder.append(element.toString());
            }
        }
        return builder.toString();
    }

    public static SocketAddress str2SocketAddress(final String addr){
        String[] s = addr.split(":");
        return new InetSocketAddress(s[0], Integer.parseInt(s[1]));
    }

    public static String parseChannelRemoterAddr(final Channel channel){
        if (channel == null) return "";
        SocketAddress remote = channel.remoteAddress();
        final String addr = remote == null? "" : remote.toString();
        if (addr.length() > 0){
            int idx = addr.lastIndexOf("/");
            if (idx >= 0){
                return addr.substring(idx + 1);
            }
            return addr;
        }
        return "";
    }

    public static String parseSocketAddressAddr(final SocketAddress socketAddress){
        if (socketAddress == null) return "";
        final String addr = socketAddress.toString();
        if (addr.length() > 0){
            return addr.substring(1);
        }
        return "";
    }
}

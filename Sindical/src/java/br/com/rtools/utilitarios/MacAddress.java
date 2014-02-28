//package br.com.rtools.utilitarios;
//
//import java.net.InetAddress;
//import java.net.NetworkInterface;
//import java.net.SocketException;
//import java.net.UnknownHostException;
//
//public class MacAddress {
//
//    public static String getMacAddress(String ipAddr) throws UnknownHostException {
//        if (ipAddr.isEmpty()) {
//            return null;
//        }
//        // TEST
//        //InetAddress addr = InetAddress.getLocalHost();
//        // InetAddress addr = InetAddress.getByName(ipAddr);
//        try {
//            InetAddress addr = InetAddress.getByName(ipAddr);
//            NetworkInterface ni = NetworkInterface.getByInetAddress(addr);
//            if (ni == null) {
//                return null;
//            }
//            byte[] mac = ni.getHardwareAddress();
//            if (mac == null) {
//                return null;
//            }
//
//            StringBuilder sb = new StringBuilder(18);
//            for (byte b : mac) {
//                if (sb.length() > 0) {
//                    sb.append(':');
//                }
//                sb.append(String.format("%02x", b));
//            }
//            return sb.toString();
//        } catch (SocketException e) {
//            return "";
//        }
//    }
//}

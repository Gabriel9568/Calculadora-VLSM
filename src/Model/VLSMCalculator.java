package Model;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class VLSMCalculator {
    public static class SubnetInfo {
        public String network;
        public String subnetMask;
        public String firstValidIp;
        public String lastValidIp;
        public String broadcast;

        public SubnetInfo(String network, String subnetMask, String firstValidIp, String lastValidIp, String broadcast) {
            this.network = network;
            this.subnetMask = subnetMask;
            this.firstValidIp = firstValidIp;
            this.lastValidIp = lastValidIp;
            this.broadcast = broadcast;
        }
    }

    public SubnetInfo calculateSubnet(String ipAddress, int numberOfHosts) {
        try {
            int prefixLength = getPrefixLength(numberOfHosts);
            String subnetMask = getSubnetMask(prefixLength);

            InetAddress ip = InetAddress.getByName(ipAddress);
            byte[] ipBytes = ip.getAddress();
            byte[] subnetMaskBytes = InetAddress.getByName(subnetMask).getAddress();

            byte[] networkBytes = new byte[4];
            byte[] broadcastBytes = new byte[4];
            for (int i = 0; i < 4; i++) {
                networkBytes[i] = (byte) (ipBytes[i] & subnetMaskBytes[i]);
                broadcastBytes[i] = (byte) (networkBytes[i] | ~subnetMaskBytes[i]);
            }

            InetAddress network = InetAddress.getByAddress(networkBytes);
            InetAddress broadcast = InetAddress.getByAddress(broadcastBytes);

            byte[] firstValidIpBytes = networkBytes.clone();
            firstValidIpBytes[3] += 1;
            InetAddress firstValidIp = InetAddress.getByAddress(firstValidIpBytes);

            byte[] lastValidIpBytes = broadcastBytes.clone();
            lastValidIpBytes[3] -= 1;
            InetAddress lastValidIp = InetAddress.getByAddress(lastValidIpBytes);

            return new SubnetInfo(network.getHostAddress(), subnetMask, firstValidIp.getHostAddress(), lastValidIp.getHostAddress(), broadcast.getHostAddress());

        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }

    private int getPrefixLength(int numberOfHosts) {
        int bits = 32 - (int) Math.ceil(Math.log(numberOfHosts + 2) / Math.log(2));
        return bits;
    }

    private String getSubnetMask(int prefixLength) {
        int mask = 0xffffffff << (32 - prefixLength);
        int value = mask;
        byte[] bytes = new byte[]{
                (byte) (value >>> 24),
                (byte) (value >> 16 & 0xff),
                (byte) (value >> 8 & 0xff),
                (byte) (value & 0xff)
        };

        try {
            return InetAddress.getByAddress(bytes).getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }
}

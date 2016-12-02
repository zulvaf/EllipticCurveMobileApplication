package utils;

import java.math.BigInteger;
import java.nio.ByteBuffer;

public class BitUtils {
    public static int setLSB(int val, int i) {
        return val & 0xFFFFFFFE | i;
    }

    public static byte setLSB(byte bval, int bit) {
        return (byte) (bval & 0xFE | bit);
    }

    public static long setLSB(long val, long bit) {
        long a = -2;
        return val & a | bit;

    }

    public static String getBits(int val) {
        return String.format("%32s", Integer.toBinaryString(val)).replace(' ', '0');
    }

    public static String getBits(byte bval) {
        return String.format("%8s", Integer.toBinaryString(bval & 0xFF)).replace(' ', '0');
    }

    public static String get2Bits(int val) {
        return String.format("%2s", Integer.toBinaryString(val & 0x3)).replace(' ', '0');
    }

    public static String getBits(byte[] val) {
        String res = "";
        for(int i = 0; i < val.length; i++) {
            res += getBits(val[i]);
        }
        return res;
    }

    public static String getBits(long val) {
        return String.format("%64s", Long.toBinaryString(val)).replace(' ', '0');
    }

    public static int getLSB(int val) {
        return val & 1;
    }

    public static int getLSB(long val) {
        return (int) val & 1;
    }

    public static int getLSB(byte bval) {
        return bval & 1;
    }

    public static int getMSB(int val) {
        return (val >> 31) & 1;
    }

    public static int getMSB(long val) {
        return (int) (val >> 63) & 1;
    }

    public static int getMSB(byte val) {
        return (int) (val >> 7) & 1;
    }


    public static long bitsToLong(String bits) {
        long res;
        if(bits.length() < 64) {
            res = Long.parseLong(bits,2);
        } else {
            res = Long.parseLong(bits.substring(1),2);
            if(bits.charAt(0) == '1') {
                res |= (1L << 63);
            }
        }
        return res;
    }

    public static byte[] longToBytes(long val) {
        ByteBuffer bf = ByteBuffer.allocate(Long.SIZE/8);
        bf.putLong(val);
        return bf.array();
    }

    public static long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.SIZE/8);
        buffer.put(bytes);
        buffer.flip();//need flip 
        return buffer.getLong();
    }

    public static byte[] concatBytes(byte[] left, byte[] rigth) {
        ByteBuffer bf = ByteBuffer.allocate(Long.SIZE / 4);
        bf.put(left);
        bf.put(rigth);
        return bf.array();
    }

    public static String byteToHex(byte[] bytes) {
        String encrypted="";
        for(int i=0; i<bytes.length; i++) {
            int idx = bytes[i] + 128;
            String hex = String.format("%02X", idx);
            encrypted+= hex;
        }
        return encrypted;
    }

    public static byte[] hexToBytes(String text) {
        int j =0;

        byte[] btext = new byte[text.length()];
        for(int i=0; i<text.length(); i=i+2) {
            String hex = text.substring(i, i+2);
            int value = Integer.parseInt(hex, 16) - 128;
            btext[j] = (byte) value;
            j++;
        }
        return btext;
    }
}

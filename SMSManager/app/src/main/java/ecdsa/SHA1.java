package ecdsa;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author zulvafachrina
 */
public class SHA1 {
    protected byte[] message;
    protected long mLength;

    protected int[] buffer = {
            0x67452301,
            0xEFCDAB89,
            0x98BADCFE,
            0x10325476,
            0xC3D2E1F0
    };

    protected int[] constant = {
            0x5A827999,
            0x6ED9EBA1,
            0x8F1BBCDC,
            0xCA62C1D6
    };

    public SHA1(byte[] msg, int length){
        message = msg;
        mLength = length;
    }

    public BigInteger shaAlgorithm(){
        byte[] padding = paddingBits();
        ArrayList<BigInteger> blocks = breakToBlock512(padding);

        for(int i=0; i<blocks.size(); i++){
            ArrayList<Integer> words = new ArrayList();
            words = breakToBlock32(blocks.get(i).toByteArray());

            //Extend the sixteen 32-bit words into eighty 32-bit words
            for(int j=16; j<80; j++) {
                int word = words.get(j - 3) ^ words.get(j - 8) ^ words.get(j - 14) ^ words.get(j - 16);
                words.add(rotateLeftShift(word,1));
            }

            //Initialize hash value for this chunk
            int a = buffer[0];
            int b = buffer[1];
            int c = buffer[2];
            int d = buffer[3];
            int e = buffer[4];
            int k=0;
            int func=0;

            for(int j=0; j<80; j++){
                if(0<=j && j<=19){
                    func =  (b & c)|(~b & d);
                    k = constant[0];
                } else if(20<=j && j<=39) {
                    func = b ^ c ^ d;
                    k = constant[1];
                } else if(40 <= j && j <= 59) {
                    func = (b & c) | (b & d) | (c & d);
                    k = constant[2];
                } else if(60 <= j && j <= 79) {
                    func = b ^ c ^ d;
                    k = constant[3];
                }

                int temp = rotateLeftShift(a,5) + func + e + k + words.get(j);
                e = d;
                d = c;
                c = rotateLeftShift(b,30);
                b = a;
                a = temp;
            }
            buffer[0] = buffer[0] + a;
            buffer[1] = buffer[1] + b;
            buffer[2] = buffer[2] + c;
            buffer[3] = buffer[3] + d;
            buffer[4] = buffer[4] + e;

        }
        byte[] bDigest = new byte[20];
        for(int j=0; j<bDigest.length; j=j+4) {
            bDigest[j] = (byte) (buffer[j/4] >> 24);
            bDigest[j+1] = (byte) (buffer[j/4] >> 16);
            bDigest[j+2] = (byte) (buffer[j/4] >> 8);
            bDigest[j+3] = (byte) (buffer[j/4]);
        }
       // return byteArrayToHexString(bDigest);
        return new BigInteger(bDigest);
    }

    public byte[] paddingBits(){
        BigInteger mPadded = new BigInteger(message);

        int shift = 448 - (int)(mLength % 512);
        BigInteger pad = new BigInteger("1").shiftLeft(shift+63);

        mPadded = mPadded.shiftLeft(shift+64);
        mPadded = mPadded.add(pad).add(new BigInteger(String.valueOf(mLength)));

        return mPadded.toByteArray();
    }

    public ArrayList<BigInteger> breakToBlock512(byte[] bytes) {
        ArrayList<BigInteger> blocks = new ArrayList();
        for(int i=0; i<bytes.length; i=i+64) {
            blocks.add(new BigInteger(Arrays.copyOfRange(bytes, i, i+64)));
        }
        return blocks;
    }

    public ArrayList<Integer> breakToBlock32(byte[] bytes) {
        ArrayList<Integer> blocks = new ArrayList();
        for(int i=0; i<bytes.length; i=i+4) {
            int MASK = 0xFF;
            int result = 0;
            result = result + ((bytes[i] & MASK) << 24);
            result = result + ((bytes[i+1] & MASK) << 16);
            result = result + ((bytes[i+2] & MASK) << 8);
            result = result + (bytes[i+3] & MASK);
            blocks.add(result);
        }
        return blocks;
    }

    public int rotateLeftShift(int x, int y) {
        return (x << y) | x >>> (32 - y);
    }

    public String byteToHexString(byte ib) {
        char[] Digit = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c',
                'd', 'e', 'f'
        };
        char[] ob = new char[2];
        ob[0] = Digit[(ib >>> 4) & 0X0F];
        ob[1] = Digit[ib & 0X0F];

        String s = new String(ob);

        return s;
    }

    public String byteArrayToHexString(byte[] bytearray) {
        String strDigest = "";

        for (int i = 0; i < bytearray.length; i++) {
            strDigest += byteToHexString(bytearray[i]);
        }

        return strDigest;
    }
}


package ecdsa;

import java.math.BigInteger;
import java.util.Random;

import utils.BitUtils;

public class ECDSA {
    protected EllipticCurve curve;
    protected Point G;
    protected BigInteger n;

    public ECDSA() {
        this.curve = new EllipticCurve();
        // Intialize G
        G = new Point(new BigInteger("188281465057972534892223778713752"), new BigInteger("3419875491033170827167861896082688"));
        // Initialize n
        n = new BigInteger("4451685225093714776491891542548933");
    }

    public Point getPublicKey(BigInteger privateKey) {
        return curve.multiply(privateKey, G);
    }

    public String[] buildSignature(BigInteger e, BigInteger privateKey) {
        BigInteger[] signature = new BigInteger[2];
        BigInteger k;
        do {
            do {
                // Randomize k, where 0 < k < n
                do {
                    k = new BigInteger(n.bitLength(), new Random());
                } while (k.compareTo(n) > 0 || k.compareTo(BigInteger.ZERO) < 0);
                Point R = curve.multiply(k, G);
                signature[0] = R.getX().mod(n);
            } while (signature[0].compareTo(BigInteger.ZERO) == 0); // signature[0] can't be zero
            BigInteger inverseK = k.modInverse(n);
            signature[1] = privateKey.multiply(signature[0]).add(e).multiply(inverseK).mod(n);
        } while (signature[1].compareTo(BigInteger.ZERO) == 0); // signature[1] can't be zero

        String[] result = new String[2];
        result[0] = signature[0].toString(16);
        result[1] = signature[1].toString(16);
        return result;
    }

    public boolean verifySignature(String[] signatureHex, BigInteger e, Point publicKey) {
        BigInteger[] signature = new BigInteger[2];
        signature[0] = new BigInteger(signatureHex[0], 16);
        signature[1] = new BigInteger(signatureHex[1], 16);

        // signature[0] and signature[1] must be between 0 and n
        if (signature[0].compareTo(BigInteger.ZERO) < 0 || signature[0].compareTo(n) > 0)
            return false;
        if (signature[1].compareTo(BigInteger.ZERO) < 0 || signature[1].compareTo(n) > 0)
            return false;

        BigInteger w = signature[1].modInverse(n);
        BigInteger u1 = e.multiply(w).mod(n);
        BigInteger u2 = signature[0].multiply(w).mod(n);
        Point p = curve.add(curve.multiply(u1, G), curve.multiply(u2, publicKey));

        // signature is valid if px = signature[0] mod n
        return (p.getX().mod(n).compareTo(signature[0].mod(n)) == 0);
    }

}


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ecdsa;

import java.math.BigInteger;

/**
 *
 * @author TOSHIBA PC
 */
public class ECDSA {
    
    protected EllipticCurve curve;
    protected Point G;

    public ECDSA() {
        this.curve = new EllipticCurve();
        
        // Intialize G
        G = new Point();
        BigInteger basex = new BigInteger("749514356");
        do {
            basex = basex.add(BigInteger.ONE).mod(curve.getP());
            G = curve.calculatePoint(basex);
        } while (!curve.checkOnCurve(G));
    }

    public Point getPublicKey(BigInteger privateKey) {
        return curve.multiply(privateKey, G);
    }
    
    public void buildSignature() {
        
    }
}

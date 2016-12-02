package ecdsa;

import java.math.BigInteger;

public class Point {
    private BigInteger x, y;
    public Point() {
    }
    public Point(BigInteger x, BigInteger y) {
        this.x = x;
        this.y = y;
    }
    public BigInteger getX() {
        return this.x;
    }
    public BigInteger getY() {
        return this.y;
    }
    public void setX(BigInteger x) {
        this.x = x;
    }
    public void setY(BigInteger y) {
        this.y = y;
    }
    public boolean equals(Point point) {
        return this.x.equals(point.getX()) && this.y.equals(point.getY());
    }
    public String toString() {
        return this.x + "\n" + this.y;
    }
}


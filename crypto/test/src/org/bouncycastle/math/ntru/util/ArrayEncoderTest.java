package org.bouncycastle.math.ntru.util;

import java.security.SecureRandom;
import java.util.Random;

import junit.framework.TestCase;
import org.bouncycastle.math.ntru.polynomial.DenseTernaryPolynomial;
import org.bouncycastle.math.ntru.polynomial.PolynomialGenerator;
import org.bouncycastle.util.Arrays;

public class ArrayEncoderTest
    extends TestCase
{
    public void testEncodeDecodeModQ()
    {
        int[] coeffs = PolynomialGenerator.generateRandom(1000, 2048).coeffs;
        byte[] data = ArrayEncoder.encodeModQ(coeffs, 2048);
        int[] coeffs2 = ArrayEncoder.decodeModQ(data, 1000, 2048);
        assertTrue(Arrays.areEqual(coeffs, coeffs2));
    }

    public void testEncodeDecodeMod3Sves()
    {
        Random rng = new Random();
        byte[] data = new byte[180];
        rng.nextBytes(data);
        int[] coeffs = ArrayEncoder.decodeMod3Sves(data, 960);
        byte[] data2 = ArrayEncoder.encodeMod3Sves(coeffs);
        assertTrue(Arrays.areEqual(data, data2));
    }

    public void testEncodeDecodeMod3Tight()
    {
        SecureRandom random = new SecureRandom();

        int[] coeffs = DenseTernaryPolynomial.generateRandom(1000, random).coeffs;
        byte[] data = ArrayEncoder.encodeMod3Tight(coeffs);
        int[] coeffs2 = ArrayEncoder.decodeMod3Tight(data, 1000);
        assertTrue(Arrays.areEqual(coeffs, coeffs2));
    }
}
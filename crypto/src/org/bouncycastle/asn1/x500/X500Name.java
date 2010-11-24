package org.bouncycastle.asn1.x500;

import java.util.Enumeration;

import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x500.style.BCStyle;

/**
 * <pre>
 *     Name ::= CHOICE {
 *                       RDNSequence }
 *
 *     RDNSequence ::= SEQUENCE OF RelativeDistinguishedName
 *
 *     RelativeDistinguishedName ::= SET SIZE (1..MAX) OF AttributeTypeAndValue
 *
 *     AttributeTypeAndValue ::= SEQUENCE {
 *                                   type  OBJECT IDENTIFIER,
 *                                   value ANY }
 * </pre>
 */
public class X500Name
    extends ASN1Encodable
    implements ASN1Choice
{
    private boolean                 isHashCodeCalculated;
    private int                     hashCodeValue;

    private X500NameStyle style;
    private RDN[] rdns;

    public X500Name(X500NameStyle style, X500Name name)
    {
        this.rdns = name.rdns;
        this.style = style;
    }

    /**
     * Return a X509Name based on the passed in tagged object.
     * 
     * @param obj tag object holding name.
     * @param explicit true if explicitly tagged false otherwise.
     * @return the X509Name
     */
    public static X500Name getInstance(
        ASN1TaggedObject obj,
        boolean          explicit)
    {
        // must be true as choice item
        return getInstance(ASN1Sequence.getInstance(obj, true));
    }

    public static X500Name getInstance(
        Object  obj)
    {
        if (obj instanceof X500Name)
        {
            return (X500Name)obj;
        }
        else if (obj != null)
        {
            return new X500Name(ASN1Sequence.getInstance(obj));
        }

        throw new IllegalArgumentException("null object in factory");
    }

    public static X500Name getInstance(
        X500NameStyle style,
        Object        obj)
    {
        if (obj instanceof X500Name)
        {
            return (X500Name)obj;
        }
        else if (obj != null)
        {
            return new X500Name(style, ASN1Sequence.getInstance(obj));
        }

        throw new IllegalArgumentException("null object in factory");
    }

    /**
     * Constructor from ASN1Sequence
     *
     * the principal will be a list of constructed sets, each containing an (OID, String) pair.
     */
    private X500Name(
        ASN1Sequence  seq)
    {
        this(BCStyle.INSTANCE, seq);
    }

    private X500Name(
        X500NameStyle style,
        ASN1Sequence  seq)
    {
        this.style = style;
        this.rdns = new RDN[seq.size()];

        int index = 0;

        for (Enumeration e = seq.getObjects(); e.hasMoreElements();)
        {
            rdns[index++] = RDN.getInstance(e.nextElement());
        }
    }

    public X500Name(
        RDN[] rDNs)
    {
        this(BCStyle.INSTANCE, rDNs);
    }

    public X500Name(
        X500NameStyle style,
        RDN[]         rDNs)
    {
        this.rdns = rDNs;
        this.style = style;
    }

    public X500Name(
        String dirName)
    {
        this(BCStyle.INSTANCE, dirName);
    }

    public X500Name(
        X500NameStyle style,
        String        dirName)
    {
        this(style.fromString(dirName));

        this.style = style;
    }

    /**
     * return an array of RDNs in structure order.
     */
    public RDN[] getRDNs()
    {
        RDN[] tmp = new RDN[this.rdns.length];

        System.arraycopy(rdns, 0, tmp, 0, tmp.length);

        return tmp;
    }

    /**
     * return an array of RDNs containing the attribute type given by OID.
     */
    public RDN[] getRDNs(ASN1ObjectIdentifier oid)
    {
//        Vector  v = new Vector();
//
//        for (int i = 0; i != ordering.size(); i++)
//        {
//            v.addElement(ordering.elementAt(i));
//        }
//
//        return v;
        return null;
    }

    public DERObject toASN1Object()
    {
        return new DERSequence(rdns);
    }

    public int hashCode()
    {
        if (isHashCodeCalculated)
        {
            return hashCodeValue;
        }

        isHashCodeCalculated = true;

        hashCodeValue = style.calculateHashCode(this);

        return hashCodeValue;
    }

    /**
     * test for equality - note: case is ignored.
     */
    public boolean equals(Object obj)
    {
        if (obj == this)
        {
            return true;
        }

        if (!(obj instanceof X500Name || obj instanceof ASN1Sequence))
        {
            return false;
        }
        
        DERObject derO = ((DEREncodable)obj).getDERObject();
        
        if (this.getDERObject().equals(derO))
        {
            return true;
        }

        try
        {
            return style.areEqual(this, new X500Name(ASN1Sequence.getInstance(((DEREncodable)obj).getDERObject())));
        }
        catch (Exception e)
        {
            return false;
        }
    }
    
    public String toString()
    {
        return style.toString(this);
    }
}
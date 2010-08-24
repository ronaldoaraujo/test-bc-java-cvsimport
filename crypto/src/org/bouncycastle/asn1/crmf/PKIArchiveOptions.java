package org.bouncycastle.asn1.crmf;

import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.DERBoolean;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERTaggedObject;

public class PKIArchiveOptions
    extends ASN1Encodable
    implements ASN1Choice
{
    private EncryptedKey encKey;
    private boolean archiveRemGenPrivKey;

    public PKIArchiveOptions(EncryptedKey encKey)
    {
        this.encKey = encKey;
    }

    public PKIArchiveOptions(boolean archiveRemGenPrivKey)
    {
        this.archiveRemGenPrivKey = archiveRemGenPrivKey;
    }

    /**
     * <pre>
     *  PKIArchiveOptions ::= CHOICE {
     *      encryptedPrivKey     [0] EncryptedKey,
     *      -- the actual value of the private key
     *      keyGenParameters     [1] KeyGenParameters,
     *      -- parameters which allow the private key to be re-generated
     *      archiveRemGenPrivKey [2] BOOLEAN }
     *      -- set to TRUE if sender wishes receiver to archive the private
     *      -- key of a key pair that the receiver generates in response to
     *      -- this request; set to FALSE if no archival is desired.
     * </pre>
     */
    public DERObject toASN1Object()
    {
        if (encKey != null)
        {
            return new DERTaggedObject(true, 0, encKey);  // choice
        }

        return new DERTaggedObject(false, 2, new DERBoolean(archiveRemGenPrivKey));
    }
}
package school.cryptocode.ecc;

import java.math.*;
import java.util.*;
import java.security.*;

/**
 *
 * <p>Title: Encryption System</p>
 * <p>Description: used in ECIES in generating the secret key which is then used in a symmetric algorithm such as DES or AES , we used it here to generate the password that is used in the OTK algorithm </p>
 
 * @version 1.2
 */
public class KeyDerivation
{

  /**
   * The out byte array is the output from this method , out is divided into two byte arrays, the first one is used as the MAC key and the secind one is used as the password of the OTK algorithm
   * @param out byte[]
   * @param shared byte[]
   * @param iv byte[]
   * @param outOff int
   * @param len int
   * @throws Exception
   * @return int
   */
  public static int generateBytes(
        byte[]  out,
        byte[] shared,
        byte[] iv,
        int     outOff,
        int     len)


     throws Exception
    {

        MessageDigest digest = MessageDigest.getInstance("SHA-1");

        if ((out.length - len) < outOff)
        {
            throw new Exception("output buffer too small");
        }

        long    oBits = len * 8;

        //
        // this is at odds with the standard implementation, the
        // maximum value should be hBits * (2^32 - 1) where hBits
        // is the digest output size in bits. We can't have an
        // array with a long index at the moment...
        //
        if (oBits > (digest.getDigestLength() * 8 * (Math.pow(2,32) - 1)))
        {
            new IllegalArgumentException("Output length too large");
        }

        int cThreshold = (int)(oBits / digest.getDigestLength());

        byte[] dig = null;

        dig = new byte[digest.getDigestLength()];

        for (int counter = 1; counter <= cThreshold; counter++)
        {
            digest.update(shared, 0, shared.length);

            digest.update((byte)(counter & 0xff));
            digest.update((byte)((counter >> 8) & 0xff));
            digest.update((byte)((counter >> 16) & 0xff));
            digest.update((byte)((counter >> 24) & 0xff));

            digest.update(iv, 0, iv.length);

            dig = digest.digest();

            if ((len - outOff) > dig.length)
            {
                System.arraycopy(dig, 0, out, outOff, dig.length);
                outOff += dig.length;
            }
            else
            {
                System.arraycopy(dig, 0, out, outOff, len - outOff);
            }
        }

        digest.reset();

        return len;
    }


}

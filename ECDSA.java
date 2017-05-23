package school.cryptocode.ecc;
import java.security.SecureRandom;
import java.security.MessageDigest;
import java.math.BigInteger;

/**Implementing the generation and verification ECDSA procedures*/

public class ECDSA
{

     /**
      * Signature Generation: Refer to ECDSA algorithm(Fp Curve)
      * @param fpCurve ECOverFp
      * @param privateKey BigInteger
      * @param message String
      * @throws Exception
      * @return ECSignature
      */
     public static ECSignature generateSignature(ECOverFp fpCurve,BigInteger privateKey,String message)
     throws Exception
     {

        BigInteger n = fpCurve.order.divide(fpCurve.cofactor);

        BigInteger k,r,s,e,d;

        d = privateKey;

        ECPointOverFp kP;

        int nbits = n.toString(2).length();


        do
        {
           do
           {
              do
              {

                       k = new BigInteger(nbits,new SecureRandom());

              }while(k.compareTo(n) != -1 || k.compareTo(BigInteger.ZERO) == 0);

              kP = fpCurve.getG().mult1(k,fpCurve);
              r = kP.x.mod(n);

           }while(r.compareTo(BigInteger.ZERO) == 0);

           e = hash(message);
           s = k.modInverse(n).multiply(e.add(d.multiply(r))).mod(n);

        }while(s.compareTo(BigInteger.ZERO) == 0);


        ECSignature signature = new ECSignature(r,s);

        return signature;


     }



    /**
     * Signature Generation: Refer to ECDSA algorithm(F2m Curve)
     * @param f2mCurve ECOverF2m
     * @param privateKey BigInteger
     * @param message String
     * @throws Exception
     * @return ECSignature
     */
    public static ECSignature generateSignature(ECOverF2m f2mCurve,BigInteger privateKey,String message)
    throws Exception
    {

        BigInteger n = f2mCurve.order.divide(f2mCurve.cofactor);

        BigInteger k,r,s,e,d;

        d = privateKey;

        ECPointOverF2m kP;

        int nbits = n.toString(2).length();

        BigInteger b;


        do
        {
           do
           {
              do
              {

                 k = new BigInteger(nbits,new SecureRandom());

              }while(k.compareTo(n) != -1 || k.compareTo(BigInteger.ZERO) == 0);

               if(k.compareTo(n.subtract(k)) != 1)
                    kP = f2mCurve.getG().mult(k,f2mCurve);
               else
               {
                        kP = f2mCurve.getG().mult(n.subtract(k),f2mCurve);
                        kP = kP.negate();
               }

               b = new BigInteger(kP.x.polyToStr(),2);
               r = b.mod(n);

            }while(r.compareTo(BigInteger.ZERO) == 0);

            e = hash(message);
            s = k.modInverse(n).multiply(e.add(d.multiply(r))).mod(n);

           }while(s.compareTo(BigInteger.ZERO) == 0);


           ECSignature signature = new ECSignature(r,s);

           return signature;


        }


        /**
         * Signature Verification: Refer to ECDSA algorithm(Fp Curve)
         * @param signature ECSignature
         * @param fpCurve ECOverFp
         * @param publicKey ECPointOverFp
         * @param message String
         * @throws Exception
         * @return boolean
         */
        public static boolean verifySignature(ECSignature signature,ECOverFp fpCurve,ECPointOverFp publicKey,String message)
        throws Exception
         {
                 BigInteger r,s,n;
                 n = fpCurve.getOrder().divide(fpCurve.cofactor);
                 r = signature.getR();

                 if(!(r.compareTo(BigInteger.ZERO) == 1 && r.compareTo(n) == -1))
                   return false;


                 s = signature.getS();
                 if(!(s.compareTo(BigInteger.ZERO) == 1 && s.compareTo(n) == -1))
                   return false;


                 BigInteger e = hash(message);

                 BigInteger w = s.modInverse(n);

                 BigInteger u1 = e.multiply(w).mod(n);
                 BigInteger u2 = r.multiply(w).mod(n);

                 ECPointOverFp Q = publicKey;

                 ECPointOverFp X = fpCurve.getG().mult1(u1,fpCurve).add(Q.mult1(u2,fpCurve),fpCurve);


                 if(X.x.compareTo(fpCurve.getP()) == 0 && X.y.compareTo(fpCurve.getP()) == 0)
                    return false;

                 BigInteger v = X.x.mod(n);
                 if(v.compareTo(r) == 0)
                   return true;
                 else
                   return false;

         }

         /**
          * Signature Verification: Refer to ECDSA algorithm(F2m Curve)
          * @param signature ECSignature
          * @param f2mCurve ECOverF2m
          * @param publicKey ECPointOverF2m
          * @param message String
          * @throws Exception
          * @return boolean
          */
         public static boolean verifySignature(ECSignature signature,ECOverF2m f2mCurve,ECPointOverF2m publicKey,String message)
         throws Exception
         {
                 BigInteger r,s,n;
                 n = f2mCurve.getOrder().divide(f2mCurve.cofactor);
                 r = signature.getR();

                 if(!(r.compareTo(BigInteger.ZERO) == 1 && r.compareTo(n) == -1))
                   return false;


                 s = signature.getS();
                 if(!(s.compareTo(BigInteger.ZERO) == 1 && s.compareTo(n) == -1))
                   return false;


                 BigInteger e = hash(message);

                 BigInteger w = s.modInverse(n);

                 BigInteger u1 = e.multiply(w).mod(n);
                 BigInteger u2 = r.multiply(w).mod(n);

                 ECPointOverF2m Q = publicKey;

                 ECPointOverF2m X;

                 if(u1.compareTo(n.subtract(u1)) != 1)
                     X = f2mCurve.getG().mult(u1,f2mCurve).add(Q.mult(u2,f2mCurve),f2mCurve);
                 else
                     X = f2mCurve.getG().mult(n.subtract(u1),f2mCurve).negate().add(Q.mult(u2,f2mCurve),f2mCurve);


                 Polynomial f = f2mCurve.getIrreduciblePoly();

                 if(X.x.polyToStr().compareTo(f.polyToStr()) == 0 &&
                                    X.y.polyToStr().compareTo(f.polyToStr()) == 0 )
                     return false;

                 BigInteger b = new BigInteger(X.x.polyToStr(),2);

                 BigInteger v = b.mod(n);

                 if(v.compareTo(r) == 0)
                   return true;
                 else
                   return false;

         }


         /**
          * Hash function(SHA-1) ,
          * Returns the hash as a BigInteger
          * @param message String
          * @throws Exception
          * @return BigInteger
          */
         public static BigInteger hash(String message) throws Exception
         {

                MessageDigest md = MessageDigest.getInstance("SHA-1");

                md.update(message.getBytes("UTF8"));

                byte[] theDigest = md.digest();

                BigInteger m = new BigInteger(1,theDigest);

                return m;


         }

         /**
          * Hash function(SHA-1) , Returns the hash as a byte array
          * @param message String
          * @throws Exception
          * @return byte[]
          */
         public static byte[] digest(String message) throws Exception
         {

               MessageDigest md = MessageDigest.getInstance("SHA-1");

               md.update(message.getBytes("UTF8"));

               byte[] theDigest = md.digest();

               return theDigest;

         }


}//end of ECDSA

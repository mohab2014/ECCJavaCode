package school.cryptocode.ecc;


//Implementing ECIES scheme using 3DES and HMACSHA-1

import java.io.*;
import java.math.*;
import java.security.*;
import java.util.*;
import javax.crypto.*;
import javax.crypto.spec.*;

import sun.misc.*;


/**
 * <p>Title: Encryption System</p>
 * <p>Description: Implementation of the ECIES algorithm over Fp</p>
 * @author RD
 * @version 1.2
 */
public class ECIES
{
  /**
   *
   */
  static ECOverFp fpCurve;
  /**
   *
   */
  public static String status = "none";


    public static String getStatus()
    {
      return status;
    }

    /**
     * Encryption Method
     * @param curve ECOverFp
     * @param Q ECPointOverFp
     * @param fileInput String
     * @param fileOutput String
     * @throws Exception
     * @return boolean
     */
    public static boolean encrypt(ECOverFp curve,ECPointOverFp Q,String fileInput,String fileOutput) throws Exception
    {


        System.out.println("ECIES started ");

        status = "ECIES started";


        BigInteger k;
        BigInteger n = curve.order.divide(curve.cofactor);

        ECPointOverFp R;
        ECPointOverFp Z;

        int nbits = n.toString(2).length();


        do
        {
           k = new BigInteger(nbits,new Random());
           Z = Q.mult1(k,curve);
           if(Z.x.compareTo(curve.getP()) == 0 && Z.y.compareTo(curve.getP()) == 0)
             continue;
        }while(k.compareTo(n) != -1 || k.compareTo(BigInteger.ZERO) == 0);



        R = curve.getG().mult1(k,curve);

        k = null;
        curve = null;
        System.gc();

        byte[] xZbytes = Library.getBytes(Z.getX());

        String rstr = Conversion.pointToOctet(R,600);

        BigInteger  r = new BigInteger(rstr,16);

        byte[] rbytes = Library.getBytes(r);

        byte[] key = new byte[xZbytes.length + rbytes.length];

        for(int i = 0;i < xZbytes.length;i++)
           key[i] = xZbytes[i];

        for(int i = xZbytes.length;i < key.length;i++)
           key[i] = rbytes[i-xZbytes.length];


        byte[] iv = new byte[8];

        SecureRandom sr = new SecureRandom();

        sr.nextBytes(iv);

        byte outkey[] = new byte[45];

        int len = KeyDerivation.generateBytes(outkey,key,iv,0,45);

        sr = null;

        byte DESkey[] = new byte[24];
        byte MACkey[] = new byte[20];

        for(int i = 0;i < 20;i++)
           MACkey[i] = outkey[i];
        for(int i = 20;i < 44;i++)
           DESkey[i-20] = outkey[i];

        SecretKeyFactory skf = SecretKeyFactory.getInstance("DESede");
        DESedeKeySpec DESSpec = new DESedeKeySpec(DESkey);
        SecretKey deskey = skf.generateSecret(DESSpec);

        status = "DES key created";


        Mac mac = Mac.getInstance("HmacSHA1");
        SecretKeySpec mkey = new SecretKeySpec(MACkey,"HmacSHA1");

        status = "Mac key created";



        Cipher cipher = Cipher.getInstance("DESede/CFB8/NoPadding");
        IvParameterSpec spec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE,deskey,spec);

        FileInputStream fis = new FileInputStream(fileInput);
        FileOutputStream fos = new FileOutputStream(fileOutput);

        fos.write(iv);
        fos.write(rbytes);

        CipherOutputStream cos = new CipherOutputStream(fos, cipher);

        int theByte = 0;

        System.out.println("Encryption started");

        status = "Encryption started";





        while((theByte = fis.read())!=-1)
        {
           cos.write(theByte);
        }

        System.out.println("Encryption finished");
        status = "Encryption finished";



        fis = new FileInputStream(fileOutput);

        byte eat[] = new byte[159];

        fis.read(eat);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();


        System.out.println("Mac started");

        status = "Mac started";



        while((theByte = fis.read()) != -1)
        {
          baos.write(theByte);
        }

        byte mbyte[] = baos.toByteArray();
        baos.close();

        mac.init(mkey);
        mac.update(mbyte);

        byte[] result = mac.doFinal();

        fos.write(result);

        System.out.println("Mac finished");
        status = "Mac finished";

        String ext = Library.getFileExt(fileInput);
        fos.write(ext.getBytes());

        fos.close();
        fis.close();
        cos.close();



        System.out.println("ECIES finished ");
        status = "ECIES finished";




        return true;

    }

    /**
     * Decryption Method
     * @param curve ECOverFp
     * @param d BigInteger
     * @param fileInput String
     * @param fileOutput String
     * @throws Exception
     * @return String
     */
    public static String decrypt(ECOverFp curve,BigInteger d,String fileInput,String fileOutput) throws Exception
    {

        String exten = null;
        System.out.println("ECIES decryption started");

        status = "ECIES decryption started";


        BASE64Encoder encoder = new BASE64Encoder();

        byte iv[] = new byte[8];

        byte rbytes[] = new byte[151];


        FileInputStream fis = new FileInputStream(fileInput);
        FileOutputStream fos = new FileOutputStream(fileOutput);


        fis.read(iv);
        fis.read(rbytes);

        BigInteger r = new BigInteger(1,rbytes);

        String octet = "0" + r.toString(16);

        ECPointOverFp R = Conversion.octetToFpPoint(octet,600);

        ECPointOverFp Z = R.mult1(curve.cofactor.multiply(d),curve);

        if(Z.x.compareTo(curve.getP()) == 0 && Z.y.compareTo(curve.getP()) == 0)
             return null;//false;

        byte[] xZbytes = Library.getBytes(Z.getX());

        String rstr = Conversion.pointToOctet(R,600);

        byte[] key = new byte[xZbytes.length + rbytes.length];

        for(int i = 0;i < xZbytes.length;i++)
           key[i] = xZbytes[i];

        for(int i = xZbytes.length;i < key.length;i++)
           key[i] = rbytes[i-xZbytes.length];


        byte outkey[] = new byte[45];

        int len = KeyDerivation.generateBytes(outkey,key,iv,0,45);


        byte DESkey[] = new byte[24];
        byte MACkey[] = new byte[20];

        for(int i = 0;i < 20;i++)
             MACkey[i] = outkey[i];

        for(int i = 20;i < 44;i++)
             DESkey[i-20] = outkey[i];

        SecretKeyFactory skf = SecretKeyFactory.getInstance("DESede");
        DESedeKeySpec DESSpec = new DESedeKeySpec(DESkey);
        SecretKey deskey = skf.generateSecret(DESSpec);

        Mac mac = Mac.getInstance("HmacSHA1");

        SecretKeySpec mkey = new SecretKeySpec(MACkey,"HmacSHA1");
        mac.init(mkey);


        int theByte = 0;

        FileInputStream fis1 = new FileInputStream(fileInput);

        byte eat[] = new byte[159];
        fis1.read(eat);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();


        System.out.println("Mac started");
        status = "Mac started";




        while((theByte = fis1.read()) != -1)
        {
           baos.write(theByte);
        }



        byte vbyte[] = baos.toByteArray();


        int i,j;

        byte hash[] = new byte[20];
        byte ext[] = new byte[3];

        for(int ii = vbyte.length-1,jj = 2;jj >= 0;ii--,jj--)
           ext[jj] = vbyte[ii];

        exten = new String(ext);


        for(i = vbyte.length-4,j = 19; j >= 0; i--,j--)
           hash[j] = vbyte[i];


        byte mbyte[] = new byte[vbyte.length-3-20];

        for(j = 0; j < mbyte.length; j++)
           mbyte[j] = vbyte[j];

        mac.update(mbyte);

        byte[] result = mac.doFinal();

        String t2 = encoder.encode(result);
        String t1 =  encoder.encode(hash);

        if(t2.equals(t1) == false)
           return null;//false;


        System.out.println("Mac finished");
        status = "Mac finished";

        Cipher cipher = Cipher.getInstance("DESede/CFB8/NoPadding");
        IvParameterSpec spec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, deskey, spec);

        CipherInputStream cis = new CipherInputStream(fis, cipher);


        System.out.println("Decryption started");
        status = "Decryption started";



        int counter = 0;

        while((theByte = cis.read())!= -1 && counter < mbyte.length)
        {
            fos.write(theByte);
            counter++;
        }

        System.out.println("Decryption finished");
        status = "Decryption finished";



        System.out.println("ECIES finished");
        status = "ECIES decryption finished";


        cis.close();
        fos.close();
        fis1.close();

        return exten;//true;

   }

}//End of class ECIES














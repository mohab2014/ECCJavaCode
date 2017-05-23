
/*
                       Faculty Of Mathematical Sciences
                                 Final year
                      Mohammed Ahmed Awad Elkariem 99-112
                        Graduation Project - August 2004
                    Implementing Elliptic curve cryptosystems


*/

//used in ElGamal and ECIES to generate keypairs over Fp and save them in files
package school.cryptocode.ecc;
import java.math.*;
import java.security.*;
import java.io.*;


public class ECFpKeyGenerator
{


  /**
   * Save the public key in publicKeyFile
   */
  static PrintWriter publicKeyFile;
  /**
   * Save the private key in privateKeyFile
   */
  static PrintWriter privateKeyFile;

  /**
   * This method generates the keypair and saves them in publickeyFile and privateKeyFile
   * @param fpCurve ECOverFp
   * @param pathName String
   * @return ECKeyPair
   */
  public static ECKeyPair generateKeys(ECOverFp fpCurve,String pathName)
  {

       ECKeyPair eckeyPair = ECOverFpKPG.generateKeyPair(fpCurve);

       //save keys in files

       File dir = new File(pathName);

       if(dir.exists() == false)
           dir.mkdirs();


       try
       {

           publicKeyFile = new PrintWriter( new FileWriter( pathName + "\\publickey.ecc" ) );
           privateKeyFile = new PrintWriter( new FileWriter( pathName +"\\privatekey.ecc" ) );

           publicKeyFile.println(fpCurve.degree);
           publicKeyFile.println(Conversion.pointToOctet(eckeyPair.getPublicKey(),fpCurve.degree));


           privateKeyFile.println(eckeyPair.getPrivateKey());
           privateKeyFile.println(fpCurve.degree);

       }

       catch(IOException e)
       {
           System.out.println("I/O Error" + e.toString());
       }

       finally
       {
           publicKeyFile.close();
           privateKeyFile.close();
       }

       return eckeyPair;


  }

  /*public static void main(String args[])
    {
                System.out.println(Library.getTime());
                generateKeys((ECOverFp)StandardEC.getStandardCurve("p-521"),"c:\\ECC");
                System.out.println(Library.getTime());
    }
   */

}

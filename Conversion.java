package school.cryptocode.ecc;

import java.math.*;

/**
 *
 * <p>Title: Encryption System</p>
* <p>Description: Coversion between several data types point,octet string,byte,fieldelement,integer</p>
 */
public class Conversion
{


  /**
   *
   * @param x BigInteger
   * @param len int
   * @return String
   */
  public static String intToOctet(BigInteger x,int len)
  {

           String octet = x.toString(16);

           int d;

           len = 2*(int)(Math.ceil((double)len/8));

           if(octet.length() < len)
           {
               d = len - octet.length();
               octet = "";

               for(int i = 0;i < d;i++)
                 octet += "0";

               octet += x.toString(16);

           }

           return octet;

     }

      //-------------------------------------------

      /**
       *
       * @param f Polynomial
       * @param len int
       * @return String
       */
      public static String polyToOctet(Polynomial f,int len)
      {

           String bits = f.polyToStr();

           BigInteger x = new BigInteger(bits,2);

           String octet = x.toString(16);

           len = 2*(int)(Math.ceil((double)len/8));

           int d;

           if(octet.length() < len)
           {
               d = len - octet.length();
               octet = "";
               for(int i = 0;i < d;i++)
                 octet += "0";
               octet += x.toString(16);
           }
           return octet;

      }

      /**
       *
       * @param point ECPointOverFp
       * @param len int
       * @return String
       */
      public static String pointToOctet(ECPointOverFp point,int len)
      {

         String xOctet = intToOctet(point.getX(),len);
         String yOctet = intToOctet(point.getY(),len);
         String pOctet = "04" + xOctet + yOctet;

         return pOctet;

      }

      /**
       *
       * @param point ECPointOverF2m
       * @param len int
       * @return String
       */
      public static String pointToOctet(ECPointOverF2m point,int len)
      {
         String xOctet = polyToOctet(point.getX(),len);
         String yOctet = polyToOctet(point.getY(),len);
         String pOctet = "04" + xOctet + yOctet;
         return pOctet;

      }

      /**
       *
       * @param octet String
       * @param len int
       * @return ECPointOverFp
       */
      public static ECPointOverFp octetToFpPoint(String octet,int len)
      {
           len = (int)(Math.ceil((double)len/8));

           if(octet.charAt(0) != '0' && octet.charAt(1) != '4')
              return null;

           String xOctet = octet.substring(2,2*len+2);
           String yOctet = octet.substring(2*len+2,4*len+2);

           BigInteger x = new BigInteger(xOctet,16);
           BigInteger y = new BigInteger(yOctet,16);

           return new ECPointOverFp(x,y);

      }
      /**
       *
       * @param octet String
       * @param len int
       * @return ECPointOverF2m
       */
      public static ECPointOverF2m octetToF2mPoint(String octet,int len)
      {
           len = (int)(Math.ceil((double)len/8));

           if(octet.charAt(0) != '0' && octet.charAt(1) != '4')
             return null;

           String xOctet = octet.substring(2,2*len+2);
           String yOctet = octet.substring(2*len+2,4*len+2);


           BigInteger x = new BigInteger(xOctet,16);
           BigInteger y = new BigInteger(yOctet,16);

           Polynomial X = Polynomial.strToPoly(x.toString(2));
           Polynomial Y = Polynomial.strToPoly(y.toString(2));
           return new ECPointOverF2m(X,Y);

      }

      /**
       *
       * @param octet String
       * @return byte[]
       */
      public static byte[] octetToBytes(String octet)
      {
            BigInteger s = new BigInteger(octet,16);
            return Library.getBytes(s);
      }

      /**
       *
       * @param bytes byte[]
       * @return String
       */
      public static String bytesToOctet(byte[] bytes)
      {

            BigInteger s = new BigInteger(1,bytes);
            return "0" + s.toString(16);

      }

      /**
       *
       * @param point ECPointOverFp
       * @param len int
       * @return byte[]
       */
      public static byte[] pointToBytes(ECPointOverFp point,int len)
      {
           String octet = pointToOctet(point,len);
           return octetToBytes(octet);
      }

      /**
       *
       * @param point ECPointOverF2m
       * @param len int
       * @return byte[]
       */
      public static byte[] pointToBytes(ECPointOverF2m point,int len)
      {
          String octet = pointToOctet(point,len);
          return octetToBytes(octet);
      }

      /**
       *
       * @param bytes byte[]
       * @param len int
       * @return ECPointOverFp
       */
      public static ECPointOverFp bytesToFpPoint(byte[] bytes,int len)
      {
          String octet = bytesToOctet(bytes);
          ECPointOverFp point = octetToFpPoint(octet,len);
          return point;
      }

      /**
       *
       * @param bytes byte[]
       * @param len int
       * @return ECPointOverF2m
       */
      public static ECPointOverF2m bytesToF2mPoint(byte[] bytes,int len)
      {
         String octet = bytesToOctet(bytes);
         ECPointOverF2m point = octetToF2mPoint(octet,len);
         return point;
      }



}//end of class conversion

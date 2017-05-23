
/*
                       Faculty Of Mathematical Sciences
                                  Final year
                      Mohammed Ahmed Awad Elkariem 99-112
                        Graduation Project - August 2004
                    Implementing Elliptic curve cryptosystems


*/
//Usually used functions

package school.cryptocode.ecc;
import java.math.*;
import java.util.Calendar;
import java.io.*;


public class Library
{

  /**
   * This method reverse the order of a string
   * @param source String
   * @return String the reverse of source
   */
  public static String reverseIt(String source)
  {

            int i, len = source.length();
            StringBuffer dest = new StringBuffer(len);
            for (i = (len - 1); i >= 0; i--)
                dest.append(source.charAt(i));
            return dest.toString();
  }


  /**
   * This method gets the current time
   * Mostly used in the evaluation of a certain algorithm
   * @return String
   */
  public static String  getTime()
  {

     Calendar calendar = Calendar.getInstance();
     int hour = calendar.get(Calendar.HOUR_OF_DAY);
     int minute = calendar.get(Calendar.MINUTE);
     int second = calendar.get(Calendar.SECOND);

     return (""+ hour + minute + second);

   }


   /**
    * This method converts a BigInteger object to byte array
    * @param big BigInteger
    * @return byte[]
    */
   public static byte[] getBytes(BigInteger big)
   {

            byte[] bigBytes = big.toByteArray();
            if ((big.bitLength() % 8) != 0)
            {
               return bigBytes;
            }
            else
            {
               byte[] smallerBytes = new byte[big.bitLength() / 8];
               System.arraycopy(bigBytes, 1, smallerBytes, 0, smallerBytes.length);
               return smallerBytes;

            }

    }

    /**
    * This method checks the validity of a filename under Windows
    * @param filename String
    * @return boolean
    */

    public static boolean isValidFileName(String filename)
    {
                if (filename.equals(""))
                  return false;
                for(int i = 0; i < filename.length(); i++)
                   if(filename.charAt(i) == '\\' ||
                      filename.charAt(i) == '/' ||
                      filename.charAt(i) == ':' ||
                      filename.charAt(i) == '*' ||
                      filename.charAt(i) == '?' ||
                      filename.charAt(i) == '<' ||
                      filename.charAt(i) == '>' ||
                      filename.charAt(i) == '|' )
                      return false;
                return true;
    }


    /**
     * This method checks whether a file is in a dir or not
     * @param dirName String
     * @param fileName String
     * @return boolean
     */
    public static boolean isFileInDir(String dirName,String fileName)
    {

                File path = new File( "c:\\" + dirName + "\\" );
                String list[];

                list = path.list();

                for ( int count = 0; count < list.length; count++ )
                           if( fileName.equals(list[count]) )
                               return true;
                return false;

    }

    /**
     * This method gets the extension of a file
     * @param fileName String
     * @return String
     */
    public static String getFileExt(String fileName)
    {
           char extensionSeparator = '.';
           int dot = fileName.lastIndexOf(extensionSeparator); // extension separator
           return fileName.substring(dot+1);
    }


    /**
     * This method extracts just the name of a file from its path
     * @param fullPath String
     * @return String
     */
    public static String getFilename(String fullPath)
    {
           char extensionSeparator = '.';
           char pathSeparator = '\\';
           int dot = fullPath.lastIndexOf(extensionSeparator);
           int sep = fullPath.lastIndexOf(pathSeparator);
           return fullPath.substring(sep + 1, dot);
    }


    /**
     *
     * @param k BigInteger
     * @return String
     */
    public static String NAF(BigInteger k)
    {
           BigInteger two = new BigInteger("2");
           BigInteger four = new BigInteger("4");

           String naf = new String();

           BigInteger temp;

           while(k.compareTo(BigInteger.ONE)!= -1)
           {
                   if(k.mod(two).compareTo(BigInteger.ZERO) != 0)
                   {

                      temp = two.subtract(k.mod(four));
                      if(temp.compareTo(BigInteger.ONE) == 0)
                          naf += "+";
                      else if(temp.compareTo(BigInteger.ZERO) == 0)
                          naf += "0";
                      else
                          naf += "-";
                      k = k.subtract(temp);
                   }
                   else
                   {
                           naf += "0";
                   }

                   k = k.divide(two);

           }

           return Polynomial.reverseIt(naf);

    }


    /**
     *
     * @param d BigDecimal
     * @return BigInteger
     */
    public static BigInteger round(BigDecimal d)
    {
         return SquareRoot.floor(d.add(new BigDecimal("0.5")));
    }

    /**
     *
     * @param folderName String
     */
    public static void emptyFolder(String folderName)
    {

           File dir = new File(folderName);

           String[] list = dir.list();

           for(int i = 0;i < list.length; i++)
           {
              File file = new File(dir + "\\" + list[i]);

              file.delete();

           }

    }



}// end of Library class

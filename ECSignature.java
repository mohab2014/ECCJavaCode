package school.cryptocode.ecc;

import java.io.Serializable;
import java.math.*;


public class ECSignature implements Serializable
{
  /**
   *
   */
  private BigInteger r;

  /**
   *
   */
  private BigInteger s;

  /**
   *
   * @param r BigInteger
   * @param s BigInteger
   */
  public ECSignature(BigInteger r,BigInteger s)
  {
      this.r = r;
      this.s = s;
  }
  /**
   *
   * @return BigInteger
   */

  public BigInteger getR()
  {
      return r;
  }
  /**
   *
   * @return BigInteger
   */
  public BigInteger getS()
  {
      return s;
  }


  /**
   *
   * @return String
   */
  public String toString()
  {
     return r.toString(16) + ";" + s.toString(16);
  }
}



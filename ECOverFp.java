package school.cryptocode.ecc;

import java.math.*;
import java.io.Serializable;

/**
 *
 * <p>Title: Encryption System</p>
 * <p>Description: Elliptic Curve over Fp: y^2 = x^3 + ax + b</p>
 */
public class ECOverFp implements Serializable
{
  /**
   * a = EC's parameter (see the equation)
   */
  private BigInteger a;
  /**
   * b = EC's parameter (see the equation)
   */
  private BigInteger b;
  /**
   * p = finite field order
   */
  private BigInteger p;

    /**
     * order = curve order
     */
    public BigInteger order;
    public BigInteger cofactor;
    /**
     * G = base point
     */
    public ECPointOverFp G;
    /**
     * degree = curve degree
     */
    public int degree;


    public ECOverFp()
    {

    }

    /**
     * Equation: y^2 = x^3 + ax + b
     * @param a BigInteger
     * @param b BigInteger
     * @param p BigInteger
     */
    public ECOverFp(BigInteger a,BigInteger b,BigInteger p)
    {
         this.a = a;
         this.b = b;
         this.p = p;
         this.order = null;
         this.cofactor = null;
         this.G = null;

    }

    /**
     * @return BigInteger
     */
    public BigInteger getA()
    {
       return a;
    }

    /**
     * @return BigInteger
     */


    public BigInteger getB()
    {
       return b;
    }

    /**
     * @return BigInteger
     */
    public BigInteger getP()
    {
       return p;
    }

    /**
     *
     * @return ECPointOverFp
     */
    public ECPointOverFp getG()
    {
        return G;
    }

    /**
     *
     * @return BigInteger
     */
    public BigInteger getOrder()
    {
       return order;
    }

    /**
     * Check the validity of a curve over Fp
     * @return boolean
     */

    public boolean isValid()
    {
       BigInteger four = new BigInteger("4");
       BigInteger twentySeven = new BigInteger("27");
       BigInteger acubed = a.pow(3);
       BigInteger bsquared = b.pow(2);

       if(four.multiply(acubed).add(twentySeven.multiply(bsquared)).mod(p).compareTo(BigInteger.ZERO) == 0)
          return false;
       return true;
    }


}//End of class ECoverFp

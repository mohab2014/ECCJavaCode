package school.cryptocode.ecc;
import java.math.*;

/**
 *
 * <p>Title: Encryption System</p>
 * <p>Description: used to speed EC's over Fp operations</p>
 
 * @version 1.2
 */
public class JacobianPoint
{
    BigInteger x;
    BigInteger y;
    BigInteger z;

    public JacobianPoint()
    {
        x = null;
        y = null;
        z = null;
    }


    /**
     *
     * @param x BigInteger
     * @param y BigInteger
     * @param z BigInteger
     */
    public JacobianPoint(BigInteger x,BigInteger y,BigInteger z)
    {
        this.x = new BigInteger(x.toString());
        this.y = new BigInteger(y.toString());
        this.z = new BigInteger(z.toString());
    }

    //----------------------------------------------------------
    public JacobianPoint copy()
    {
        return new JacobianPoint(x,y,z);
    }

    //----------------------------------------------------------
    public JacobianPoint negate()
    {
       return new JacobianPoint(x,y.negate(),z);
    }

    //----------------------------------------------------------
    public void display()
    {
         System.out.println("(" + x + "," + y + "," + z + ")");
    }

    /**
     * Conversion from (x,y) to (x,y,z)
     * @param point ECPointOverFp
     * @return JacobianPoint
     */
    public static JacobianPoint affineToJacobian(ECPointOverFp point)
    {
         return new JacobianPoint(point.x,point.y, BigInteger.ONE);
    }


    /**
     * Conversion from (x,y,z) to (x,y)
     * @param fpCurve ECOverFp
     * @return ECPointOverFp
     */
    public ECPointOverFp toAffine(ECOverFp fpCurve)
    {


        BigInteger p = fpCurve.getP();

        if(x.compareTo(BigInteger.ONE) == 0 && y.compareTo(BigInteger.ONE) == 0
           && z.compareTo(BigInteger.ZERO) == 0)
           return new ECPointOverFp(p,p);

         BigInteger Z = z;

         BigInteger invZ2 = Z.pow(2).modInverse(p);
         BigInteger invZ3 = Z.pow(3).modInverse(p);

         return new ECPointOverFp(x.multiply(invZ2).mod(p),y.multiply(invZ3).mod(p));

    }

    /**
     * 2*point(x,y,z)
     * @param fpCurve ECOverFp
     * @return JacobianPoint
     */

    public JacobianPoint doublePoint(ECOverFp fpCurve)
    {

        BigInteger p = fpCurve.getP();

        if(x.compareTo(BigInteger.ONE) == 0 && y.compareTo(BigInteger.ONE) == 0
                   && x.compareTo(BigInteger.ZERO) == 0)
          return this;

        BigInteger two = new BigInteger("2");
        BigInteger three = new BigInteger("3");

        BigInteger T1 = z.pow(2).mod(p);
        BigInteger T2 = x.subtract(T1);

        T1 = x.add(T1);
        T2 = T2.multiply(T1).mod(p);
        T2 = three.multiply(T2).mod(p);

        BigInteger resultX,resultY,resultZ;

        resultY = two.multiply(y).mod(p);
        resultZ = resultY.multiply(z).mod(p);
        resultY = resultY.pow(2).mod(p);

        BigInteger T3 = resultY.multiply(x).mod(p);

        resultY = resultY.pow(2).mod(p);

        resultY = resultY.multiply(two.modInverse(fpCurve.getP())).mod(p);

        resultX = T2.pow(2).mod(p);

        T1 = two.multiply(T3).mod(p);

        resultX = resultX.subtract(T1);

        T1 = T3.subtract(resultX);

        T1 = T1.multiply(T2).mod(p);

        resultY = T1.subtract(resultY);


        return new JacobianPoint(resultX,resultY,resultZ);

  }


  /**
   * Addition: p1(x,y,z) + p2(x,y,z)
   * @param point ECPointOverFp
   * @param fpCurve ECOverFp
   * @return JacobianPoint
   */
  public JacobianPoint add(ECPointOverFp point,ECOverFp fpCurve)
  {

                BigInteger p = fpCurve.getP();
                if(point.x.compareTo(p) == 0 && point.y.compareTo(p) == 0)
                  return this;

                if(x.compareTo(BigInteger.ONE) == 0 && y.compareTo(BigInteger.ONE) == 0
                   && x.compareTo(BigInteger.ZERO) == 0)
                return affineToJacobian(point);

                BigInteger T1 = z.pow(2).mod(p);

                BigInteger T2 = T1.multiply(z).mod(p);

                T1 = T1.multiply(point.x).mod(p);

                T2 = T2.multiply(point.y).mod(p);

                T1 = T1.subtract(x).mod(p);

                T2 = T2.subtract(y).mod(p);

                if(T1.compareTo(BigInteger.ZERO) == 0)
                {
                        if(T2.compareTo(BigInteger.ZERO) == 0)
                          return affineToJacobian(point).doublePoint(fpCurve);
                        else
                          return new JacobianPoint(BigInteger.ONE,BigInteger.ONE,BigInteger.ZERO);
                }

                BigInteger resultX,resultY,resultZ;

                resultZ = z.multiply(T1).mod(p);

                BigInteger T3 = T1.pow(2).mod(p);

                BigInteger T4 = T3.multiply(T1).mod(p);

                T3 = T3.multiply(x).mod(p);

                BigInteger two = new BigInteger("2");
                BigInteger three = new BigInteger("3");


                T1 = two.multiply(T3).mod(p);

                resultX = T2.pow(2).mod(p);

                resultX = resultX.subtract(T1).mod(p);

                resultX = resultX.subtract(T4).mod(p);

                T3 = T3.subtract(resultX).mod(p);

                T3 = T3.multiply(T2).mod(p);

                T4 = T4.multiply(y).mod(p);

                resultY = T3.subtract(T4).mod(p);



                return new JacobianPoint(resultX,resultY,resultZ);



    }
    /**
     * k*p(x,y,z) = p(x,y)
     * @param k BigInteger
     * @param curve ECOverFp
     * @return ECPointOverFp
     */
    public ECPointOverFp mult(BigInteger k,ECOverFp curve)
    {

            if(k.compareTo(BigInteger.ONE) == 0)
              return this.toAffine(curve);

            BigInteger three = new BigInteger("3");
            BigInteger H = three.multiply(k);


            String hbitString = Library.reverseIt(H.toString(2));
            String ebitString = Library.reverseIt(k.toString(2));


            int r = hbitString.length();

            while(ebitString.length() < r)
               ebitString += "0";

                JacobianPoint R = this.copy();

                ECPointOverFp N = this.negate().toAffine(curve);
                ECPointOverFp THIS = this.toAffine(curve);


                for(int i = r - 2;i >= 1; i--)
                {

                        R = R.doublePoint(curve);

                        if(hbitString.charAt(i) == '1' && ebitString.charAt(i) == '0')
                             R = R.add(THIS,curve);

                        else if(hbitString.charAt(i) == '0' && ebitString.charAt(i) == '1')
                             R = R.add(N,curve);


                }



       return R.toAffine(curve);

    }


    /**
     * k*p(x,y,z) = p(x,y) using NAF
     * @param k BigInteger
     * @param curve ECOverFp
     * @return ECPointOverFp
     */
    public ECPointOverFp multNAF(BigInteger k,ECOverFp curve)
    {

       if(k.compareTo(BigInteger.ONE) == 0)
          return this.toAffine(curve);

       String naf = Library.NAF(k);

       JacobianPoint Q = new JacobianPoint(BigInteger.ONE,BigInteger.ONE,BigInteger.ZERO);


       ECPointOverFp THIS = this.toAffine(curve);
       ECPointOverFp NTHIS = this.negate().toAffine(curve);


       for(int i = 0;i < naf.length();i++)
       {
            System.out.print(".");
            Q = Q.doublePoint(curve);

            if(naf.charAt(i) == '+')
               Q = Q.add(THIS,curve);
            else if(naf.charAt(i) == '-')
               Q = Q.add(NTHIS,curve);

       }

        System.out.println("");
        return Q.toAffine(curve);

    }


    /*public static void main(String args[])
    {


        ECOverFp curve = (ECOverFp)StandardEC.getStandardCurve("p-521");

        ECPointOverFp point;

        point = curve.getG().toJacobianPoint().mult(curve.getOrder(),curve);

        point.display();


     }*/



}

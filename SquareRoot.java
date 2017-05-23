
/*
                       Faculty Of Mathematical Sciences
                                Final year
                     Mohammed Ahmed Awad Elkariem 99-112
                       Graduation Project - August 2004
                   Implementing Elliptic curve cryptosystems


*/

// solving for x the equation x^2 = a(mod p) if a solution exists
// This method is used in finding a random point on an elliptic curve over Fp
// The class is not function where p = 1(mod 4).

package school.cryptocode.ecc;
import java.math.*;
import java.util.*;
import java.security.*;

public class SquareRoot
{


   public static BigInteger sqrtMod(BigInteger g,BigInteger p)
   {

       BigInteger y,u,z;
       BigInteger zero = BigInteger.ZERO;
       BigInteger one = BigInteger.ONE;
       BigInteger two = new BigInteger("2");
       BigInteger three = new BigInteger("3");
       BigInteger four = new BigInteger("4");
       BigInteger five = new BigInteger("5");
       BigInteger eight = new BigInteger("8");

       if(p.mod(four).compareTo(three) == 0)
       {
                   u = p.subtract(three).divide(four);
                   y = g.modPow(u.add(one),p);
                   z = y.pow(2).mod(p);

                   if(z.compareTo(g) == 0)
                      return y;
                   else
                      return null;

           }
           else if(p.mod(eight).compareTo(five) == 0)
           {
                   u = p.subtract(five).divide(eight);

                   BigInteger y1,i;
                   y1 = g.multiply(two).modPow(u,p);
                   i = g.multiply(two).multiply(y1.pow(2));
                   y = g.multiply(y1).multiply(i.subtract(one)).mod(p);
                   z = y.pow(2).mod(p);

                   if(z.compareTo(g) == 0)
                     return y;
                   else
                     return null;

           }
           else if(p.mod(four).compareTo(one) == 0)
           {
          //not done yet
           }

           return null;

   }

   //--------------------------------------------------------------------------------

   public static BigDecimal sqrt(BigDecimal x)
   {

            if(x.compareTo(new BigDecimal("1")) == 0 || x.compareTo(new BigDecimal("0")) == 0)
              return x;

        BigDecimal epsilon = new BigDecimal("1.0E-50");
        BigDecimal d = x;
                BigDecimal a = new BigDecimal("-1.0");
                BigDecimal b = x.add(new BigDecimal("1.0"));

        BigDecimal two = new BigDecimal("2.0");

                BigDecimal c;
                int v;

                do
                {
                   c = a.add(b).multiply(new BigDecimal("5")).movePointLeft(1);//divide(new BigDecimal(2),3,BigDecimal.ROUND_CEILING);//

                   v = f(a,d).multiply(f(c,d)).compareTo(new BigDecimal("0"));

                   if(v == 0 || v == -1)
                     b = new BigDecimal(c.toString());
                   else
                     a = new BigDecimal(c.toString());


                }
                while(b.subtract(a).abs().compareTo(epsilon) == 1);

                return b.add(a).multiply(new BigDecimal("5")).movePointLeft(1);

   }

   //--------------------------------------------------------------------------------


   public static BigDecimal f(BigDecimal x,BigDecimal y)
   {
       return x.multiply(x).subtract(y);
   }

   //--------------------------------------------------------------------------------

   public static BigInteger floor(BigDecimal decimal)
   {
           return decimal.toBigInteger();
   }


   //--------------------------------------------------------------------------------

   public static BigInteger ceil(BigDecimal decimal)
   {
           return decimal.toBigInteger().add(BigInteger.ONE);
   }

   //--------------------------------------------------------------------------------

   public static BigDecimal cut(BigDecimal decimal,int precision)
   {
            BigDecimal floor = new BigDecimal(floor(decimal));
        BigDecimal left = decimal.subtract(floor);

        int size = 2 + precision;
        if(left.toString().length() <= size)
          return decimal;

        String str = left.toString();
        String nstr = new String();
        for(int i = 0;i < size;i++)
           nstr += str.charAt(i);

        return new BigDecimal(nstr).add(floor);


   }

   //--------------------------------------------------------------------------------

   public static BigDecimal round(BigDecimal decimal,int precision)
   {

        BigDecimal result = cut(decimal,precision);

        BigDecimal left = decimal.subtract(result);
        BigDecimal d = new BigDecimal("5");

        BigDecimal addedOne = new BigDecimal("1");

        addedOne = addedOne.movePointLeft(precision);

        d = d.movePointLeft(precision+1);

        result = cut(decimal,precision);



        if(left.compareTo(d) == -1)
          return result;

        else
        {
                        result = result.add(addedOne);
                        return result;
                }


   }


}
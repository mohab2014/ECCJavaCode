
//Implementing the arithmetic of elliptic curves over Fp
package school.cryptocode.ecc;
import java.math.*;
import java.util.*;
import java.io.Serializable;


class Comp implements Comparator
{
        public int compare(Object o1,Object o2)
        {

                ECPointOverFp p1 = (ECPointOverFp)o1;
                ECPointOverFp p2 = (ECPointOverFp)o2;


                if(p1.x.longValue() == p2.x.longValue() &&
                   p1.y.longValue() == p2.y.longValue())
                   return 0;
                else
                if(p1.x.longValue() == p2.x.longValue() &&
                   p1.y.longValue() > p2.y.longValue())
                   return 1;
                else
                if(p1.x.longValue() == p2.x.longValue() &&
                   p1.y.longValue() < p2.y.longValue())
                   return -1;
                else
                if(p1.x.longValue() > p2.x.longValue())
                  return 1;
                else
                if(p1.x.longValue() < p2.x.longValue())
                  return -1;
                return -1;
        }

        public boolean equals(Object obj)
        {
        return true;
        }

}

class BigComp implements Comparator
{
        public int compare(Object o1,Object o2)
        {

                BigInteger b1 = (BigInteger)o1;
                BigInteger b2 = (BigInteger)o2;


                if(b1.longValue() == b1.longValue())
                   return 0;
                else
                if(b1.longValue() > b1.longValue())
                  return 1;
                else
                  return -1;

        }

        public boolean equals(Object obj)
        {
        return true;
        }

}

class BComp implements Comparator
{
    public int compare(Object o1,Object o2)
        {

                ECPointOverFp p1 = (ECPointOverFp)o1;
                ECPointOverFp p2 = (ECPointOverFp)o2;


                if(p1.x.compareTo(p2.x) == 0 &&
                   p1.y.compareTo(p2.y) == 0)
                   return 0;
                else
                if(p1.x.compareTo(p2.x) == 0 &&
                   p1.y.compareTo(p2.y) == 1)
                   return 1;
                else
                if(p1.x.compareTo(p2.x) == 0 &&
                   p1.y.compareTo(p2.y) == -1)
                   return -1;
                else
                if(p1.x.compareTo(p2.x) == 1)
                  return 1;
                else
                if(p1.x.compareTo(p2.x) == -1)
                  return -1;
                return -1;
        }

        public boolean equals(Object obj)
        {
        return true;
        }

}

//------------------------------------------------------




      /**
       *
       * <p>Title: Encryption System</p>
       * <p>Description: Elliptic curve over Fp points operations: addition + mult.</p>
       * <p>Copyright: Copyright (c) 2005</p>
       * <p>Company: Fania RD</p>
       * @author RD
       * @version 1.2
       */
public class ECPointOverFp implements Serializable
{

  /**
   * x coordinate
   */
  BigInteger x;
  /**
   * y coordinate
   */
  BigInteger y;
  int tag;
  BigInteger btag;


        public ECPointOverFp()
        {
        }

        //-------------------------------------------------------------

        public ECPointOverFp(BigInteger x,BigInteger y)
        {
                this.x = x;
                this.y = y;
                tag = -1;
        }

        //-------------------------------------------------------------

        public BigInteger getX()
        {
                return x;
        }

        //-------------------------------------------------------------

        public BigInteger getY()
        {
                return y;
        }

    //-------------------------------------------------------------
        public ECPointOverFp copy()
        {
                BigInteger X = new BigInteger(x.toString());
                BigInteger Y = new BigInteger(y.toString());
                return new ECPointOverFp(X,Y);
        }

        /**
         * Addition operation
         * @param point ECPointOverFp
         * @param pECurve ECOverFp
         * @return ECPointOverFp
         */
        public ECPointOverFp add(ECPointOverFp point,ECOverFp pECurve)
        {

       ECPointOverFp result = new ECPointOverFp();

       BigInteger p = pECurve.getP();
           BigInteger a = pECurve.getA();
       BigInteger b = pECurve.getB();
       BigInteger l,temp;

       BigInteger two = new BigInteger("2");
       BigInteger three = new BigInteger("3");

       while(this.x.compareTo(BigInteger.ZERO) == -1)
         this.x = this.x.add(p);
       while(point.x.compareTo(BigInteger.ZERO) == -1)
         point.x = point.x.add(p);
       while(this.y.compareTo(BigInteger.ZERO) == -1)
         this.y = this.y.add(p);
       while(point.y.compareTo(BigInteger.ZERO) == -1)
         point.y = point.y.add(p);


       if(this.x.compareTo(p) == 0 && this.y.compareTo(p) == 0)
          return point;
       else if(point.x.compareTo(p) == 0 && point.y.compareTo(p) == 0)
         return this;
       if(this.toString().compareTo(point.toString()) == 0)
       {
                   if(y.compareTo(BigInteger.ZERO) == 0)
                     return new ECPointOverFp(p,p);

                   temp = y.multiply(two);
                   while(temp.compareTo(BigInteger.ZERO) == -1)
                     temp = temp.add(p);

           l = x.pow(2).multiply(three).add(a).multiply(temp.modInverse(p)).mod(p);
           }
           else if(this.x.compareTo(point.x) == 0)
           {
                   return new ECPointOverFp(p,p);
           }
           else
           {
                   temp = point.x.subtract(x);
                   while(temp.compareTo(BigInteger.ZERO) == -1)
                     temp = temp.add(p);

                   l = point.y.subtract(y).multiply(temp.modInverse(p)).mod(p);

           }


           result.x = l.pow(2).subtract(x).subtract(point.x).mod(p);
           result.y = l.multiply(x.subtract(result.x)).subtract(y).mod(p);

       while(result.x.compareTo(BigInteger.ZERO) == -1)
         result.x = result.x.add(p);

       while(result.y.compareTo(BigInteger.ZERO) == -1)
         result.y = result.y.add(p);

       return result;

        }

        /**
         * mult = k additions
         * @param k long
         * @param curve ECOverFp
         * @return ECPointOverFp
         */
        public ECPointOverFp mult(long k,ECOverFp curve)
        {

            BigInteger e = BigInteger.valueOf(k);
                BigInteger three = new BigInteger("3");
                BigInteger x = e.multiply(three);


                String hbitString = Polynomial.reverseIt(x.toString(2));
                String ebitString = Polynomial.reverseIt(e.toString(2));


                int r = hbitString.length();

            while(ebitString.length() <= r)
               ebitString += "0";

                ECPointOverFp R = this.copy();


                for(int i = r - 2;i >= 1; i--)
                {

                        R = R.add(R,curve);

                        if(hbitString.charAt(i) == '1' && ebitString.charAt(i) == '0')
                             R = R.add(this.copy(),curve);

                        if(hbitString.charAt(i) == '0' && ebitString.charAt(i) == '1')
                             R = R.add(this.negate(),curve);


                }


                return R;



        }
        //------------------------------------------------------------
    public ECPointOverFp mult1(BigInteger k,ECOverFp curve)
        {

            if(k.compareTo(BigInteger.ONE) == 0)
              return this;
            BigInteger e = new BigInteger(k.toString());
                BigInteger three = new BigInteger("3");
                BigInteger x = e.multiply(three);


                String hbitString = Polynomial.reverseIt(x.toString(2));
                String ebitString = Polynomial.reverseIt(e.toString(2));


                int r = hbitString.length();

            while(ebitString.length() <= r)
               ebitString += "0";

                ECPointOverFp R = this.copy();


                for(int i = r - 2;i >= 1; i--)
                {

            //System.out.print(".");
                        R = R.add(R,curve);

                        if(hbitString.charAt(i) == '1' && ebitString.charAt(i) == '0')
                             R = R.add(this.copy(),curve);

                        if(hbitString.charAt(i) == '0' && ebitString.charAt(i) == '1')
                             R = R.add(this.negate(),curve);


                }
                //System.out.println();


                return R;



        }
        //-------------------------------------------------------------

        public static ECPointOverFp findPoint(ECOverFp fpCurve)
        {
                BigInteger a = fpCurve.getA();
                BigInteger b = fpCurve.getB();
                BigInteger p = fpCurve.getP();

        BigInteger x = BigInteger.ZERO;
        BigInteger y;
        BigInteger alpha;
        BigInteger sqrt = null;

        while(sqrt == null)
        {

                   do
                   {

                      x = new BigInteger(p.toString(2).length(),new Random());


                   }while((x.compareTo(p) == 1) || (x.compareTo(p) == 0));


           alpha = x.pow(2).multiply(x).add(a.multiply(x)).add(b).mod(p);

           if(alpha.compareTo(BigInteger.ZERO) == 0)
             return new ECPointOverFp(x,BigInteger.ZERO);

           sqrt = SquareRoot.sqrtMod(alpha,p);

            }

            return new ECPointOverFp(x,sqrt);


        }
        //-------------------------------------------------------------
        public static ECPointOverFp findPointOfPrimeOrder(ECOverFp fpCurve)
        {

                ECPointOverFp R = findPoint(fpCurve);

                ECPointOverFp G = R.mult1(fpCurve.cofactor,fpCurve);

                BigInteger n = fpCurve.order.divide(fpCurve.cofactor);

                if(!n.isProbablePrime(50))
                  return null;

                ECPointOverFp Q = G.mult1(n,fpCurve);

                if(Q.x.compareTo(fpCurve.getP()) == 0 &&
                   Q.y.compareTo(fpCurve.getP()) == 0)
                   return G;
                else
                   return null;

        }

    //-------------------------------------------------------------
        public static long getCurveOrder(ECOverFp fpCurve)
        {
                int i,j;
                BigInteger p = fpCurve.getP();

                long d = (long)Math.ceil(2*Math.sqrt(Math.sqrt(p.longValue())));

                System.out.println("d = " + d);


                Vector BS;
                //Vector P1BS = null;
                //Vector P2BS;
                Vector ij;

                Vector X;

                ECPointOverFp P;
                ECPointOverFp R;
                long orderOfP;

                long order = 0;

                ECPointOverFp Q;

                ECPointOverFp LP;


                int counter = 0;

                ECPointOverFp P1 = null;
                ECPointOverFp P2 = null;



                long orderOfP1 = 0;
                long orderOfP2 = 0;

                int psCounter = 0;


                long INF = (long)Math.floor(p.longValue()-2*Math.sqrt(p.longValue())+1);
        long SUP = (long)Math.ceil(p.longValue()+2*Math.sqrt(p.longValue())+1);
        long x;
        long i1,j1,i2,j2;

        int found;
        Date time;
        int sec,min,hour;

        ECPointOverFp temp;
        ECPointOverFp temp2;


            outerloop:
            while(true)
                {


           //step1
                   P = ECPointOverFp.findPoint(fpCurve);

           if(P1 != null)
           {
             if(P.x.longValue() == P1.x.longValue() && P.y.longValue() == P1.y.longValue())
             {
                                         psCounter = 1;
                                         continue outerloop;
                     }
                   }



                   Q = P.mult(d,fpCurve);

                   if(Q.x.longValue() == p.longValue() && Q.y.longValue() == p.longValue())
                             continue outerloop;


                   //step2
                   BS = new Vector();
                   X = new Vector();
                   R = new ECPointOverFp(p,p);
                   R.tag = 0;
                   BS.addElement(R);
                   P.tag = 1;
                   BS.addElement(P);

                   R = P.copy();



                   for(i = 2;i <= (int)Math.ceil(d/2);i++)
                   {

                          R = R.add(P,fpCurve);//mult(i,fpCurve);
                          if(R.x.longValue() == p.longValue() && R.y.longValue() == p.longValue())
                             continue outerloop;

                          R.tag = i;
                      BS.addElement(R);

                   }


           //step3

               ij = new Vector();

               counter = 0;

               LP = P.mult(INF,fpCurve);

           Collections.sort(BS,new Comp());

           for(i = 0;i <= (int)Math.ceil(d/2);i++)
           {
                           X.addElement(((ECPointOverFp)BS.elementAt(i)).x);
                   }




         //time = new Date();

         Library.getTime();
           temp = new ECPointOverFp(p,p);

           for(j = 1;j <= d;j++)
           {

              temp = temp.add(Q,fpCurve);
              R = LP.add(temp,fpCurve);


                         found = Collections.binarySearch(X,R.x,new BigComp());
                         if(found >= 0)
                         {

                                        if(R.y.longValue() == ((ECPointOverFp)BS.elementAt(found)).y.longValue())
                                        {

                                          counter++;
                                          found = ((ECPointOverFp)BS.elementAt(found)).tag;
                                          ij.addElement(new Long(found));
                                          ij.addElement(new Long(j));
                                    }
                                    else
                                    {

                      //temp2 = P.mult((long)Math.ceil(d/2),fpCurve);
                                          for(i = 1;i < d;i++)
                                          {
                                                temp2 = P.mult(i,fpCurve);
                                            if(R.y.longValue()
                                            == temp2.y.longValue())

                                            {

                                                counter++;
                                                      ij.addElement(new Long(i));
                                                ij.addElement(new Long(j));
                                                break;

                                            }
                                          }

                                        }


                                 }

                  //}


               }

           //step4

           System.out.println("counter = " + counter);

               if(counter == 1)
               {

                     i = (int)((Long)ij.elementAt(0)).longValue();
                     j = (int)((Long)ij.elementAt(1)).longValue();

                     order = INF + j*d - i;
                         return order;

                   }
                   else
                   {

                     i1 = ((Long)ij.elementAt(0)).longValue();
                         j1 = ((Long)ij.elementAt(1)).longValue();

                         i2 = ((Long)ij.elementAt(2)).longValue();
                         j2 = ((Long)ij.elementAt(3)).longValue();

                         orderOfP = (j2 - j1)*d - (i2 - i1);

                         if(orderOfP < (Math.sqrt(p.longValue()) - 1))
                            continue outerloop;


                      //step5
                      //step 6
                      psCounter++;


                      if(psCounter == 1)
                      {

                         P1 = P.copy();
                         orderOfP1 = orderOfP;
                         //P1BS = (Vector)BS.clone();
                         continue outerloop;

                          }
                      else if(psCounter == 2)
                      {


                         P2 = P.copy();
                         orderOfP2 = orderOfP;
                         //P2BS = (Vector)BS.clone();

                          }



                          System.out.println("pscounter = " + psCounter);
                          System.out.println("P1 = " + P1 + ", O = " + orderOfP1 );
                          System.out.println("P2 = " + P2 + ", O = " + orderOfP2);




                          ECPointOverFp tempP;


                          for(long s = 1;s <= orderOfP2;s++)
                          {
                                  if(orderOfP2 % s == 0)
                                  {

                                          R = P2.mult(s,fpCurve);

                                          for(i = 1;i <= orderOfP1;i++)
                                          {
                                                  tempP = P1.mult(i,fpCurve);
                                                  if(R.x.longValue() == tempP.x.longValue()
                                                  && R.y.longValue() == tempP.y.longValue())
                                                  {

                                                          if(s * orderOfP1 < 4 * (long)Math.sqrt(p.longValue()))
                                                          {
                                                             psCounter = 1;
                                                             continue outerloop;
                                                          }
                                                          else
                                                          {

                                                                  order = 1;
                                                                  System.out.println("INF = " + INF);
                                                                  System.out.println("SUP = " + SUP);

                                                                  while(true)
                                                                  {
                                                                    x = order*s*orderOfP1;
                                                                    if(x >= INF && x <= SUP)
                                                                      return x;

                                                                    order++;
                                                                    System.out.println("x = " + x);
                                                                  }


                                                          }


                                                  }


                                          }

                                  }

                          }//end of for

                          System.out.println("FAIL");

                  }//else


          }//while

        }
   //-------------------------------------------------------------
        public static BigInteger getBCurveOrder(ECOverFp fpCurve)
        {
                BigInteger i,j;
                BigInteger p = fpCurve.getP();


                BigDecimal one = new BigDecimal("1");
        BigDecimal two = new BigDecimal("2");

                BigInteger d = SquareRoot.ceil(
                        two.multiply(

                                SquareRoot.round(SquareRoot.sqrt( SquareRoot.sqrt(new BigDecimal(p)) ),5)

                                )

                        );

                System.out.println("d = " + d);




                Vector BS;
                Vector ij;

                ECPointOverFp P;
                ECPointOverFp R;
                BigInteger orderOfP;

                BigInteger order = BigInteger.ZERO;

                ECPointOverFp Q;

                ECPointOverFp LP;


                int counter = 0;

                ECPointOverFp P1 = null;
                ECPointOverFp P2 = null;



                BigInteger orderOfP1 = null;
                BigInteger orderOfP2 = null;

                int psCounter = 0;





                BigDecimal tempp = new BigDecimal(p);

                BigInteger INF = SquareRoot.floor(

                        tempp.subtract(
                                two.multiply(SquareRoot.round(SquareRoot.sqrt(tempp),5)
                                )).add(one));




        BigInteger SUP = SquareRoot.ceil(

                        tempp.add(two.multiply(SquareRoot.round(SquareRoot.sqrt(tempp),5)

                        )).add(one));


        BigInteger x,index;
        BigInteger i1,j1,i2,j2;

        int found;
        Date time;
        int sec,min,hour;

        ECPointOverFp temp;


            outerloop:
            while(true)
                {


           //step1
                   P = ECPointOverFp.findPoint(fpCurve);

           if(P1 != null)
           {
             if(P.x.compareTo(P1.x) == 0  && P.y.compareTo(P1.y) == 0)
             {
                                         psCounter = 1;
                                         continue outerloop;
                     }
                   }



                   Q = P.mult1(d,fpCurve);

                   if(Q.x.compareTo(p) == 0 && Q.y.compareTo(p) == 0)
                             continue outerloop;


                   //step2
                   BS = new Vector();
                   R = new ECPointOverFp(p,p);
                   R.btag = BigInteger.ZERO;
                   BS.addElement(R);

                   P.btag = BigInteger.ONE;
                   BS.addElement(P);

                   R = P.copy();

                   i = new BigInteger("2");

                   while(i.compareTo(d) == -1)
                   {

                          R = R.add(P,fpCurve);
                          if(R.x.compareTo(p) == 0 && R.y.compareTo(p) == 0)
                             continue outerloop;

                          R.btag = new BigInteger(i.toString());
                      BS.addElement(R);
                      i = i.add(BigInteger.ONE);

                   }


           //step3

               ij = new Vector();

               counter = 0;

               LP = P.mult1(INF,fpCurve);

                   Collections.sort(BS,new BComp());


           Library.getTime();
           temp = new ECPointOverFp(p,p);

           j = BigInteger.ONE;

           while(j.compareTo(d) != 1)
           {

              temp = temp.add(Q,fpCurve);
              R = LP.add(temp,fpCurve);


                      found = Collections.binarySearch(BS,R,new BComp());
                      if(found >= 0)
                      {

                                 counter++;
                                 index = ((ECPointOverFp)BS.elementAt(found)).btag;
                                 ij.addElement(new BigInteger(index.toString()));
                                 ij.addElement(new BigInteger(j.toString()));


                          }

                          j = j.add(BigInteger.ONE);



               }

           //step4

           System.out.println("counter = " + counter);

               if(counter == 1)
               {

                     i = ((BigInteger)ij.elementAt(0));
                     j = ((BigInteger)ij.elementAt(1));

                     order = INF.add(d.multiply(j)).subtract(i);
                         return order;

                   }
                   else
                   {


                     i1 = (BigInteger)ij.elementAt(0);
                         j1 = (BigInteger)ij.elementAt(1);

                         i2 = (BigInteger)ij.elementAt(2);
                         j2 = (BigInteger)ij.elementAt(3);

                         orderOfP = j2.subtract(j1).multiply(d).subtract(i2.subtract(i1));

                         BigDecimal dorderOfP = new BigDecimal(orderOfP);

                         if(dorderOfP.compareTo(
                                 SquareRoot.round(SquareRoot.sqrt(tempp),5).subtract(new BigDecimal("1"))
                                 ) == -1)
                            continue outerloop;


                      //step5
                      //step 6
                      psCounter++;


                      if(psCounter == 1)
                      {

                         P1 = P.copy();
                         orderOfP1 = orderOfP;
                         continue outerloop;

                          }
                      else if(psCounter == 2)
                      {


                         P2 = P.copy();
                         orderOfP2 = orderOfP;

                          }



                          System.out.println("pscounter = " + psCounter);
                          System.out.println("P1 = " + P1 + ", O = " + orderOfP1 );
                          System.out.println("P2 = " + P2 + ", O = " + orderOfP2);




                          ECPointOverFp tempP;

                          BigInteger s = BigInteger.ONE;
                          BigDecimal four = new BigDecimal("4");
                          BigDecimal ds;
                          BigDecimal oOfP;


                          while(s.compareTo(orderOfP2) != 1)
                          {
                                  if(orderOfP2.mod(s).compareTo(BigInteger.ZERO) == 0)
                                  {

                                          R = P2.mult1(s,fpCurve);

                                          i = BigInteger.ONE;

                                          while(i.compareTo(orderOfP1) != 1)
                                          {

                                                  tempP = P1.mult1(i,fpCurve);
                                                  if(R.x.compareTo(tempP.x) == 0
                                                  && R.y.compareTo(tempP.y) == 0)
                                                  {

                                                          ds = new BigDecimal(s);
                                                          oOfP = new BigDecimal(orderOfP1);
                                                          if(ds.multiply(oOfP).compareTo(
                                                                  four.multiply(SquareRoot.round(SquareRoot.sqrt(tempp),5))) == -1)

                                                          {
                                                             psCounter = 1;
                                                             continue outerloop;
                                                          }
                                                          else
                                                          {

                                                                  order = BigInteger.ONE;
                                                                  System.out.println("INF = " + INF);
                                                                  System.out.println("SUP = " + SUP);

                                                                  while(true)
                                                                  {
                                                                    x = order.multiply(s).multiply(orderOfP1);
                                                                    if(x.compareTo(INF) != -1 && x.compareTo(SUP) != 1)
                                                                      return x;

                                                                    order = order.add(BigInteger.ONE);
                                                                    System.out.println("x = " + x);
                                                                  }


                                                          }


                                                  }

                                                  i = i.add(BigInteger.ONE);


                                          }



                                  }

                                s = s.add(BigInteger.ONE);

                          }//end of while s

                          System.out.println("FAIL");

                  }//else


          }//while

        }




    //-------------------------------------------------------------
    public static ECPointOverFp convertByteToPoint(int mByte,ECOverFp fpCurve)
    {

                BigInteger t = BigInteger.valueOf(100*mByte);
                BigInteger x = t,y = null;

                for(int j = 0;j < 100;j++)
                {
                        x = x.add(BigInteger.valueOf(j));
                        y = SquareRoot.sqrtMod(x,fpCurve.getP());

                        if(y == null)
                          continue;
                        else
                          break;

                }

                ECPointOverFp point = new ECPointOverFp(x,y);

                return point;


        }
        //--------------------------------------------------------------
        public static int convertPointToByte(ECPointOverFp point,ECOverFp fpCurve)
        {
                BigInteger x = point.x;

                BigDecimal d = (new BigDecimal(x)).movePointLeft(2);

                x = SquareRoot.floor(d);

                return x.intValue();


        }
    /**
     *
     * @return ECPointOverFp
     */
    public ECPointOverFp negate()
    {
        return new ECPointOverFp(x,y.negate());
    }

    /**
     *
     * @return JacobianPoint
     */
    public  JacobianPoint toJacobianPoint()
    {
         return new JacobianPoint(x,y,BigInteger.ONE);
    }

    /**
     *
     * @return String
     */
    public String toString()
    {
        String str = "(" + x.toString() + "," + y.toString() + ")";
        return str;
    }

    //-------------------------------------------------------------
    public void display()
    {
       System.out.println(toString());
    }



}


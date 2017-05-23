

package school.cryptocode.ecc;
import java.util.*;
import java.math.*;



//LDPoint class speeds the point multiplication over F2m curves when a = 0 or 1


class LDPoint
{
        Polynomial x;
        Polynomial y;
        Polynomial z;

    public LDPoint()
    {
                x = null;
                y = null;
                z = null;
        }

        //----------------------------------------------------------

        public LDPoint(Polynomial x,Polynomial y,Polynomial z)
        {
                this.x = x.copy();
                this.y = y.copy();
                this.z = z.copy();
        }

        //----------------------------------------------------------

    public LDPoint copy()
    {
                return new LDPoint(x,y,z);
    }

    //----------------------------------------------------------

        public LDPoint add(ECPointOverF2m point,ECOverF2m f2mCurve)
        {
        Polynomial f = f2mCurve.getIrreduciblePoly();

                if(point.x.polyToStr().compareTo(f.polyToStr()) == 0 &&
                    point.y.polyToStr().compareTo(f.polyToStr()) == 0 )
                   return this;
                else if(this.x.polyToStr().compareTo("1") == 0 &&
                   this.y.polyToStr().compareTo("0") == 0 &&
                   this.z.polyToStr().compareTo("0") == 0)
                   return affineToLD(point);

            Polynomial T1 = z.emultiply(point.x).mod(f);

            Polynomial T2 = z.square().mod(f);

            Polynomial x3 = x.add(T1);

            T1 = z.emultiply(x3).mod(f);

            Polynomial T3 = T2.emultiply(point.y).mod(f);

            Polynomial y3 = y.add(T3);

            if(x3.polyToStr().compareTo("0") == 0)
            {
                        if(y3.polyToStr().compareTo("0") == 0)
                           return affineToLD(point).doublePoint(f2mCurve);
                        else
                           return new LDPoint(Polynomial.strToPoly("1"),
                           Polynomial.strToPoly("0"),Polynomial.strToPoly("0"));
                }

                Polynomial z3 = T1.square().mod(f);
                T3 = T1.emultiply(y3).mod(f);

                if(f2mCurve.getA().polyToStr().compareTo("1") == 0)
                   T1 = T1.add(T2);

                T2 = x3.square().mod(f);
                x3 = T2.emultiply(T1).mod(f);
                T2 = y3.square().mod(f);

                x3 = x3.add(T2);
                x3 = x3.add(T3);

                T2 = point.x.emultiply(z3).mod(f);
                T2 = T2.add(x3);
                T1 = z3.square().mod(f);
                T3 = T3.add(z3);
                y3 = T3.emultiply(T2).mod(f);
                T2 = point.x.add(point.y);
                T3 = T1.emultiply(T2).mod(f);
                y3 = y3.add(T3);

                return new LDPoint(x3,y3,z3);


        }

        //----------------------------------------------------------

        public LDPoint doublePoint(ECOverF2m f2mCurve)
        {
                if(x.polyToStr().compareTo("1") == 0 &&
                   y.polyToStr().compareTo("0") == 0 &&
                   z.polyToStr().compareTo("0") == 0)
                   return this;

                Polynomial f = f2mCurve.getIrreduciblePoly();
                Polynomial T1 = z.square().mod(f);
                Polynomial T2 = x.square().mod(f);

                Polynomial z3 = T1.emultiply(T2).mod(f);
                Polynomial x3 = T2.square().mod(f);
                T1 = T1.square().mod(f);
                T2 = T1.emultiply(f2mCurve.getB()).mod(f);
                x3 = x3.add(T2);
                T1 = y.square().mod(f);

                if(f2mCurve.getA().polyToStr().compareTo("1") == 0)
                  T1 = T1.add(z3);

                T1 = T1.add(T2);
                Polynomial y3 = x3.emultiply(T1).mod(f);
                T1 = T2.emultiply(z3).mod(f);
                y3 = y3.add(T1);

                return new LDPoint(x3,y3,z3);

        }

    //----------------------------------------------------------

    public ECPointOverF2m mult(BigInteger k,ECOverF2m curve)
    {


            if(k.compareTo(BigInteger.ONE) == 0)
              return this.toAffine(curve);

        BigInteger three = new BigInteger("3");
                BigInteger H = three.multiply(k);


                String hbitString = Polynomial.reverseIt(H.toString(2));
                String ebitString = Polynomial.reverseIt(k.toString(2));


                int r = hbitString.length();

            while(ebitString.length() < r)
               ebitString += "0";

                LDPoint R = this.copy();

                ECPointOverF2m N = this.negate().toAffine(curve);
                ECPointOverF2m THIS = this.toAffine(curve);


                for(int i = r - 2;i >= 1; i--)
                {

                        System.out.print(".");
                        R = R.doublePoint(curve);

                        if(hbitString.charAt(i) == '1' && ebitString.charAt(i) == '0')
                             R = R.add(THIS,curve);

                        else if(hbitString.charAt(i) == '0' && ebitString.charAt(i) == '1')
                             R = R.add(N,curve);


                }

            System.out.println("");

                return R.toAffine(curve);

    }
    //----------------------------------------------------------

    public ECPointOverF2m multNAF(BigInteger k,ECOverF2m curve)
    {

                if(k.compareTo(BigInteger.ONE) == 0)
               return this.toAffine(curve);

            String naf = NAF(k);

        LDPoint Q = new LDPoint(Polynomial.strToPoly("1"),
                                            Polynomial.strToPoly("0"),
                                            Polynomial.strToPoly("0"));


        ECPointOverF2m THIS = this.toAffine(curve);
                ECPointOverF2m NTHIS = this.negate().toAffine(curve);


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

    //----------------------------------------------------------
        public LDPoint negate()
        {
                return new LDPoint(x,x.add(y),z);
        }

        //----------------------------------------------------------

        public static LDPoint affineToLD(ECPointOverF2m point)
        {
                return new LDPoint(point.x,point.y,Polynomial.strToPoly("1"));
        }

        //---------------------------------------------------------

        public ECPointOverF2m toAffine(ECOverF2m f2mCurve)
        {

                Polynomial f = f2mCurve.getIrreduciblePoly();


                if(x.polyToStr().compareTo("1") == 0 &&
                   y.polyToStr().compareTo("0") == 0 &&
                   z.polyToStr().compareTo("0") == 0)
                   return new ECPointOverF2m(f,f);

                Polynomial Z = this.z;


                Polynomial invZ = Z.modInverse(f);
                Polynomial invZ2 = Z.square().mod(f).modInverse(f);

                return new ECPointOverF2m(x.emultiply(invZ).mod(f),y.emultiply(invZ2).mod(f));

        }

        //---------------------------------------------------
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




}


//Implementing the arithmetic of elliptic curves over F2m

class ECOverF2m
{

    private Polynomial a;
        private Polynomial b;
        private Polynomial irr;
        private BigInteger orderOfEF2m;

        public String type;
        public ECPointOverF2m G;
        public BigInteger order;
        public BigInteger cofactor;
        public BigInteger s0,s1;
        static int m;
        ECPointOverF2m fixedPointCalcs[];

        int degree;


        ECOverF2m()
        {

        }

        //-------------------------------------------------------------

        ECOverF2m(Polynomial a,Polynomial b,Polynomial f)
        {
                this.a = a.copy();
                this.b = b.copy();
                this.irr = f.copy();
                m = irr.degree;

            this.order = null;
                this.cofactor = null;
                this.G = null;
                type = "B";

        }
    //-------------------------------------------------------------

        public Polynomial getA()
        {
                return a;
        }

        //-------------------------------------------------------------

        public Polynomial getB()
        {
                return b;
        }

        //-------------------------------------------------------------

        public Polynomial getIrreduciblePoly()
        {
                return irr;
        }

        //-------------------------------------------------------------
        public ECPointOverF2m getG()
        {
                return G;
        }
        //--------------------------------------------------------------
        public BigInteger getOrder()
        {
                return order;
        }

        //-------------------------------------------------------------

        public static BigInteger getLargeCurveOrder(ECOverF2m largeCurve,ECOverF2m smallCurve)
        {

       int m = largeCurve.getIrreduciblePoly().degree;
       int l = smallCurve.getIrreduciblePoly().degree;

       if(m < l || m % l != 0)
          return null;


       System.out.println("m = " + m);
       System.out.println("l = " + l);

       int k = m/l;

       long smallCurveOrder = getSmallCurveOrder(smallCurve).longValue();

       //calculate alpha^k + beta^k

       Long a1 = new Long((long)Math.pow(2,l) + 1 - smallCurveOrder);
       BigInteger a = new BigInteger(a1.toString());
           BigInteger two = new BigInteger("2");
       BigInteger q = two.pow(l);
           BigInteger s0 = new BigInteger("2");
           BigInteger s1 = new BigInteger(a.toString());
           BigInteger sn = new BigInteger("0");

              for(int n = 2;n <= k;n++)
           {

                  sn = a.multiply(s1).subtract(q.multiply(s0));
                  s0 = s1;
                  s1 = sn;

           }


           BigInteger largeCurveOrder = two.pow(m).add(BigInteger.ONE).subtract(sn);

           return largeCurveOrder;

        }

    //-------------------------------------------------------------

        public static Polynomial trace(Polynomial alpha,Polynomial irrd)
        {

                Polynomial T = alpha.copy();

                for(int i = 1;i < irrd.degree;i++)
                   T = T.square().mod(irrd).add(alpha.copy());

                T = T.mod(irrd);
                return T;

        }

        //-------------------------------------------------------------

        public static BigInteger getSmallCurveOrder(ECOverF2m f2mCurve)
        {
                BigInteger two = new BigInteger("2");
                BigInteger orderOfE = new BigInteger(two.toString());

                Polynomial a = f2mCurve.getA();
                Polynomial b = f2mCurve.getB();
                Polynomial irrd = f2mCurve.getIrreduciblePoly();
                long m = irrd.degree;
                BigInteger e;
                Polynomial element;
                Long x;
                Polynomial T;

                for(long i = 1;i < Math.pow(2,m);i++)
                {
                        x = new Long(i);
                        e = new BigInteger(x.toString());
                        element = Polynomial.strToPoly(e.toString(2));


                        T = trace(a.add(element).add(b.emultiply(element.square().modInverse(irrd.copy()))),irrd);

                        if(T.polyToStr().compareTo("0") == 0)
                          orderOfE = orderOfE.add(two);

                        System.out.println(i);

                }

                return orderOfE;


        }


}//End Of ECOverF2m class


//ECPointOverF2m class
public class ECPointOverF2m
{

   Polynomial x;
   Polynomial y;

   ECPointOverF2m()
   {
           x = null;
           y = null;
   }

   //-------------------------------------------------------------

   ECPointOverF2m(Polynomial x,Polynomial y)
   {
           this.x = x.copy();
           this.y = y.copy();
   }

   //-------------------------------------------------------------

   public Polynomial getX()
   {
           return x;
   }

   //-------------------------------------------------------------

   public Polynomial getY()
   {
           return y;
   }

   //-------------------------------------------------------------

   public ECPointOverF2m copy()
   {
                return new ECPointOverF2m(x,y);
   }

   //-------------------------------------------------------------

   public ECPointOverF2m add(ECPointOverF2m point,ECOverF2m f2mECurve)
   {

           ECPointOverF2m result = new ECPointOverF2m();
           Polynomial f = f2mECurve.getIrreduciblePoly();
           Polynomial a = f2mECurve.getA();
           Polynomial b = f2mECurve.getB();
       Polynomial l,temp;

       if(this.x.polyToStr().compareTo(f.polyToStr()) == 0 &&
          this.y.polyToStr().compareTo(f.polyToStr()) == 0)
                   return point;
           else if(point.x.polyToStr().compareTo(f.polyToStr()) == 0 &&
                   point.y.polyToStr().compareTo(f.polyToStr()) == 0 )
                   return this;
       else if(this.toString().compareTo(point.toString()) == 0)
       {


           if(this.x.polyToStr().compareTo("0") == 0)
              return new ECPointOverF2m(f.copy(),f.copy());

           l = x.add(y.emultiply(x.modInverse(f)));

           if(l.degree >= f.degree)
              l = l.mod(f);

           result.x = l.square().add(l).add(a);

           if(result.x.degree >= f.degree)
              result.x = result.x.mod(f);
           result.y = x.square().add( result.x.emultiply( l.add(Polynomial.getOnePoly(0))));

           if(result.y.degree >= f.degree)
              result.y = result.y.mod(f);

           return result;

           }
           else if(this.x.polyToStr().compareTo(point.x.polyToStr()) == 0)
           {
           return new ECPointOverF2m(f.copy(),f.copy());
           }
           else
           {


                   temp = x.add(point.x);
                   l = this.y.add(point.y).emultiply((temp.modInverse(f)));

                   if(l.degree >= f.degree)
              l = l.mod(f);


                   temp = x.add(point.x);
                   temp = temp.add(a);

                   result.x = temp.add(l.square().add(l));

           if(result.x.degree >= f.degree)
              result.x = result.x.mod(f);

           result.y = l.emultiply( x.add(result.x)).add(result.x).add(y);

           if(result.y.degree >= f.degree)
              result.y = result.y.mod(f);

                   return result;

           }


   }
   //------------------------------------------------------------

   public ECPointOverF2m rtlMult(BigInteger k,ECOverF2m curve)
   {
           ECPointOverF2m Q = new ECPointOverF2m(curve.getIrreduciblePoly(),curve.getIrreduciblePoly());

           String bitString = k.toString(2);


           ECPointOverF2m P = this.copy();
           for(int i = bitString.length()-1;i >= 0;i--)
           {
                   if(bitString.charAt(i) == '1')
                      Q = Q.add(P,curve);
                   P = P.add(P,curve);
           }
           return Q;
   }


   //-------------------------------------------------------------

   public ECPointOverF2m mult(BigInteger k,ECOverF2m curve)
   {

       if(curve.type.equals("B") == true)
       {
                  return this.toLDPoint().multNAF(k,curve);

           }
           else //type = K
           {
                   return this.ekoblitzMult(k,curve);

           }

   }

   //-------------------------------------------------------------
   public ECPointOverF2m mult1(BigInteger k,ECOverF2m curve)
   {


            if(k.compareTo(BigInteger.ONE) == 0)
              return this;

        BigInteger three = new BigInteger("3");
                BigInteger H = three.multiply(k);


                String hbitString = Polynomial.reverseIt(H.toString(2));
                String ebitString = Polynomial.reverseIt(k.toString(2));


                int r = hbitString.length();

            while(ebitString.length() < r)
               ebitString += "0";

                ECPointOverF2m R = this.copy();

                ECPointOverF2m N = this.negate();
                ECPointOverF2m THIS = this.copy();


                for(int i = r - 2;i >= 1; i--)
                {

                        System.out.print(".");
                        R = R.add(R,curve);

                        if(hbitString.charAt(i) == '1' && ebitString.charAt(i) == '0')
                             R = R.add(THIS,curve);

                        else if(hbitString.charAt(i) == '0' && ebitString.charAt(i) == '1')
                             R = R.add(N,curve);


                }

            System.out.println("");

                return R;



   }
   //----------------------------------------------------------
   //used in ekoblitzMult
   public static BigInteger[] mod(BigInteger a0,BigInteger a1,BigInteger b0,BigInteger b1,
                                  BigInteger Meu)
   {

           BigInteger two = new BigInteger("2");
           BigInteger g0 = a0.multiply(b0).add(a0.multiply(b1.multiply(Meu))).add(two.multiply(a1).multiply(b1));
           BigInteger g1 = a1.multiply(b0).subtract(a0.multiply(b1));//.negate();




           BigInteger N = b0.pow(2).add(b0.multiply(b1.multiply(Meu))).add(two.multiply(b1.pow(2)));

           BigDecimal lamda0 = (new BigDecimal(g0)).divide(new BigDecimal(N),2,1);
           BigDecimal lamda1 = (new BigDecimal(g1)).divide(new BigDecimal(N),2,1);

           BigDecimal lamda[] = new BigDecimal[2];

           lamda[0] = lamda0;
           lamda[1] = lamda1;

           BigDecimal u[] = new BigDecimal[2];
           BigInteger f[] = new BigInteger[2];
           BigInteger h[] = new BigInteger[2];

           BigDecimal M = new BigDecimal(Meu);
           BigDecimal done = new BigDecimal("1");
           BigDecimal dtwo = new BigDecimal("2");
           BigDecimal dthree = new BigDecimal("3");
           BigDecimal dfour = new BigDecimal("4");
       BigDecimal dminusOne = new BigDecimal("-1");


           for(int i = 0;i < 2;i++)
           {
                   f[i] = Library.round(lamda[i]);
                   u[i] = lamda[i].subtract(new BigDecimal(f[i]));
                   h[i] = BigInteger.ZERO;
           }




           BigDecimal u1 = dtwo.multiply(u[0]).add(M.multiply(u[1]));

           if(u1.compareTo(done) != -1)
           {
                        if(u[0].subtract(dthree.multiply(M).multiply(u[1])).compareTo(dminusOne) == -1)
                           h[1] = M.toBigInteger();
                        else
                           h[0] = BigInteger.ONE;
           }
           else
           {
                        if(u[0].add(dfour.multiply(M).multiply(u[1])).compareTo(dtwo) != -1)
                           h[1] = M.toBigInteger();

           }

           if(u1.compareTo(dminusOne) == -1)
           {

                        if(u[0].subtract(dthree.multiply(M).multiply(u[1])).compareTo(done) != -1)
                           h[1] = M.toBigInteger().negate();
                        else
                           h[0] = BigInteger.ONE.negate();


           }
           else
           {

                        if(u[0].add(dfour.multiply(M).multiply(u[1])).compareTo(dtwo.negate()) == -1)
                           h[1] = M.toBigInteger().negate();

           }



       BigInteger q[] = new BigInteger[2];
           q[0] = f[0].add(h[0]);
           q[1] = f[1].add(h[1]);

           BigInteger r[] = new BigInteger[2];

           r[0] = a0.subtract(b0.multiply(q[0])).add(two.multiply(b1.multiply(q[1])));
           r[1] = a1.subtract(
                   b1.multiply(q[0]).add(b0.multiply(q[1])).add( b1.multiply(Meu).multiply(q[1]) )
                   );


           return  r;



   }

   //----------------------------------------------------------
   //used in ekoblitzMult
   public static Vector TNAF2(BigInteger r0,BigInteger r1,BigInteger Meu)
   {
            BigInteger two = new BigInteger("2");
            BigInteger four = new BigInteger("4");
            BigInteger ui,temp;

            Vector tnaf = new Vector();

        while(r0.compareTo(BigInteger.ZERO) != 0 || r1.compareTo(BigInteger.ZERO) != 0)
        {

                        if(r0.mod(two).compareTo(BigInteger.ZERO) != 0)
                        {
                        ui = two.subtract(r0.subtract(two.multiply(r1)).mod(four));

                        r0 = r0.subtract(ui);
                        }
                        else
                        {
                            ui = BigInteger.ZERO;
                        }


                        tnaf.addElement(ui);

                        temp = new BigInteger(r0.toString());

                        r0 = r1.add(Meu.multiply(r0).divide(two));
                        r1 = temp.negate().divide(two);


                }

                return tnaf;

   }

   //-----------------------------------------------------------

   public ECPointOverF2m ekoblitzMult(BigInteger n,ECOverF2m koblitz)
   {

        BigInteger Meu;


        BigInteger s1 = koblitz.s1;
        BigInteger b1 = s1.negate();

        BigInteger s0 = koblitz.s0;

        BigInteger b0;

        if(koblitz.getA().polyToStr().compareTo("0") == 0)
        {
            Meu = new BigInteger("-1");
            b0 = s0.add(b1);
                }
        else
        {
            Meu = new BigInteger("1");
            b0 = s0.subtract(b1);
                }



        Polynomial f = koblitz.getIrreduciblePoly();


        BigInteger v[] = mod(n,BigInteger.ZERO,b0,b1,Meu);


        Vector naf = TNAF2(v[0],v[1],Meu);


        LDPoint Q = new LDPoint(Polynomial.strToPoly("1"),
                                            Polynomial.strToPoly("0"),
                                            Polynomial.strToPoly("0"));

        ECPointOverF2m P0 = this.copy();
        ECPointOverF2m NP0 = P0.negate();


        BigInteger ui;


        for(int i = naf.size()-1;i >= 0;i--)
        {


                   Q.x = Q.x.square().mod(f);
                   Q.y = Q.y.square().mod(f);
           Q.z = Q.z.square().mod(f);

                   ui = (BigInteger)naf.elementAt(i);

           if(ui.compareTo(BigInteger.ONE) == 0)
                                Q = Q.add(P0,koblitz);
                   if(ui.compareTo(BigInteger.ONE.negate()) == 0)
                                 Q = Q.add(NP0,koblitz);

           System.out.print(".");

                }

                System.out.println();

                return Q.toAffine(koblitz);



   }
   //------------------------------------------------------------

   public ECPointOverF2m half(ECOverF2m curve)
   {

       Polynomial f = curve.getIrreduciblePoly();

       Polynomial lamda = Polynomial.solveQuadraticEquation(x.add(curve.getA()),f);

       Polynomial t = y.add(x.emultiply(lamda).mod(f));

       BigInteger two = new BigInteger("2");

       Polynomial lamdap,hx,hy;

       if(ECOverF2m.trace(t,f).polyToStr().compareTo("0") == 0)
       {
                   lamdap = lamda;
                   hx = t.add(x).pow(two.pow(f.degree-1),f);
           }
           else
           {
                   lamdap = lamda.add(Polynomial.strToPoly("1"));
                   hx = t.pow(two.pow(f.degree-1),f);
           }

           hx = hx.mod(f);
           hy = hx.emultiply(lamdap).add(hx.square());
           hy = hy.mod(f);

           return new ECPointOverF2m(hx,hy);

   }

   //-------------------------------------------------------------

   public static ECPointOverF2m findPoint(ECOverF2m f2mCurve)
   {
        Polynomial a = f2mCurve.getA();
        Polynomial b = f2mCurve.getB();
        Polynomial irr = f2mCurve.getIrreduciblePoly();
        int m = irr.degree;


        BigInteger rand;
                Polynomial x = null;
                Polynomial xcubed;
                Polynomial xsquared;
                Polynomial beta;
                Polynomial alpha;

        Polynomial z = null;
        BigInteger px;

        while(z == null)
        {
           rand = new BigInteger(m,new Random());

           x = Polynomial.strToPoly(rand.toString(2));

           x.display();

                   if(x.polyToStr().compareTo("0") == 0)
             return new ECPointOverF2m(Polynomial.getZeroPoly(0),
                                       b.pow(new BigInteger("2").pow(m-1),irr));



           xsquared = x.square().mod(irr);

           xcubed = xsquared.emultiply(x.copy()).mod(irr);


                   alpha = xcubed.add(a.emultiply(xsquared.copy())).add(b).mod(irr);


                   if(alpha.polyToStr().compareTo("0") == 0)
                     return new ECPointOverF2m(x,Polynomial.getZeroPoly(0));



                   beta = xsquared.modInverse(irr).emultiply(alpha.copy()).mod(irr);

           z = Polynomial.solveQuadraticEquation(beta,irr.copy());


            }

                Polynomial y = x.emultiply(z);

                y = y.mod(irr);

                return new ECPointOverF2m(x,y);


   }

   //-------------------------------------------------------------

   public ECPointOverF2m negate()
   {
           return new ECPointOverF2m(x.copy(),x.copy().add(y.copy()));
   }

   //-------------------------------------------------------------

   public  LDPoint toLDPoint()
   {
                return new LDPoint(x.copy(),y.copy(),Polynomial.strToPoly("1"));
   }
        //----------------------------------------------------------------


   public String toString()
   {
           String str = x.polyToStr() + "," + y.polyToStr();
           return str;
   }

   //-------------------------------------------------------------

   public void display()
   {
           System.out.println("(" + toString() + ")");
   }


}




/*
                       Faculty Of Mathematical Sciences
                                Final year
                     Mohammed Ahmed Awad Elkariem 99-112
                       Graduation Project - August 2004
                  Implementing Elliptic curve cryptosystems


*/


//Polynomial class: to represent the elements of the field F2m

package school.cryptocode.ecc;
import java.util.*;
import java.math.*;

class Polynomial
{

        public Vector v;
        public int t;
        public int s;
        static public int w = 32;
        public int degree;



        Polynomial()
        {

        }

    //----------------------------------------------
        Polynomial(int degree)
        {

                t = (int)Math.ceil((double)degree/w);

        if(degree % 32 == 0)
           t += 1;

                s = w*t - degree;

        this.degree = degree;

        v = new Vector();


        }

        //-------------------------------------------

        Polynomial copy()
        {

                Polynomial poly = new Polynomial(degree);
                BitSet bits;
                for(int i = 0;i < poly.t;i++)
                {
                   bits = (BitSet)v.elementAt(i);
                   poly.v.addElement(bits.clone());
                }

                return poly;

        }

        //--------------------------------------------
        void setBit(int index1,int index2)
        {
                BitSet bits = (BitSet)v.elementAt(index1);
                bits.set(index2);
        }

        //--------------------------------------------
        void clearBit(int index1,int index2)
        {
                BitSet bits = (BitSet)v.elementAt(index1);
                bits.clear(index2);
        }

        //--------------------------------------------
        public static Polynomial strToPoly(String bitString)
        {

          int degree = bitString.length() - 1;
          int t = (int)Math.ceil((double)degree/w);


                  if(degree % 32 == 0)
                        t += 1;

          if(bitString.length() % 32 != 0)
          {
                           int x = bitString.length()/32;
                           int l = 32 * (x + 1) - 1;
                           int r = l - (degree + 1) + 1;
                           String zeroString = new String();
                           for(int i = 1;i <= r;i++)
                              zeroString += "0";

                           bitString = zeroString.concat(bitString);

                  }


          String reversedBitString = Polynomial.reverseIt(bitString);

          Vector v = new Vector();

          BitSet bits = new BitSet();

          int counter = 0;

          String tempBitString = new String();

          for(int i = 0;i < t;i++)
          {

                          tempBitString = reversedBitString.substring(counter,counter+32);
                          for(int j = 0;j < 32;j++)
                          {
                                  if(tempBitString.charAt(j) == '1')
                                     bits.set(j);
                          }


                          v.addElement((BitSet)bits.clone());

                          counter += 32;

                          //now clear bits to reuse it
                          for(int k = 0;k < 32;k++)
                                bits.clear(k);

                  }

                  Polynomial poly = new Polynomial();

                  poly.degree = degree;
                  poly.t = t;

                  poly.v = new Vector();

                  for(int i = 0;i < poly.t;i++)
                  {
                          bits = (BitSet)v.elementAt(i);
                          poly.v.addElement(bits.clone());
                  }


                  return poly;


        }
        //--------------------------------------------
        String polyToStr()
        {
                int i,j;
                String str = new String();

                int strLength = degree + 1;

                BitSet bits = new BitSet();

                for(i = t - 1;i >= 0;i--)
                {
            bits = (BitSet)v.elementAt(i);
            for(j = 31;j >= 0;j--)
            {
                                if(bits.get(j) == true)
                                   str += "1";
                                else
                                   str += "0";
                        }
                }

        //truncate the leading zeros
                for(i = 0;i < str.length();i++)
                {
                        if(str.charAt(i) == '1')
                           break;
            }

            if(i != str.length())
                str = str.substring(i);
            else
                str = "0";

            degree = str.length()-1;

                return str;

        }

        //--------------------------------------------
        Polynomial add(Polynomial poly)
        {

        int i;
        Polynomial result = new Polynomial(Math.max(degree,poly.degree));
                BitSet bits1;
                BitSet bits2;

                for(i = 0;i < Math.min(t,poly.t);i++)
                {
                        bits1 = (BitSet)v.elementAt(i);
                        bits2 = (BitSet)poly.v.elementAt(i);
                        bits1 = (BitSet)bits1.clone();
                        bits2 = (BitSet)bits2.clone();
                        bits1.xor(bits2);
            result.v.addElement(bits1.clone());

                }


        if(t != poly.t)
        {
                   for(int j = i;j < Math.max(t,poly.t);j++)
                   {
                          if(t >= poly.t)
                          {
                           bits1 = (BitSet)v.elementAt(j);
                           result.v.addElement(bits1.clone());
                      }
                      else
                      {
                           bits1 = (BitSet)poly.v.elementAt(j);
                           result.v.addElement(bits1.clone());
                          }

                   }
            }

        result.polyToStr();
                return result;

        }
        //--------------------------------------------
        Polynomial multiply(Polynomial poly)
        {
        Polynomial result = new Polynomial();

        BitSet bits = (BitSet)v.elementAt(0);

        if(bits.get(0) == true)
           result = poly.copy();
        else
           result = Polynomial.getZeroPoly(poly.degree);

        String bitString = polyToStr();

        String reversedBitString = Polynomial.reverseIt(bitString);


        for(int i = 1;i < reversedBitString.length();i++)
        {
                        poly = poly.leftShift(1);

                        if(reversedBitString.charAt(i) == '1')
                             result = result.add(poly);

                }

                return result;
        }
        //--------------------------------------------

        Polynomial emultiply(Polynomial poly)
        {

                Polynomial result = Polynomial.getZeroPoly(degree + poly.degree);
                BitSet bits;
                BitSet rbits;


        int x;
                for(int k = 0;k < w;k++)
                {
                        for(int j = 0;j < t;j++)
                        {

                                bits = (BitSet)v.elementAt(j);
                                if(bits.get(k) == true)
                                {
                                        x = 0;
                                        for(int i = j;i < result.t;i++)
                                        {
                        if(x < poly.t)
                        {
                                                   rbits = (BitSet)result.v.elementAt(i);
                                                   rbits.xor((BitSet)poly.v.elementAt(x++));
                                            }

                                    }

                                }


                        }

                    if(k != w-1)
                           poly = poly.leftShift(1);


                }

                return result;

        }
        //--------------------------------------------
        Polynomial square()
        {

                Polynomial result = Polynomial.getZeroPoly(2 * degree);
                BitSet T[][] = new BitSet[t][4];
                BitSet bits;

                for(int i = 0;i < t;i++)
                {
                        for(int j = 0,b = 0;j < 4;j++,b+=8)
                        {

                                T[i][j] = new BitSet();

                                bits = (BitSet)v.elementAt(i);

                                for(int k = 0;k < 8;k++)
                                {
                                    if(bits.get(k+b) == true)
                                    {
                                                T[i][j].set(k*2);
                                        }

                                }

                        }

                }

                BitSet rbits;

                for(int i = 0;i < t;i++)
                {

                    if(2 * i < result.t)
                    {
                      rbits = (BitSet)result.v.elementAt(2 * i);

                      for(int k = 0;k < 16;k++)
                      {

                                        if(T[i][0].get(k) == true)
                                          rbits.set(k);
                                        if(T[i][1].get(k) == true)
                                          rbits.set(k+16);

                      }

                    }

                    if((2 * i + 1) < result.t)
                    {

                        rbits = (BitSet)result.v.elementAt(2 * i + 1);

                        for(int k = 0;k < 16;k++)
                        {

                                        if(T[i][2].get(k) == true)
                                      rbits.set(k);
                                    if(T[i][3].get(k) == true)
                                          rbits.set(k+16);

                        }
                    }


                }


                return result;

        }

        //---------------------------------------------

        Polynomial mod2(Polynomial f)
        {

        Polynomial tempPoly;
            Polynomial poly;
            Polynomial div = Polynomial.getZeroPoly(0);

            int x = this.degree;

        //TO DO EXCEPTION IF f = ZERO Polynomial
            if(f.degree == 0)
               return div;

        Polynomial result = this.copy();

            while(result.degree >= f.degree)
            {

            poly = new Polynomial(x - f.degree);
                        for(int j = 0;j < poly.t;j++)
                           poly.v.addElement(new BitSet());
                        poly.setBit(poly.t - 1,poly.degree - 32*(poly.t-1));

                        tempPoly = poly.emultiply(f.copy());
                        result = tempPoly.add(result.copy());
                        x = result.degree;

                }


                return result;

        }

        //---------------------------------------------------------
        Polynomial div(Polynomial f)
        {
        Polynomial tempPoly;
            Polynomial poly;
            Polynomial div = Polynomial.getZeroPoly(0);

            int x = this.degree;

        Polynomial result = this.copy();

        if(f.degree == 0)
             return this;

            while(result.degree >= f.degree)
            {

            poly = new Polynomial(x - f.degree);
                        for(int j = 0;j < poly.t;j++)
                           poly.v.addElement(new BitSet());
                        poly.setBit(poly.t - 1,poly.degree - 32*(poly.t-1));

                        div = div.add(poly.copy());
                        tempPoly = poly.emultiply(f.copy());
                        result = tempPoly.add(result.copy());
                        x = result.degree;

                }


                return div;
        }

        //--------------------------------------------

        public Polynomial pow(BigInteger k,Polynomial f)
        {
                Polynomial s = Polynomial.getOnePoly(0);
                if(k.compareTo(BigInteger.ZERO) == 0)
                  return s;

        String kbitString = Polynomial.reverseIt(k.toString(2));
                Polynomial G = this.copy();

                if(kbitString.charAt(0) == '1')
             s = this.copy();

        for(int i = 1;i < kbitString.length();i++)
        {
                        G = G.square().mod(f);
                        if(kbitString.charAt(i) == '1')
                          s = G.emultiply(s.copy()).mod(f);
                }


                return s;



        }

        //---------------------------------------------

        public static Polynomial egcd(Polynomial g,Polynomial h)
        {

                Polynomial s,t,d,s1,s2,t1,t2,q,r;

                if((h.polyToStr()).compareTo("0") == 0)
                {
                        d = g.copy();
                        s = Polynomial.getOnePoly(0);
                        t = Polynomial.getZeroPoly(0);
                        return d;

                }

                s2 = Polynomial.getOnePoly(0);
                s1 = Polynomial.getZeroPoly(0);
                t2 = Polynomial.getZeroPoly(0);
                t1 = Polynomial.getOnePoly(0);




                while((h.polyToStr()).compareTo("0") != 0)
                {


                        q = g.copy().div(h.copy());
                        r = g.add(h.multiply(q.copy()));
                        s = s2.copy().add(s1.copy().multiply(q.copy()));
                        t = t2.copy().add(t1.copy().multiply(q.copy()));

                        g = h.copy();
                        h = r.copy();

                        s2 = s1.copy();
                        s1 = s.copy();

                        t2 = t1.copy();
                        t1 = t.copy();


                }

                d = g.copy();
                s = s2.copy();
                t = t2.copy();

                return s;



        }

        //---------------------------------------------
        void display()
        {

                String bitString = polyToStr();

                if(bitString.length() == 0 || bitString == "0")
                {
                    System.out.println("Zero Polynomial");
                    System.out.println();
                    return;
                }


                int power = degree;

        if(bitString.charAt(0) == '1')
                     System.out.print("X^" + power);
                power--;

                for(int i = 1;i < bitString.length();i++)
                {

                    if(bitString.charAt(i) == '1')
                    {
                if(power != 0)
                           System.out.print("+X^" + power);
                        else
                           System.out.print("+1");

                    }

                    power--;

                }

                System.out.println();

        }

        //--------------------------------------------------

        public static Polynomial getZeroPoly(int degree)
        {
                Polynomial zeroPoly = new Polynomial(degree);
                for(int i = 0;i < zeroPoly.t;i++)
                   zeroPoly.v.addElement(new BitSet());
                return zeroPoly;
        }

        //--------------------------------------------------
        public static Polynomial  getOnePoly(int degree)
        {
                Polynomial onePoly = new Polynomial(degree);
                for(int i = 0;i < onePoly.t;i++)
                   onePoly.v.addElement(new BitSet());
                onePoly.setBit(0,0);
                return onePoly;
        }

        //--------------------------------------------------

        public static Polynomial getXPoly()
        {
                Polynomial xPoly = new Polynomial(1);
            xPoly.v.addElement(new BitSet());
            xPoly.setBit(0,1);
            return xPoly;
        }

        //--------------------------------------------------
        public  Polynomial leftShift(int n)
        {
            String bitString = polyToStr();
            BigInteger bits = new BigInteger(bitString,2);
            BigInteger shifted = bits.shiftLeft(n);
            String shiftedBitString = shifted.toString(2);
            Polynomial poly = Polynomial.strToPoly(shiftedBitString);
            return poly;
        }
        //---------------------------------------------------
        public Polynomial rightShift(int n)
        {
                String bitString = polyToStr();
                BigInteger bits = new BigInteger(bitString,2);
                BigInteger shifted = bits.shiftRight(n);
                String shiftedBitString = shifted.toString(2);
                Polynomial poly = Polynomial.strToPoly(shiftedBitString);
                return poly;
        }
        //--------------------------------------------------
        public Polynomial modInverse(Polynomial irr)
        {

                Polynomial u = this.copy();
                Polynomial v = irr.copy();
                Polynomial g1 = Polynomial.getOnePoly(0);
                Polynomial g2 = Polynomial.getZeroPoly(0);
                Polynomial x = Polynomial.getXPoly();

                Polynomial temp;

                String check = new String();

                while((u.polyToStr()).compareTo("1") != 0 && (v.polyToStr()).compareTo("1") != 0)
                {
            check = u.polyToStr();
                        while(check.charAt(check.length()-1) == '0')
                        {

               u = u.rightShift(1);

               check = g1.polyToStr();

               if(check.charAt(check.length()-1) == '0')
                  g1 = g1.rightShift(1);
               else
               {
                                  temp = g1.add(irr);
                                  g1 = temp.rightShift(1);
                           }


               check = u.polyToStr();

                        }



            check = v.polyToStr();

                        while(check.charAt(check.length()-1) == '0')
                        {


               v = v.rightShift(1);

               check = g2.polyToStr();
               if(check.charAt(check.length()-1) == '0')
                  g2 = g2.rightShift(1);
               else
               {

                                  temp = g2.add(irr);
                                  g2 = temp.rightShift(1);

                           }


               check = v.polyToStr();

                        }

                        if(u.degree > v.degree)
                        {

               u = u.add(v);
               g1 = g1.add(g2);

                        }
                        else
                        {
               v = v.add(u);
               g2 = g2.add(g1);
                        }


                }

                if(u.polyToStr().compareTo("1") == 0)
                   return g1;
                return g2;


        }
        //--------------------------------------------------
    public Polynomial modInverse2(Polynomial irr)
        {

                Polynomial u = this.copy();
                Polynomial v = irr.copy();
                Polynomial g1 = Polynomial.getOnePoly(0);
                Polynomial g2 = Polynomial.getZeroPoly(0);

                int k = 0;


                String check = new String();

                while((u.polyToStr()).compareTo("1") != 0 && (v.polyToStr()).compareTo("1") != 0)
                {
            check = u.polyToStr();
                        while(check.charAt(check.length()-1) == '0')
                        {

               u = u.rightShift(1);
               g2 = g2.leftShift(1);
               check = u.polyToStr();
               k += 1;

                        }

            check = v.polyToStr();
                        while(check.charAt(check.length()-1) == '0')
                        {
               v = v.rightShift(1);
               g1 = g1.leftShift(1);
               check = v.polyToStr();
               k += 1;
                        }

                        if(u.degree > v.degree)
                        {

               u = u.add(v.copy());
               g1 = g1.add(g2.copy());

                        }
                        else
                        {
               v = v.add(u.copy());
               g2 = g2.add(g1.copy());
                        }

                }

                Polynomial g;

                if(u.polyToStr().compareTo("1") == 0)
                   g = g1;
                else
                   g = g2;




                return g;


        }

        //--------------------------------------------------
        public boolean isIrreducible()
        {
                Polynomial f = this.copy();
                int d = f.degree;
                Polynomial u = Polynomial.getXPoly();
                Polynomial g;

            d = (int)Math.floor(d/2);

                for(int i = 1;i <= d;i++)
                {

                        u = u.square();
                        u = u.mod2(f.copy());
                        g = Polynomial.egcd(u.copy().add(Polynomial.getXPoly()), f.copy());

                        if(g.polyToStr().compareTo("1") != 0)
                             return false;

                }

                return true;

        }
        //--------------------------------------------------
        public static Polynomial getIrreducible(int m)
        {
                Polynomial result = new Polynomial(m);

                for(int i = 0;i < result.t;i++)
                   result.v.addElement(new BitSet());


                result.setBit(0,0);
                result.setBit(result.t-1,m%32);

                int rand1 = (int)Math.random()*(result.t-1);
                int rand2 = (int)Math.random()*m;

                do
                {
                  if(rand1 != 0 || rand2 != 0)
                    result.clearBit(rand1,rand2);

                  rand1 = (int)(Math.random()*(result.t-1));

                  if(rand1 == 0)
                    rand2 = (int)(Math.random()*m);
                  else
            rand2 = (int)(Math.random()*32);

                  result.setBit(rand1,rand2);

            }while(!result.isIrreducible());

            return result;


        }
        //--------------------------------------------------
        public static Polynomial getIrreducibleStandard(int m)
        {
                Polynomial result = new Polynomial(m);

                for(int i = 0;i < result.t;i++)
                {
                        result.v.addElement(new BitSet());
                }

                switch(m)
                {
                        case 113: //X^113+X^9+1
                          result.setBit(0,0);
                          result.setBit(0,9);
                          result.setBit(result.t-1,m%32);
                        break;
                        case 131: //X^131+X^8+X^3+X^2+1
                          result.setBit(0,0);
                          result.setBit(0,2);
                          result.setBit(0,3);
                          result.setBit(0,8);
                          result.setBit(result.t-1,m%32);
                        break;
                        case 163: //X^163+X^7+X^6+X^3+1
                          result.setBit(0,0);
                          result.setBit(0,3);
                          result.setBit(0,6);
                          result.setBit(0,7);
                          result.setBit(result.t-1,m%32);
                        break;

                        case 233: //X^233+X^74+1
                          result.setBit(0,0);
                          result.setBit(2,10);
                          result.setBit(result.t-1,m%32);
                        break;

                        case 283: //X^283+X^12+X^7+X^5+1
                          result.setBit(0,0);
                          result.setBit(0,5);
                          result.setBit(0,7);
                          result.setBit(0,12);
                          result.setBit(result.t-1,m%32);
                        break;

                        case 409: //X^409+X^87+1
                          result.setBit(0,0);
                          result.setBit(2,23);
                          result.setBit(result.t-1,m%32);
                        break;

                        case 571: //X^571+X^10+X^5+X^2+1
                          result.setBit(0,0);
                          result.setBit(0,2);
              result.setBit(0,5);
              result.setBit(0,10);
                          result.setBit(result.t-1,m%32);
                }

                return result;


        }
        //--------------------------------------------------
        //solve z^2 + z = C
        public static Polynomial solveQuadraticEquation(Polynomial constantCoef,Polynomial irr)
        {


                if(constantCoef.polyToStr().compareTo("0") == 0)
                    return Polynomial.getZeroPoly(0);

                Polynomial u = Polynomial.getZeroPoly(0);
                Polynomial l = Polynomial.getZeroPoly(irr.degree-1);

                BigInteger B;

                Polynomial z = null;
        Polynomial w = null;

                while(u.polyToStr().compareTo("0") == 0)
                {

                   //choose a random l belongs to F2m
                   B = new BigInteger(irr.degree,new Random());
                   l = Polynomial.strToPoly(B.toString(2));
           z = Polynomial.getZeroPoly(irr.degree-1);
           w = constantCoef.copy();


                   for(int i = 1;i < irr.degree;i++)
                   {
                          z = z.square().mod(irr).add(w.square().mod(irr).emultiply(l.copy()).mod(irr)).mod(irr);//2(irr.copy());

                          w = w.square().add(constantCoef.copy()).mod(irr);
                   }

                   if(w.polyToStr().compareTo("0") != 0)
                      return null; //no solution

                   u = z.square().add(z.copy()).mod(irr);

            }

            return z;

        }
        //--------------------------------------------------
        public static Polynomial gcd(Polynomial gx,Polynomial hx)
        {

                Polynomial rx = Polynomial.getZeroPoly(Math.min(gx.degree,hx.degree));
                while(hx.polyToStr().compareTo("0") != 0)
                {
                   rx = gx.mod2(hx);
                   gx = hx.copy();
                   hx = rx.copy();
                }
                return rx;

        }
        //--------------------------------------------------
        public Polynomial mod(Polynomial f)
        {
                int m = f.degree;

                Polynomial result;
                switch(m)
                {
                        case 163:
                           result = this.mod163();
                        break;

                        case 233:
                           result = this.mod233();
                        break;

                        case 283:
                           result = this.mod283();
            break;

                        case 409:
                           result = this.mod409();
                        break;

                        case 571:
                           result = this.mod571();
                        break;

                        default:
                           result = this.mod2(f);

                }

                return result;
        }

        //--------------------------------------------------
        public  Polynomial mod163()
        {
                if(this.degree < 163)
                  return this;

                BitSet C[] = new BitSet[11];

                for(int i = 0;i < 11;i++)
                   C[i] = new BitSet();


                for(int i = 0;i < this.v.size();i++)
                   C[i] = (BitSet)this.v.elementAt(i);



                BitSet T;

                for(int i = 10;i >= 6;i--)
                {

                        T = (BitSet)C[i].clone();

                        C[i-6].xor(shiftLeft((BitSet)T.clone(),29));

                        C[i-5].xor(shiftLeft((BitSet)T.clone(),4));
                        C[i-5].xor(shiftLeft((BitSet)T.clone(),3));
                        C[i-5].xor((BitSet)T.clone());
                        C[i-5].xor(shiftRight((BitSet)T.clone(),3));

            C[i-4].xor(shiftRight((BitSet)T.clone(),28));
            C[i-4].xor(shiftRight((BitSet)T.clone(),29));

                }

                T = shiftRight((BitSet)C[5].clone(),3);

                C[0].xor(shiftLeft((BitSet)T.clone(),7));
                C[0].xor(shiftLeft((BitSet)T.clone(),6));
                C[0].xor(shiftLeft((BitSet)T.clone(),3));
                C[0].xor((BitSet)T.clone());


        C[1].xor(shiftRight((BitSet)T.clone(),25));
        C[1].xor(shiftRight((BitSet)T.clone(),26));

        BitSet seven = new BitSet();

        seven.set(0);
        seven.set(1);
        seven.set(2);

        C[5].and(seven);

        Polynomial result = new Polynomial(162);

        for(int i = 0;i <= 5;i++)
        {
                        result.v.addElement(C[i]);
                }

                return result;

        }
        //--------------------------------------------------
        public Polynomial mod233()
        {

                if(this.degree < 233)
                  return this;

                BitSet C[] = new BitSet[16];

                for(int i = 0;i < 16;i++)
                   C[i] = new BitSet();


                for(int i = 0;i < this.v.size();i++)
                   C[i] = (BitSet)this.v.elementAt(i);


                BitSet T;


                for(int i = 15;i >= 8;i--)
                {

                        T = (BitSet)C[i].clone();

                        C[i-8].xor(shiftLeft((BitSet)T.clone(),23));
                        C[i-7].xor(shiftRight((BitSet)T.clone(),9));
                        C[i-5].xor(shiftLeft((BitSet)T.clone(),1));
            C[i-4].xor(shiftRight((BitSet)T.clone(),31));

                }

                T = shiftRight((BitSet)C[7].clone(),9);

                C[0].xor((BitSet)T.clone());
                C[2].xor(shiftLeft((BitSet)T.clone(),10));

        C[3].xor(shiftRight((BitSet)T.clone(),22));

        BitSet bits = new BitSet();

                for(int i = 0;i < 9;i++)
                    bits.set(i);


        C[7].and(bits);//bits = 111111111(0x1FF)


            Polynomial result = new Polynomial(232);

                for(int i = 0;i <= 7;i++)
                {
                        result.v.addElement(C[i]);
                }

            return result;

        }
        //--------------------------------------------------
        public Polynomial mod283()
        {
            if(this.degree < 283)
                  return this;

                BitSet C[] = new BitSet[18];

                for(int i = 0;i < 18;i++)
                   C[i] = new BitSet();


                for(int i = 0;i < this.v.size();i++)
                   C[i] = (BitSet)this.v.elementAt(i);



                BitSet T;

                for(int i = 17;i >= 9;i--)
                {

                        T = (BitSet)C[i].clone();


                        C[i-9].xor(shiftLeft((BitSet)T.clone(),5));
                        C[i-9].xor(shiftLeft((BitSet)T.clone(),10));
                        C[i-9].xor(shiftLeft((BitSet)T.clone(),12));
                        C[i-9].xor(shiftLeft((BitSet)T.clone(),17));

            C[i-8].xor(shiftRight((BitSet)T.clone(),27));
            C[i-8].xor(shiftRight((BitSet)T.clone(),22));
            C[i-8].xor(shiftRight((BitSet)T.clone(),20));
            C[i-8].xor(shiftRight((BitSet)T.clone(),15));


                }

                T = shiftRight((BitSet)C[8].clone(),27);

            C[0].xor((BitSet)T.clone());
                C[0].xor(shiftLeft((BitSet)T.clone(),5));
                C[0].xor(shiftLeft((BitSet)T.clone(),7));
                C[0].xor(shiftLeft((BitSet)T.clone(),12));




        BitSet bits = new BitSet();

        for(int i = 0;i < 27;i++)
           bits.set(i);


        C[8].and(bits);

        Polynomial result = new Polynomial(282);

        for(int i = 0;i <= 8;i++)
        {
                        result.v.addElement(C[i]);
                }

                return result;

        }
        //--------------------------------------------------
        public Polynomial mod409()
        {

                if(this.degree < 409)
                  return this;

                BitSet C[] = new BitSet[26];

                for(int i = 0;i < 26;i++)
                   C[i] = new BitSet();


                for(int i = 0;i < this.v.size();i++)
                   C[i] = (BitSet)this.v.elementAt(i);


                BitSet T;


                for(int i = 25;i >= 13;i--)
                {

                        T = (BitSet)C[i].clone();

                        C[i-13].xor(shiftLeft((BitSet)T.clone(),7));
                        C[i-12].xor(shiftRight((BitSet)T.clone(),25));
                        C[i-11].xor(shiftLeft((BitSet)T.clone(),30));
            C[i-10].xor(shiftRight((BitSet)T.clone(),2));

                }

                T = shiftRight((BitSet)C[12].clone(),25);

                C[0].xor((BitSet)T.clone());
                C[2].xor(shiftLeft((BitSet)T.clone(),23));


        BitSet bits = new BitSet();

                for(int i = 0;i < 25;i++)
                    bits.set(i);


        C[12].and(bits);//bits = 0x1FFFFFF


            Polynomial result = new Polynomial(408);

                for(int i = 0;i <= 12;i++)
                {
                        result.v.addElement(C[i]);
                }

            return result;

        }
        //--------------------------------------------------
        public Polynomial mod571()
        {


            if(this.degree < 571)
                  return this;

                BitSet C[] = new BitSet[36];

                for(int i = 0;i < 36;i++)
                   C[i] = new BitSet();


                for(int i = 0;i < this.v.size();i++)
                   C[i] = (BitSet)this.v.elementAt(i);



                BitSet T;

                for(int i = 35;i >= 18;i--)
                {

                        T = (BitSet)C[i].clone();

                        C[i-18].xor(shiftLeft((BitSet)T.clone(),5));
                        C[i-18].xor(shiftLeft((BitSet)T.clone(),7));
                        C[i-18].xor(shiftLeft((BitSet)T.clone(),10));
                        C[i-18].xor(shiftLeft((BitSet)T.clone(),15));

            C[i-17].xor(shiftRight((BitSet)T.clone(),27));
            C[i-17].xor(shiftRight((BitSet)T.clone(),25));
            C[i-17].xor(shiftRight((BitSet)T.clone(),22));
            C[i-17].xor(shiftRight((BitSet)T.clone(),17));


                }

                T = shiftRight((BitSet)C[17].clone(),27);

            C[0].xor((BitSet)T.clone());
                C[0].xor(shiftLeft((BitSet)T.clone(),2));
                C[0].xor(shiftLeft((BitSet)T.clone(),5));
                C[0].xor(shiftLeft((BitSet)T.clone(),10));




        BitSet bits = new BitSet();

        for(int i = 0;i < 27;i++)
           bits.set(i);


        C[17].and(bits);

        Polynomial result = new Polynomial(570);

        for(int i = 0;i <= 17;i++)
        {
                        result.v.addElement(C[i]);
                }
                result.v.addElement(new BitSet());

                return result;

        }
        //--------------------------------------------------
        public static BitSet shiftRight(BitSet bits,int n)
        {

                String bitString = new String();

                for(int i = 31;i >= 0;i--)
                {
                        if(bits.get(i) == true)
                          bitString += "1";
                        else
                          bitString += "0";
                }

                BigInteger big = new BigInteger(bitString,2);
                big = big.shiftRight(n);

                bitString = big.toString(2);
            String temp = "";


                if(bitString.length() < 32)
                {
                        do
                        {
                         temp += "0";
                    }while(temp.length() + bitString.length()< 32);
                    temp += bitString;
                    bitString = temp;
                }
                else if(bitString.length() > 32)
                {
                        bitString = bitString.substring(bitString.length()-32);
                }


                for(int i = 0;i < 32;i++)
                {
                        if(bitString.charAt(i) == '1')
                          bits.set(31-i);
                        else
                          bits.clear(31-i);
                }

                return bits;
        }
        //--------------------------------------------------
        public static BitSet shiftLeft(BitSet bits,int n)
        {


                String bitString = new String();

                for(int i = 31;i >= 0;i--)
                {
                        if(bits.get(i) == true)
                          bitString += "1";
                        else
                          bitString += "0";
                }

                BigInteger big = new BigInteger(bitString,2);
                big = big.shiftLeft(n);

                bitString = big.toString(2);


                String temp = "";
                if(bitString.length() < 32)
                {
                        do
                        {
                         temp += "0";
                    }while(temp.length() + bitString.length()< 32);
                    temp += bitString;
                    bitString = temp;
                }
        else if(bitString.length() > 32)
                {
                        bitString = bitString.substring(bitString.length()-32);
                }


                for(int i = 0;i < 32;i++)
                {
                        if(bitString.charAt(i) == '1')
                          bits.set(31-i);
                        else
                          bits.clear(31-i);
                }

                return bits;

        }
        //--------------------------------------------------
        public static String reverseIt(String source)
        {

            int i, len = source.length();
            StringBuffer dest = new StringBuffer(len);
            for (i = (len - 1); i >= 0; i--)
                dest.append(source.charAt(i));
            return dest.toString();
        }


}//End of class Polynomial

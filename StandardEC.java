package school.cryptocode.ecc;


//Implementing the calling of NIST recommended elliptic curves

import java.math.*;

 */
public class StandardEC
{
        static BigInteger s0,s1,v;
        /**
         *
         * @param type String, type = "p-n" , "B-n" or "K-n", where n = 192, 224, 256, 384 or 521 bits
         * @return Object (ECPointOverFp or ECPointOverF2m)
         */
        public static Object getStandardCurve(String type)
        {

         ECOverFp curveFp = null;
         BigInteger a = new BigInteger("-3");
         BigInteger b,p,o;
         String bstring,ostring;
         BigInteger two = new BigInteger("2");

         BigInteger x,y;

         Polynomial pb;
         Polynomial pa = Polynomial.strToPoly("1");
         Polynomial pa0 = Polynomial.strToPoly("0");



         Polynomial px,py;


         ECPointOverF2m f2mPoint;
         ECPointOverFp fpPoint;

         ECOverF2m curveF2m = null;

         if(type.compareTo("p-192") == 0)
         {
             bstring = "64210519e59c80e70fa7e9ab72243049feb8deecc146b9b1";
             b = new BigInteger(bstring,16);

             p = two.pow(192).subtract(two.pow(64)).subtract(BigInteger.ONE);

             ostring = "6277101735386680763835789423176059013767194773182842284081";
             o = new BigInteger(ostring);

             curveFp = new ECOverFp(a,b,p);
             curveFp.order = new BigInteger(o.toString());

             x = new BigInteger("188da80eb03090f67cbf20eb43a18800f4ff0afd82ff1012",16);
             y = new BigInteger("07192b95ffc8da78631011ed6b24cdd573f977a11e794811",16);

             fpPoint = new ECPointOverFp(x,y);
             curveFp.G = fpPoint;
             curveFp.degree = 192;


         }
         else if(type.compareTo("p-224") == 0)
         {
             bstring = "b4050a850c04b3abf54132565044b0b7d7bfd8ba270b39432355ffb4";
             b = new BigInteger(bstring,16);
             p = two.pow(224).subtract(two.pow(96)).add(BigInteger.ONE);

             ostring = "26959946667150639794667015087019625940457807714424391721682722368061";
             o = new BigInteger(ostring);

             curveFp = new ECOverFp(a,b,p);
             curveFp.order = new BigInteger(o.toString());//since h = 1

             x = new BigInteger("b70e0cbd6bb4bf7f321390b94a03c1d356c21122343280d6115c1d21",16);
             y = new BigInteger("bd376388b5f723fb4c22dfe6cd4375a05a07476444d5819985007e34",16);

             fpPoint = new ECPointOverFp(x,y);
             curveFp.G = fpPoint;

             curveFp.degree = 224;

          }
          else if(type.compareTo("p-256") == 0)
          {

             bstring = "5ac635d8aa3a93e7b3ebbd55"
                                                   + "769886bc651d06b0cc53b0f63bce3c3e27d2604b";
             b = new BigInteger(bstring,16);

             p = two.pow(256).subtract(two.pow(224)).add(two.pow(192)).add(two.pow(96)).subtract(BigInteger.ONE);

             ostring = "11579208921035624876269744694940757352999695"
                          + "5224135760342422259061068512044369";

             o = new BigInteger(ostring);

             curveFp = new ECOverFp(a,b,p);
             curveFp.order = new BigInteger(o.toString());

             x = new BigInteger("6b17d1f2e12c4247f8bce6e563a440f277037d812deb33a0f4a13945d898c296",16);
             y = new BigInteger("4fe342e2fe1a7f9b8ee7eb4a7c0f9e162bce33576b315ececbb6406837bf51f5",16);

             fpPoint = new ECPointOverFp(x,y);
             curveFp.G = fpPoint;

             curveFp.degree = 256;

          }
          else if(type.compareTo("p-384") == 0)
          {

             bstring = "b3312fa7e23ee7e4" +
                       "988e056be3f82d19181d9c6efe8141120314088f"
                       + "5013875ac656398d8a2ed19d2a85c8edd3ec2aef";
             b = new BigInteger(bstring,16);
             p = two.pow(384).subtract(two.pow(128)).subtract(two.pow(96)).add(two.pow(32)).subtract(BigInteger.ONE);
             ostring = "39402006196394479212279040100143613805079739"+
                                   "27046544666794690527962765939911326356939895"+
                       "6308152294913554433653942643";
             o = new BigInteger(ostring);
             curveFp = new ECOverFp(a,b,p);
             curveFp.order = new BigInteger(o.toString());

             x = new BigInteger("aa87ca22be8b05378eb1c71ef320ad746e1d3b628ba79b9859f741e0"+
                                "82542a385502f25dbf55296c3a545e3872760ab7",16);
             y = new BigInteger("3617de4a96262c6f5d9e98bf9292dc29f8f41dbd289a147ce9da3113"
                                + "b5f0b8c00a60b1ce1d7e819d7a431d7c90ea0e5f",16);


            fpPoint = new ECPointOverFp(x,y);
            curveFp.G = fpPoint;

            curveFp.degree = 384;





          }
          else if(type.compareTo("p-521") == 0)
          {
             bstring = "051953eb961"+
                         "8e1c9a1f929a21a0b68540eea2da725b99b315f3"+
                         "b8b489918ef109e156193951ec7e937b1652c0bd"+
                         "3bb1bf073573df883d2c34f1ef451fd46b503f00";
             b = new BigInteger(bstring,16);
             p = two.pow(521).subtract(BigInteger.ONE);
             ostring = "68647976601306097149819007990813932172694353"+
                                   "00143305409394463459185543183397655394245057"+
                                   "74633321719753296399637136332111386476861244"+
                       "0380340372808892707005449";
             o = new BigInteger(ostring);
             curveFp = new ECOverFp(a,b,p);
             curveFp.order = new BigInteger(o.toString());

             x = new BigInteger("c6858e06b7"+
                                "0404e9cd9e3ecb662395b4429c648139053fb521"+
                                "f828af606b4d3dbaa14b5e77efe75928fe1dc127"+
                                "a2ffa8de3348b3c1856a429bf97e7e31c2e5bd66",16);

             y = new BigInteger("11839296a78"+
                             "9a3bc0045c8a5fb42c7d1bd998f54449579b4468"+
                            "17afbd17273e662c97ee72995ef42640c550b901"+
                            "3fad0761353c7086a272c24088be94769fd16650",16);

             fpPoint = new ECPointOverFp(x,y);
             curveFp.G = fpPoint;

             curveFp.degree = 521;





            }
            else if(type.compareTo("B-163") == 0)
            {

             bstring = "20a601907b8c953ca1481eb10512f78744a3205fd";

             x = new BigInteger(bstring,16);

             pb = Polynomial.strToPoly(x.toString(2));
             curveF2m = new ECOverF2m(pa,pb,Polynomial.getIrreducibleStandard(163));


                     curveF2m.cofactor = new BigInteger("2");

                         o = new BigInteger("5846006549323611672814742442876390689256843201587");

             o = o.multiply(curveF2m.cofactor);

             curveF2m.order = o;


             x = new BigInteger("3f0eba16286a2d57ea0991168d4994637e8343e36",16);
                         y = new BigInteger("0d51fbc6c71a0094fa2cdd545b11c5c0c797324f1",16);

                         px = Polynomial.strToPoly(x.toString(2));
                     py = Polynomial.strToPoly(y.toString(2));

                     f2mPoint = new ECPointOverF2m(px,py);
                     curveF2m.G = f2mPoint;

                     curveF2m.degree = 163;

                     return curveF2m;
                 }
                 else if(type.compareTo("B-233") == 0)
                 {

             bstring = "066647ede6c332c7f8c0923bb58213b333b20e9ce4281fe115f7d8f90ad";
             x = new BigInteger(bstring,16);

             pb = Polynomial.strToPoly(x.toString(2));
             curveF2m = new ECOverF2m(pa,pb,Polynomial.getIrreducibleStandard(233));


                     curveF2m.cofactor = new BigInteger("2");

                         o = new BigInteger("69017463467905637874347558622770255558398127373450135"+
                                "55379383634485463");

             o = o.multiply(curveF2m.cofactor);

             curveF2m.order = o;


             x = new BigInteger("0fac9dfcbac8313bb2139f1bb755fef65bc391f8b36f8f8eb7371fd558b",16);
             y = new BigInteger("1006a08a41903350678e58528bebf8a0beff867a7ca36716f7e01f81052",16);

             px = Polynomial.strToPoly(x.toString(2));
             py = Polynomial.strToPoly(y.toString(2));

             f2mPoint = new ECPointOverF2m(px,py);
             curveF2m.G = f2mPoint;

             curveF2m.degree = 233;

             return curveF2m;

            }
            else if(type.compareTo("B-283") == 0)
            {

             bstring = "27b680ac8b8596da5a4af8a19a0303fca97fd7645309fa2a581485af6263e313b79a2f5";
             x = new BigInteger(bstring,16);

             pb = Polynomial.strToPoly(x.toString(2));
             curveF2m = new ECOverF2m(pa,pb,Polynomial.getIrreducibleStandard(283));


                     curveF2m.cofactor = new BigInteger("2");

                         o = new BigInteger("77706755689029162836778476272940756265696259243769048"+
                                "89109196526770044277787378692871"
                                );

             o = o.multiply(curveF2m.cofactor);

             curveF2m.order = o;


             x = new BigInteger("5f939258db7dd90e1934f8c70b0dfec2eed25b8557eac9c80e2e198f8cdbecd86b12053",16);
                         y = new BigInteger("3676854fe24141cb98fe6d4b20d02b4516ff702350eddb0826779c813f0df45be8112f4",16);

                         px = Polynomial.strToPoly(x.toString(2));
                     py = Polynomial.strToPoly(y.toString(2));

                     f2mPoint = new ECPointOverF2m(px,py);
                     curveF2m.G = f2mPoint;

                     curveF2m.degree = 283;

                     return curveF2m;

                 }
                 else if(type.compareTo("B-409") == 0)
                 {

             bstring = "021a5c2c8ee9feb5c4b9a753b7b476b7fd6422ef1f3dd674761fa99d6ac27c8"+
                       "a9a197b272822f6cd57a55aa4f50ae317b13545f";
             x = new BigInteger(bstring,16);

             pb = Polynomial.strToPoly(x.toString(2));
             curveF2m = new ECOverF2m(pa,pb,Polynomial.getIrreducibleStandard(409));


                     curveF2m.cofactor = new BigInteger("2");

                         o = new BigInteger("66105596879024859895191530803277103982840468296428121"+
                                "92846487983041577748273748052081437237621791109659798"+
                                "67288366567526771"
                                );

             o = o.multiply(curveF2m.cofactor);

             curveF2m.order = o;


             x = new BigInteger("15d4860d088ddb3496b0c6064756260441cde4af1771d4db01ffe5b34e59703"+
                                "dc255a868a1180515603aeab60794e54bb7996a7",16);
                         y = new BigInteger("061b1cfab6be5f32bbfa78324ed106a7636b9c5a7bd198d0158aa4f5488d08f"+
                                "38514f1fdf4b4f40d2181b3681c364ba0273c706",16);

                         px = Polynomial.strToPoly(x.toString(2));
                     py = Polynomial.strToPoly(y.toString(2));

                     f2mPoint = new ECPointOverF2m(px,py);
                     curveF2m.G = f2mPoint;

                     curveF2m.degree = 409;

                     return curveF2m;

                 }
                 else if(type.compareTo("B-571") == 0)
         {
                         bstring = "2f40e7e2221f295de297117"+
                       "b7f3d62f5c6a97ffcb8ceff1cd6ba8ce4a9a18ad"+
                       "84ffabbd8efa59332be7ad6756a66e294afd185a"+
                       "78ff12aa520e4de739baca0c7ffeff7f2955727a";

             x = new BigInteger(bstring,16);

             pb = Polynomial.strToPoly(x.toString(2));
             curveF2m = new ECOverF2m(pa,pb,Polynomial.getIrreducibleStandard(571));


                     curveF2m.cofactor = new BigInteger("2");

                         o = new BigInteger("38645375230172583446953518909319873442989273297064349"+
                                "98657235251451519142289560424536143999389415773083133"+
                                "88112192694448624687246281681307023452828830333241139"+
                                "3191105285703"
                                );

             o = o.multiply(curveF2m.cofactor);

             curveF2m.order = o;

             x = new BigInteger("303001d34b856296c16c0d40d3cd7750a93d1d2955fa80aa5f40fc8db7b2abd"+
                                "bde53950f4c0d293cdd711a35b67fb1499ae6003"+
                                "8614f1394abfa3b4c850d927e1e7769c8eec2d19",16);
                         y = new BigInteger("37bf27342da639b6dccfffeb73d69d78c6c27a6009cbbca1980f8533921e8a6"+
                                "84423e43bab08a576291af8f461bb2a8b3531d2f"+
                                "0485c19b16e2f1516e23dd3c1a4827af1b8ac15b",16);

                         px = Polynomial.strToPoly(x.toString(2));
                     py = Polynomial.strToPoly(y.toString(2));

                     f2mPoint = new ECPointOverF2m(px,py);
                     curveF2m.G = f2mPoint;

                     curveF2m.degree = 571;

                     return curveF2m;



                 }
                 else if(type.compareTo("K-163") == 0)
         {

             s0 = new BigInteger("2579386439110731650419537");
             s1 = new BigInteger("-755360064476226375461594");
             v = new BigInteger("-4845466632539410776804317");

             pb = Polynomial.strToPoly("1");
             curveF2m = new ECOverF2m(pa,pb,Polynomial.getIrreducibleStandard(163));


                     curveF2m.cofactor = new BigInteger("2");

                         o = new BigInteger("5846006549323611672814741753598448348329118574063");

             o = o.multiply(curveF2m.cofactor);

             curveF2m.order = o;


             x = new BigInteger("2fe13c0537bbc11acaa07d793de4e6d5e5c94eee8",16);
                         y = new BigInteger("289070fb05d38ff58321f2e800536d538ccdaa3d9",16);

                         px = Polynomial.strToPoly(x.toString(2));
                     py = Polynomial.strToPoly(y.toString(2));

                     f2mPoint = new ECPointOverF2m(px,py);
                     curveF2m.G = f2mPoint;

                     curveF2m.s0 = s0;
                     curveF2m.s1 = s1;

                     curveF2m.type = "K";

                     curveF2m.degree = 163;

                     return curveF2m;

                 }
                 else if(type.compareTo("K-233") == 0)
                 {
             s0 = new BigInteger("-27859711741434429761757834964435883");
             s1 = new BigInteger("-44192136247082304936052160908934886");
             v = new BigInteger("-137381546011108235394987299651366779");

             pb = Polynomial.strToPoly("1");
             curveF2m = new ECOverF2m(pa0,pb,Polynomial.getIrreducibleStandard(233));


                     curveF2m.cofactor = new BigInteger("4");

                         o = new BigInteger("34508731733952818937173779311385127605709409888622521"+
                                "26328087024741343");

             o = o.multiply(curveF2m.cofactor);

             curveF2m.order = o;


             x = new BigInteger("17232ba853a7e731af129f22ff4149563a419c26bf50a4c9d6eefad6126",16);
                         y = new BigInteger("1db537dece819b7f70f555a67c427a8cd9bf18aeb9b56e0c11056fae6a3",16);

                         px = Polynomial.strToPoly(x.toString(2));
                     py = Polynomial.strToPoly(y.toString(2));

                     f2mPoint = new ECPointOverF2m(px,py);
                     curveF2m.G = f2mPoint;

                     curveF2m.s0 = s0;
                     curveF2m.s1 = s1;

                     curveF2m.type = "K";

             curveF2m.degree = 233;
                     return curveF2m;

                 }
                 else if(type.compareTo("K-283") == 0)
                 {
             s0 = new BigInteger("-665981532109049041108795536001591469280025");
             s1 = new BigInteger("1155860054909136775192281072591609913945968");
             v = new BigInteger("7777244870872830999287791970962823977569917");

             pb = Polynomial.strToPoly("1");
             curveF2m = new ECOverF2m(pa0,pb,Polynomial.getIrreducibleStandard(283));


                     curveF2m.cofactor = new BigInteger("4");

                         o = new BigInteger("38853377844514581418389238136470378132848117337930613"+
                                "24295874997529815829704422603873");

             o = o.multiply(curveF2m.cofactor);

             curveF2m.order = o;


             x = new BigInteger("503213f78ca44883f1a3b8162f188e553cd265f23c1567a16876913b0c2ac2458492836",16);
                         y = new BigInteger("1ccda380f1c9e318d90f95d07e5426fe87e45c0e8184698e45962364e34116177dd2259",16);

                         px = Polynomial.strToPoly(x.toString(2));
                     py = Polynomial.strToPoly(y.toString(2));

                     f2mPoint = new ECPointOverF2m(px,py);
                     curveF2m.G = f2mPoint;

                     curveF2m.s0 = s0;
                     curveF2m.s1 = s1;

             curveF2m.type = "K";
             curveF2m.degree = 283;
                     return curveF2m;


                 }
         else if(type.compareTo("K-409") == 0)
                 {
             s0 = new BigInteger("-1830751045600238213781031719875646137859054248755686"+
                                 "9338419259");
             s1 = new BigInteger("-8893048526138304097196653241844212679626566100996606"+
                                 "444816790");

             v = new BigInteger("1045728873731562592744768538704832073763879695768757"+
                                "5791173829");

             pb = Polynomial.strToPoly("1");
             curveF2m = new ECOverF2m(pa0,pb,Polynomial.getIrreducibleStandard(409));


                     curveF2m.cofactor = new BigInteger("4");

                         o = new BigInteger("33052798439512429947595765401638551991420234148214060"+
                                "96423243950228807112892491910506732584577774580140963"+
                                "66590617731358671");

             o = o.multiply(curveF2m.cofactor);

             curveF2m.order = o;


             x = new BigInteger("060f05f658f49c1ad3ab189"+
                                "0f7184210efd0987e307c84c27accfb8f9f67cc2"+
                                "c460189eb5aaaa62ee222eb1b35540cfe9023746",16);

                         y = new BigInteger("1e369050b7c4e42acba1dac"+
                                "bf04299c3460782f918ea427e6325165e9ea10e3"+
                                "da5f6c42e9c55215aa9ca27a5863ec48d8e0286b",16);

                         px = Polynomial.strToPoly(x.toString(2));
                     py = Polynomial.strToPoly(y.toString(2));

                     f2mPoint = new ECPointOverF2m(px,py);
                     curveF2m.G = f2mPoint;

                     curveF2m.s0 = s0;
                     curveF2m.s1 = s1;

             curveF2m.type = "K";


             curveF2m.degree = 409;
                     return curveF2m;


                 }
         else if(type.compareTo("K-571") == 0)
         {
             s0 = new BigInteger("-373731944687646369242938589247611556714729396459613"+
                                 "1024123406420235241916729983261305");

             s1 = new BigInteger("-3191857706446416099583814595948959674131968912148564"+
                                 "65861056511758982848515832612248752");

             v = new BigInteger("-148380926981691413899619140297051490364542574180493"+
                                "936232912339534208516828973111459843");

             pb = Polynomial.strToPoly("1");
             curveF2m = new ECOverF2m(pa0,pb,Polynomial.getIrreducibleStandard(571));


                     curveF2m.cofactor = new BigInteger("4");

                         o = new BigInteger("19322687615086291723476759454659936721494636648532174"+
                                "99328617625725759571144780212268133978522706711834706"+
                                "71280082535146127367497406661731192968242161709250355"+
                                "5733685276673");

             o = o.multiply(curveF2m.cofactor);

             curveF2m.order = o;


             x = new BigInteger("26eb7a859923fbc82189631"+
                                "f8103fe4ac9ca2970012d5d46024804801841ca4"+
                                "4370958493b205e647da304db4ceb08cbbd1ba39"+
                                "494776fb988b47174dca88c7e2945283a01c8972",16);

                         y = new BigInteger("349dc807f4fbf374f4aeade"+
                                "3bca95314dd58cec9f307a54ffc61efc006d8a2c"+
                                "9d4979c0ac44aea74fbebbb9f772aedcb620b01a"+
                                "7ba7af1b320430c8591984f601cd4c143ef1c7a3",16);

                         px = Polynomial.strToPoly(x.toString(2));
                     py = Polynomial.strToPoly(y.toString(2));

                     f2mPoint = new ECPointOverF2m(px,py);
                     curveF2m.G = f2mPoint;

                     curveF2m.s0 = s0;
                     curveF2m.s1 = s1;

             curveF2m.type = "K";
             curveF2m.degree = 571;
                     return curveF2m;

                 }

                 curveFp.cofactor = BigInteger.ONE;
                 return curveFp;

        }

}

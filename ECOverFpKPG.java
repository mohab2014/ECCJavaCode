package school.cryptocode.ecc;
import java.math.BigInteger;
import java.security.SecureRandom;

class ECKeyPair
{
	BigInteger d;
	ECPointOverFp Q;

	public ECKeyPair()
	{
		d = null;
		Q = null;
	}

	public ECKeyPair(ECPointOverFp Q,BigInteger d )
	{
		this.d = d;
		this.Q = Q;
	}

	public BigInteger getPrivateKey()
	{
		return d;
	}

	public ECPointOverFp getPublicKey()
	{
		return Q;
	}


}//End of ECKeyPair


public class ECOverFpKPG
{

  /**
   * KeyPair(Q,d) generation
   * @param fpCurve ECOverFp
   * @return ECKeyPair
   */
  public static ECKeyPair generateKeyPair(ECOverFp fpCurve)
  {

		BigInteger n = fpCurve.order.divide(fpCurve.cofactor);

		//private key
		BigInteger d;

		int nbits = n.toString(2).length();

		SecureRandom random = new SecureRandom();

		do
		{

		 d = new BigInteger(nbits,random);

	    }while(d.compareTo(n) != -1 || d.compareTo(BigInteger.ZERO) == 0);

	    //public key
	    ECPointOverFp Q = fpCurve.getG().toJacobianPoint().mult(d,fpCurve);//mult1(d,fpCurve);


	    ECKeyPair ecKeyPair = new ECKeyPair(Q,d);

	    return ecKeyPair;

  }

  /**
   *
   * @param publicKey ECPointOverFp
   * @param fpCurve ECOverFp
   * @return boolean
   */
  public static boolean isValidPublicKey(ECPointOverFp publicKey,ECOverFp fpCurve)
  {

            if((publicKey.x.compareTo(fpCurve.getP()) == 0 && publicKey.y.compareTo(fpCurve.getP()) == 0))
               return false;

            if(publicKey.x.compareTo(BigInteger.ZERO) < 0 || publicKey.x.compareTo(fpCurve.getP().subtract(BigInteger.ONE)) > 0 )
               return false;

            if(publicKey.y.compareTo(BigInteger.ZERO) < 0 || publicKey.y.compareTo(fpCurve.getP().subtract(BigInteger.ONE)) > 0 )
	       return false;


            BigInteger rightSide, leftSide;

            rightSide = publicKey.y.pow(2).mod(fpCurve.getP());
            leftSide  = publicKey.x.pow(3).add(fpCurve.getA().multiply(publicKey.x)).add(fpCurve.getB()).mod(fpCurve.getP());

            if(rightSide.compareTo(leftSide) != 0)
              return false;

            return true;
  }


  /**
   *
   * @param Q ECPointOverFp
   * @param d BigInteger
   * @param fpCurve ECOverFp
   * @return boolean
   */
  public static boolean isValidKeyPair(ECPointOverFp Q,BigInteger d,ECOverFp fpCurve)
  {
           ECPointOverFp publickey =  fpCurve.getG().toJacobianPoint().mult(d,fpCurve);

           if(publickey.toString().equals(Q.toString()))
              return true;

           return false;
  }


}//End of ECOverFpKPG

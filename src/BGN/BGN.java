package BGN;

import java.math.BigInteger;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a1.TypeA1CurveGenerator;
import BGN.PrivateKey;
import BGN.Publickey;

public class BGN {
	private static final int T = 1000; // The max range of message m
	private Publickey pubkey;
	private PrivateKey prikey;
	
	public void keyGeneration(int k) {
		TypeA1CurveGenerator pg = new
		TypeA1CurveGenerator(2, k);
		PairingParameters pp = pg.generate();
		Pairing pairing = PairingFactory.getPairing(pp);
		BigInteger n = pp.getBigInteger("n");
		BigInteger q = pp.getBigInteger("n0");
		BigInteger p = pp.getBigInteger("n1");
		Field<Element> Field_G = pairing.getG1();
		Field<Element> Field_GT = pairing.getGT();
		Element g = Field_G.newRandomElement().getImmutable();
		Element h = g.pow(q).getImmutable();
		pubkey = new Publickey(n, Field_G, Field_GT,pairing, g, h);
		prikey = new PrivateKey(p);
	}
	public Publickey getPubkey() {
		return pubkey;
	}
	public PrivateKey getPrikey() {
		return prikey;
	}
	public static Element encrypt(int m, Publickey pubkey)throws Exception {
		if (m > T) { throw new Exception(
		"BGN.encrypt(int m, PublicKey pubkey): "+ "plaintext m is not in [0,1,2,...,"+ T + "]");
		}
		Pairing pairing = pubkey.getPairing();
		Element g = pubkey.getG();
		Element h = pubkey.getH();
		BigInteger r = pairing.getZr().newRandomElement().toBigInteger();
		return g.pow(BigInteger.valueOf(m)).mul(h.pow(r)).getImmutable();
	}
	public static int decrypt(Element c, Publickey pubkey,PrivateKey prikey) throws Exception {
		BigInteger p = prikey.getP();
		Element g = pubkey.getG();
		Element cp = c.pow(p).getImmutable();
		Element gp = g.pow(p).getImmutable();
		for (int i = 0; i <= T; i++) {
			if (gp.pow(BigInteger.valueOf(i)).isEqual(cp)) return i;
		}
		throw new Exception("BGN.decrypt(Element c, PublicKey pubkey,PrivateKey prikey): "+ "plaintext m is not in [0,1,2,...,"+ T + "]");
	}
	public static int decrypt_mul2(Element c, Publickey pubkey,PrivateKey prikey) throws Exception {
		BigInteger p = prikey.getP();
		Element g = pubkey.getG();
		Element cp = c.pow(p).getImmutable();
		Element egg = pubkey.getPairing().pairing(g, g).pow(p).getImmutable();
		for (int i = 0; i <= T; i++) {
				if (egg.pow(BigInteger.valueOf(i)).isEqual(cp)) {
						return i;
				}
		}
		throw new Exception("BGN.decrypt(Element c, PublicKey pubkey,PrivateKey prikey): "+ "plaintext m is not in [0,1,2,...,"+ T + "]");
	}
	
	public static Element add(Element c1, Element c2) {
		return c1.mul(c2).getImmutable();
	}
	
	public static Element mul1(Element c1, int m2) {
		return c1.pow(BigInteger.valueOf(m2)).getImmutable();
	}
	public static Element mul2(Element c1, Element c2,Publickey pubkey) {
		Pairing pairing = pubkey.getPairing();
		return pairing.pairing(c1, c2).getImmutable();
	}
	public static Element selfBlind(Element c1, BigInteger r2,Publickey pubkey) {
		Element h = pubkey.getH();
		return c1.mul(h.pow(r2)).getImmutable();
	}
	
}

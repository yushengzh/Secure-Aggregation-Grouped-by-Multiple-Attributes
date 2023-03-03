package BGN;

import java.math.BigInteger;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a1.TypeA1CurveGenerator;
import it.unisa.dia.gas.plaf.jpbc.pairing.a1.TypeA1Pairing;
import it.unisa.dia.gas.plaf.jpbc.util.math.BigIntegerUtils;
import java.security.SecureRandom;
import BGN.PrivateKey;
import BGN.Publickey;

public class BGN {
	private static final int T = Integer.MAX_VALUE; // The max range of message m
	private Publickey pubkey;
	private PrivateKey prikey;
	public BGN(int num){
		keyGeneration(num);
	}
	
	public void keyGeneration(int k) {
		TypeA1CurveGenerator pg = new TypeA1CurveGenerator(2, k);
		PairingParameters pp = pg.generate();
		Pairing pairing = PairingFactory.getPairing(pp);
		BigInteger n = pp.getBigInteger("n");
		BigInteger q = pp.getBigInteger("n0");
		BigInteger p = pp.getBigInteger("n1");
		Field<Element> Field_G = pairing.getG1();
		Field<Element> Field_GT = pairing.getGT();
		Element g = Field_G.newRandomElement().getImmutable();
		Element h = g.pow(q).getImmutable();
		pubkey = new Publickey(n, Field_G, Field_GT, pairing, g, h);
		prikey = new PrivateKey(p);
	}
	public Publickey getPubkey() {
		return pubkey;
	}
	public PrivateKey getPrikey() {
		return prikey;
	}
	public static Element encrypt(BigInteger m, Publickey pubkey)throws Exception {
		/*if (m.compareTo(new BigInteger(String.valueOf(T)))==1) { throw new Exception(
		"BGN.encrypt(int m, PublicKey pubkey): "+ "plaintext m is not in [0,1,2,...,"+ T + "]");
		}*/
		Pairing pairing = pubkey.getPairing();
		Element g = pubkey.getG();
		Element h = pubkey.getH();
		BigInteger r = pairing.getZr().newRandomElement().toBigInteger();
		return g.pow(m).mul(h.pow(r)).getImmutable();
	}
	public static BigInteger decrypt(Element c, Publickey pubkey,PrivateKey prikey) throws Exception {
		
		BigInteger p = prikey.getP();
		Element g = pubkey.getG();
		Element cp = c.pow(p).getImmutable();
		Element gp = g.pow(p).getImmutable();
		for (int i = 0; i <= T; i++) {
			if (gp.pow(BigInteger.valueOf(i)).isEqual(cp)) return BigInteger.valueOf(i);
		}
		throw new Exception("BGN.decrypt(Element c, PublicKey pubkey,PrivateKey prikey): "+ "plaintext m is not in [0,1,2,...,"+ T + "]");
		/*
		Field f = pubkey.getField_G();
		Element T = f.newElement();
		Element K = f.newElement();
		Element aux = f.newElement();
		T = T.set(pubkey.getG());
		T = T.mul(prikey.getP());
		K = K.set(c);
		aux = aux.set(T);
		BigInteger m = new BigInteger("1");
		while(!aux.isEqual(K)) {
			aux = aux.add(T);
			m = m.add(BigInteger.valueOf(1));
		}
		return m;*/
	}
	
	
	public static BigInteger decrypt_mul2(Element c, Publickey pubkey,PrivateKey prikey) throws Exception {
		BigInteger p = prikey.getP();
		Element g = pubkey.getG();
		Element cp = c.pow(p).getImmutable();
		Element egg = pubkey.getPairing().pairing(g, g).pow(p).getImmutable();
		for (int i = 0; i <= T; i++) {
			if (egg.pow(BigInteger.valueOf(i)).isEqual(cp)) {
					return BigInteger.valueOf(i);
			}
		}
		/*
		for (BigInteger i = new BigInteger("0"); i.compareTo(BigInteger.valueOf(T))!=1; i.add(new BigInteger("1"))) {
				if (egg.pow(i).isEqual(cp)) {
						return i;
				}
		}*/
		throw new Exception("BGN.decrypt(Element c, PublicKey pubkey,PrivateKey prikey): "+ "plaintext m is not in [0,1,2,...,"+ T + "]");
	}
	
	public static Element add(Element c1, Element c2) {
		return c1.add(c2).getImmutable();
	}
	
	public static Element mul1(Element c1, BigInteger m2) {
		return c1.pow(m2).getImmutable();
	}
	public static Element mul2(Element c1, Element c2,Publickey pubkey) {
		Pairing pairing = pubkey.getPairing();
		return pairing.pairing(c1, c2).getImmutable();
	}
	public static Element selfBlind(Element c1, BigInteger r2,Publickey pubkey) {
		Element h = pubkey.getH();
		return c1.mul(h.pow(r2)).getImmutable();
	}
	
	//Homomorphic addition/multiplication
	/*
	public static Element homoAdd(int m1, int m2, Publickey pubkey, PrivateKey prikey) {
		try {
			Element c1 = encrypt(m1, pubkey);
			Element c2 = encrypt(m2, pubkey);
			Element c1addc2 = BGN.add(c1, c2);
			return c1addc2;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			//throw new Exception("Homo add failed");
		}
		
	}*/
}




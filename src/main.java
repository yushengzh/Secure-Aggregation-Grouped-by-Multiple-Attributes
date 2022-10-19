import java.math.BigInteger;
import BGN.BGN;
import BGN.PrivateKey;
import BGN.Publickey;
import it.unisa.dia.gas.jpbc.Element;

public class main {

	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		System.out.println("====test BGN====");
		System.out.println("================");
		BGN bgn = new BGN();
		bgn.keyGeneration(512);
		Publickey pubkey = bgn.getPubkey();
		PrivateKey prikey = bgn.getPrikey();
		int m = 5;
		Element c = null;
		int decrypted_m = 0;
		try {
			c = BGN.encrypt(m, pubkey);
			decrypted_m = BGN.decrypt(c, pubkey, prikey); 
		}catch (Exception e) {
			e.printStackTrace();
		}
		if(decrypted_m == m)System.out.println("Encryption and Decryption "+ "test successfully.");
		
		//addition
		int m1 = 201, m2 = 202;
		try {
			Element c1 = BGN.encrypt(m1, pubkey);
			Element c2 = BGN.encrypt(m2, pubkey);
			Element c1mulc2 = BGN.add(c1, c2);
			System.out.println(c1.hashCode());
			System.out.println(c2.hashCode());
			System.out.println(c1mulc2.hashCode());
			int decrypted_c1mulc2 = BGN.decrypt(c1mulc2, pubkey, prikey);
			System.out.println(decrypted_c1mulc2);
			if (decrypted_c1mulc2 == (m1 + m2)) 
				System.out.println("Homomorphic addition "+ "tests successfully.");		
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		//multiplication
		m1 = 15;
		m2 = 16;
		try {
			Element c1 = BGN.encrypt(m1, pubkey);
			Element c1expm2 = BGN.mul1(c1, m2);
			int decrypted_c1expm2 = BGN.decrypt(c1expm2,pubkey, prikey);
			if (decrypted_c1expm2 == (m1 * m2)) 
				System.out.println("Homomorphicmultiplication-1 "+ "tests successfully.");
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		// multiplication-2
		m1 = 15;
		m2 = 16;
		try {
			Element c1 = BGN.encrypt(m1, pubkey);
			Element c2 = BGN.encrypt(m2, pubkey);
			Element c1pairingc2 = pubkey.getPairing().pairing(c1, c2).getImmutable();
			int decrypted_c1pairingc2 = BGN.decrypt_mul2(c1pairingc2, pubkey, prikey);
			if (decrypted_c1pairingc2 == (m1 * m2)) 
				System.out.println("Homomorphicmultiplication-2 "+ "tests successfully.");
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		// self-Blinding
		m1 = 15;
		try {
			BigInteger r2 = pubkey.getPairing().getZr().newRandomElement().toBigInteger();
			Element c1 = BGN.encrypt(m1, pubkey);
			Element c1_selfblind = BGN.selfBlind(c1,r2, pubkey);
			int decrypted_c1_selfblind = BGN.decrypt(c1_selfblind, pubkey, prikey);
			if (decrypted_c1_selfblind == m1) 
				System.out.println("Homomorphic self-blinding "+ "tests successfully.");
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}

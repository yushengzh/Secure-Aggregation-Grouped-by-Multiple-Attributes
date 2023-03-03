import java.math.BigInteger;
import BGN.BGN;
import BGN.PrivateKey;
import BGN.Publickey;
import it.unisa.dia.gas.jpbc.Element;
import java.util.Scanner;


public class main {
	
	static int bucketSize = 2;
	
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		System.out.println("====test BGN====");
		System.out.println("================");
		Scanner in = new Scanner(System.in);//输入
		BGN bgn = new BGN(512);
		Publickey pubkey = bgn.getPubkey();
		PrivateKey prikey = bgn.getPrikey();
		System.out.println("====test Encryption & Decryption====");
		System.out.println("输入明文：");
		BigInteger m = in.nextBigInteger();
		Element c = null;
		BigInteger decrypted_m = new BigInteger("0");
		
		try {
			c = BGN.encrypt(m, pubkey);
			decrypted_m = BGN.decrypt(c, pubkey, prikey); 
		}catch (Exception e) {
			e.printStackTrace();
		}
		BigInteger bm = m;
		if(decrypted_m.compareTo(bm)==0)System.out.println("Encryption and Decryption "+ "test successfully.");
		
		
		//addition
		int m1 = 2019, m2 = 2022;
		System.out.println("====test Homomorphic addition====");
		System.out.println("输入明文：");
		m1 = in.nextInt();
		
		m2 = in.nextInt();
		try {
			Element c1 = BGN.encrypt(new BigInteger(String.valueOf(m1)), pubkey);
			Element c2 = BGN.encrypt(new BigInteger(String.valueOf(m2)), pubkey);
			Element c1addc2 = BGN.add(c1, c2);
			System.out.println("密文摘要(输入1、输入2、输出)");
			System.out.println(c1.hashCode());
			System.out.println(c2.hashCode());
			System.out.println(c1addc2.hashCode());
			BigInteger decrypted_c1addc2 = BGN.decrypt(c1addc2, pubkey, prikey);
			System.out.println("解密结果："+decrypted_c1addc2);
			if (decrypted_c1addc2.compareTo(BigInteger.valueOf(m1 + m2))==0) 
				System.out.println("Homomorphic addition "+ "tests successfully.");		
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
		
		// multiplication-2 ciphertext*ciphertext
		System.out.println("====test Homomorphic multiplication====");
		System.out.println("输入明文：");
		m1 = in.nextInt();
		m2 = in.nextInt();
		try {
			Element c1 = BGN.encrypt(new BigInteger(String.valueOf(m1)), pubkey);
			Element c2 = BGN.encrypt(new BigInteger(String.valueOf(m2)), pubkey);
			Element c1pairingc2 = pubkey.getPairing().pairing(c1, c2).getImmutable();
			System.out.println("密文摘要(输入1、输入2、输出)");
			System.out.println(c1.hashCode());
			System.out.println(c2.hashCode());
			System.out.println(c1pairingc2.hashCode());
			BigInteger decrypted_c1pairingc2 = BGN.decrypt_mul2(c1pairingc2, pubkey, prikey);
			System.out.println("解密结果："+decrypted_c1pairingc2);
			if (decrypted_c1pairingc2.compareTo(BigInteger.valueOf(m1 * m2))==0) 
				System.out.println("Homomorphic multiplication "+ "tests successfully.");
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}

}

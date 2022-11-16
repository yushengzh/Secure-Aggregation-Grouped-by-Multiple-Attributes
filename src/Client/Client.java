package Client;

import BGN.*;
import it.unisa.dia.gas.jpbc.Element;
import java.math.BigInteger;
import test.Entry;
import test.PostEntry;
import util.Bucketize;
public class Client {
	
	public static int str2int(String s) {
		return Integer.valueOf(s).intValue();
	}
	public static String int2str(int i) {
		return String.valueOf(i);
	}
	
	public static void main(String[] args) throws Exception {
		Entry e1 = new Entry(1000,"male","Henry","Sales");
		Entry e2 = new Entry(5000,"fmale","Jessica","Sales");
		Entry e3 = new Entry(1500,"fmale","Alice","Finance");
		Entry e4 = new Entry(3000,"male","Bob","Sales");
		Entry e5 = new Entry(2000,"male","Paul","Facility");
		Entry[] T = new Entry[5];
		T[0] = e1;
		T[1] = e2;
		T[2] = e3;
		T[3] = e4;
		T[4] = e5;
		
		BGN bgn = new BGN();
		bgn.keyGeneration(512);
		Publickey pubkey = bgn.getPubkey();
		PrivateKey prikey = bgn.getPrikey();
		Element c = null;
		
		PostEntry []postT = new PostEntry[T.length];
		Bucketize bt = new Bucketize(2);
		postT = bt.buildBucket(T);
		System.out.println(" | " + "id" + " | " + "salary" + " | " + "gender" + " | " + "name" + " | " + "dept" + " | ");
		for(int i=0;i<postT.length;i++) {
			postT[i].print();
			c = BGN.encrypt(postT[i].getSalary(), pubkey);
			postT[i].setSalary(c.hashCode());
			c = BGN.encrypt(str2int(postT[i].getGender()), pubkey);
			postT[i].setGender(int2str(c.hashCode()));
			c = BGN.encrypt(str2int(postT[i].getDept()), pubkey);
			postT[i].setDept(int2str(c.hashCode()));
			c = BGN.encrypt(str2int(postT[i].getGdd()), pubkey);
			postT[i].setGdd(c.hashCode());
		}
		System.out.println("after computing encrypted tabled...");
		for(int i=0;i<postT.length;i++) {
			postT[i].print();
		}
		
		
	}
}

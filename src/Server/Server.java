package Server;

import BGN.*;
import it.unisa.dia.gas.jpbc.Element;
import java.math.BigInteger;
import test.CryptEntry;
import test.Entry;
import test.PostEntry;
import util.Bucket;
import util.Bucketize;
import java.net.Socket;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;

public class Server {
	
	private static int PORT = 8800;
	static final String QUIT = "quit";
	static ServerSocket serverSocket = null;
	static Socket socket = null;
	static BufferedReader bufferedReader = null;
	static BufferedWriter bufferedWriter = null;
	static ArrayList<BigInteger> coef = new ArrayList<BigInteger>();
	
	static int num = 10;
	static BGN bgn = new BGN(512);
	static Publickey pubkey = bgn.getPubkey();
	static PrivateKey prikey = bgn.getPrikey();
	
	public static int str2int(String s) {
		return Integer.valueOf(s).intValue();
	}
	
	public static String int2str(int i) {
		return String.valueOf(i);
	}
	
	// 1, G1, G2, G1G2
	//P = a0 + a1，G1 + a2，G2 + a3，G1G2
	public static BigInteger encrptShift(ArrayList<BigInteger>coef, PostEntry pt) {
		String g1 = pt.getGender();
		String g2 = pt.getDept();
		String g1g2 = pt.getGdd();
		return coef.get(0).add(
				coef.get(1).multiply(new BigInteger(g1))).add(
						coef.get(2).multiply(new BigInteger(g2))).add(
								coef.get(3).multiply(new BigInteger(g1g2)));
	}
	
	
	public static Element []encrptShift(ArrayList<BigInteger>coef,CryptEntry []cpt) throws Exception {
		Element []a = new Element[coef.size()];
		Element []resShift = new Element[cpt.length];
		a[0] = BGN.encrypt(coef.get(0), pubkey);
		
		for(int i=0;i<resShift.length;i++) {
			Element t1 = BGN.mul1(cpt[i].getGender(), coef.get(1));
			Element t2 = BGN.add(t1, BGN.mul1(cpt[i].getDept(), coef.get(2)));
			Element t3 = BGN.add(t2, BGN.mul1(cpt[i].getGdd(), coef.get(3)));
			
			resShift[i] = BGN.add(a[0], t3);
		}
		System.out.println("\n Computing encrypted shift successfully!\n");
		return resShift;
	}
	
	public static Element []cryptValueShift(CryptEntry []cpt, BigInteger []shift, ArrayList<Integer>btComRes) throws Exception{
		int n = btComRes.size();
		Element []res = new Element[n];
		for(int i=0;i<n;i++) {
			Element e1 = cpt[btComRes.get(i)-1].getSalary();
			Element e2 = BGN.encrypt(shift[btComRes.get(i)-1], pubkey);
			res[i] = pubkey.getPairing().pairing(e1, e2).getImmutable();
		}
		return res;
	}
	
	public static Element valueShifted(Element []rs, CryptEntry []cpt, ArrayList<Integer> btComRes) throws Exception{
		int n = btComRes.size();
		Element encAggRes = pubkey.getPairing().pairing(cpt[btComRes.get(0) - 1].getSalary(), rs[btComRes.get(0) - 1]).getImmutable();
		
		for(int i=1;i<n;i++) {
			Element v = cpt[btComRes.get(i) - 1].getSalary();
			Element x = rs[btComRes.get(i) - 1];
			encAggRes = BGN.add(encAggRes, pubkey.getPairing().pairing(v, x).getImmutable());
		}
		return encAggRes;
	}
	
	
	public static void main(String []args) throws Exception {
		
		Entry[] T = new Entry[num];
		T[0] = new Entry(1000,"male","Henry","Sales");
		T[1] = new Entry(5000,"fmale","Jessica","Sales");
		T[2] = new Entry(1500,"fmale","Alice","Finance");
		T[3] = new Entry(3000,"male","Bob","Sales");
		
		T[4] = new Entry(1500, "male", "Zhang", "Sales");
		
		T[5] = new Entry(5000, "male", "Zhao", "Facility");
		T[6] = new Entry(8000, "ffmale", "Zhou", "Software");
		T[7] = new Entry(3000, "fmale", "Zheng", "Finance");
		T[8] = new Entry(1500, "male", "Zhang", "Sales");
		T[9] = new Entry(2000, "fmale", "Qian", "Outsource");
		
		/*
		T[0] = new Entry(1000,"male","Henry","Sales");
		T[1] = new Entry(3000,"male","Bob","Sales");
		T[2] = new Entry(1500, "male", "Zhang", "Sales");
		T[3] = new Entry(1500, "male", "Zhang", "Sales");
		
		*/
		System.out.println("Table is...\n");
		for(int i=0;i<num;i++)T[i].print();
		
		PostEntry []postT = new PostEntry[T.length];
		Bucketize bt = new Bucketize(2);
		int bucketSize = bt.getBucketSize();
		System.out.printf("\n bucket size is %d. \n",bucketSize);
		System.out.println("Building buckets...\n");
		
		Bucket []resBuckets = bt.buildBuckets(T);
		System.out.println(" | " + "bucket" + " | " + "rows" + " | ");
		for(int i=0;i<resBuckets.length;i++) {
			resBuckets[i].print();
		}
		
		System.out.println("\n encoding table...\n");
		postT = bt.encodeTable(T);
		
		CryptEntry []cryptPT = new CryptEntry[T.length];
		
		System.out.println(" | " + "id" + " | " + "salary" + " | " + "gender" + " | " + "name" + " | " + "dept" + " | ");
		Element c1,c2,c3,c4;
		BigInteger temp;
		for(int i=0;i<postT.length;i++) {
			postT[i].print();
			temp = new BigInteger(String.valueOf(postT[i].getSalary()));
			c1 = BGN.encrypt(BigInteger.valueOf(postT[i].getSalary()), pubkey);
			c2 = BGN.encrypt(new BigInteger(postT[i].getGender()), pubkey);
			c3 = BGN.encrypt(new BigInteger(postT[i].getDept()), pubkey);
			c4 = BGN.encrypt(new BigInteger(postT[i].getGdd()), pubkey);
			//c1 = BGN.encrypt(temp, pubkey);
			//copyPT[i].setSalary(c.hashCode());
			//temp = new BigInteger(String.valueOf((int)Math.pow(Math.pow(2.0, 32.0),(double)str2int(postT[i].getGender())%bucketSize)));
			//c2 = BGN.encrypt(temp, pubkey);
			//copyPT[i].setGender(int2str(c.hashCode()));
			//temp = new BigInteger(String.valueOf((int)Math.pow(Math.pow(2.0, 32.0),(double)str2int(postT[i].getDept())%bucketSize)));
			//c3 = BGN.encrypt(temp, pubkey);
			//copyPT[i].setDept(int2str(c.hashCode()));
			//temp = new BigInteger(String.valueOf((int)Math.pow(Math.pow(2.0, 32.0),(double)str2int(postT[i].getGdd())%bucketSize)));
			//c4 = BGN.encrypt(temp, pubkey);
			//copyPT[i].setGdd(c.hashCode());
			cryptPT[i] = new CryptEntry(c1, c2, c3, c4);
		}
		System.out.println("\n after computing encrypted table...\n");
		System.out.println(" | " + "id" + " | " + "salary" + " | " + "gender"  + " | " + "dept" + " | "+ "gender，dept" + " | ");
		for(int i=0;i<postT.length;i++) {
			cryptPT[i].print();
		}
		
		//socket
		try {
			serverSocket = new ServerSocket(PORT);
			System.out.println("\n=================\n Socket is built...\n");
			System.out.println("Listening the port...");
			
			while(true) {
				socket = serverSocket.accept();
				System.out.println("Client["+socket.getInetAddress()+":"+socket.getPort()+"] connecting");
			
				InputStream is = socket.getInputStream();
				OutputStream os = socket.getOutputStream();
				bufferedReader = new BufferedReader(new InputStreamReader(is));
				bufferedWriter = new BufferedWriter(new OutputStreamWriter(os));
	
				String msg = null;
				Thread t = new Thread();
				while((msg = bufferedReader.readLine()) != null) {
					if(QUIT.equalsIgnoreCase(msg)) {
						System.out.println("Client["+socket.getInetAddress()+":"+socket.getPort()+"] disconnected");
						break;
					}
					
					System.out.println("Client["+socket.getInetAddress()+":"+socket.getPort()+"]:"+msg);
					bufferedWriter.write(msg +"\n");
					bufferedWriter.flush();
					t.sleep(1000);
					coef.add(new BigInteger(msg));
				}
				System.out.println("\n coefficients are... \n");
				t.sleep(1000);
				for(int i=0;i<coef.size();i++) {
					System.out.println("a"+i+" = "+coef.get(i));
				}
				
				BigInteger []shift = new BigInteger[num];
				System.out.println("\n shifts are... \n");
				t.sleep(1000);
				for(int i=0;i<num;i++) {
					shift[i] = 	encrptShift(coef, postT[i]);
					System.out.printf("shift[%d] is: "+shift[i] +"; while the codes is:" + postT[i].getGender()+postT[i].getDept()+postT[i].getGdd() , i+1);
					System.out.println();
				}
				Bucket []deptBucket = bt.getDeptBuckets();
				Bucket []genderBucket = bt.getGenderBuckets();
				ArrayList<ArrayList<String>>bucketCombi = bt.bucketCombination(genderBucket, deptBucket);
				System.out.printf("\n bucket combinations are... \n\n");
				for(int i=0;i<bucketCombi.size();i++) {
					System.out.println("(" + bucketCombi.get(i).get(0) + ",  " + bucketCombi.get(i).get(1) + ")");
				}
				ArrayList<ArrayList<Integer>> aggreList = new ArrayList<ArrayList<Integer>>();
				for(int i=0;i<bucketCombi.size();i++) {
					ArrayList<Integer> g = bt.mapRow(bucketCombi.get(i).get(0), resBuckets);
					ArrayList<Integer> d = bt.mapRow(bucketCombi.get(i).get(1), resBuckets);
					ArrayList<Integer> tp = bt.capList(g, d);
					if(tp.isEmpty())continue;
					aggreList.add(tp);
				}
				
				//aggregate query: gender, dept
				
				//server aggregates				
				ArrayList<ArrayList<Integer>>aggRes = new ArrayList<ArrayList<Integer>>();
				for(int i=0;i<aggreList.size();i++) {
					BigInteger sumBi = new BigInteger("0");
					for(int j=0;j<aggreList.get(i).size();j++) {
						int idx = aggreList.get(i).get(j);
						sumBi = sumBi.add(shift[idx-1].multiply(BigInteger.valueOf(postT[idx-1].getSalary())));
					}
					String sumStr = sumBi.toString(2);
					while(sumStr.length()<128) {
						sumStr = '0' + sumStr;
					}
					String[] sub = new String[sumStr.length() / 32];
					for(int j=0, cnt = 0;j<sumStr.length();j+=32) {
						sub[cnt++] = sumStr.substring(j, j+32);
					}

					ArrayList<Integer>tempAl = new ArrayList<Integer>();
					for(int j=0;j<sumStr.length()/32;j++) {
						tempAl.add(Integer.parseInt(sub[j], 2));
					}

					aggRes.add(tempAl);
					
				}
				
				Element []enShift = encrptShift(coef, cryptPT);
				Element []valShifted = new Element[aggreList.size()];;
				for(int i=0;i<aggreList.size();i++) {
					valShifted[i] = valueShifted(enShift, cryptPT, aggreList.get(i));
				}
				
				System.out.printf("(encrypt)shift value result...\n\n");
				for(int i=0;i<enShift.length;i++) {
					//BigInteger resBii = BGN.decrypt(enShift[i], pubkey, prikey);
					System.out.printf("shift[%d] value:" + enShift[i].hashCode()+"\n", i+1);
					//System.out.println(resBii);	
				}
				
				System.out.printf("\n bucket-combination result...\n\n");
				for(int i=0;i<aggreList.size();i++) {
					System.out.printf("combination %d:" + aggreList.get(i).toString() + "\n",i+1);
				}
				
				System.out.printf("\n (encrypt)aggregation value result...\n\n");
				for(int i=0;i<aggreList.size();i++) {
					//BigInteger resbij = BGN.decrypt_mul2(valShifted[i], pubkey, prikey);
					//System.out.println(resbij);
					System.out.printf("aggregation value:" + valShifted[i].hashCode()+"\n", i+1);
				}
				
				
				System.out.printf("\n shift and aggregation result...\n\n");
				for(ArrayList<Integer> al : aggRes) {
					System.out.println(al.toString());
				}
				

				
				
			}
			
			
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			try {
				serverSocket.close();
				bufferedReader.close();
				bufferedWriter.close();
			} catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
}

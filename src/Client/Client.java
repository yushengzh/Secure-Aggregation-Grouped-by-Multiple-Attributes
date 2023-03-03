package Client;

import BGN.*;
import it.unisa.dia.gas.jpbc.Element;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.math.BigInteger;
import test.Entry;
import test.PostEntry;
import util.Bucket;
import util.Bucketize;

import java.net.ServerSocket;
import java.net.Socket;


public class Client {
	
	private static int PORT = 8800;
	private static String address = "127.0.0.1";
	static final String QUIT = "quit";
	static Socket socket = null;
	static BufferedReader bufferedReader = null;
	static BufferedReader consleReader = null;
	static BufferedWriter bufferedWriter = null;
	
	static int num = 5;
	
	//map<String,String>combination;
	
	public static int str2int(String s) {
		return Integer.valueOf(s).intValue();
	}
	
	public static String int2str(int i) {
		return String.valueOf(i);
	}

	
	
	public static void main(String[] args) throws Exception {
		
		
		
		Bucketize bt = new Bucketize(2);
		System.out.println("\n compute shift function coefficient...\n");
		BigInteger []coef = bt.shiftFunction(2);
		System.out.println("coefficient are as followed:");
		for(int i=0;i<coef.length;i++) {
			System.out.print("a" + i + " = " + coef[i] + "; ");
		}
		System.out.print("\n");
		
		//socket
		
		try {
			socket = new Socket(address,PORT);
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();
			
			bufferedWriter =new BufferedWriter(new OutputStreamWriter(os));
			bufferedReader =new BufferedReader(new InputStreamReader(is));
			consleReader = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Client --> Server: transfer the coefficients...");
			while(true) {	
				for(int i=0;i<coef.length;i++) {
					bufferedWriter.write(String.valueOf(coef[i])+"\n");
					bufferedWriter.flush();
				}
				String input = consleReader.readLine();
				bufferedWriter.write(input +"\n");
				try {
					bufferedWriter.flush();
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				if(QUIT.equalsIgnoreCase(input)) {
					break;
				}
			}
	
			
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			try {
				
				bufferedWriter.close();
				bufferedReader.close();
				socket.close();
				consleReader.close();
			} catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		
		
	}
}

package test;
import java.math.BigInteger;

import Client.Client;
import test.Entry;
import util.Solver;
public class Test {
	
	public static void main(String[] args) throws Exception {
		//double [][]a = new double[5][5];
		//double []b = new double[5];
		//BigInteger [][]a = new BigInteger()[][]{{"1","0","0","0"},{"1","0","1","0"},{"1","1","0","0"},{"1","1","1","1"}};
		//double []b = {1,Math.pow(2.0, 32.0),Math.pow(2.0, 64.0),Math.pow(2.0, 96.0)};
		//Solver solver = new Solver(a, b, 4);
		BigInteger a[][] = 
			   {{new BigInteger("1"),new BigInteger("0"),new BigInteger("0"),new BigInteger("0")},
				{new BigInteger("1"),new BigInteger("0"),new BigInteger("1"),new BigInteger("0")},
				{new BigInteger("1"),new BigInteger("1"),new BigInteger("0"),new BigInteger("0")},
				{new BigInteger("1"),new BigInteger("1"),new BigInteger("1"),new BigInteger("1")}};
		BigInteger bia[] = new BigInteger[4];
		
		BigInteger base = new BigInteger("2");
		bia[0] = new BigInteger("1");
		bia[1] = base.pow(32);
		bia[2] = base.pow(64);
		bia[3] = base.pow(96);
		Solver solver = new Solver(a,  bia, 4);
		
		Entry[] T = new Entry[6];
		T[0] = new Entry(1000,"male","Henry","Sales");
		T[1] = new Entry(5000,"fmale","Jessica","Sales");
		T[2] = new Entry(1500,"fmale","Alice","Finance");
		T[3] = new Entry(3000,"male","Bob","Sales");
		T[4] = new Entry(2000,"male","Paul","Facility");
		T[5] = new Entry(5000, "male", "Zhao", "Facility");
	}
}

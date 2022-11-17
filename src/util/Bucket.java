package util;

import java.util.ArrayList;
import java.util.Vector;

import org.omg.CosNaming.NamingContextExtPackage.AddressHelper;

public class Bucket {
	private static int cnt = 1;
	private String bucketMark;
	private ArrayList<Integer>rowsArrayList = new ArrayList<Integer>();
	private int idx;
	
	public Bucket(String mark) {
		// TODO 自动生成的构造函数存根
		this.bucketMark = mark;
		this.idx = cnt;
		Bucket.cnt = Bucket.cnt + 1;
	}
	
	public void addRow(int rowId) {
		rowsArrayList.add(rowId);
	}
	
	public String getMark() {
		return bucketMark;
	}
	
	public int getIdx() {
		return idx;
	}
	
	public void print() {
		System.out.println(" | " + bucketMark + " | " + rowsArrayList.toString() + " | ");
	}
	
}

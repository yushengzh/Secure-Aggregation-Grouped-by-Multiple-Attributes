package util;

import test.Entry;
import test.PostEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.Iterator;

import java.math.BigInteger;

public class Bucketize {
	private int bucket;
	
	public Bucketize(int bucket) {
		this.bucket = bucket;
	}
	
	//build bucket ¨C row index to support SE
	public Bucket[] buildBuckets(Entry []T) {
		int tableSize = T.length;
		Set<String>genderSet = new LinkedHashSet<String>();
		Set<String>deptSet = new LinkedHashSet<String>();
		for(int i=0;i<tableSize;i++) {
			//int salary, String gender, String dept
			genderSet.add(T[i].getGender());
			deptSet.add(T[i].getDept());
		}
		// non-numerical attribute size
		int c1 = genderSet.size(), c2 = deptSet.size();;
		
		// non-numerical attribute --> bucket nums
		int b1 = (int) Math.ceil((double)c1 / bucket), b2 = (int) Math.ceil((double)c2 / bucket);
		
		// build buckets
		Bucket []resBuckets = new Bucket[b1+b2];
		//Bucket []genderBuckets = new Bucket[b1];
		//Bucket []deptBuckets = new Bucket[b2];
		for(int i=0;i<b1;i++) {
			resBuckets[i] = new Bucket("Gen"+String.valueOf(i));
		}
		for(int i=b1;i<b1+b2;i++) {
			resBuckets[i] = new Bucket("dept"+String.valueOf(i));
		}
		
		//decide attribute mapping e.g. f(gender) = 1, 2; f(dept) = 1, 2, 3
		Map<String, Integer>attrMap = new HashMap<String, Integer>();
		Iterator<String> it1 = genderSet.iterator();
		Iterator<String> it2 = deptSet.iterator();
		int cnt1 = 0, cnt2 = 0;
		while(it1.hasNext()) {
			cnt1++;
			attrMap.put(it1.next(), cnt1-1);			
		}
		while(it2.hasNext()) {
			cnt2++;
			attrMap.put(it2.next(), cnt2-1);
		}
		
		for(int i=0;i<tableSize;i++) {
			int gValue = attrMap.get(T[i].getGender());
			resBuckets[gValue / bucket].addRow(T[i].getId());
			try {
				int dValue = attrMap.get(T[i].getDept());
				resBuckets[b1 + dValue / bucket].addRow(T[i].getId());
			}catch (Exception e) {
				// TODO: handle exception
			}
			
		}
		return resBuckets;	
	}
	
	//compute encrypted table
	public PostEntry[] encodeTable(Entry []T) {
		int tableSize = T.length;
		Set<String>genderSet = new LinkedHashSet<String>();
		Set<String>deptSet = new LinkedHashSet<String>();
		for(int i=0;i<tableSize;i++) {
			//int salary, String gender, String dept
			genderSet.add(T[i].getGender());
			deptSet.add(T[i].getDept());
		}
		
		// set --> array
		String []genderStr = (String [])genderSet.toArray(new String[genderSet.size()]);
		String []deptStr = (String [])deptSet.toArray(new String[deptSet.size()]);
		
		Map<String, Integer>valueEncodeMap = new HashMap<String, Integer>();
		for (int i = 0;i < genderStr.length;i++) {
			valueEncodeMap.put(genderStr[i], i % bucket);
		}
		for (int i = 0; i < deptStr.length; i++) {
			valueEncodeMap.put(deptStr[i], i % bucket);
		}
			
		// row index bucketize
		PostEntry []postT = new PostEntry[tableSize];
		for(int i=0;i<tableSize;i++) {
			postT[i] = new PostEntry(T[i].getSalary(), T[i].getGender(), T[i].getName(), T[i].getDept());
			postT[i].setGender("" + valueEncodeMap.get(T[i].getGender()));
			postT[i].setDept("" + valueEncodeMap.get(T[i].getDept()));
			postT[i].setGdd();
		}
		//System.out.println(genderSet.toString());
		return postT;
	}
	
	//
	public BigInteger[] shiftFunction(int attrNum, ArrayList<Integer>codes) {
		BigInteger []coef = new BigInteger[attrNum * bucket];
		int len = codes.size();
		
		return coef;
	}
	
}

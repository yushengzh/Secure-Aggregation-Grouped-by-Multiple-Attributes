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

public class Bucketize {
	private int bucket;
	
	public Bucketize(int bucket) {
		this.bucket = bucket;
	}
	public PostEntry[] buildBucket(Entry []T) {
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
		Bucket []genderBuckets = new Bucket[b1];
		Bucket []deptBuckets = new Bucket[b2];
		for(int i=0;i<b1;i++) {
			genderBuckets[i] = new Bucket("Gen"+String.valueOf(i));
		}
		for(int i=0;i<b2;i++) {
			deptBuckets[i] = new Bucket("dept"+String.valueOf(i));
		}
		
		// set --> array
		String []genderStr = (String [])genderSet.toArray(new String[genderSet.size()]);
		String []deptStr = (String [])deptSet.toArray(new String[deptSet.size()]);
		
		Map<String, Integer>valueDecodeMap = new HashMap<String, Integer>();
		for(int i=0;i<genderStr.length;i++)
			valueDecodeMap.put(genderStr[i], i % bucket);
		for (int i = 0; i < deptStr.length; i++) {
			valueDecodeMap.put(deptStr[i], i % bucket);
		}
			
		// row index bucketize
		PostEntry []postT = new PostEntry[tableSize];
		for(int i=0;i<tableSize;i++) {
			postT[i] = new PostEntry(T[i].getSalary(), T[i].getGender(), T[i].getName(), T[i].getDept());
			postT[i].setGender("" + valueDecodeMap.get(T[i].getGender()));
			postT[i].setDept("" + valueDecodeMap.get(T[i].getDept()));
			postT[i].setGdd();
		}
		//System.out.println(genderSet.toString());
		return postT;
	}
	
}

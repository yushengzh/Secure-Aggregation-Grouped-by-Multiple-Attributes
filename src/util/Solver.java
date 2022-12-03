package util;

import java.math.BigInteger;

public class Solver {
	private BigInteger a[][];
	private BigInteger b[];
	private BigInteger x[];
	private int n;
	private static int swapRow; 
	public Solver(BigInteger a[][],BigInteger b[],int num) {
		this.a = new BigInteger[num][num];
		this.b = new BigInteger[num];
		//assignMatrix(this.a, a, num);
		//assignMatrix(this.b, b, num);
		this.a = a;
		this.b = b;
		this.n = num;
		x = new BigInteger[num];
		printA();
		Elimination();
		Back();
		printAns();
		Determinant();
	}
	
	private void assignMatrix(BigInteger a1[][], BigInteger a2[][], int num) {
		for(int i=0;i<num;i++) {
			for(int j=0;j<num;j++) {
				a1[i][j] = a2[i][j];
			}
		}
	}
	
	//reload
	private void assignMatrix(BigInteger a[], BigInteger b[], int num) {
		for(int i=0;i<num;i++) {
			a[i] = b[i];			
		}
	}
	
	private void printAns() {
		System.out.println("root is: ");
		for(int i=0;i<n;i++) {
			System.out.println(x[i]);
		}
	}
	
	private void printA() {
		System.out.println("Ôö¹ã¾ØÕóÎª");
		for(int i=0;i<n;i++) {
			for(int j=0;j<n;j++) {
				System.out.print(a[i][j] + " ");
			}
			System.out.print(b[i] + " ");
			System.out.print("\n");
		}
	}
	
	private void Wrap(int k) {
		BigInteger max = a[k][k];
		int n1 = k;
		for(int i=k+1;i<n;i++) {
			if(a[i][k].compareTo(max)==1) {
				n1 = i;
				max = a[i][k];
			}
		}
		if(n1 != k) {
			swapRow++;
			System.out.println("k=" + k + ", swap row is" + k + "and" + n1);
			for(int j=k;j<n;j++) {
				BigInteger temp = a[k][j];
				a[k][j] = a[n1][j];
				a[n1][j] = temp;
			}
			BigInteger temp2 = b[k];
			b[k] = b[n1];
			b[n1] = temp2;
			System.out.println("after swap");
			printA();
		}		
	}
	
	private void Elimination() {
		for(int k=0;k<n-1;k++) {
			Wrap(k);
			for(int i=k+1;i<n;i++) {
				BigInteger l = a[i][k].divide(a[k][k]); // / a[k][k];
				a[i][k] = new BigInteger("0");
				for(int j=k+1;j<n;j++) {
					a[i][j] = a[i][j].subtract(l.multiply(a[k][j]));//  - l * a[k][j];
				}
				b[i] = b[i].subtract(l.multiply(b[k])); // - l * b[k];
			}
			System.out.println("after k-th elimination: ");
			printA();
		}
	}
	
	private void Back() {
		x[n-1] = b[n-1].divide(a[n-1][n-1]); // / a[n-1][n-1];
		for(int i=n-2;i>=0;i--) {
			BigInteger tp = b[i].subtract(Cal(i));
			x[i] = tp.divide(a[i][i]);
			//x[i] = (b[i]-Cal(i))/a[i][i];	
		}
	}
	
	private BigInteger Cal(int i) {
		BigInteger he = new BigInteger("0");
		for(int j=i+1;j<n;j++) {
			try {
				he = he.add(x[j].multiply(a[i][j])); // + x[j] * a[i][j];
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
		return he;
	}
	
	private void Determinant() {
		BigInteger dm = new BigInteger("1");
		for(int i=0;i<n;i++) {
			BigInteger a2 = a[i][i];
			dm = dm.multiply(a2); // * a2;
		}
		int n3 = swapRow;
		BigInteger base = new BigInteger("-1");
		//double tp = Math.pow(-1.0,  n3);
		dm = dm.multiply(base.pow(n3)); // * Math.pow(-1.0,  n3);
		System.out.println("det(A) = " + dm);
	}
}

package test;

import BGN.*;
import java.util.Scanner;

public class Entry {
	private static int cnt = 0;
	private int id;
	private int salary;
	private String gender;
	private String name;
	private String dept;
	
	public Entry(int salary, String gender, String name, String dept) {
		this.salary = salary;
		this.gender = gender;
		this.name = name;
		this.dept = dept;
		Entry.cnt = Entry.cnt + 1;
		this.id = cnt;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int newId) {
		this.id = newId;
	}
	public int getSalary() {
		return salary;
	}
	
	public void setSalary(int newSalary) {
		this.salary = newSalary;
	}
	
	public String getName() {
		return name;
	}
	
	public String getGender() {
		return gender;
	}
	
	public void setGender(String newGender) {
		this.gender = newGender;
	}
	
	public String getDept() {
		return dept;
	}
	
	public void setDept(String newDept) {
		this.dept = newDept;
	}
	
	public void print() {
		System.out.println(" | " + id + " | " + salary + " | " + gender + " | " + name + " | " + dept + " | ");
	}
	
	public static void main(String[] args) {
		
		
	}
}



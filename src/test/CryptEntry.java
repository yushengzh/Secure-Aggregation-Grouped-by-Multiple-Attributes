package test;

import it.unisa.dia.gas.jpbc.Element;

public class CryptEntry {
	private static int cnt = 0;
	private int id;
	private Element salary;
	private Element gender;
	private Element gdd;
	private Element dept;
	
	public CryptEntry(Element salary, Element gender, Element dept, Element gdd) {
		this.salary = salary;
		this.gender = gender;
		this.gdd = gdd;
		this.dept = dept;
		CryptEntry.cnt = CryptEntry.cnt + 1;
		this.id = cnt;
	}
	public int getId() {
		return id;
	}
	public void setId(int newId) {
		this.id = newId;
	}
	public Element getSalary() {
		return salary;
	}
	
	public void setSalary(Element newSalary) {
		this.salary = newSalary;
	}
	
	public Element getGdd() {
		return gdd;
	}
	
	public Element getGender() {
		return gender;
	}
	
	public void setGender(Element newGender) {
		this.gender = newGender;
	}
	
	public Element getDept() {
		return dept;
	}
	
	public void setDept(Element newDept) {
		this.dept = newDept;
	}
	
	public void print() {
		System.out.println(" | " + id + " | " + salary.hashCode() + " | " + gender.hashCode() + " | " + dept.hashCode() + " | "+gdd.hashCode() + " | ");
	}
}

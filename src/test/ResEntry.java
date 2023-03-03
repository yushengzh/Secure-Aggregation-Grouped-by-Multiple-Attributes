package test;

public class ResEntry {
	private static int cnt = 0;
	private int id;
	private int salary;
	private String gender;
	private String dept;
	
	public ResEntry(int salary, String gender, String dept) {
		this.salary = salary;
		this.gender = gender;
		this.dept = dept;
		ResEntry.cnt = ResEntry.cnt + 1;
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
		System.out.println(" | " + salary + " | " + gender + " | " + dept + " | ");
	}
}

package test;

public class PostEntry extends Entry{
	private String genderDotDept;
	private static int cnt = 1;
	public PostEntry(int salary, String gender, String name, String dept) {
		super(salary, gender, name, dept);
		// TODO 自动生成的构造函数存根
		setGdd();
		setId(cnt);
		cnt++;
		
	}
	public void setGdd() {
		try {
			int a = Integer.valueOf(this.getGender()).intValue();
			int b = Integer.valueOf(this.getDept()).intValue();
			this.genderDotDept = "" + a*b; 
		}catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
		}
	}
	
	public void setGdd(int newGdd) {
		this.genderDotDept = String.valueOf(newGdd);
	}
	
	public String getGdd() {
		return genderDotDept;
	}
	
	public void print() {
		System.out.println(" | " + getId() + " | " + getSalary() + " | " + getGender() + " | " + getName() + " | " + getDept() +  " | " + getGdd() + " | ");
	}

}

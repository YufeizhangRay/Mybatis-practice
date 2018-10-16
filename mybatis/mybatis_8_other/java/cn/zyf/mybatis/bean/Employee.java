package cn.zyf.mybatis.bean;

public class Employee {

	private int id;
	private String lastName;
	private String email;
	private String gender;
	private EmpStatus state;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	
	public EmpStatus getState() {
		return state;
	}

	public void setState(EmpStatus state) {
		this.state = state;
	}

	

	@Override
	public String toString() {
		return "Employee [id=" + id + ", lastName=" + lastName + ", email=" + email + ", gender=" + gender + ", state="
				+ state + "]";
	}

	public Employee() {
	}
	
	public Employee(String lastName, String email, String gender) {
		this.lastName = lastName;
		this.email = email;
		this.gender = gender;
	}

}

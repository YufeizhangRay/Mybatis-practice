package cn.zyf.mybatis.dao;

import java.util.List;

import cn.zyf.mybatis.bean.Employee;

public interface EmployeeMapper {

	public Employee getEmpById(Integer id);

	public List<Employee> getEmps();

	public void addEmp(Employee employee);
}

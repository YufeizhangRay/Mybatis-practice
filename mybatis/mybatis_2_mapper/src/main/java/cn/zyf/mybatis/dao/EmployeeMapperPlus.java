package cn.zyf.mybatis.dao;

import java.util.List;

import cn.zyf.mybatis.bean.Employee;

public interface EmployeeMapperPlus {

	public Employee getEmployeeById(Integer id);
	
	public Employee getEmployeeAndDept(Integer id);
	
	public Employee getEmployeeByIdStep(Integer id);
	
	public List<Employee> getEmployeeByDeptId(Integer deptId);
}

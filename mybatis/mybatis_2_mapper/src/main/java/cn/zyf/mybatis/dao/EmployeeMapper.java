package cn.zyf.mybatis.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import cn.zyf.mybatis.bean.Employee;

public interface EmployeeMapper {

	//多条记录封装一个map：Map<Integer,Employee>：键是这条记录的主键，值是封装记录后的javaBean
	//告诉mybatis封装这个map的时候使用哪个作为主键
	@MapKey("id")
	public Map<Integer,Employee> getEmpByLastNameLikeReturnMap(String lastName);
	
	//返回一个记录的map：key就是列名，值就是对应值
	public Map<String, Object> getEmpByReturnMap(Integer id);
	
	public List<Employee> getEmpByLastNameLike(String lastName);
	
	public Employee getEmpByMap(Map<String, Object> map);

	public Employee getEmpByIdAndLastName(@Param("id") Integer id, @Param("lastName") String lastName);

	public Employee getEmpById(Integer id);

	public void addEmp(Employee employee);

	public Boolean updateEmp(Employee employee);

	public void deleteEmpById(Integer id);
}

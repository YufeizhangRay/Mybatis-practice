package cn.zyf.mybatis.dao;

import org.apache.ibatis.annotations.Select;

import cn.zyf.mybatis.bean.Employee;

public interface EmployeeMapperAnnotation {

	@Select("select id,last_name,email,gender from tbl_employee where id = #{id}")
	public Employee getEmpById(Integer id);
}

package cn.zyf.mybatis.dao;

import cn.zyf.mybatis.bean.Department;

public interface DepartmentMapper {

	public Department getDeaprtmentById(Integer id);
	
	public Department getDeaprtmentByIdPlus(Integer id);
	
	public Department getDeaprtmentByIdStep(Integer id);
}

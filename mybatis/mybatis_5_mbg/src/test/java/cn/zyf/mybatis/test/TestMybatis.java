package cn.zyf.mybatis.test;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import cn.zyf.mybatis.bean.Employee;
import cn.zyf.mybatis.bean.EmployeeExample;
import cn.zyf.mybatis.bean.EmployeeExample.Criteria;
import cn.zyf.mybatis.dao.EmployeeMapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

class TestMybatis {

	public SqlSessionFactory getSqlSessionFactory() throws IOException {
		String resource = "mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		return new SqlSessionFactoryBuilder().build(inputStream);
	}

	@Test
	public void testMbg() throws Exception {
		List<String> warnings = new ArrayList<String>();
		boolean overwrite = true;
		String path = this.getClass().getClassLoader().getResource("mbg.xml").getPath();
		File configFile = new File(path);
		ConfigurationParser cp = new ConfigurationParser(warnings);
		Configuration config = cp.parseConfiguration(configFile);
		DefaultShellCallback callback = new DefaultShellCallback(overwrite);
		MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
		myBatisGenerator.generate(null);
	}
	
	@Test
	public void testMybatis3Simple() throws IOException {
		SqlSessionFactory sessionFactory = getSqlSessionFactory();
		SqlSession session = sessionFactory.openSession();
		try {
			EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
			List<Employee> list = mapper.selectByExample(null);
			for (Employee employee : list) {
				System.out.println(employee.getId());
			}
		} finally {
			session.close();
		}
	}

	@Test
	public void testMybatis3() throws IOException {
		SqlSessionFactory sessionFactory = getSqlSessionFactory();
		SqlSession session = sessionFactory.openSession();
		try {
			EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
			//xxxExample就是封装查询条件的
			//1.查询所有
			//List<Employee> emps = mapper.selectByExample(null);
			//2.查询员工名字中有e字母的，和员工性别是1的
			//select id, last_name, gender, email, d_id from tbl_employee 
			//WHERE ( last_name like ? and gender = ? ) or( email like ? ) 
			EmployeeExample employeeExample = new EmployeeExample();
			Criteria criteria =  employeeExample.createCriteria();
			criteria.andLastNameLike("%e%");
			criteria.andGenderEqualTo("1");
			
			Criteria criteria2 =  employeeExample.createCriteria();
			criteria2.andEmailLike("e");
			employeeExample.or(criteria2);
			
			List<Employee> list = mapper.selectByExample(employeeExample);
			for (Employee emp : list) {
				System.out.println(emp.getId());
			}
		} finally {
			session.close();
		}
	}

}
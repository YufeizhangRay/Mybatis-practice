package cn.zyf.mybatis.test;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import cn.zyf.mybatis.bean.Department;
import cn.zyf.mybatis.bean.Employee;
import cn.zyf.mybatis.dao.DepartmentMapper;
import cn.zyf.mybatis.dao.EmployeeMapper;
import cn.zyf.mybatis.dao.EmployeeMapperAnnotation;
import cn.zyf.mybatis.dao.EmployeeMapperDynamicSQL;
import cn.zyf.mybatis.dao.EmployeeMapperPlus;

class TestMybatis {

	public SqlSessionFactory getSqlSessionFactory() throws IOException {
		String resource = "mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		return new SqlSessionFactoryBuilder().build(inputStream);
	}

	
	public void testInnerParam() throws IOException {
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession sqlSession = sqlSessionFactory.openSession(true);
		try {
			EmployeeMapperDynamicSQL mapper = sqlSession.getMapper(EmployeeMapperDynamicSQL.class);
			Employee employee = new Employee();
			employee.setLastName("%e%");
			List<Employee> list = mapper.getEmpsTestInnerParameter(employee);
			for (Employee emp : list) {
				System.out.println(emp);
			}
		}finally {
			sqlSession.close();
		}
	}
	
	@Test
	public void testBatchSave() throws IOException {
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession sqlSession = sqlSessionFactory.openSession(true);
		try {
			EmployeeMapperDynamicSQL mapper = sqlSession.getMapper(EmployeeMapperDynamicSQL.class);
			List<Employee> emps = new ArrayList<>();
			emps.add(new Employee(null, "simth", "simth@163.com", "0",new Department(1))); 
			emps.add(new Employee(null, "allen", "allen@163.com", "0",new Department(2))); 
			mapper.addEmps(emps);
		} finally {
			sqlSession.close();
		}
	}

	@Test
	public void testDynamicSql() throws IOException {
		// 获取sqlSessionFactory对象
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();

		// 获取sqlSession对象
		SqlSession sqlSession = sqlSessionFactory.openSession(true);
		try {
			// 获取接口实现类对象
			EmployeeMapperDynamicSQL mapper = sqlSession.getMapper(EmployeeMapperDynamicSQL.class);
			// select * from tbl_employee where id=? and last_name like ? and email=?
			// 测试if/where
			Employee employee = new Employee(1, "Admin", null, null);
			// List<Employee> employees = mapper.getEmpsByConditionIf(employee);
			// for (Employee emp : employees) {
			// System.out.println(emp);
			// }
			// 查询的时候如果某些条件没带可能sql拼装会有问题
			// 1.给where后面加上 1=1，以后的条件都and xxx.
			// 2.mybatis使用where标签来将所有的查询条件包括在内
			// mybatis就会将where标签中拼装的sql中多出来的and或者or去掉
			// 只会去掉第一个多出来的and或者or

			// 测试trim
			// List<Employee> employees2 = mapper.getEmpsByConditionTrim(employee);
			// for (Employee emp : employees2) {
			// System.out.println(emp);
			// }

			// 测试choose
			// List<Employee> employee3 = mapper.getEmpsByConditionChoose(employee);
			// for (Employee emp : employee3) {
			// System.out.println(emp);
			// }

			// 测试set标签
			// mapper.updateEmp(employee);

			// 测试foreach
			List<Employee> list = mapper.getEmpsByConditionForeach(Arrays.asList(1, 2));
			for (Employee emp : list) {
				System.out.println(emp);
			}
		} finally {
			sqlSession.close();
		}
	}

	/**
	 * 1.根据xml配置文件(全局)配置文件创建一个 SqlSessionFactory 对象 有数据源一些运行环境信息
	 * 2.sql映射文件：配置了每一个sql和sql的封装规则 
	 * 3.将映射文件配置在全局配置文件 
	 * 4.写代码： 
	 * 1）根据全局配置文件得到SqlSessionFactory 
	 * 2）使用qlSessionFactory，获取到sqlSession对象来进行增删改查 一个sqlSession对象就是一次会话，用完关闭 
	 * 3）使用sql的唯一标识来告诉mybatis执行哪一条sql
	 * 
	 * @throws IOException
	 */
	@Test
	void test() throws IOException {
		String resource = "mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

		// 获取sqlSessionFactory实例,只能直接执行已经映射的sql语句
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			// sql的唯一标识：statement Unique identifier matching the statement to use.
			// 执行sql要用的参数：parameter A parameter object to pass to the statement.
			Employee employee = sqlSession.selectOne("cn.zyf.mybatis.EmployeeMapper.selectEmp", 1);
			System.out.println(employee);
		} catch (Exception e) {
		} finally {
			sqlSession.close();
		}
	}

	/**
	 * 1.接口式编程 原生： Dao ==> DaoImpl mybatis: mapper ==> xxMapper.xml
	 * 
	 * 2.sqlSession代表和数据库的一次会话，用完必须关闭 3.sqlSession和connection一样
	 * 都是非线程安全的，每次使用都应该获取新的对象 4.mapper接口没有实现类，但是mybatis会为这个接口生成一个代理对象 （将接口和xml）进行绑定
	 * EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
	 * 5.两个重要的配置文件： mybatis的全局配置文件，包含数据库连接池信息，事务管理器等系统运行环境信息 sql映射文件：保存了每一个sql的映射信息
	 * 将sql抽取出来
	 * 
	 * @throws IOException
	 */
	@Test
	public void test01() throws IOException {
		// 获取sqlSessionFactory对象
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();

		// 获取sqlSession对象
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			// 获取接口实现类对象
			EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);

			System.out.println(mapper);
			Employee employee = mapper.getEmpById(1);

			System.out.println(employee);
		} finally {
			sqlSession.close();
		}
	}

	@Test
	public void test02() throws IOException {
		// 获取sqlSessionFactory对象
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();

		// 获取sqlSession对象
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			// 获取接口实现类对象
			EmployeeMapperAnnotation mapper = sqlSession.getMapper(EmployeeMapperAnnotation.class);

			System.out.println(mapper);
			Employee employee = mapper.getEmpById(1);

			System.out.println(employee);
		} finally {
			sqlSession.close();
		}
	}

	/**
	 * 测试增删改 
	 * 1.mybatis允许增删改直接定义以下类型返回值 Integer Long Boolean 
	 * 2.提交数据 sqlSessionFactory.openSession(); 手动 
	 * 			sqlSessionFactory.openSession(true); 自动
	 * @throws IOException
	 */
	@Test
	public void test03() throws IOException {
		// 获取sqlSessionFactory对象
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();

		// 获取sqlSession对象 不自动提交
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			// 获取接口实现类对象
			EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
			// 添加测试
			// Employee employee = new Employee(null, "Jerry", "jerry@163.com", "1");
			// mapper.addEmp(employee);
			// System.out.println(employee.getId());
			// 修改测试
			// Employee employee = new Employee(1, "Tom", "jerry@163.com", "1");
			// Boolean boolean1 = mapper.updateEmp(employee);
			// System.out.println(boolean1);
			// 删除测试
			// mapper.deleteEmpById(2);
			// 手动提交数据
			sqlSession.commit();
		} finally {
			sqlSession.close();
		}
	}

	@Test
	public void test04() throws IOException {
		// 获取sqlSessionFactory对象
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();

		// 获取sqlSession对象 自动提交
		SqlSession sqlSession = sqlSessionFactory.openSession(true);
		try {
			// 获取接口实现类对象
			EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
			// Map<String, Object> map = new HashMap<>();
			// map.put("id",1);
			// map.put("lastName","Tom");
			// Employee employee = mapper.getEmpByMap(map);
			// System.out.println(employee);
			// List<Employee> employees = mapper.getEmpByLastNameLike("%o%");
			// System.out.println(employees);
			// Map<String, Object> map = mapper.getEmpByReturnMap(1);
			Map<Integer, Employee> map = mapper.getEmpByLastNameLikeReturnMap("%o%");
			System.out.println(map);
		} finally {
			sqlSession.close();
		}
	}

	@Test
	public void test05() throws IOException {
		// 获取sqlSessionFactory对象
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();

		// 获取sqlSession对象 自动提交
		SqlSession sqlSession = sqlSessionFactory.openSession(true);
		try {
			// 获取接口实现类对象
			EmployeeMapperPlus mapper = sqlSession.getMapper(EmployeeMapperPlus.class);
			//// Employee employee = mapper.getEmployeeById(1);
			// Employee employee = mapper.getEmployeeAndDept(1);
			Employee employee = mapper.getEmployeeByIdStep(1);
			System.out.println(employee.getLastName());
			System.out.println(employee.getDept());
		} finally {
			sqlSession.close();
		}
	}

	@Test
	public void test06() throws IOException {
		// 获取sqlSessionFactory对象
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();

		// 获取sqlSession对象 自动提交
		SqlSession sqlSession = sqlSessionFactory.openSession(true);
		try {
			// 获取接口实现类对象
			DepartmentMapper mapper = sqlSession.getMapper(DepartmentMapper.class);
			// Department department = mapper.getDeaprtmentById(1);
			// Department department = mapper.getDeaprtmentByIdPlus(1);
			Department department = mapper.getDeaprtmentByIdStep(1);
			System.out.println(department.getDepartmentName());
			System.out.println(department.getEmps());
		} finally {
			sqlSession.close();
		}
	}
}

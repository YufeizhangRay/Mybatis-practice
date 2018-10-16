package cn.zyf.mybatis.test;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.zyf.mybatis.bean.EmpStatus;
import cn.zyf.mybatis.bean.Employee;
import cn.zyf.mybatis.dao.EmployeeMapper;

class TestMybatis {

	public SqlSessionFactory getSqlSessionFactory() throws IOException {
		String resource = "mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		return new SqlSessionFactoryBuilder().build(inputStream);
	}

	/**
	 * 1.根据xml配置文件(全局)配置文件创建一个 SqlSessionFactory 对象 有数据源一些运行环境信息
	 * 2.sql映射文件：配置了每一个sql和sql的封装骨规则 3.将映射文件配置在全局配置文件 4.写代码： 1）根据全局配置文件得到
	 * SqlSessionFactory 2）使用qlSessionFactory，获取到sqlSession对象来进行增删改查
	 * 一个sqlSession对象就是一次会话，用完关闭 3）使用sql的唯一标识来告诉mybatis执行哪一条sql
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
			Page<Object> page = PageHelper.startPage(1, 4);
			List<Employee> emps = mapper.getEmps();
			// 传入一次显示多少页
			PageInfo<Employee> info = new PageInfo<>(emps, 2);
			for (Employee employee : emps) {
				System.out.println(employee);
			}
			 System.out.println("当前页码："+page.getPageNum());
			 System.out.println("总记录数："+page.getTotal());
			 System.out.println("每页的记录数："+page.getPageSize());
			 System.out.println("总页码数："+page.getPages());

			System.out.println("当前页码：" + info.getPageNum());
			System.out.println("总记录数：" + info.getTotal());
			System.out.println("每页的记录数：" + info.getPageSize());
			System.out.println("总页码数：" + info.getPages());
			System.out.println("是否是第一页：" + info.isIsFirstPage());
			System.out.println("连续显示的页码：");
			int[] nums = info.getNavigatepageNums();
			for (int i = 0; i < nums.length; i++) {
				System.out.println(nums[i]);
			}
		} finally {
			sqlSession.close();
		}
	}

//	@Test
//	public void test02() throws IOException {
//		// 获取sqlSessionFactory对象
//		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
//
//		// 获取sqlSession对象
//		SqlSession sqlSession = sqlSessionFactory.openSession();
//		try {
//			// 获取接口实现类对象
//			EmployeeMapperAnnotation mapper = sqlSession.getMapper(EmployeeMapperAnnotation.class);
//
//			System.out.println(mapper);
//			Employee employee = mapper.getEmpById(1);
//
//			System.out.println(employee);
//		} finally {
//			sqlSession.close();
//		}
//	}

	@Test
	public void testBatch() throws IOException {
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		//可以执行批量操作的sqlSession
		SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
		try {
			EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
			long start = System.currentTimeMillis();
			for(int i = 0;i<10000;i++) {
				mapper.addEmp(new Employee(UUID.randomUUID().toString().substring(0, 5), "a@163.com", "1"));
			}
			sqlSession.commit();
			long end = System.currentTimeMillis();
			//批量（预编译sql一次==>设置参数10000次==>执行1次） ==> 4787
			//非批量（（预编译sql==>设置参数==>执行）==>10000次） ==>4901
			System.out.println("执行时长："+(end-start));
		} finally {
			sqlSession.close();
		}
	}
	
	@Test
	public void testEnumUse(){
		EmpStatus login = EmpStatus.LOGIN;
		System.out.println("枚举的索引："+login.ordinal());
		System.out.println("枚举的名字："+login.name());
		
		System.out.println("枚举的状态码："+login.getCode());
		System.out.println("枚举的消息提示："+login.getMsg());
	}
	
	/**
	 * 默认：mybatis在处理枚举类型的时候保存的是枚举的名字
	 * 改变使用：EnumOrdinalTypeHandler
	 * @throws IOException
	 */
	@Test
	public void testEnum() throws IOException {
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
			Employee employee = new Employee("test_enum", "enum@163.com", "1");
			mapper.addEmp(employee);
			System.out.println("保存成功："+employee.getId());
//			Employee employee = mapper.getEmpById(19926);
		//	System.out.println(employee.getEmpStatus());
			sqlSession.commit();
		} finally {
			sqlSession.close();
		}
	}

}

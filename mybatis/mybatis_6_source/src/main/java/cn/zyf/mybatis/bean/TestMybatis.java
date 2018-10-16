package cn.zyf.mybatis.bean;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

import cn.zyf.mybatis.dao.EmployeeMapper;
import cn.zyf.mybatis.dao.EmployeeMapperAnnotation;

class TestMybatis {

	public SqlSessionFactory getSqlSessionFactory() throws IOException {
		String resource = "mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		return new SqlSessionFactoryBuilder().build(inputStream);
	}
	
	/**
	 * 1.根据xml配置文件(全局)配置文件创建一个 SqlSessionFactory 对象
	 *  	有数据源一些运行环境信息
	 * 2.sql映射文件：配置了每一个sql和sql的封装骨规则
	 * 3.将映射文件配置在全局配置文件
	 * 4.写代码：
	 * 		1）根据全局配置文件得到 SqlSessionFactory
	 * 		2）使用qlSessionFactory，获取到sqlSession对象来进行增删改查
	 * 			一个sqlSession对象就是一次会话，用完关闭
	 *      3）使用sql的唯一标识来告诉mybatis执行哪一条sql
	 * @throws IOException 
	 */
	@Test
	void test() throws IOException {
		String resource = "mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		
			//获取sqlSessionFactory实例,只能直接执行已经映射的sql语句
			SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			//sql的唯一标识：statement Unique identifier matching the statement to use.
			//执行sql要用的参数：parameter A parameter object to pass to the statement.
			Employee employee = sqlSession.selectOne("cn.zyf.mybatis.EmployeeMapper.selectEmp",1);
			System.out.println(employee);
		}catch (Exception e) {
		}
		finally {
			sqlSession.close();
		}
	}
	
	/**
	 * 1.接口式编程
	 *   原生：    Dao   ==>    DaoImpl
	 *   mybatis: mapper ==>  xxMapper.xml
	 *   
	 *  2.sqlSession代表和数据库的一次会话，用完必须关闭
	 *  3.sqlSession和connection一样 都是非线程安全的，每次使用都应该获取新的对象
	 *  4.mapper接口没有实现类，但是mybatis会为这个接口生成一个代理对象
	 *  		（将接口和xml）进行绑定
	 *  		EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);	
	 *  5.两个重要的配置文件：
	 *  	mybatis的全局配置文件，包含数据库连接池信息，事务管理器等系统运行环境信息
	 *  	sql映射文件：保存了每一个sql的映射信息
	 *  					将sql抽取出来
	 * @throws IOException
	 */
	@Test
	public void test01() throws IOException {
		//获取sqlSessionFactory对象
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		
		//获取sqlSession对象
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
		//获取接口实现类对象
		EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
		
		System.out.println(mapper);
		Employee employee = mapper.getEmpById(1);
		
		System.out.println(employee);
		}finally {
			sqlSession.close();
		}
	}

	@Test
	public void test02() throws IOException {
		//获取sqlSessionFactory对象
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		
		//获取sqlSession对象
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
		//获取接口实现类对象
		EmployeeMapperAnnotation mapper = sqlSession.getMapper(EmployeeMapperAnnotation.class);
		
		System.out.println(mapper);
		Employee employee = mapper.getEmpById(1);
		
		System.out.println(employee);
		}finally {
			sqlSession.close();
		}
	}

}

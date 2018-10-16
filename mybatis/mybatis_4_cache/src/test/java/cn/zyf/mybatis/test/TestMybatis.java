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

	/**
	 * 两级缓存：
	 * 一级缓存：（本地缓存）SqlSession级别的缓存 一直开启
	 * 		与数据库同一次会话期间查询到的数据会放在本地缓存
	 * 		以后如果需要获取相同的数据，直接从缓存中拿，没必要再去查询数据库
	 * 
	 * 		一级缓存失效情况:
	 * 		1.SqlSession不同
	 * 		2.SqlSession相同，查询条件不同
	 * 		3.SqlSession相同，但是两次查询之间执行了增删改操作(这次增删改查可能对数据有影响)
	 * 		4.SqlSession相同，手动清除了一级缓存
	 * 
	 * 二级缓存：（全局缓存）namespace级别的缓存：一个namespace对应一个二级缓存
	 * 		工作机制：
	 * 		1.一个会话，查询一条数据，这个数据就会被放在当前会话的一级缓存中
	 * 		2.如果会话关闭了，一次缓存中的数据会被保存到二级缓存中。新的会话可以参照二级缓存中的内容
	 * 		3.SqlSession====EmployeeMapper====>Employee
	 * 						DepartmentMapper====>Department
	 * 				不同namespace查出的数据会放在自己对应的map中(map)
	 * 
	 * 		使用：
	 * 			1).开启二级缓存配置<setting name="cacheEnabled" value="true"/>
	 * 			2).去mapper.xml中配置使用二级缓存 <cache></cache>
	 * 			3).我们的POJO需要实现序列化接口
	 * 
	 * 和缓存有关的设置和属性：
	 * 			1)cacheEnabled=true false:关闭二级缓存（一级缓存仍然有效）
	 * 			2)每个select标签都有useCache=true 
	 * 					false:缓存（一级缓存有效，二级缓存无效）
	 * 			3)每个增删改标签的flushCache=true
	 * 					会清除缓存 一次缓存清空 二级缓存清空
	 * 					查询标签flushCache=false
	 * 							如果改为true 每次缓存被清空
	 * 			4)sqlSession.clearCache();清除当前Session的一级缓存，和二级没关系
	 * 			5)localCacheScope本地缓存作用域（一级缓存） 当前会话的所有数据都会保存在会话缓存中
	 * 					STATEMENT可以禁用一级缓存
	 * 
	 * 第三方缓存的整合
	 * 		1)导入第三方缓存包
	 * 		2)导入与第三方缓存整合的适配包
	 * 		3)mapper.xml中使用自定义缓存
	 * 		<cache type="org.mybatis.caches.ehcache.EhcacheCache"></cache>
	 * @throws IOException 
	 */
	
	@Test
	public void testSecondLevelCache() throws IOException {
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession sqlSession1 = sqlSessionFactory.openSession();
		SqlSession sqlSession2 = sqlSessionFactory.openSession();
		try {
			EmployeeMapper mapper1 = sqlSession1.getMapper(EmployeeMapper.class);
			EmployeeMapper mapper2 = sqlSession2.getMapper(EmployeeMapper.class);

			//第一次查询
			Employee employee = mapper1.getEmpById(1);
			System.out.println(employee);
			sqlSession1.close();//会话关闭才会刷新缓存，从一级放入二级
			
			//第二次查询使从缓存中提取的，没有发送sql语句
			Employee employee2 = mapper2.getEmpById(1);
			System.out.println(employee2);
			sqlSession2.close();
		}finally {
		}
	}
	
	@Test
	public void testFirstLevelCache() throws IOException {
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession sqlSession = sqlSessionFactory.openSession(true);
		try {
			EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
			Employee employee = mapper.getEmpById(1);
			System.out.println(employee);
			
			//SqlSession不同
//			SqlSession sqlSession2 = sqlSessionFactory.openSession(true);
//			EmployeeMapper mapper2 = sqlSession2.getMapper(EmployeeMapper.class);
//			
//			mapper.addEmp(new Employee(null,"testCache","cache","1"));
//			System.out.println("数据添加成功");
			
//			sqlSession.clearCache();
			
			//SqlSession相同，查询条件不同
			Employee employee2 = mapper.getEmpById(1);
//			Employee employee3 = mapper.getEmpById(3);
			
			System.out.println(employee2);
//			System.out.println(employee3);
			System.out.println(employee==employee2);
			
//			sqlSession2.close();
		} finally {
			sqlSession.close();
		}
	}
	
}
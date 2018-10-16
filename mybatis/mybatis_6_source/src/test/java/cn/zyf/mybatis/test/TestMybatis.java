package cn.zyf.mybatis.test;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;

import cn.zyf.mybatis.bean.Employee;
import cn.zyf.mybatis.dao.EmployeeMapper;

import java.io.IOException;
import java.io.InputStream;

class TestMybatis {

	public SqlSessionFactory getSqlSessionFactory() throws IOException {
		String resource = "mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		return new SqlSessionFactoryBuilder().build(inputStream);
	}

	/**
	 *1.获取SqlSessionFactory对象
	 *		解析文件的每一个信息保存在Configuration中，返回包含Configuration的DefaultSqlSession
	 *		注意：MappedStatement：代表一个增删改查的详细信息
	 *2.获取SqlSession对象
	 *		返回一个DefaultSQLSession对象， 包含Executor和Configuration
	 *		这一步会创建Executor对象
	 *3.获取接口的代理对象(MapperProxy)
	 *		getMapper，使用MapperProxyFactory创建一个MapperProxy代理对象
	 *		代理对象里面包含了DefaultSqlSession(Executor)
	 *4.执行增删改查方法
	 *		
	 *总结：
	 *	1.根据配置文件（全局，sql映射）初始化Configuration对象
	 *  2.创建一个DefaultSqlSession对象，
	 *  	里面包含Configuration以及
	 *  	Executor（根据全局配置文件中的defaultExecutorType创建出对应的Executor）
	 *  3.DefaultSqlSession.getMapper():拿到mapper接口对应的MapperProxy
	 *  4.MapperProxy里面有DefaultSqlSession
	 *  5.执行增删改查方法:
	 *  	1)调用DefaultSqlSession的增删改查（Executor）
	 *  	2)会创建一个StatementHandler对象
	 *  		（同时也会创建出ParameterHandler和ResultSetHandler）
	 *  	3)调用StatementHandler预编译参数以及设置参数值
	 *  		使用ParameterHandler来给sql设置参数
	 *  	4)调用StatementHandler的增删改查方法
	 *  	5)使用ResultSetHandler封装结果
	 *  注意：
	 *  	四大对象每个创建的时候都有一个interceptorChain.pluginAll(parameterHandler);
	 * @throws IOException
	 */
	@Test
	public void testMybatis3Simple() throws IOException {
		SqlSessionFactory sessionFactory = getSqlSessionFactory();
		SqlSession session = sessionFactory.openSession();
		try {
			EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
			Employee employee = mapper.getEmpById(1);
			System.out.println(mapper.getClass());
			System.out.println(employee);
		} finally {
			session.close();
		}
	}

}
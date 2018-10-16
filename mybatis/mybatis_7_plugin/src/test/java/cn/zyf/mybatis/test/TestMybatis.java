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

	/**
	 * 插件原理
	 * 在四大对象创建的时候
	 * 1.每个创建出来的对象不是直接返回的，而是
	 * 		interceptorChain.pluginAll(parameterHandler);
	 * 2.获取所有的Interceptor（拦截器）（插件需要实现的接口）
	 * 		调用interceptor.pluginAll(target)返回target包装后的对象
	 * 3.插件机制，我们可以使用插件为目标创建一个代理对象；AOP（面向切面）
	 * 		我们的插件可以为四大对象创建出代理对象
	 * 		代理对象就可以拦截到四大对象的每一个执行方法
	 * 
	 * public Object pluginAll(Object target){
	 * 		for(Interceptor interceptor:interceptors){
	 * 			target = interceptor.pluginAll(target);
	 * 		}
	 * 	  	return target;
	 * }
	 */
	/**
	 * 插件编写
	 * 1.编写Interceptor的实现类
	 * 2.使用@Intercepts注解完成插件的签名
	 * 3.将写好的插件注册到全局配置文件中
	 */
	@Test
	public void testPlugin() {
		
	}
}
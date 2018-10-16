package cn.zyf.mybatis.bean;

import java.util.Properties;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

/**
 * 完成插件签名：
 * 		告诉Mybatis当前插件用来拦截哪个对象的哪个方法
 *
 */
@Intercepts({
	@Signature(type=StatementHandler.class,method="parameterize",args=java.sql.Statement.class)
})
public class MyFirstPlugin implements Interceptor{

	/**
	 * 拦截目标对象的目标方法的执行
	 */
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		System.out.println("MyFirstPlugin...intercept:"+invocation.getMethod());
		//动态的改变一下sql运行的参数，以前1号员工，实际从数据库查询3号员工
		//拿到拦截的StatementHandler对象
		Object target = invocation.getTarget();
		System.out.println("当前拦截到的对象:"+target);
		//拿到StatementHandler===>ParameterHandler===>parameterObject
		//拿到target的元数据
		MetaObject metaObject =  SystemMetaObject.forObject(target);
		//拿到传入SQL语句的参数
	 	Object value = metaObject.getValue("parameterHandler.parameterObject");
		System.out.println("sql语句用的参数是："+value);
		//修改sql要用的参数
		metaObject.setValue("parameterHandler.parameterObject", 3);
		//执行目标方法
		Object proceed =  invocation.proceed();
		//返回执行后的返回值
		return proceed;
	}

	/**
	 * 包装目标对象 为目标对象创建一个代理对象
	 */
	@Override
	public Object plugin(Object target) {
		System.out.println("MyFirstPlugin...plugin:mybatis将要包装的对象"+target);
		//可以借助Plugin类的wrap方法来使用当前intercept包装目标对象
		Object warp = Plugin.wrap(target, this);
		//返回为当前target创建的动态代理
		return warp;
	}

	/**
	 * 将插件注册时的property属性设置进来
	 */
	@Override
	public void setProperties(Properties properties) {
		System.out.println("插件配置的信息："+properties);
	}

	
}

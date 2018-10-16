package cn.zyf.mybatis.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import cn.zyf.mybatis.bean.EmpStatus;

public class MyEnumEmpStatusTypeHandler implements TypeHandler<EmpStatus>{
	
	@Override
	public EmpStatus getResult(ResultSet rs, String columnName) throws SQLException {
		int code = rs.getInt(columnName);
		return EmpStatus.getEmpStatusByCode(code);
	}

	@Override
	public EmpStatus getResult(ResultSet rs, int columnIndex) throws SQLException {
		int code = rs.getInt(columnIndex);
		return EmpStatus.getEmpStatusByCode(code);
	}

	@Override
	public EmpStatus getResult(CallableStatement cs, int columnIndex) throws SQLException {
		int code = cs.getInt(columnIndex);
		return EmpStatus.getEmpStatusByCode(code);
	}

	/**
	 * 这是传入数据库的参数
	 */
	@Override
	public void setParameter(PreparedStatement ps, int i, EmpStatus parameter, JdbcType jdbcType) throws SQLException {
		ps.setInt(i, parameter.getCode());
	}

}

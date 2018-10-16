package cn.zyf.mybatis.bean;

public enum EmpStatus {
	LOGIN(200, "正常状态"), FORBID(400, "禁用状态");

	private int code;
	private String msg;

	private EmpStatus(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public static EmpStatus getEmpStatusByCode(int code) {
		if (code == 200) {
			return EmpStatus.LOGIN;
		} else if (code == 400) {
			return EmpStatus.FORBID;
		} else {
			return EmpStatus.FORBID;
		}
	}

}

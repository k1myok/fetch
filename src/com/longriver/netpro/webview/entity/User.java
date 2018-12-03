package com.longriver.netpro.webview.entity;
/**
 * 这里的变量名要与LoginUser的对应
 * */
public enum User {
	REALNAME("姓名*", "realName", 1, null, 1), 
	SEX("性别*", "sexValue", 1, null, 2), 
	NATION("民族", "nationName", 0, "nation", 3), 
	BIRTHDAY("出生年月*", "birthday", 1, null, 4), 
	POLITICAL("政治面貌*", "politicalValue", 1, "political", 5), 
	DEPTID("所在部门*", "deptName", 1, "deptid", 6), 
	JOB("职位", "job", 0, null, 7), 
	USERTYPE("人员属性", "userTypeValue", 0, "userType", 8), 
	DEGREE("文化程度", "degreeValue", 0, "degree", 9), 
	NICK("常用账号网络昵称*", "nick", 1, null, 10), 
	PLAT("账号平台*", "platStr", 1, "platid", 11), 
	URL("账号URL*", "url", 1, null, 12), 
	TEL("手机*", "tel", 1, null, 13), 
	EMAIL("常用邮箱*", "email", 1, null, 14), 
	QQ("QQ", "QQ", 0, null, 15), 
	MICRO("微信", "micro", 0, null, 16), 
	BRANKNAME("开户行", "brankName", 0, null, 17), 
	BRANKACCOUNT("银行卡号", "brankAccount", 0, null, 18);
	private String fieldName;//Excel表头
	private String properties;//loginUser属性
	private int isNotNull;//是否为必填属性
	private String validity;//是否验证数据有效性 1:代表需要验证
	private int sort;//Excel表字段排序
	private User(String fieldName, String properties, int isNotNull, String validity, int sort) {
		this.fieldName = fieldName;
		this.properties = properties;
		this.isNotNull = isNotNull;
		this.validity = validity;
		this.sort = sort;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getProperties() {
		return properties;
	}
	public void setProperties(String properties) {
		this.properties = properties;
	}
	public int getIsNotNull() {
		return isNotNull;
	}
	public void setIsNotNull(int isNotNull) {
		this.isNotNull = isNotNull;
	}
	public String getValidity() {
		return validity;
	}
	public void setValidity(String validity) {
		this.validity = validity;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
}

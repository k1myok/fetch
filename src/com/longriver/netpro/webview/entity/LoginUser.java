package com.longriver.netpro.webview.entity;

import java.io.Serializable;

public class LoginUser implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private int index;
	private int id; //用户id
	private int fid;
	private int wpyid;//网评员id
	private String name; //用户名
	private String password;//登录密码
	private String nick; //昵称
	private String realName;///真实姓名
	private int sex;
	private String email;//邮箱
	private String tel;//电话
	private int deptid; //所属部门id
	private String deptName;//所属部门名称
	private String url;//用户URL
	private int platid;//用户所属平台id
	private String platStr;//平台名称
	//private int areaid;//用户所属区域id
	//private String areaStr;//区域名称
	private String descript;//备注
	//private int grade; //
	//private String gradeStr;//等级
	private int level; //系统用户级别， 是否是最高权限的管理员(1:是，0：否) 此字段的值是在程序中判断的
	private int roleid;
	private int isManager;
	private String roleName;
	private String showRoleName;
	private int nation;
	private String nationName;
	private String birthday;
	private String QQ;
	private String micro;
	private String brankName;
	private String brankAccount;
	private String job;
	private int degree;
	private String degreeValue;
	private int political;
	private String politicalValue;
	private String sexValue;
	private int userType;
	private String userTypeValue;
	private int countNum;
	private String openId;
	private String createTime;
	private int noticeType;
	private int setAdmin;
	private int flag;//区分组、部门
	
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public int getIsManager() {
		return isManager;
	}
	public void setIsManager(int isManager) {
		this.isManager = isManager;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getFid() {
		return fid;
	}
	public void setFid(int fid) {
		this.fid = fid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public int getDeptid() {
		return deptid;
	}
	public void setDeptid(int deptid) {
		this.deptid = deptid;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getPlatid() {
		return platid;
	}
	public void setPlatid(int platid) {
		this.platid = platid;
	}
	public String getDescript() {
		return descript;
	}
	public void setDescript(String descript) {
		this.descript = descript;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getPlatStr() {
		return platStr;
	}
	public void setPlatStr(String platStr) {
		this.platStr = platStr;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public int getWpyid() {
		return wpyid;
	}
	public void setWpyid(int wpyid) {
		this.wpyid = wpyid;
	}
	public int getRoleid() {
		return roleid;
	}
	public void setRoleid(int roleid) {
		this.roleid = roleid;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getShowRoleName() {
		return showRoleName;
	}
	public void setShowRoleName(String showRoleName) {
		this.showRoleName = showRoleName;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getNation() {
		return nation;
	}
	public void setNation(int nation) {
		this.nation = nation;
	}
	public String getNationName() {
		return nationName;
	}
	public void setNationName(String nationName) {
		this.nationName = nationName;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getQQ() {
		return QQ;
	}
	public void setQQ(String qq) {
		QQ = qq;
	}
	public String getMicro() {
		return micro;
	}
	public void setMicro(String micro) {
		this.micro = micro;
	}
	public String getBrankName() {
		return brankName;
	}
	public void setBrankName(String brankName) {
		this.brankName = brankName;
	}
	public String getBrankAccount() {
		return brankAccount;
	}
	public void setBrankAccount(String brankAccount) {
		this.brankAccount = brankAccount;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public int getDegree() {
		return degree;
	}
	public void setDegree(int degree) {
		this.degree = degree;
	}
	public String getDegreeValue() {
		return degreeValue;
	}
	public void setDegreeValue(String degreeValue) {
		this.degreeValue = degreeValue;
	}
	public int getPolitical() {
		return political;
	}
	public void setPolitical(int political) {
		this.political = political;
	}
	public String getPoliticalValue() {
		return politicalValue;
	}
	public void setPoliticalValue(String politicalValue) {
		this.politicalValue = politicalValue;
	}
	public String getSexValue() {
		return sexValue;
	}
	public void setSexValue(String sexValue) {
		this.sexValue = sexValue;
	}
	public int getUserType() {
		return userType;
	}
	public void setUserType(int userType) {
		this.userType = userType;
	}
	public String getUserTypeValue() {
		return userTypeValue;
	}
	public void setUserTypeValue(String userTypeValue) {
		this.userTypeValue = userTypeValue;
	}
	public int getCountNum() {
		return countNum;
	}
	public void setCountNum(int countNum) {
		this.countNum = countNum;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public int getNoticeType() {
		return noticeType;
	}
	public void setNoticeType(int noticeType) {
		this.noticeType = noticeType;
	}
	public int getSetAdmin() {
		return setAdmin;
	}
	public void setSetAdmin(int setAdmin) {
		this.setAdmin = setAdmin;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
}

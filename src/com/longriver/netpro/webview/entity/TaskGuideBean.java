package com.longriver.netpro.webview.entity;

import java.io.Serializable;
import java.util.List;

import com.longriver.netpro.util.JsonHelper;


public class TaskGuideBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String account;	//执行总数
	private String areaid;		//子任务id
	private String id;		//类别acid
	private String id2;		//类别id
	private String hostIp;		//类别id
	private String hostName;		//任务链接	
	private String hostPassword; 	//任务id
	private String hostPort;	//数据
	private List<TaskGuideBean> rlist; //返回数据结果
	private String nick;		//用户名称
	private String password;		//用户链接
	private String address;		//点赞数
	private String category;		//转发数
	private String corpus;		//评论数
	private String name;		//执行时间

	private String number;	//执行内容
	private String spaceTimeEnd;	//执行标题
	private String spaceTimeStart;
	private String startTime;
	private int commentOrPost;//是否转发 评论
	private String title; //论坛的标题
	private String ipname; //代理ip用户名
	private String ippwd; //代理ip密码
	private String mark; //服务器标示
	private String praiseWho;//点赞对象
	private String praiseContent;//赞的内容
	
	private String url;
	private int corpusid;//语料id
	private int gid;//语料id
	private int suc;//是否成功 1成功
	private String executeDate;
	private String executeTime;
	private int toMQ; //是否已经发送到mq
	private int grid; //guide_result 引导结果id
	private String bbsaddress;
	private String appInfo;//app需要的信息
	private int isApp; //是否是app引导 0电脑版 1app
	private String png;
	private int status2;
	private int code;
	private String design;//账号注册还是引导任务
	private String testAccount;//
	private String picData;//图片数据
	private String cookieData;
	private Integer taskTimes;//执行次数
	private int isreg;//注册或者找回
	private String remark2;//备用2
	private String remark3;//备用3
	private String remark4;//备用4
	private String an;

	public String getAn() {
		return an;
	}
	public void setAn(String an) {
		this.an = an;
	}
	public int getIsreg() {
		return isreg;
	}
	public void setIsreg(int isreg) {
		this.isreg = isreg;
	}
	public Integer getTaskTimes() {
		return taskTimes;
	}
	public void setTaskTimes(Integer taskTimes) {
		this.taskTimes = taskTimes;
	}
	public String getTestAccount() {
		return testAccount;
	}
	public void setTestAccount(String testAccount) {
		this.testAccount = testAccount;
	}
	public String getDesign() {
		return design;
	}
	public void setDesign(String design) {
		this.design = design;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public int getStatus2() {
		return status2;
	}
	public void setStatus2(int status2) {
		this.status2 = status2;
	}
	public String getPng() {
		return png;
	}
	public void setPng(String png) {
		this.png = png;
	}
	public int getIsApp() {
		return isApp;
	}
	public void setIsApp(int isApp) {
		this.isApp = isApp;
	}
	public String getAppInfo() {
		return appInfo;
	}
	public void setAppInfo(String appInfo) {
		this.appInfo = appInfo;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getCorpusid() {
		return corpusid;
	}
	public void setCorpusid(int corpusid) {
		this.corpusid = corpusid;
	}
	public int getGid() {
		return gid;
	}
	public void setGid(int gid) {
		this.gid = gid;
	}
	public int getSuc() {
		return suc;
	}
	public void setSuc(int suc) {
		this.suc = suc;
	}
	public String getExecuteDate() {
		return executeDate;
	}
	public void setExecuteDate(String executeDate) {
		this.executeDate = executeDate;
	}
	public String getExecuteTime() {
		return executeTime;
	}
	public void setExecuteTime(String executeTime) {
		this.executeTime = executeTime;
	}
	public int getToMQ() {
		return toMQ;
	}
	public void setToMQ(int toMQ) {
		this.toMQ = toMQ;
	}
	public int getGrid() {
		return grid;
	}
	public void setGrid(int grid) {
		this.grid = grid;
	}
	public String getBbsaddress() {
		return bbsaddress;
	}
	public void setBbsaddress(String bbsaddress) {
		this.bbsaddress = bbsaddress;
	}
	public String getPraiseWho() {
		return praiseWho;
	}
	public void setPraiseWho(String praiseWho) {
		this.praiseWho = praiseWho;
	}
	public String getPraiseContent() {
		return praiseContent;
	}
	public void setPraiseContent(String praiseContent) {
		this.praiseContent = praiseContent;
	}

	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public String getIpname() {
		return ipname;
	}



	public void setIpname(String ipname) {
		this.ipname = ipname;
	}



	public String getIppwd() {
		return ippwd;
	}



	public void setIppwd(String ippwd) {
		this.ippwd = ippwd;
	}



	public String getTitle() {
		return title;
	}



	public void setTitle(String title) {
		this.title = title;
	}



	public int getCommentOrPost() {
		return commentOrPost;
	}



	public void setCommentOrPost(int commentOrPost) {
		this.commentOrPost = commentOrPost;
	}



	private String type;

	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	public void setAccountInfo(String accountInfo) {
		if(accountInfo!=null && accountInfo.length()>10)
			rlist = JsonHelper.parseJson2GuideObject_2(accountInfo);
	}

	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	

	public List<TaskGuideBean> getRlist() {
		return rlist;
	}

	public void setRlist(List<TaskGuideBean> rlist) {
		this.rlist = rlist;
	}



	public String getAccount() {
		return account;
	}



	public void setAccount(String account) {
		this.account = account;
	}



	public String getAreaid() {
		return areaid;
	}



	public void setAreaid(String areaid) {
		this.areaid = areaid;
	}



	public String getHostIp() {
		return hostIp;
	}



	public void setHostIp(String hostIp) {
		this.hostIp = hostIp;
	}



	public String getHostName() {
		return hostName;
	}



	public void setHostName(String hostName) {
		this.hostName = hostName;
	}



	public String getHostPassword() {
		return hostPassword;
	}



	public void setHostPassword(String hostPassword) {
		this.hostPassword = hostPassword;
	}



	public String getHostPort() {
		return hostPort;
	}



	public void setHostPort(String hostPort) {
		this.hostPort = hostPort;
	}



	public String getNick() {
		return nick;
	}



	public void setNick(String nick) {
		this.nick = nick;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public String getAddress() {
		return address;
	}



	public void setAddress(String address) {
		this.address = address;
	}



	public String getCategory() {
		return category;
	}



	public void setCategory(String category) {
		this.category = category;
	}



	public String getCorpus() {
		return corpus;
	}



	public void setCorpus(String corpus) {
		this.corpus = corpus;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getNumber() {
		return number;
	}



	public void setNumber(String number) {
		this.number = number;
	}



	public String getSpaceTimeEnd() {
		return spaceTimeEnd;
	}



	public void setSpaceTimeEnd(String spaceTimeEnd) {
		this.spaceTimeEnd = spaceTimeEnd;
	}



	public String getSpaceTimeStart() {
		return spaceTimeStart;
	}



	public void setSpaceTimeStart(String spaceTimeStart) {
		this.spaceTimeStart = spaceTimeStart;
	}



	public String getStartTime() {
		return startTime;
	}



	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getId2() {
		return id2;
	}
	public void setId2(String id2) {
		this.id2 = id2;
	}
	public String getPicData() {
		return picData;
	}
	public void setPicData(String picData) {
		this.picData = picData;
	}
	
	public String getCookieData() {
		return cookieData;
	}
	public void setCookieData(String cookieData) {
		this.cookieData = cookieData;
	}
	public String getRemark2() {
		return remark2;
	}
	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}
	public String getRemark3() {
		return remark3;
	}
	public void setRemark3(String remark3) {
		this.remark3 = remark3;
	}
	public String getRemark4() {
		return remark4;
	}
	public void setRemark4(String remark4) {
		this.remark4 = remark4;
	}
	

	
}

package com.longriver.netpro.webview.entity;

import java.io.Serializable;
import java.util.List;

import com.longriver.netpro.util.JsonHelper;

public class FetchTaskRiceverBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String totalRecords;	//执行总数
	private String id;		//子任务id
	private String cid;		//类别id
	private String ifid;		//类别id
	private String url;		//任务链接	
	private String taskId; 	//任务id
	private String items;	//数据
	private List<FetchTaskRiceverBean> rlist; //返回数据结果
	private String an;		//用户名称
	private String au;		//用户链接
	private String ac;		//点赞数
	private String fds;		//转发数
	private String cts;		//评论数
	private String pt;		//执行时间
	private String content;	//执行内容
	private String title;	//执行标题
	private String crontab;
	private String type;
	private String taskid;
	private String count;//采集页数
	private String mark; //服务器标示
	private String design;//采集返回到哪个mq结果上
	private String dbdesign;//采集返回到哪个mq结果上
	
	private String uname; //采集用的用户名密码
	private String upwd;
	private String temp;	//备用
	private String temp2;
	
	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getUpwd() {
		return upwd;
	}

	public void setUpwd(String upwd) {
		this.upwd = upwd;
	}

	public String getTemp() {
		return temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

	public String getTemp2() {
		return temp2;
	}

	public void setTemp2(String temp2) {
		this.temp2 = temp2;
	}

	public String getDesign() {
		return design;
	}

	public void setDesign(String design) {
		this.design = design;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getCrontab() {
		return crontab;
	}

	public void setCrontab(String crontab) {
		this.crontab = crontab;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public void setItems(String items) {
		if(items!=null && items.length()>10)
			rlist = JsonHelper.parseJson2Object_2(items);
	}

	public String getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(String totalRecords) {
		this.totalRecords = totalRecords;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public List<FetchTaskRiceverBean> getRlist() {
		return rlist;
	}

	public void setRlist(List<FetchTaskRiceverBean> rlist) {
		this.rlist = rlist;
	}

	public String getAn() {
		return an;
	}

	public void setAn(String an) {
		this.an = an;
	}

	public String getAu() {
		return au;
	}

	public void setAu(String au) {
		this.au = au;
	}

	public String getAc() {
		if(ac==null) return "0";
		else return ac;
	}

	public void setAc(String ac) {
		this.ac = ac;
	}

	public String getFds() {
		if(fds==null) return "0";
		else return fds;
	}

	public void setFds(String fds) {
		this.fds = fds;
	}

	public String getCts() {
		if(cts==null) return "0";
		else return cts;
	}

	public void setCts(String cts) {
		this.cts = cts;
	}

	public String getPt() {
		return pt;
	}

	public void setPt(String pt) {
		this.pt = pt;
	}

	public String getContent() {
		
//		if(content!=null){
//			String ct = content.replaceAll("'", "");
//			ct = ct.replaceAll("\\<(.*?)\\>", "").replaceAll("\\\\", "");
//			return ct;
//		}else{
//			return "";
//		}
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		if(title != null)
			return title.replaceAll("'", "");
		else
			return "";
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getData() {
		return items;
	}

	public String getIfid() {
		if(ifid==null) return "1";
		else return ifid;
	}

	public void setIfid(String ifid) {
		this.ifid = ifid;
	}

	public String getDbdesign() {
		return dbdesign;
	}

	public void setDbdesign(String dbdesign) {
		this.dbdesign = dbdesign;
	}
}

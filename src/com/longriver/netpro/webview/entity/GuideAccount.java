package com.longriver.netpro.webview.entity;

import java.io.Serializable;

import com.longriver.netpro.util.DateUtil;

public class GuideAccount implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String content;
	private String nick;	//返回的登录名
	private String an;		//用户名称
	private String au;		//用户密码
	@SuppressWarnings("unused")
	private String execTime;		//用户密码
	private String mark;
	private String funs;//粉丝数
	private String url;
	private String png;
	private String code;
	private String design;
	private String testAccount;
	private String cookieData;//发帖cookie信息
	private int isreg;//注册或者找回
	
	public int getIsreg() {
		return isreg;
	}
	public void setIsreg(int isreg) {
		this.isreg = isreg;
	}
	public String getCookieData() {
		return cookieData;
	}
	public void setCookieData(String cookieData) {
		this.cookieData = cookieData;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getPng() {
		return png;
	}
	public void setPng(String png) {
		this.png = png;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getFuns() {
		return funs;
	}
	public void setFuns(String funs) {
		this.funs = funs;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public String getExecTime() {
		return DateUtil.getCurrentTime();
	}
	public void setExecTime(String execTime) {
		this.execTime = execTime;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
}
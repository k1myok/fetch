package com.longriver.netpro.common.spcard;

import java.io.Serializable;
import java.util.Date;

/**
 * 短信消息
 * @author rhy
 * @date 2018-3-16 下午4:18:10
 * @version V1.0
 */
public class Message implements Serializable{

	private static final long serialVersionUID = 1L;
	private int id;
	private int portNum;
	private Date smsDate;
	private String smsContent;
	private int isRead;
	
	public Message() {
		super();
		
	}
	public Message(int id, int portNum, Date smsDate, String smsContent,
			int isRead) {
		super();
		this.id = id;
		this.portNum = portNum;
		this.smsDate = smsDate;
		this.smsContent = smsContent;
		this.isRead = isRead;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPortNum() {
		return portNum;
	}
	public void setPortNum(int portNum) {
		this.portNum = portNum;
	}
	public Date getSmsDate() {
		return smsDate;
	}
	public void setSmsDate(Date smsDate) {
		this.smsDate = smsDate;
	}
	public String getSmsContent() {
		return smsContent;
	}
	public void setSmsContent(String smsContent) {
		this.smsContent = smsContent;
	}
	public int getIsRead() {
		return isRead;
	}
	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}
	

}

package com.longriver.netpro.common.spcard;

import java.io.Serializable;
import java.util.List;

/**
 * 分页
 * @author rhy
 * @date 2018-3-21 上午9:42:43
 * @version V1.0
 */
public class Page implements Serializable{

	private static final long serialVersionUID = 1L;
	private int currentPage;//当前页
	private int pageSize;//每页条数
	private int pageTotal;//总页数
	private List<Message> list;//数据
	
	
	public Page() {
		super();
		
	}


	public Page(int currentPage, int pageSize, int pageTotal, List<Message> list) {
		super();
		this.currentPage = currentPage;
		this.pageSize = pageSize;
		this.pageTotal = pageTotal;
		this.list = list;
	}


	public int getCurrentPage() {
		return currentPage;
	}


	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}


	public int getPageSize() {
		return pageSize;
	}


	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}


	public int getPageTotal() {
		return pageTotal;
	}


	public void setPageTotal(int pageTotal) {
		this.pageTotal = pageTotal;
	}


	public List<Message> getList() {
		return list;
	}


	public void setList(List<Message> list) {
		this.list = list;
	}
	
	
}

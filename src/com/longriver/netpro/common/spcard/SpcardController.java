package com.longriver.netpro.common.spcard;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.longriver.netpro.common.asdl.ConnectNetWork;
import com.longriver.netpro.fetchScript.util.Jdbc2MysqlSpcard;
import com.longriver.netpro.util.GetProprities;

/**
 * 获取短信消息
 * @author rhy
 * @date 2018-3-16 下午4:15:03
 * @version V1.0
 */
@Controller
@RequestMapping("spcard")
public class SpcardController {

	@RequestMapping(value="getMessage")
	public void getMessage(HttpServletRequest request,HttpServletResponse response,Model model){
		
		try {
			
			String currentPage = request.getParameter("currentPage");
			
			if(StringUtils.isBlank(currentPage)){
				
				currentPage = "1";
			}
			int pageSize = 10;
			
			List<Message> list = Jdbc2MysqlSpcard.getResultAll(Integer.parseInt(currentPage),pageSize);
			int pageTotal = Jdbc2MysqlSpcard.getTotal();
			pageTotal = (int) Math.ceil((float)pageTotal/pageSize);
			
			Page page = new Page(Integer.parseInt(currentPage),pageSize,pageTotal,list);
			request.setAttribute("page", page);
			request.getRequestDispatcher("/source/index.jsp").forward(request, response);
//			return "index.jsp";
//			String string = JSON.toJSONString(message);
//			response.setContentType("text/json;charset=utf-8");
//			PrintWriter writer = response.getWriter();
//			writer.println(string);
//			writer.flush();
			
//			model.addAttribute("message", message);
			
//			return "forward:/index.jsp";
		} catch (Exception e) {
			e.printStackTrace();
		}
//		return null;
	}
	
	@RequestMapping(value="sendMessage")
	public void sendMessage(HttpServletRequest request,HttpServletResponse response){
		
		try {
		String portNum = request.getParameter("portNum");
		String destination = request.getParameter("destination");
		String content = request.getParameter("content");
		
		int sendMsg = Jdbc2MysqlSpcard.sendMsg(content, destination, Integer.parseInt(portNum));
		
		Map<String,String> map = new HashMap<String,String>();
		
		if(sendMsg > 0){
			map.put("msg", "恭喜你短信发送成功~~~");
		}else{
			map.put("msg", "短信发送失败^^^");
		}
		response.setContentType("text/json;charset=utf-8");
		PrintWriter writer = response.getWriter();
		writer.print(JSON.toJSONString(map));
		writer.flush();
		
		}catch (Exception e) {
			e.printStackTrace();
		}
//		return "redirect:index.jsp";
	}
	
	@RequestMapping(value="switchCard")
	public void switchCard(HttpServletRequest request,HttpServletResponse response){
		
		try {
			
			String portNum = request.getParameter("portNum");
			int switchCard = Jdbc2MysqlSpcard.switchCard("AP$SIM=0,"+portNum);
			
			PrintWriter writer = response.getWriter();
			if(switchCard>0){
				writer.print("换卡成功~~~");
			}else{
				writer.print("换卡失败^^^");
			}
			writer.flush();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@RequestMapping(value="disconnect")
	public void disconnect(HttpServletRequest request,HttpServletResponse response){
		
		try {
			boolean has = ConnectNetWork.cutAdsl(GetProprities.AsdlConfig.getProperty("asdlname"));
			
			PrintWriter writer = response.getWriter();
//			if(has){
				
				writer.print("断开成功");
//			}else{
				
//				writer.print("断开失败");
//			}
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value="again")
	public void again(HttpServletRequest request,HttpServletResponse response){
		
		try {
			boolean cnt = ConnectNetWork.connAdsl(GetProprities.AsdlConfig.getProperty("asdlname"),GetProprities.AsdlConfig.getProperty("asdluser"),GetProprities.AsdlConfig.getProperty("asdlpwd"));
		
			PrintWriter writer = response.getWriter();
			if(cnt){
				
				writer.print("重连成功");
			}else{
				
				writer.print("重连失败");
			}
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@RequestMapping(value="updateMessage")
	public void updateMessage(HttpServletRequest request,HttpServletResponse response){
		
		String ids = request.getParameter("id");
		String isReaded = request.getParameter("isReaded");
		
		int id = Integer.parseInt(ids);
		int isRead = Integer.parseInt(isReaded);
		
		try {
			Jdbc2MysqlSpcard.updateMessageById(id,isRead);
			
		} catch (SQLException e) {
		}
	}
	
}

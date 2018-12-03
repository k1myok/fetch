package com.longriver.netpro.util;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
public class TaskConfig {

	/// <summary>
    /// 任务运行名称
    /// </summary>
    public String TaskName = "";

    /// <summary>
    /// 任务脚本文件，不得为空
    /// </summary>
    public String TaskFile = "";

    /// <summary>
    /// 开发版SDK的Key
    /// </summary>
    public String SKey = "";

    /// <summary>
    /// 缓存文件目录,不指定系统自动创建
    /// </summary>
    public String CacheFiles = "";

    /// <summary>
    /// 浏览器窗口显示模式，0:默认大小,1:最小化,2:最大化
    /// </summary>
    public int WindowState = 0;

    /// <summary>
    /// 日志文件目录，空为不写日志
    /// </summary>
    public String LogFilePath = "";

    /// <summary>
    /// 返回的所有变量值保存的文件,为空不返回值
    /// </summary>
    public String ResultFile = "";

    /// <summary>
    /// 脚本运行完成后界面还显示多长时间，方便查看
    /// </summary>
    public int ShowTime = 0;

    /// <summary>
    /// 所有变量的键值对
    /// </summary>
    public Map<String, String> Varlist1 = new HashMap<String, String>();

    /// <summary>
    /// 将配置文件生成xml
    /// </summary>
    /// <returns></returns>
    
    public   String   ToArgString()throws ParserConfigurationException, TransformerConfigurationException, TransformerException, TransformerFactoryConfigurationError, UnsupportedEncodingException{  
		String   xml   =  "";  
		/**   建立document对象   */  
		Document   document   =DocumentHelper.createDocument();  
		/**   建立XML文档的根books   */  
		Element   root   =   document.addElement("root");  
		/**   加入一行注释   */  
		root.addAttribute("TaskName", this.TaskName);
        root.addAttribute("TaskFile", this.TaskFile);
        root.addAttribute("SKey", this.SKey);
        root.addAttribute("WindowState", this.WindowState+"");
        if (LogFilePath!="") root.addAttribute("LogFilePath", LogFilePath);
        if (ResultFile!="") root.addAttribute("ResultFile", ResultFile);
        root.addAttribute("ShowTime", ShowTime+"");
		/**   加入第一个book节点   */  
		Element   varList   =   root.addElement("Varlist");  

        for (Entry<String, String> entry: Varlist1.entrySet()) {

          String key = entry.getKey();
          String value = entry.getValue();
          Element child= varList.addElement(key);
          child.setText(value);
        }
		
		try{  
		/** 设置输出的编码UTF-8 */
		OutputFormat format = OutputFormat.createPrettyPrint(); 
		format.setEncoding("utf-8");
		StringWriter writer = new StringWriter();
        XMLWriter output = new XMLWriter(writer, format);
        output.write(document);
        writer.close();
        output.close();
        System.out.println(writer.toString());
		 xml = writer.toString();
		}catch(Exception   ex){  
		ex.printStackTrace();  
		}  
		return   java.net.URLEncoder.encode(xml,"utf-8");  
		}  
    
}

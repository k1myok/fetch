package com.longriver.netpro.util;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

public class Jdbc2Mysql {
	public static void updateAccountInfo(List<Map> list) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        // 下面语句之前就要先创建javademo数据库118.190.172.62
//        String url = "jdbc:mysql://localhost:3306/network?user=root&password=chwx62115358&useUnicode=true&characterEncoding=UTF8";
        String url = "jdbc:mysql://118.190.172.62:3306/network?user=fjx&password=fengjinxin&useUnicode=true&characterEncoding=UTF8";
        try {
            Class.forName("com.mysql.jdbc.Driver");// 动态加载mysql驱动
            conn = DriverManager.getConnection(url);
            String sql = "UPDATE maintain_account SET account=?,url=? WHERE nick=? AND `password`=? AND platid=1";
            pstmt = conn.prepareStatement(sql);
            pstmt.clearBatch();
            conn.setAutoCommit(false);  
            for(int i=0;i<list.size();i++){
            	if(list.get(i).get("nick")==null||list.get(i).get("uid")==null){
            		continue;
            	}
            	pstmt.setString(1, ((String)list.get(i).get("nick")).trim());
            	pstmt.setString(2, ((String)list.get(i).get("uid")).trim());
            	pstmt.setString(3, (String)list.get(i).get("userId"));
            	pstmt.setString(4, (String)list.get(i).get("pwd"));
            	System.out.println((String)list.get(i).get("nick")+"------"+(String)list.get(i).get("uid"));
            	pstmt.addBatch();
            }
            pstmt.executeBatch();
            conn.commit();
            
        } catch (SQLException e) {
            System.out.println("MySQL操作错误");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	pstmt.close();
            conn.close();
        }
 
    }
}

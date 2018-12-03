package com.longriver.netpro.common.activemq;

import java.util.ArrayList;
import java.util.List;

import com.longriver.netpro.util.JsonHelper;
import com.longriver.netpro.webview.entity.TaskGuideBean;

/**
 * @author liyunlong
 *
 */
public class sendweibocomment {
    static String sendType = "1";// 发送的类型 1发布一对一 ；2订阅一对多
    String isNotFile = "false";// 是否有附件true有 false没有
    static String ip = "218.64.13.147";// 接收ip
    static String modeName = "test3";// 模式名称

    public static void main(String[] args) {
        
//        sendsinaresport("weibo.sina.repost", "https://weibo.com/1907518591/Gh4PDDj1a?from=page_1005051907518591_profile&wvr=6&mod=weibotime&type=comment", "15002237262", "fenglong19940117");
        sendsinastatus("weibo.sina.status","15002237262", "fenglong19940117","肚子饿了想吃饭");
        
    }
//    public void send(missiondetail missiondetail) {
//        // TODO Auto-generated method stub 
//        TaskGuideBean task = new TaskGuideBean();
//        if(missiondetail.getPlatformcode().equals("weibo.sina.comment1"))//评论并转发
//        {
//            task.setType("weibo.sina.comment");
//            task.setAddress(missiondetail.getTargetaddress());
//            task.setCommentOrPost(1);
//            task.setPraiseWho(missiondetail.getCommentid());
//        }
//        else
//        {
//        task.setType(missiondetail.getPlatformcode());
//        task.setAddress(missiondetail.getTargetaddress());
//        task.setCommentOrPost(0);
//        task.setPraiseWho(missiondetail.getCommentid());
//        }
//        TaskGuideBean taskdetail = new TaskGuideBean();
//        List<TaskGuideBean> list = new ArrayList<TaskGuideBean>();
//        taskdetail.setNick(missiondetail.getAccountname());
//        taskdetail.setPassword(missiondetail.getAccountpassword());
//        taskdetail.setCorpus(missiondetail.getCorpus());
//        list.add(taskdetail);
//        task.setRlist(list);
//        String json = JsonHelper.Object2Json(task);// 要发送的json数据
//        new SendMessageByMq().send(sendType, ip, modeName,
//                json, null);
//    }
    
    

    
    /**新浪评论
     * author:liyunlong
     * date  :2018-5-17
     * @param type
     * @param address
     * @param nick
     * @param password
     * @param corpus
     */
    public static void sendsinacomment(String type, String address,
            String nick, String password, String corpus) {
        TaskGuideBean task = new TaskGuideBean();
        task.setType("weibo.sina.comment");
        task.setAddress(address);
        task.setCommentOrPost(0);
        TaskGuideBean taskdetail = new TaskGuideBean();
        List<TaskGuideBean> list = new ArrayList<TaskGuideBean>();
        taskdetail.setNick(nick);
        taskdetail.setPassword(password);
        taskdetail.setCorpus(corpus);
        list.add(taskdetail);
        task.setRlist(list);
        String json = JsonHelper.Object2Json(task);// 要发送的json数据
        // 发送方法
        String result = new SendMessageByMq().send(sendType, ip, modeName,
                json, null);
    }

    /**新浪评论+转发
     * author:liyunlong
     * date  :2018-5-17
     * @param type
     * @param address
     * @param nick
     * @param password
     * @param corpus
     */
    public static void sendsinacomment2(String type, String address,
            String nick, String password, String corpus) {
        TaskGuideBean task = new TaskGuideBean();
        task.setType("weibo.sina.comment");
        task.setAddress(address);
        task.setCommentOrPost(1);
        TaskGuideBean taskdetail = new TaskGuideBean();
        List<TaskGuideBean> list = new ArrayList<TaskGuideBean>();
        taskdetail.setNick(nick);
        taskdetail.setPassword(password);
        taskdetail.setCorpus(corpus);
        list.add(taskdetail);
        task.setRlist(list);
        String json = JsonHelper.Object2Json(task);// 要发送的json数据
        // 发送方法
        String result = new SendMessageByMq().send(sendType, ip, modeName,
                json, null);
    }
    
    /**新浪转发
     * author:liyunlong
     * date  :2018-5-17
     * @param type
     * @param address
     * @param nick
     * @param password
     * @param corpus
     */
    public static void sendsinaresport(String type, String address,
            String nick, String password) {
        TaskGuideBean task = new TaskGuideBean();
        task.setType("weibo.sina.repost");
        task.setAddress(address);
        task.setCommentOrPost(1);
        TaskGuideBean taskdetail = new TaskGuideBean();
        List<TaskGuideBean> list = new ArrayList<TaskGuideBean>();
        taskdetail.setNick(nick);
        taskdetail.setPassword(password);
        list.add(taskdetail);
        task.setRlist(list);
        String json = JsonHelper.Object2Json(task);// 要发送的json数据
        // 发送方法
        String result = new SendMessageByMq().send(sendType, ip, modeName,
                json, null);
    }
    
    /**新浪评论点赞
     * author:liyunlong
     * date  :2018-5-17
     * @param type
     * @param address
     * @param nick
     * @param password
     * @param corpus
     */
    public static void sendsinapraise(String type, String address,
            String nick, String password) {
        TaskGuideBean task = new TaskGuideBean();
        task.setType("weibo.sina.repost");
        task.setAddress(address);
        task.setCommentOrPost(1);
        TaskGuideBean taskdetail = new TaskGuideBean();
        List<TaskGuideBean> list = new ArrayList<TaskGuideBean>();
        taskdetail.setNick(nick);
        taskdetail.setPassword(password);
        list.add(taskdetail);
        task.setRlist(list);
        String json = JsonHelper.Object2Json(task);// 要发送的json数据
        // 发送方法
        String result = new SendMessageByMq().send(sendType, ip, modeName,
                json, null);
    }
    
    /**新浪微博直发
     * author:liyunlong
     * date  :2018-5-18
     * @param type
     * @param nick
     * @param password
     * @param corpus
     */
    public static void sendsinastatus(String type,
            String nick, String password, String corpus) {
        TaskGuideBean task = new TaskGuideBean();
        task.setType("weibo.sina.status");
        task.setStatus2(0);
        TaskGuideBean taskdetail = new TaskGuideBean();
        List<TaskGuideBean> list = new ArrayList<TaskGuideBean>();
        taskdetail.setNick(nick);
        taskdetail.setPassword(password);
        taskdetail.setCorpus(corpus);
        list.add(taskdetail);
        task.setRlist(list);
        String json = JsonHelper.Object2Json(task);// 要发送的json数据
        // 发送方法
        String result = new SendMessageByMq().send(sendType, ip, modeName,
                json, null);
    }

   

}

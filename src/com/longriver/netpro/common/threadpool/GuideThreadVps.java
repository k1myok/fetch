package com.longriver.netpro.common.threadpool;

import java.util.logging.Logger;

import com.longriver.netpro.common.asdl.ConnectNetWork;
import com.longriver.netpro.util.GetProprities;
import com.longriver.netpro.webview.carcontroller.CarXgoComment;
import com.longriver.netpro.webview.carcontroller.CheShiComment;
import com.longriver.netpro.webview.carcontroller.CheXiaofeiComment;
import com.longriver.netpro.webview.carcontroller.CheZhuComment;
import com.longriver.netpro.webview.carcontroller.EastMoneyComment;
import com.longriver.netpro.webview.carcontroller.EmaoComment;
import com.longriver.netpro.webview.carcontroller.ICarComment;
import com.longriver.netpro.webview.carcontroller.JRJComment;
import com.longriver.netpro.webview.carcontroller.JieMianComment;
import com.longriver.netpro.webview.carcontroller.PacificCarComment;
import com.longriver.netpro.webview.carcontroller.QCTTComment;
import com.longriver.netpro.webview.carcontroller.XinchepingComment;
import com.longriver.netpro.webview.carcontroller.YiCheComment;
import com.longriver.netpro.webview.controller.BaiDuComment;
import com.longriver.netpro.webview.controller.CarQCDPComment;
import com.longriver.netpro.webview.controller.CarQCZJComment;
import com.longriver.netpro.webview.controller.CarXinHuaComment;
import com.longriver.netpro.webview.controller.DianpingComment2;
import com.longriver.netpro.webview.controller.DianpingRegister;
import com.longriver.netpro.webview.controller.DoubanComment;
import com.longriver.netpro.webview.controller.DoubanRegister;
import com.longriver.netpro.webview.controller.IFNewsComment;
import com.longriver.netpro.webview.controller.IFengCommentApp;
import com.longriver.netpro.webview.controller.IFengNewsSupport;
import com.longriver.netpro.webview.controller.IFengVideoComment;
import com.longriver.netpro.webview.controller.IfengCommentJietu;
import com.longriver.netpro.webview.controller.JinlvComment;
import com.longriver.netpro.webview.controller.MafengwoComment;
import com.longriver.netpro.webview.controller.MaoyanComment;
import com.longriver.netpro.webview.controller.MaoyanRegister;
import com.longriver.netpro.webview.controller.MtimeComment;
import com.longriver.netpro.webview.controller.PepoleDigg;
import com.longriver.netpro.webview.controller.QQCommentJietu;
import com.longriver.netpro.webview.controller.QQNewsComment;
import com.longriver.netpro.webview.controller.QQVideoComment;
import com.longriver.netpro.webview.controller.SinaCommentJietu;
import com.longriver.netpro.webview.controller.SinaNewsComment;
import com.longriver.netpro.webview.controller.SinaNewsCommentApp;
import com.longriver.netpro.webview.controller.SinaNewsSupport;
import com.longriver.netpro.webview.controller.SinaNewsSupportApp;
import com.longriver.netpro.webview.controller.SinaVideoCommentJietu;
import com.longriver.netpro.webview.controller.SohuCommentApp;
import com.longriver.netpro.webview.controller.SohuCommentJietu;
import com.longriver.netpro.webview.controller.SohuCommentVps;
import com.longriver.netpro.webview.controller.SohuCommentVps2;
import com.longriver.netpro.webview.controller.SohuPriseApp;
import com.longriver.netpro.webview.controller.SohuVideoComment;
import com.longriver.netpro.webview.controller.TengxunCommentApp;
import com.longriver.netpro.webview.controller.ThepaperComment;
import com.longriver.netpro.webview.controller.ThepaperDigg;
import com.longriver.netpro.webview.controller.TianyaComment;
import com.longriver.netpro.webview.controller.TouTiaoComment;
import com.longriver.netpro.webview.controller.TrChinanewsComment;
import com.longriver.netpro.webview.controller.TrHuanQiuComment;
import com.longriver.netpro.webview.controller.TrPepoleComment;
import com.longriver.netpro.webview.controller.WYSupportVps;
import com.longriver.netpro.webview.controller.WangyiComment2;
import com.longriver.netpro.webview.controller.WeiboAccountRevise;
import com.longriver.netpro.webview.controller.WeiboAddFen;
import com.longriver.netpro.webview.controller.WeiboAddPhoto;
import com.longriver.netpro.webview.controller.WeiboComment;
import com.longriver.netpro.webview.controller.WeiboCommentJietu;
import com.longriver.netpro.webview.controller.WeiboCommentPraise;
import com.longriver.netpro.webview.controller.WeiboHotSearch2;
import com.longriver.netpro.webview.controller.WeiboJuBao;
import com.longriver.netpro.webview.controller.WeiboLoginYanghao;
import com.longriver.netpro.webview.controller.WeiboPost;
import com.longriver.netpro.webview.controller.WeiboPostJietu;
import com.longriver.netpro.webview.controller.WeiboPraise;
import com.longriver.netpro.webview.controller.WeiboPublish;
import com.longriver.netpro.webview.controller.WeiboPublishJietu;
import com.longriver.netpro.webview.controller.WeiboRegister;
import com.longriver.netpro.webview.controller.WeiboSecondComment;
import com.longriver.netpro.webview.controller.WeiboWeiHuaTi;
import com.longriver.netpro.webview.controller.WyCommentApp;
import com.longriver.netpro.webview.controller.WyPraiseApp;
import com.longriver.netpro.webview.controller.WyRegister;
import com.longriver.netpro.webview.controller.WyVideoComment;
import com.longriver.netpro.webview.controller.XiciComment;
import com.longriver.netpro.webview.controller.XiciPost;
import com.longriver.netpro.webview.controller.XinHuaComment;
import com.longriver.netpro.webview.controller.XinHuaDigg;
import com.longriver.netpro.webview.controller.YDZXComment;
import com.longriver.netpro.webview.controller.YDZXCommentApp;
import com.longriver.netpro.webview.controller.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import com.longriver.netpro.common.asdl.ConnectNetWork;
import com.longriver.netpro.util.GetProprities;
import com.longriver.netpro.webview.controller.*;
import com.longriver.netpro.webview.cookieController.WeiboCommentCookie;
import com.longriver.netpro.webview.cookieController.WeiboPostCookie;
import com.longriver.netpro.webview.cookieController.WeiboPraiseCookie;
import com.longriver.netpro.webview.cookieController.WeiboPublishCookie;
import com.longriver.netpro.webview.entity.TaskGuideBean;
public class GuideThreadVps{
	private Logger logger = Logger.getLogger(GuideThreadVps.class.getName());
	private TaskGuideBean taskdo;
	private TaskGuideBean jh1; //外层
	
	public static void main(String args[]){

		TaskGuideBean task = new TaskGuideBean();
		Integer taskTimes = task.getTaskTimes();
		System.out.println(taskTimes == 0);
		System.out.println(task.getCookieData());
		System.out.println(StringUtils.isNotBlank(task.getCookieData()));
	}
	
	public GuideThreadVps(TaskGuideBean t,TaskGuideBean tt){
		taskdo = t;
		jh1 = tt;
	}
    public String run() {
    	
    		System.out.println("runTime执行...");
        	try {
        		qieIP();
        		System.out.println("start...");
        		guideDo(taskdo,jh1);
        		System.out.println("end...");
        	} catch (Exception e) {
        		e.printStackTrace();
        		System.out.println("session close...");
        	}
        	return "";
    }
    //jh1外层
    public void guideDo(TaskGuideBean taskdo,TaskGuideBean jh1) throws Exception{
    	//把配置文件里面的key,value转化成 map
		logger.info(jh1.getType()+":::");
		taskdo.setAn(taskdo.getId());
		taskdo.setAddress(jh1.getAddress());
		taskdo.setId2(jh1.getId());
		taskdo.setCommentOrPost(jh1.getCommentOrPost());
		taskdo.setNumber(jh1.getNumber());
		taskdo.setPraiseWho(jh1.getPraiseWho());
		taskdo.setPraiseContent(jh1.getPraiseContent());
		taskdo.setNumber(jh1.getNumber());
		taskdo.setIsApp(jh1.getIsApp());
		taskdo.setStatus2(jh1.getStatus2());
		taskdo.setCorpus(taskdo.getCorpus());
		taskdo.setIsApp(jh1.getIsApp());
		taskdo.setStatus2(jh1.getStatus2());
		taskdo.setCorpus(taskdo.getCorpus());
		taskdo.setDesign(jh1.getDesign());
		taskdo.setPicData(jh1.getPicData());
		
		if(jh1.getType().equals("weibo.sina.comment")){ // 新浪微博评论/同时转发
			if(taskdo.getAddress().indexOf("@")>0){
				WeiboSecondComment.toComment(taskdo);
			}else{
				if(jh1.getStatus2()==1){
					WeiboCommentJietu.toComment(taskdo);
				}else{
					WeiboComment.sina(taskdo);
				}
				WeiboComment.sina(taskdo);
//				if(jh1.getStatus2()==1){
//					WeiboPostJietu.toComment(taskdo);
//				}else{
//				}
			}
		}else if(jh1.getType().equals("weibo.sina.repost")){ //转发
			if(jh1.getStatus2()==1){
				WeiboPostJietu.toComment(taskdo);
			}else{
				WeiboPost.sina(taskdo);
			}
		}else if(jh1.getType().equals("weibo.sina.status")){ //新浪微博直发
			System.out.println("weibo.sina.status-----zhifa");
			if(jh1.getStatus2()==1){//调用截图的脚本
				System.out.println("截图脚本");
				WeiboPublishJietu.toComment(taskdo);
			}else{
				WeiboPublish.sina(taskdo);
			}
		}else if(jh1.getType().equals("weibo.sina.praise")){//微博评论赞
			WeiboCommentPraise.sina(taskdo);
		}else if(jh1.getType().equals("weibo.sina.p.praise")){ //微博赞
			WeiboPraise.sina(taskdo);
		}else if(jh1.getType().equals("weibo.sina.addFen")){ //微博加粉
			WeiboAddFen.sina(taskdo);
				WeiboPraise.sina(taskdo);
		}else if(jh1.getType().equals("weibo.sina.addFen")){ //微博加粉
			WeiboAddFen.sina(taskdo);
		}else if(jh1.getType().equals("weibo.huati")){ //微话题
			WeiboWeiHuaTi.toComment(taskdo);
		}else if(jh1.getType().equals("weibo.hotSearch")){ //微博热搜
			WeiboHotSearch2.toComment(taskdo);
		}else if(jh1.getType().equals("weibo.jubao.support")){ //新浪微博举报
			WeiboJuBao.weiboReport(taskdo);
		}else if(jh1.getType().equals("weibo.sina.register")){ //微博注册
			WeiboRegister.torun(taskdo);
		}else if(jh1.getType().equals("weibo.sina.back")){ //微博找回
			WeiboAccountRevise.toComment(taskdo);
		}else if(jh1.getType().equals("weibo.huati")){ //微话题
			WeiboWeiHuaTi.toComment(taskdo);
		}else if(jh1.getType().equals("weibo.sina.login")){ //微博登录养号
			WeiboLoginYanghao.toComment(taskdo);
		}else if(jh1.getType().equals("weibo.sina.touxiang")){ //微博添加头像
			WeiboAddPhoto.toComment(taskdo);
			
		}else if(jh1.getType().equals("news.sina.praise")){//新浪新闻点赞
			if(jh1.getIsApp()!=0) SinaNewsSupportApp.toRun(taskdo); //app
			else SinaNewsSupport.sina(taskdo);
		}else if(jh1.getType().equals("news.sina.comment")){ //新浪新闻评论
			if(jh1.getIsApp()!=0){
				SinaNewsCommentApp.toRun(taskdo);
			}else if(taskdo.getAddress().contains("video.sina.com")){//调用视频新闻
				SinaVideoCommentJietu.toComment(taskdo);
			}else if(jh1.getStatus2()==1){//调用截图的脚本
				System.out.println("截图脚本");
				SinaCommentJietu.toComment(taskdo);
			}else{
				SinaCommentJietu.toComment(taskdo);
				//SinaNewsComment.sina(taskdo); 昵称乱码先注销
			}
		
		}else if(jh1.getType().equals("163.reply.status")){ //网易新闻评论
			if(jh1.getIsApp()!=0){
				WyCommentApp.toRun(taskdo);
			}else if(taskdo.getAddress().contains("v.163.com")){
				WyVideoComment.toComment(taskdo);
			}else{
				WangyiComment2.toComment(taskdo);
			}
		}else if(jh1.getType().equals("news.163.support")){ //网易新闻点赞
			if(jh1.getIsApp()!=0){
				WyPraiseApp.toRun(taskdo);
			}else{
				WYSupportVps.wangyi(taskdo);
			}
		}else if(jh1.getType().equals("wangyi.register")){
			WyRegister.torun(taskdo);
			
		}else if(jh1.getType().equals("news.sohu.comment")){ //搜狐评论
			if(jh1.getIsApp()!=0){
				SohuCommentApp.toRun(taskdo);
			}else if(taskdo.getAddress().contains("tv.sohu.com")){
				SohuVideoComment.toComment(taskdo);
			}else if(taskdo.getAddress().contains("www.")){
				System.out.println("搜狐新版");
				SohuCommentJietu.toComment(taskdo);
				//SohuCommentVps2.sendContent(taskdo);
//				if(jh1.getStatus2()==1){//需要截图
//				}else{
//					SohuCommentVps2.sendContent(taskdo);
//				}
			}else{
				SohuCommentVps.sendContent(taskdo);
			}
		}else if(jh1.getType().equals("news.sohu.praise")){ //搜狐点赞
			if(jh1.getIsApp()!=0){
				SohuPriseApp.toRun(taskdo);
			} if(taskdo.getAddress().contains("www.")){
				System.out.println("搜狐点赞新版");
				SohuCommentVps2.sendPraise(taskdo);
			}else{
				SohuCommentVps.sendPraise(taskdo);
			}
		}else if(jh1.getType().equals("news.ifeng.comment")){//凤凰新闻评论
			if(jh1.getIsApp()!=0){
				IFengCommentApp.toRun(taskdo);
			}else if(taskdo.getAddress().contains("v.ifeng.com")){
				IFengVideoComment.toComment(taskdo);
			}else if(jh1.getStatus2()==1){
				IfengCommentJietu.toComment(taskdo);
			}else{
				IFNewsComment.ifeng(taskdo);
			}
		}else if(jh1.getType().equals("news.ifeng.praise")){//凤凰新闻点赞
			IFengNewsSupport.ifeng(taskdo);
		}else if(jh1.getType().equals("news.toutiao.comment")){ //今日头条评论
			TouTiaoComment.toutiaoComment(taskdo);
		}else if(jh1.getType().equals("news.toutiao.praise")){ //头条赞
			TouTiaoComment.toutiaoDigg(taskdo);
		}else if(jh1.getType().equals("xinhua.news.comment")){ //新华网新闻评论
			XinHuaComment.xinhua(taskdo);
		}else if(jh1.getType().equals("news.xinhua.praise")){ //新华网新闻顶
			XinHuaDigg.xinhua(taskdo);
		}else if(jh1.getType().equals("news.people.comment")){ //人民网新闻评论
			TrPepoleComment.toRun(taskdo);
		}else if(jh1.getType().equals("news.people.praise")){ //人民网新闻赞
			PepoleDigg.xinhua(taskdo);
		}else if(jh1.getType().equals("news.pengpai.comment")){ //澎湃新闻评论
			ThepaperComment.ifeng(taskdo);
		}else if(jh1.getType().equals("news.pengpai.praise")){ //澎湃新闻顶
			ThepaperDigg.ifeng(taskdo);
		}else if(jh1.getType().equals("news.qq.comment")){ //腾讯新闻评论
			System.out.println("Status2=="+jh1.getStatus2());
			if(jh1.getIsApp()!=0){
				TengxunCommentApp.toRun(taskdo);
			}else if(taskdo.getAddress().contains("v.qq.com")){
				QQVideoComment.toComment(taskdo);
			}else{
				QQCommentJietu.toComment(taskdo);
			}
//			else{
//				QQNewsComment2.toComment(taskdo);
//			}
			
		}else if(jh1.getType().equals("news.qq.praise")){ //腾讯新闻顶
			if(jh1.getIsApp()!=0){
				TengxunCommentApp.toRun(taskdo);
			}else{
				QQNewsComment.digg(taskdo);
			}
		}else if(jh1.getType().equals("bbs.baidu.comment")){ //百度贴吧
			BaiDuComment.baidu(taskdo);
		}
		
		else if(jh1.getType().equals("news.qctt.comment")){ //汽车头条新闻评论
			CarToutiaoComment.toRun(taskdo);
		}else if(jh1.getType().equals("news.yidianzixun.comment")){ //一点资讯新闻评论
			if(jh1.getIsApp()!=0){
				YDZXCommentApp.toRun(taskdo);
			}else{
				YDZXComment.toRun(taskdo);
			}
		}else if(jh1.getType().equals("news.jiemian.comment")){ //界面网新闻评论
			CarJMComment.toRun(taskdo);
		}else if(jh1.getType().equals("news.pcauto.comment")){ //太平洋汽车网
			CarPcautoComment.toRun(taskdo);
		}else if(jh1.getType().equals("news.cheshi.comment")){ //车市网
			CarCheShiComment.toRun(taskdo);
		}else if(jh1.getType().equals("news.emao.comment")){ //一猫网
			CarEmaoComment.toRun(taskdo);
		}else if(jh1.getType().equals("news.58che.comment")){ //58车网
			CarQCDPComment.toRun(taskdo);
		}else if(taskdo.getType().equals("news.315che.comment")){ //中国汽车消费网
			CarZGQCXFWComment.toRun(taskdo);
		}else if(jh1.getType().equals("news.eastday.comment")){ //东方网
			CarDFWComment.toRun(taskdo);
		}else if(jh1.getType().equals("news.xinche.comment")){ //新车评网评论
			CarXCPComment.toRun(taskdo);
		}else if(jh1.getType().equals("news.xinhua.comment")){ //新华网车评
			CarXinHuaComment.xinhua(taskdo);
		}else if(jh1.getType().equals("news.yiche.comment")){ //易车网
			CarYCComment.yc(taskdo);
		}else if(jh1.getType().equals("news.autohome.comment")){ //汽车之家
			CarQCZJComment.yc(taskdo);
		}else if(jh1.getType().equals("news.xcar.comment")){ //爱卡汽车
			CarAKComment.yc(taskdo);
		}else if(jh1.getType().equals("news.douban.comment")){ //豆瓣
			DoubanComment.toComment(taskdo);
		}else if(jh1.getType().equals("register.douban.com")){//豆瓣注册
			DoubanRegister.toRegister(taskdo);	
		}else if(jh1.getType().equals("news.mtime.comment")){ //时光网
			MtimeComment.toComment(taskdo);
		}else if(jh1.getType().equals("news.huanqiu.comment")){ //环球
			TrHuanQiuComment.toComment(taskdo);
		}else if(jh1.getType().equals("news.chinanews.comment")){ //中国新闻网
			TrChinanewsComment.toComment(taskdo);
		}else if(jh1.getType().equals("bbs.tianya.comment")){ // 天涯引导
			TianyaComment.toComment(taskdo);
		}else if(jh1.getType().equals("news.mafengwo.comment")){ // 马蜂窝
			
			MafengwoComment.toComment(taskdo);
			
		}else if(jh1.getType().equals("news.jinlv.comment")){ // 劲旅
			
			JinlvComment.toComment(taskdo); 
			
		}else if(jh1.getType().equals("sina.weibo.appention")){ // 微博关注
			
			WeiboAddFen.sina(taskdo);
			
		}else if(jh1.getType().equals("news.maoyao.comment")){ // 猫眼评论
			
			MaoyanComment.toComment(taskdo);
			
		}else if(jh1.getType().equals("register.maoyan.com")){ // 猫眼注册
			
			MaoyanRegister.toRegister(taskdo);
			
		}else if(jh1.getType().equals("register.dianping.com")){
			
			DianpingRegister.toRegister(taskdo);
		}else if(jh1.getType().equals("bbs.xici.comment")){//西祠胡同评论
			XiciComment.toComment(taskdo);
		}else if(jh1.getType().equals("bbs.xici.post")){//西祠胡同评论
			XiciPost.toComment(taskdo);
	    }

	
		else{
			System.out.println("vps找不到程序"); 
		}
    }
    public static void qieIP(){
		if(GuideFixedThreadPool.qieTimes==GuideFixedThreadPool.qieNumber){
			GuideFixedThreadPool.qieTimes = 0;
			try {
				for(int i=0;i<5;i++){
					boolean has = ConnectNetWork.cutAdsl(GetProprities.AsdlConfig.getProperty("asdlname"));
					Thread.sleep(1000);
					if(has) break;
				}
				for(int i=0;i<5;i++){
					boolean cnt = ConnectNetWork.connAdsl(GetProprities.AsdlConfig.getProperty("asdlname"),GetProprities.AsdlConfig.getProperty("asdluser"),GetProprities.AsdlConfig.getProperty("asdlpwd"));
					Thread.sleep(1000);  
					if(cnt){
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			GuideFixedThreadPool.qieTimes++;
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public void qieIP2(){
		try {
			for(int i=0;i<5;i++){
				boolean has = ConnectNetWork.cutAdsl(GetProprities.AsdlConfig.getProperty("asdlname"));
				Thread.sleep(1000);
				if(has) break;
			}
			for(int i=0;i<5;i++){
				boolean cnt = ConnectNetWork.connAdsl(GetProprities.AsdlConfig.getProperty("asdlname"),GetProprities.AsdlConfig.getProperty("asdluser"),GetProprities.AsdlConfig.getProperty("asdlpwd"));
				Thread.sleep(1000);  
				if(cnt){
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public TaskGuideBean getTaskdo() {
		return taskdo;
	}

	public void setTaskdo(TaskGuideBean taskdo) {
		this.taskdo = taskdo;
	}

	public TaskGuideBean getJh1() {
		return jh1;
	}

	public void setJh1(TaskGuideBean jh1) {
		this.jh1 = jh1;
	}

} 
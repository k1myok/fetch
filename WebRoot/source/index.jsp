<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
    
<title>短信平台</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">    
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link rel="stylesheet" type="text/css" href="source/css/bootstrap.min.css"></link>
<script type="text/javascript" src="source/js/jquery.min.js"></script>
<script type="text/javascript" src="source/js/bootstrap.min.js"></script>
<style>
	*{
	font-family: 微软雅黑;
/* 	text-align: center; */
	}
	tr th{
	text-align: center;
	}
	.container{
	margin-top: 20px;
	margin-bottom: 20px;
	}
	p{
	width:100%;
	height:100px;
	text-align: center;
	line-height: 90px;
	font-size: 50px;
	}
	.row{
	margin-top: 5px;
	}
	button{
	float:right;
	margin-bottom: 20px;
	width:70px;
	margin-top: 20px;
	margin-left:5px;
	}
	select{
/* 	width: 100px; */
/* 	height:30px; */
/* 	padding-right: 0px; */
/* 	margin-right: 0px; */
	}
	#footer{
/* 	display:none; */
	position: absolute;
	width: 100px;
	height: 50px;
	z-index: 999;
	}
	.btnsize{
	opacity:0.4;
	}
	.out, .ip{
	display:none;
	margin-top: 30px;
	}
	.loading{
		position: absolute;
/* 		top:400px; */
/* 		left:800px; */
		background-color: #ccc;
		width: 130px;
		height: 130px;
		display: none;
	}
	.pager{
	float:right;
	}
	
	dl dd{
	margin-bottom: 10px;
	margin-left: 50px;
	}
	dd a:hover{
	opacity:0.4;
	}
</style>
</head>
<body>
	<div class="container">
	
	<p class="bg-primary">
	短信平台
	</p>
	<nav class="navbar">
	<div>
		<ul class="nav nav-tabs nav-justified">
			<li role="presentation" class="active"><a href="javascript:void(0);"><h3 class="glyphicon glyphicon-envelope">短信详情</h3></a></li>
			<li role="presentation"><a href="javascript:void(0);"><h3 class="glyphicon glyphicon-file">短信发送</h3></a></li>
			<li role="presentation"><a href="javascript:void(0);"><h3 class="glyphicon glyphicon-lock">工具</h3></a></li>
			<li role="presentation"><a href="javascript:void(0);"><h3 class="glyphicon glyphicon-user">帮助</h3></a></li>
		</ul>
	</div>
	</nav>
	<div class="out">
	<div class="table-responsive"> 
		<table class="table table-hover table-bordered">
			<tr class="danger">
				<th style="width:6%;">
				序号
				</th>
				<th style="width:6%;">
				串口号
				</th>
				<th style="width:18%;">
				时间
				</th>
				<th>
				短信内容
				</th>
				<th  style="width:6%;">
				操作
				</th>
			</tr>
			<c:forEach items="${page.list}" var="page">
			<tr>
				<td>
				${page.id}
				</td>
				<td>
				${page.portNum}
				</td>
				<td>
				${page.smsDate}
				</td>
				<td>
				${page.smsContent}
				</td>
				<c:if test="${page.isRead != 0}">
				<td>
				<button type="button" class="btm btn-success isRead" onclick="isReaded(${page.id},0,this)">已读</button>
				</td>
				</c:if>
				<c:if test="${page.isRead == 0}">
				<td>
				<button type="button" class="btm btn-danger isRead" onclick="isReaded(${page.id},1,this)">未读</button>
				</td>
				</c:if>
			</tr>
			</c:forEach>
		</table>
	</div>
	<div class="row"">
		<div class="col-md-3">
		<h4 class="text-primary">当前第${page.currentPage}页,总计${page.pageTotal}页</h4>
		</div>
		<div class="col-md-9">
		<nav aria-label="...">
			<ul class="pager">
				<li><a href="${pageContext.request.contextPath}/spcard/getMessage.do?currentPage=1"><span class="glyphicon glyphicon-step-backward">首页</span></a></li>
				<c:if test="${page.currentPage !=1}">
				<li><a href="${pageContext.request.contextPath}/spcard/getMessage.do?currentPage=${page.currentPage-1}"><span class="glyphicon glyphicon-chevron-left">上一页</span></a></li>
				</c:if>
				<li><a href="${pageContext.request.contextPath}/spcard/getMessage.do?currentPage=${page.currentPage+1}">下一页<span class="glyphicon glyphicon-chevron-right"></span></a></li>
				<li><a href="${pageContext.request.contextPath}/spcard/getMessage.do?currentPage=${page.pageTotal}">尾页<span class="glyphicon glyphicon-step-forward"></span></a></li>
			</ul>
		</nav>
		</div>
	</div>
	</div>
	<div class="out">
		<form class="form-horizontal" onsubmit="return false;">
			<div class="form-group">
				<label for="" class="col-sm-2 text-primary"><h4>串口号</h4></label>
				<div class="col-sm-10">
					<select class="form-control" name="portNum">
						<option value="4">串口4</option>
						<option value="5">串口5</option>
						<option value="6">串口6</option>
						<option value="7">串口7</option>
						<option value="8">串口8</option>
						<option value="9">串口9</option>
						<option value="10">串口10</option>
						<option value="11">串口11</option>
					</select>
				</div>
			</div>
			<div class="form-group">
				<label for="" class="col-sm-2"><h4 class="text-primary">目的地</h4></label>
				<div class="col-sm-10">
					<input type="text" class="form-control" name="destination" placeholder="发送给谁"/>
				</div>
			</div>
			<div class="form-group">
				<label for="" class="col-sm-2"><h4 class="text-primary">内容</h4></label>
				<div class="col-sm-10">
					<textarea class="form-control" rows="5" placeholder="短信内容" nam="content"></textarea>
				</div>
			</div>
			<div class="form-group">
				<lable class="col-sm-6"></lable>
				<div class="col-sm-6">
					<button type="reset" class="btn btn-danger">重置</button>
					<button id="send" type="submit" class="btn btn-success">提交</button>
				</div>
			</div>
		</form>
	</div>
	<div class="out">
		<div class="row">
			<div class="col-md-2">
			<ul class="nav nav-pills nav-stacked">
				<li role="presentation" class="active"><a href="javascript:void(0);"><h4>换卡</h4></a></li>
				<li role="presentation"><a href="javascript:void(0);"><h4>换IP</h4></a></li>
			</ul>
			</div>
			<div class="col-md-10">
				<div class="card  tool">
					<select name="lead" class="form-control"> 
						<option value="1">第一排</option>
						<option value="2">第二排</option>
						<option value="3">第三排</option>
						<option value="4">第四排</option>
						<option value="5">第五排</option>
						<option value="6">第六排</option>
						<option value="7">第七排</option>
						<option value="8">第八排</option>
						<option value="9">第九排</option>
						<option value="10">第十排</option>
						<option value="11">第十一排</option>
						<option value="12">第十二排</option>
						<option value="13">第十三排</option>
						<option value="14">第十四排</option>
						<option value="15">第十五排</option>
						<option value="16">第十六排</option>
						<option value="17">第十七排</option>
						<option value="18">第十八排</option>
						<option value="19">第十九排</option>
						<option value="20">第二十排</option>
						<option value="21">第二一排</option>
						<option value="22">第二二排</option>
						<option value="23">第二三排</option>
						<option value="24">第二四排</option>
						<option value="25">第二五排</option>
						<option value="26">第二六排</option>
						<option value="27">第二七排</option>
						<option value="28">第二八排</option>
						<option value="29">第二九排</option>
						<option value="30">第三十排</option>
					</select>
					<button type="button" class="btn btn-warning switchCard">确认换卡</button>
				</div>
				<div class="tool">
					<div>
						<dl class="">
							<dt><span class="bg-danger">第一步:</span></dt>
							<dd>
								<a type="button" class="btn btn-danger" id="disconnect">断开连接
								</a>
								<span style="margin-left: 10px;font-size:16px;" class="text-success">
								</span>
								</dd>
							<dt><span class="bg-danger">第二步:</span></dt>
							<dd>
								<a type="button" class="btn btn-success" id="again">重新连接
								</a>
								<span style="margin-left: 10px;font-size:16px;" class="text-success">
								</span>
								</dd>
						</dl>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="out">
		<div class="well"><h4 class="text-success"><span class="glyphicon glyphicon-check"></span>&nbsp;&nbsp;欢迎使用短信平台,如有疑问,请联系开发人员</h4></div>
	</div>
	<div class="loading">
		<div><img src="source/img/loading.png"/></div>
<!-- 		<div><span style="margin-left: 30px;font-size: 20px;">正在换卡</span></div> -->
	</div>
	</div>
<script>
	
	$('.loading').css({'top':$(window).height()/2-65,'left':$(window).width()/2-65});
	
	$(function(){
		$.ajax({
			type:'GET',
			url:'${pageContext.request.contextPath}/spcard/getMessage.do',
			data:'',
			success:function(data){
// 				var result = data;
// 				for(var i=0;i<result.length;i++){
// 				html += '<tr><td>'+result[i].id+'</td><td>'+result[i].portNum+'</td><td>'+result[i].smsDate+'</td><td>'+result[i].smsContent+'</td></tr>';
// 				}
// 				$('table').append(html);
			},error:function(){
				console.log('失败');
			}
		
		});
	});
</script>
<script>
	
	$('div.out').eq(0).show();
	$('ul.nav-tabs li').click(function(){
	
		$(this).addClass('active');
		$('ul.nav-tabs li').not($(this)).removeClass('active');
		$('div.out').eq($(this).index()).show();
		$('div.out').not($('div.out')[$(this).index()]).hide();
	});
	
	$('div.tool').eq(1).hide();
	$('ul.nav-pills li').click(function(){
	
		$(this).addClass('active');
		$('ul.nav-pills li').not($(this)).removeClass('active');
		$('div.tool').eq($(this).index()).show();
		$('div.tool').not($('div.tool')[$(this).index()]).hide();
	});
	$('button.switchCard').click(function(){
		$('.loading').show();
		var portNum = $('.tool option:selected').val();
		$.ajax({
			url:'${pageContext.request.contextPath}/spcard/switchCard.do',
			type:'POST',
			data:{'portNum':portNum},
			success:function(data){
				$('.loading').hide();
				alert(data);
			},error:function(){
				alert('换卡失败');
			}
		
		});
	});
</script>
<script>
	$('#send').click(function(){
	
		var portNum = $('option:selected').val();
		var destination = $('input[name="destination"]').val();
		if(destination == null || destination == ''){
			alert('请输入目的地');
			return;
		}
		var content = $('textarea').val();
		if(content == null || content == ''){
			alert('请输入内容');
			return;
		}
		$.ajax({
			url:'${pageContext.request.contextPath}/spcard/sendMessage.do',
			type:'POST',
			dataType:'json',
			data:{'portNum':portNum,'destination':destination,'content':content},
			success:function(data){
				alert(data.msg);
				$('input[name="destination"]').val('');
				$('textarea').val('');
			},error:function(){
				alert('短信发送失败');
			}
		});
		
	});
	
	$('#disconnect').click(function(){
		$('.loading').show();
		$.ajax({
			url:'${pageContext.request.contextPath}/spcard/disconnect.do',
			type:'GET',
			success:function(data){
				$('.loading').hide();
				alert(data);
				$('dl dd:eq(0) span').html(data);
			},error:function(){
				alert('断开失败');
			}
		});
	});
	
	$('#again').click(function(){
		$('.loading').show();
		$.ajax({
			url:'${pageContext.request.contextPath}/spcard/again.do',
			type:'GET',
			success:function(data){
				$('.loading').hide();
				alert(data);
				$('dl dd:eq(1) span').html(data);
			},error:function(){
				alert('重连失败');
			}
		});
	});
	
	function isReaded(id,isReaded,doc){
		
		$.ajax({
			url:'${pageContext.request.contextPath}/spcard/updateMessage.do',
			type:'POST',
			data:{'id':id,'isReaded':isReaded},
			success:function(data){
// 				alert('修改成功');
				window.location.reload();
			},error:function(){
// 				alert('修改失败');
			}
		});
	}
	
</script>
<script>
	$('tr').hover(function(){
		$(this).addClass('success');
		$('tr').not($(this)).removeClass('success');
	},function(){
		$(this).removeClass('success');
	});
	
	$('.isRead').hover(function(){
		$(this).parent().parent().removeClass('success');
	},function(){
		$(this).parent().parent().addClass('success');
	});
</script>
<script>
	$('button').hover(function(){
		$(this).addClass('btnsize');
	},function(){
		$(this).removeClass('btnsize');
	});
</script>
<script>
	$('input[name="portNum"]').focusout(function(){
		var portNum = $(this).val();
		if(isNaN(portNum)){
			alert('串口号是数字类型');
		}
	});
	$('input[name="destination"]').focusout(function(){
		var portNum = $(this).val();
		if(isNaN(portNum)){
			alert('目的地是数字类型');
			$(this).val('');
		}
	});
</script>
</body>
</html>

<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017/8/7
  Time: 15:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="Path.jsp"></c:import>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>login</title>
    <!--<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">-->
    <link rel="stylesheet" href="/resources/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="/resources/css/htmleaf-demo.css">
    <link href="http://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="/resources/css/htmleaf-demo.css">
    <link rel="stylesheet" type="text/css" href="/resources/css/verify.css">
    <style type="text/css">
        .form-bg{
            background: #00b4ef;
        }
        .form-horizontal{
            background: #fff;
            padding-bottom: 40px;
            border-radius: 15px;
            text-align: center;
        }
        .form-horizontal .heading{
            display: block;
            font-size: 35px;
            font-weight: 700;
            padding: 35px 0;
            border-bottom: 1px solid #f0f0f0;
            margin-bottom: 30px;
        }
        .form-horizontal .form-group{
            padding: 0 40px;
            margin: 0 0 25px 0;
            position: relative;
        }
        .form-horizontal .form-control{
            background: #f0f0f0;
            border: none;
            border-radius: 20px;
            box-shadow: none;
            padding: 0 20px 0 45px;
            height: 40px;
            transition: all 0.3s ease 0s;
        }
        .form-horizontal .form-control:focus{
            background: #e0e0e0;
            box-shadow: none;
            outline: 0 none;
        }
        .form-horizontal .form-group i{
            position: absolute;
            top: 12px;
            left: 60px;
            font-size: 17px;
            color: #c8c8c8;
            transition : all 0.5s ease 0s;
        }
        .form-horizontal .form-control:focus + i{
            color: #00b4ef;
        }
        .form-horizontal .fa-question-circle{
            display: inline-block;
            position: absolute;
            top: 12px;
            right: 60px;
            font-size: 20px;
            color: #808080;
            transition: all 0.5s ease 0s;
        }
        .form-horizontal .fa-question-circle:hover{
            color: #000;
        }
        .form-horizontal .main-checkbox{
            float: left;
            width: 20px;
            height: 20px;
            background: #11a3fc;
            border-radius: 50%;
            position: relative;
            margin: 5px 0 0 5px;
            border: 1px solid #11a3fc;
        }
        .form-horizontal .main-checkbox label{
            width: 20px;
            height: 20px;
            position: absolute;
            top: 0;
            left: 0;
            cursor: pointer;
        }
        .form-horizontal .main-checkbox label:after{
            content: "";
            width: 10px;
            height: 5px;
            position: absolute;
            top: 5px;
            left: 4px;
            border: 3px solid #fff;
            border-top: none;
            border-right: none;
            background: transparent;
            opacity: 0;
            -webkit-transform: rotate(-45deg);
            transform: rotate(-45deg);
        }
        .form-horizontal .main-checkbox input[type=checkbox]{
            visibility: hidden;
        }
        .form-horizontal .main-checkbox input[type=checkbox]:checked + label:after{
            opacity: 1;
        }
        .form-horizontal .text{
            float: left;
            margin-left: 7px;
            line-height: 20px;
            padding-top: 5px;
            text-transform: capitalize;
        }
        .form-horizontal .btn{
            float: right;
            font-size: 14px;
            color: #fff;
            background: #00b4ef;
            border-radius: 30px;
            padding: 10px 25px;
            border: none;
            text-transform: capitalize;
            transition: all 0.5s ease 0s;
        }
        @media only screen and (max-width: 479px){
            .form-horizontal .form-group{
                padding: 0 25px;
            }
            .form-horizontal .form-group i{
                left: 45px;
            }
            .form-horizontal .btn{
                padding: 10px 20px;
            }
        }
    </style>
    <!--[if IE]>
    <script src="http://cdn.bootcss.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <![endif]-->
    <script type="text/javascript" src="/resources/js/jquery-1.7.2.min.js" ></script>
    <script type="text/javascript" src="/resources/js/jquery.cookie.js"></script>
    <script src="http://cdn.bootcss.com/jquery/1.11.0/jquery.min.js" type="text/javascript"></script>
    <script>window.jQuery || document.write('<script src="/resources/js/jquery-1.11.0.min.js"><\/script>')</script>
    <script type="text/javascript" src="/resources/js/verify.js" ></script>
    <script type="text/javascript">
        $(function(){
            if($.cookie("login")=="true"){
                $("#username").val($.cookie("username"));
                $("#password").val($.cookie("password"));
                $("#checkbox1").prop("checked",true);
            }
            $("#submit").click(function(){
                debugger;
                if(check()){
                    save();
                    login();
                }
            });
        });
        function save(){
            if($("#checkbox1").get(0).checked){
                var username = $("#username").val();
                var password = $("#password").val();
                $.cookie("login","true",{expire:7});
                $.cookie("username",username,{expires:7});
                $.cookie("password",password,{expires:7});
            }else{
                $.cookie("login","false",{expire:-1});
                $.cookie("username","",{expires:-1});
                $.cookie("password","",{expires:-1});
            }
        };
        function check(){
            var username = $("#username").val();
            var password = $("#password").val();
            if(username==""){
                alert("请输入用户名");
                $("#username").focus();
                return false;
            }
            if(password==""){
                alert("请输入密码");
                $("#password").focus();
                return false;
            }
            return true;
        }
        function login() {
            debugger;
            var name = $("#username").val();
            var password = $("#password").val();
            var json = {"username": ' tang ', "password": '123456'};
            $.ajax({
                type: "post",
                url: "/myspringboot/login.do",
                data: JSON.stringify(json),
                datatype: "json",
                async: false,
                contentTyle:"application/json; charset=utf-8",
                success: function (data) {
                    if ("00" == data.rspCode) {
                        save();
                        window.location.href = "main.html";
                    } else {
                        alert(data.rspMessage);
                        window.location.href = "login.html";
                    }
                }
            });
        }

        $('#mpanel4').slideVerify({
            type : 2,		//类型
            vOffset : 5,	//误差量，根据需求自行调整
            vSpace : 5,	//间隔
            imgName : ['1.jpg', '2.jpg'],
            imgSize : {
                width: '400px',
                height: '200px',
            },
            blockSize : {
                width: '40px',
                height: '40px',
            },
            barSize : {
                width : '400px',
                height : '40px',
            },
            ready : function() {
            },
            success : function() {
                alert('验证成功，添加你自己的代码！');
                //......后续操作
            },
            error : function() {
                //		        	alert('验证失败！');
            }

        });
    </script>
</head>
<body style="min-width: 470px;">
<div class="htmleaf-container">

    <div class="demo form-bg" style="padding: 20px 0;">
        <div style="margin-left: 30px;">
            <a href="http://weibo.com/henuxiehui?topnav=1&wvr=6&topsug=1&is_all=1">
                <img width="70px" height="70px" src="/resources/img/logo3.jpg" />
            </a>
        </div>
        <div class="container" style="margin-top: 10px;">
            <div class="row">
                <div class="col-md-offset-3 col-md-6">
                    <form class="form-horizontal" id="loginform" action="/myspringboot/login.do" method="post">
                        <span class="heading">用户登录</span>
                        <div class="form-group">
                            <input type="text" class="form-control" name="username" id="username" placeholder="用户名">
                            <i class="fa fa-user"></i>
                        </div>
                        <div class="form-group help">
                            <input type="password" class="form-control" name="password" id="password" placeholder="密　码">
                            <i class="fa fa-lock"></i>
                            <a href="#" class="fa fa-question-circle"></a>
                        </div>
                        <div class="row">
                            <div class="col-md-offset-4 col-md-4">
                                <p style="margin-top:50px;">拖动方块到空白处完成验证</p>
                                <div id="mpanel4" ></div>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="main-checkbox">
                                <input type="checkbox" value="None" id="checkbox1" name="check"/>
                                <label for="checkbox1"></label>
                            </div>
                            <span class="text">Remember me</span>
                            <button type="button" class="btn btn-default" id="submit">登录</button>
                            <button type="reset" class="btn btn-default" style="margin-right: 20px;">重置</button>
                            <button type="button" class="btn btn-default" style="margin-right: 30px;"><a href="/page/register.html">注册</a></button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <div class="banquan" style="height: 6%;width: 100%;text-align: center;margin-top: 60px;">
        <span style="font-family:'楷体';">Copyright © 2017 微协的小小窝 版权所有</span>
    </div>
</div>

</body>
</html>

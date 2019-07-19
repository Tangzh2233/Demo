<%--
  Created by IntelliJ IDEA.
  User: Tangzhihao
  Date: 2018/5/17
  Time: 11:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <title>注册界面</title>
    <link rel="stylesheet" href="/resources/css/reset.css" />
    <link rel="stylesheet" href="/resources/css/common.css" />
    <link rel="stylesheet" href="/resources/css/font-awesome.min.css" />
</head>
<body>
<div class="wrap login_wrap">
    <div class="content">

        <div class="logo"></div>

        <div class="login_box">

            <div class="login_form">
                <div class="login_title">
                    注册
                </div>
                <form  id="registerForm" action="/myspringboot/login.do" method="post">

                    <div class="form_text_ipt">
                        <input name="username" type="text" placeholder="用户名/邮箱">
                    </div>
                    <div class="ececk_warning"><span>手机号/邮箱不能为空</span></div>
                    <div class="form_text_ipt">
                        <input name="password" type="password" placeholder="密码">
                    </div>
                    <div class="ececk_warning"><span>密码不能为空</span></div>
                    <div class="form_text_ipt">
                        <input name="repassword" type="password" placeholder="重复密码">
                    </div>
                    <div class="ececk_warning"><span>密码不能为空</span></div>
                    <div class="form_text_ipt">
                        <input name="code" type="text" placeholder="验证码">
                    </div>
                    <div class="ececk_warning"><span>验证码不能为空</span></div>

                    <div class="form_btn">
                        <button type="button" id="submit">注册</button>
                    </div>
                    <div class="form_reg_btn">
                        <span>已有帐号？</span><a href="/page/login.html">马上登录</a>
                    </div>
                </form>
                <div class="other_login">
                    <div class="left other_left">
                        <span>其它登录方式</span>
                    </div>
                    <div class="right other_right">
                        <a href="#"><i class="fa fa-qq fa-2x"></i></a>
                        <a href="#"><i class="fa fa-weixin fa-2x"></i></a>
                        <a href="#"><i class="fa fa-weibo fa-2x"></i></a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="/resources/js/jquery.min.js" ></script>
<script type="text/javascript" src="/resources/js/common.js" ></script>
<script type="text/javascript">
    $(function(){
        $("#submit").click(function(){
            debugger;
            var json = {"username": " tang ","password": "123456"};
            // var data = json.serializeObject();
            $.ajax({
                type: "post",
                url: "/myspringboot/login.do",
                data: json,
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
            // $.ajax({
            //     type:"post",
            //     url:"/myspringboot/register.do",
            //     data:$('#registerForm').serialize(),
            //     datatype:"json",
            //     async:true,
            //     success:function(data){
            //         if("00" == data.status){
            //             alert(data.msg);
            //             window.location.href = "login.html";
            //         }else{
            //             alert(data.msg);
            //             window.location.href = "register.html";
            //         }
            //     }
            // });
        });
    });

    $.fn.serializeObject = function()
    {
        var o = {};
        var a = this.serializeArray();
        $.each(a, function() {
            if (o[this.name] !== undefined) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });
        return o;
    };
</script>
</body>
</html>

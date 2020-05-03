<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017/9/15
  Time: 10:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>主页</title>
    <link rel="stylesheet" href="/resources/plugin/layui/css/layui.css" />
    <link rel="stylesheet" href="/resources/css/main.css" />
    <link rel="stylesheet" href="/resources/plugin/font-awesome/css/font-awesome.min.css" />
    <script type="text/javascript" src="/resources/plugin/layui/layui.js" ></script>
    <script type="text/javascript" src="/resources/js/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" src="/resources/js/canvas.js" ></script>
    <script type="text/javascript" src="/resources/js/main.js"></script>

</head>
<body style="background: #EEEEEE;">
<div class="layui-header" style="min-width: 950px;">
    <ul class="layui-nav" lay-filter="" style="text-align: center;">
        <a class="mylog" href="">微协的小小窝</a>
        <li class="layui-nav-item layui-this">
            <a href=""><i class="fa fa-file-text fa-fw"></i>&nbsp;新闻资讯</a>
        </li>
        <li class="layui-nav-item">
            <a href=""><i class="fa fa-video-camera"></i>&nbsp;河大影像</a>
        </li>
        <li class="layui-nav-item">
            <a href=""><i class="fa fa-comments"></i>&nbsp;谈天论地</a>
        </li>
        <li class="layui-nav-item">
            <a href="/mytest/addDlog.html"><i class="fa fa-hand-o-right"></i>&nbsp;加入我们</a>
        </li>
        <li class="layui-nav-item">
            <a href="javascript:;"><i class="fa fa-caret-right"></i>&nbsp;关于微协</a>
            <dl class="layui-nav-child"> <!-- 二级菜单 -->
                <dd><a href="">成员构成</a></dd>
                <dd><a href="/myspringboot/sendEmail.do">部门职能</a></dd>
                <dd><a id="loginOut" href="" onclick="loginOut()">退出</a></dd>
            </dl>
        </li>
    </ul>
</div>
<!-- canvas特效 -->
<canvas id="canvas-banner" style="background: #393D49;"></canvas>
<script>
    //canvas模块
    var canvas = document.getElementById('canvas-banner');
    canvas.width = window.document.body.clientWidth - 10;//减去滚动条的宽度
    if (screen.width >= 992) {
        canvas.height = window.innerHeight * 1 / 3;
    } else {
        canvas.height = window.innerHeight * 2 / 7;
    }
    function loginOut(){
        var token = getCookie("USER_TOKEN");
        $.ajax({
            type:"post",
            url:"/myspringboot/exit.do",
            data:{token:token},
            datatype:"json",
            async:true,
            success:function(data){
                if("00"==data.status){
                    window.location.href = "login.html";
                }else {
                    window.location.href = "login.html";
                }
            }
        });
    }
    function jsonpTest() {
        var token = "";
        $.ajax({
            type:"get",
            url:"http://www.baidu,com",
            jsonp:"callJsonP",
            data:{token:token,callback:"callJsonP"},
            datatype:"jsonp",
            success:function(data){
                alert("Jsonp CatGrepFileForJava");
            }
        });
    }
    function getCookie(name){
        var user_token;
        var cookies = document.cookie.split(";");
        for(var i=0;i<cookies.length;i++){
            var str = cookies[i].split("=");
            if(name == str[0].trim()){
                user_token = str[1].trim();
                break;
            }
        }
        return user_token;
    }

</script>
<div class="Tag-body"style="width: 100%;">
    <div class="Tag-container" style="width: 95%;border: 1px solid black;">
        <!-- 网站公告提示 -->
        <div class="Tag-home-tips">
            <i style="float:left;line-height:30px;color: red;font-size: 15px;" class="fa fa-volume-up"></i>
            <div class="Tag-home-tips-container">
                <span style="color: #009688">本网站为仿照layui社区  不落阁设计。非直接copy</span>
                <span style="color: #009688">网站新增留言回复啦！使用QQ登陆即可回复，人人都可以回复！</span>
                <span style="color: #009688">如果你觉得网站做得还不错，来Fly社区点个赞吧！<a href="http://fly.layui.com/case/2017/" target="_blank" style="color:#01AAED">点我前往</a></span>
                <span style="color: #009688">不落阁 &nbsp;—— &nbsp;一个.NET程序员的个人博客，新版网站采用Layui为前端框架，目前正在建设中！</span>
            </div>
        </div>
        <div class="Tag-body-main" style="border:1px solid green;display: flex;">
            <div class="body-left" style="border: 1px solid blueviolet;flex: 5;">
                <div class="article" style="height: 120px;background: white;padding: 15px;">
                    <div class="article-img" style="width: 25%;float: left;">
                        <img src="/resources/img/201703181909057125.jpg" alt="示例图片" style="width: 100%;"/>
                    </div>
                    <div style="width: 500px;float: left;margin-left: 20px;">
                        <div>
                            <span style="font-size: 18px;"><a>基于laypage的layui扩展模块（pagesize.js）！</a></span>
                        </div>
                        <div style="margin-top: 10px;">
                            <span>&nbsp;&nbsp;&nbsp;&nbsp;该模块主要是针对当前版本laypage没有页容量控制功能而制作，使用该模块后即可实现每页显示多少条数据的控制！本人原创，但是可能有可能只对本人的分页写法有用！</span>
                        </div>
                    </div>
                    <div style="clear: both;"></div>
                    <div class="article-footer">
                        <span><i class="fa fa-clock-o"></i>&nbsp;&nbsp;2017-03-18</span>
                        <span class="article-author"><i class="fa fa-user"></i>&nbsp;&nbsp;Absolutely</span>
                        <span class="article-author"><i class="fa fa-tag"></i>&nbsp;&nbsp;<a href="#" style="color: green;">Web前端</a></span>
                        <span class="article-viewinfo"><i class="fa fa-eye"></i>&nbsp;0</span>
                        <span class="article-viewinfo"><i class="fa fa-commenting"></i>&nbsp;4</span>
                    </div>
                </div>
                <div class="article" style="height: 120px;background: white;padding: 15px;">
                    <div class="article-img" style="width: 25%;float: left;">
                        <img src="/resources/img/201703181909057125.jpg" alt="示例图片" style="width: 100%;"/>
                    </div>
                    <div style="width: 500px;float: left;margin-left: 20px;">
                        <div>
                            <span style="font-size: 18px;"><a>基于laypage的layui扩展模块（pagesize.js）！</a></span>
                        </div>
                        <div style="margin-top: 10px;">
                            <span>&nbsp;&nbsp;&nbsp;&nbsp;该模块主要是针对当前版本laypage没有页容量控制功能而制作，使用该模块后即可实现每页显示多少条数据的控制！本人原创，但是可能有可能只对本人的分页写法有用！</span>
                        </div>
                    </div>
                    <div style="clear: both;"></div>
                    <div class="article-footer">
                        <span><i class="fa fa-clock-o"></i>&nbsp;&nbsp;2017-03-18</span>
                        <span class="article-author"><i class="fa fa-user"></i>&nbsp;&nbsp;Absolutely</span>
                        <span class="article-author"><i class="fa fa-tag"></i>&nbsp;&nbsp;<a href="#" style="color: green;">Web前端</a></span>
                        <span class="article-viewinfo"><i class="fa fa-eye"></i>&nbsp;0</span>
                        <span class="article-viewinfo"><i class="fa fa-commenting"></i>&nbsp;4</span>
                    </div>
                </div>
                <div class="article" style="height: 120px;background: white;padding: 15px;">
                    <div class="article-img" style="width: 25%;float: left;">
                        <img src="/resources/img/201703181909057125.jpg" alt="示例图片" style="width: 100%;"/>
                    </div>
                    <div style="width: 500px;float: left;margin-left: 20px;">
                        <div>
                            <span style="font-size: 18px;"><a>基于laypage的layui扩展模块（pagesize.js）！</a></span>
                        </div>
                        <div style="margin-top: 10px;">
                            <span>&nbsp;&nbsp;&nbsp;&nbsp;该模块主要是针对当前版本laypage没有页容量控制功能而制作，使用该模块后即可实现每页显示多少条数据的控制！本人原创，但是可能有可能只对本人的分页写法有用！</span>
                        </div>
                    </div>
                    <div style="clear: both;"></div>
                    <div class="article-footer">
                        <span><i class="fa fa-clock-o"></i>&nbsp;&nbsp;2017-03-18</span>
                        <span class="article-author"><i class="fa fa-user"></i>&nbsp;&nbsp;Absolutely</span>
                        <span class="article-author"><i class="fa fa-tag"></i>&nbsp;&nbsp;<a href="#" style="color: green;">Web前端</a></span>
                        <span class="article-viewinfo"><i class="fa fa-eye"></i>&nbsp;0</span>
                        <span class="article-viewinfo"><i class="fa fa-commenting"></i>&nbsp;4</span>
                    </div>
                </div>

            </div>
            <div class="body-right" style="width: 20%;border: 1px solid gold;margin-left: 20px;">
                <div class="body-right-info">
                    <div class="blogerinfo-figure" style="width: 100px;height: 100px;border-radius: 50%;overflow: hidden;margin: auto;margin-top: 20px;">
                        <img src="/resources/img/tu7460_14.jpg" alt="Tag" style="width: 100px;height: 100px;"  />
                    </div>
                    <p class="tag-name" style="font-size: 28px;font-weight: bold;">螳-tang</p>
                    <p class="tag-introduce" style="font-size: 18px;margin-top: 10px;">一枚90后程序员，JAVA开发工程师</p>
                    <p class="tag-location" style="margin-top: 10px;"><i class="fa fa-location-arrow"></i>&nbsp;北京-北京</p>
                    <hr />
                    <div class="tag-contact">
                        <a target="_blank" title="QQ交流" href="javascript:layer.msg('启动QQ会话窗口')" style="margin-left: 10px;"><i class="fa fa-qq fa-2x"></i></a>
                        <a target="_blank" title="给我写信" href="javascript:layer.msg('启动邮我窗口')" style="margin-left: 10px;"><i class="fa fa-envelope fa-2x"></i></a>
                        <a target="_blank" title="新浪微博" href="javascript:layer.msg('转到你的微博主页')" style="margin-left: 10px;"><i class="fa fa-weibo fa-2x"></i></a>
                        <a target="_blank" title="码云" href="javascript:layer.msg('转到你的github主页')" style="margin-left: 10px;"><i class="fa fa-git fa-2x"></i></a>
                    </div>
                </div>
            </div>
        </div>

    </div>
</div>

</body>
</html>

<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017/10/10
  Time: 10:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript" src="/resources/js/jquery-1.7.2.min.js" ></script>
    <%--<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.6-rc.0/css/select2.min.css" rel="stylesheet" />--%>
    <%--<script type="text/javascript" src="http://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js"></script>--%>
    <%--<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.6-rc.0/js/select2.min.js"></script>--%>
    <script type="text/javascript" src="/resources/plugin/select" />
    <script type="text/javascript">
        $(function () {
            $("#select").select2();
            $('#login').click(function(){
                login();
                /*$.post("http://127.0.0.1:8848/myspringboot/login.do1",$('form').serialize());*/
            });
        });
        function login () {
            $.ajax({
                type:"post",
                url:"/addDlog",
                data:$('#loginform').serialize(),
                datatype:"json",
                async:true,
                success:function(data){
                    console.log(data);
                    if("main" == data){
                     window.location.href = "test.jsp";
                     }
                }
            });
        }
    </script>
</head>

<body>
    <form action="/mytest/addDlog.do" method="post" id="loginform1">
        <%--<input type="text" name="username">--%>
        <input type="text" name="title">
        <input type="text" name="content">
        <input type="submit" id="" value="提交">
    </form>
    <select id="select" >
        <option value="" selected="selected"></option>
        <option value="1">1</option>
        <option value="2">2</option>
    </select>

</body>
</html>

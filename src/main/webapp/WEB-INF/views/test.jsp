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
    <script type="text/javascript">
        $(function () {
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

</body>
</html>

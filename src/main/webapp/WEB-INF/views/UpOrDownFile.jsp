<%--
  Created by IntelliJ IDEA.
  User: tangzh
  Date: 2019/1/7
  Time: 11:07 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>文件上传下载</title>
    <script type="text/javascript" src="/resources/js/jquery-1.7.2.min.js"></script>
    <script type="text/javascript">
        function upFile() {
            debugger;
            var form = new FormData(document.getElementById("formUploadFile"));
            $.ajax({
                type: "post",
                url: "/myspringboot/test/upFile.do",
                data: form,
                contentType: false,
                processData: false,
                success:function (data) {
                    alert(data);
                }
            });
        }
    </script>
</head>
<body>
    <form id="formUploadFile" enctype="multipart/form-data">
        <input type="file" name="file">
        <input type="button" onclick="upFile()" value="上传">
    </form>

</body>
</html>

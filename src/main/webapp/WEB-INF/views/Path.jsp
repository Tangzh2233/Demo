<%--
  Created by IntelliJ IDEA.
  User: Tangzhihao
  Date: 2017/10/11
  Time: 9:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = "";
    if(request.getServerPort()!=80){
        path = request.getScheme() + "://" + request.getServerName() + ":" +request.getServerPort() + "/" + request.getContextPath();
    }else{
        path = request.getScheme() + "://" + request.getServerName()  + "/" + request.getContextPath();
    }

    pageContext.setAttribute("basePath",path);
%>

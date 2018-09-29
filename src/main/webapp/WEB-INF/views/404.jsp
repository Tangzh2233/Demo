<%--
  Created by IntelliJ IDEA.
  User: Tangzhihao
  Date: 2017/10/11
  Time: 14:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error Page</title>
    <link rel="stylesheet" type="text/css" href="/resources/css/htmleaf-demo.css">
    <link rel="stylesheet" href="/resources/css/style.css">
    <!--[if IE]>
    <script src="http://libs.useso.com/js/html5shiv/3.7/html5shiv.min.js"></script>
    <![endif]-->
</head>
<body>
<div class="htmleaf-container">
    <header class="htmleaf-header">
        <h1>嗯？这是哪儿？我在那儿？我是谁？ <span>404 page animation - missing book</span></h1>
        <div class="htmleaf-links">
            <a class="htmleaf-icon icon-htmleaf-home-outline" href="http://127.0.0.1:8848/myspringboot/main.html" title="微协的小小窝" <%--target="_blank" 是否重新打开新网页--%>><span> 微协的小小窝</span></a>
            <a class="htmleaf-icon icon-htmleaf-arrow-forward-outline" href="javascript:history.go(-1)" title="此路不通" target="_blank"><span> 此路不通</span></a>
        </div>
    </header>

</div>
<nav class="shelf">
    <a class="book home-page">java虚拟机</a>
    <a class="book about-us">Java Web技术内幕</a>
    <a class="book contact">Spring源码解析</a>
    <a class="book faq">Maven实战</a>

    <span class="book not-found"></span>

    <span class="door left"></span>
    <span class="door right"></span>
</nav>
<h1>Error 404</h1>
<p>糟糕，您要找的书籍不在书架中。。。</p>

</body>
</html>

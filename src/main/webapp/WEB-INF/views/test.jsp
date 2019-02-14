<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no"/>
    <title>jquery验证码插件verify.js|DEMO_jQuery之家-自由分享jQuery、html5、css3的插件库</title>
    <link href="http://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="/resources/css/htmleaf-demo.css">
    <link rel="stylesheet" type="text/css" href="/resources/css/verify.css">
</head>
<body>
<div class="htmleaf-container">
    <header class="htmleaf-header">
        <h1>jquery验证码插件verify.js <span>A jQuery identifying code plugin</span></h1>
        <div class="htmleaf-links">
            <a class="htmleaf-icon icon-htmleaf-home-outline" href="http://www.htmleaf.com/" title="jQuery之家" target="_blank"><span> jQuery之家</span></a>
            <a class="htmleaf-icon icon-htmleaf-arrow-forward-outline" href="http://www.htmleaf.com/jQuery/Form/201711064813.html" title="返回下载页" target="_blank"><span> 返回下载页</span></a>
        </div>
    </header>
    <div class="container">
        <div class="row">
            <div class="col-md-offset-4 col-md-4">
                <h3>普通验证码</h3>
                <div id="mpanel2" ></div>
                <button type="button" id="check-btn" class="verify-btn">确定</button>
            </div>
        </div>
        <hr>
        <div class="row">
            <div class="col-md-offset-4 col-md-4">
                <h3>数字计算验证码</h3>
                <div id="mpanel3" style="margin-top: 20px"></div>
                <button type="button" id="check-btn2" class="verify-btn">确定</button>
            </div>
        </div>
        <hr>
        <div class="row">
            <div class="col-md-offset-4 col-md-4">
                <h3>滑动验证码</h3>
                <p>滑动滑块到最右侧完成验证</p>
                <div id="mpanel1" ></div>
                <p style="margin-top:50px;">拖动方块到空白处完成验证</p>
                <div id="mpanel4" ></div>
            </div>
        </div>
        <hr>
        <div class="row">
            <div class="col-md-offset-4 col-md-4">
                <h3>点选验证码</h3>
                <div id="mpanel5" style="margin-top:50px;"></div>
                <div id="mpanel6" style="margin-top:50px;"></div>
            </div>
        </div>

    </div>
    <div class="related">
        <h3>如果你喜欢这个插件，那么你可能也喜欢:</h3>
        <a href="http://www.htmleaf.com/jQuery/Form/201707184635.html">
            <img src="related/1.jpg" width="300" alt="前端jquery生成验证码插件"/>
            <h3>前端jquery生成验证码插件</h3>
        </a>
        <a href="http://www.htmleaf.com/jQuery/Form/201703284425.html">
            <img src="related/2.jpg" width="300" alt="jQuery滑动解锁插件"/>
            <h3>jQuery滑动解锁插件</h3>
        </a>
    </div>
</div>

<script src="http://cdn.bootcss.com/jquery/1.11.0/jquery.min.js" type="text/javascript"></script>
<script>window.jQuery || document.write('<script src="/resources/js/jquery-1.11.0.min.js"><\/script>')</script>
<script type="text/javascript" src="/resources/js/verify.js" ></script>
<script type="text/javascript">
    $('#mpanel2').codeVerify({
        type : 1,
        width : '400px',
        height : '50px',
        fontSize : '30px',
        codeLength : 6,
        btnId : 'check-btn',
        ready : function() {
        },
        success : function() {
            alert('验证匹配！');
        },
        error : function() {
            alert('验证码不匹配！');
        }
    });


    $('#mpanel3').codeVerify({
        type : 2,
        figure : 100,	//位数，仅在type=2时生效
        arith : 0,	//算法，支持加减乘，不填为随机，仅在type=2时生效
        width : '200px',
        height : '50px',
        fontSize : '30px',
        btnId : 'check-btn2',
        ready : function() {
        },
        success : function() {
            alert('验证匹配！');
        },
        error : function() {
            alert('验证码不匹配！');
        }
    });



    $('#mpanel1').slideVerify({
        type : 1,		//类型
        vOffset : 5,	//误差量，根据需求自行调整
        barSize : {
            width : '80%',
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



    $('#mpanel5').pointsVerify({
        defaultNum : 4,	//默认的文字数量
        checkNum : 2,	//校对的文字数量
        vSpace : 5,	//间隔
        imgName : ['1.jpg', '2.jpg'],
        imgSize : {
            width: '600px',
            height: '200px',
        },
        barSize : {
            width : '600px',
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

    $('#mpanel6').pointsVerify({
        defaultNum : 4,	//默认的文字数量
        checkNum : 2,	//校对的文字数量
        vSpace : 5,	//间隔
        imgName : ['1.jpg', '2.jpg'],
        imgSize : {
            width: '600px',
            height: '200px',
        },
        barSize : {
            width : '600px',
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
</body>
</html>
/*主界面js代码*/
layui.use('element', function(){
			  var element = layui.element();
			  playTips(4000);
			});
    
    /*公告轮播方法*/
    function playTips(interval){
    	var index = 0;
        var $announcement = $('.Tag-home-tips-container>span');
        //自动轮换
        setInterval(function () {
            index++;    //下标更新
            if (index >= $announcement.length) {
                index = 0;
            }
            $announcement.eq(index).stop(true, true).fadeIn().siblings('span').fadeOut();  //下标对应的图片显示，同辈元素隐藏
        }, interval);
    }
    
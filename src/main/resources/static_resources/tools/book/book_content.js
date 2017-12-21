var bookContent = {
    ty: 0,
    cy: 0
};

(function (bookContent) {

    /**
     * 触摸滚动
     */
    bookContent.tapScroll = function () {
        $('body').on('tap', function(e){

            var isTop = $(document).scrollTop() == 0;
            var isBottom = ($(document).height() -
                ($(window).height()+$(document).scrollTop())) == 0;

            var tapY = e.clientY;
            if (!bookContent.ty) {
                bookContent.ty = tapY;
            }
            var height = screen.height;
            if ((tapY > (height / 8 * 5))
                && !isBottom) {
                console.log(bookContent.cy);
                bookContent.cy += height-100;
                //向下滚动
                $("html,body")
                    .animate({scrollTop:bookContent.cy},300);
                return;
            }

            if (tapY < (height / 8 * 3)
                && !isTop) {
                //向上滚动
                bookContent.cy -= height+100;
                $("html,body")
                    .animate({scrollTop:bookContent.cy},300);
                return;
            }

            //点击屏幕中央唤起菜单
            if (bookMenu.isShow()) {
                bookMenu.hide();
            } else {
                bookMenu.show();
            }
        });
    };


    bookContent.init = function () {
        bookContent.tapScroll();
    };

    bookContent.init();
})(bookContent);


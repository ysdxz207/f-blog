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
            if ((tapY > (height / 3 * 2))
                && !isBottom) {
                console.log(bookContent.cy);
                bookContent.cy += height-160;
                //向下滚动
                $("html,body")
                    .animate({scrollTop:bookContent.cy},300);
                return;
            }

            if (tapY < (height / 3 * 1)
                && !isTop) {
                //向上滚动
                bookContent.cy -= height+160;
                $("html,body")
                    .animate({scrollTop:bookContent.cy},300);
                return;
            }
        });
    };


    bookContent.init = function () {
        bookContent.tapScroll();
    };

    bookContent.init();
})(bookContent);


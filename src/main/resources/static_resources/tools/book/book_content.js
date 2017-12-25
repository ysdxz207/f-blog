var bookContent = {
    bookId: undefined,
    lastReadingChapter: '',
    lastReadingChapterLink: '',
    ty: 0,
    cy: 0
};

(function (bookContent) {

    /**
     * 触摸滚动
     */
    bookContent.tapScroll = function () {
        $('body').on('tap', function(e){



            var isTop = $(document).scrollTop() < 10;
            var isBottom = ($(document).height() -
                ($(window).height()+$(document).scrollTop())) == 0;

            var tapY = e.clientY;
            if (!bookContent.ty) {
                bookContent.ty = tapY;
            }
            var height = screen.height;

            //点击屏幕中央唤起菜单
            if (tapY < (height / 8 * 5) && tapY > (height / 8 * 3)) {
                bookMenu.toggle();
                return;
            }

            if (bookMenu.isShow()) {
                return;
            }

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

        });
    };


    bookContent.saveReading = function(){
        var bookReading = {};
            bookReading.bookId = bookContent.bookId;
            bookReading.lastReadingChapterLink =  bookContent.lastReadingChapter;
            bookReading.lastReadingChapterLink = bookContent.lastReadingChapterLink;
        var storage = window.localStorage;
        storage.setItem(bookContent.bookId, bookReading);
        //保存到后端
        $.ajax({
            url: "/book/saveReading",
            data: bookReading,
            method: "POST",
            dataType: "json",
            success: function (result) {
                if (result.statusCode == 200) {

                } else {
                    salert(result.message);
                }
            }
        });
    }

    bookContent.init = function () {

        bookContent.bookId = $('#hidden_book_content_book_id').val();
        bookContent.tapScroll();

        bookContent.saveReading();
    };

    bookContent.init();
})(bookContent);


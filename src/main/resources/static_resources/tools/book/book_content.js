var bookContent = {
    bookId: undefined,
    lastReadingChapter: '',
    lastReadingChapterLink: '',
};

(function (bookContent) {

    /**
     * 触摸滚动
     */
    bookContent.tapScroll = function () {

        $('.book-content-main').on('click', function (e) {

            var lineHeight = 28;
            var isTop = $(document).scrollTop() < 10;
            var isBottom = ($(document).height() -
                ($(window).height() + $(document).scrollTop())) == 0;

            var tapY = e.clientY;

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
                //向下滚动
                $("html,body")
                    .animate({scrollTop: $(document).scrollTop() + height - lineHeight}, 300);
                return;
            }

            if (tapY < (height / 8 * 3)
                && !isTop) {
                //向上滚动
                $("html,body")
                    .animate({scrollTop: $(document).scrollTop() - height + lineHeight}, 300);
                return;
            }

        });
    };


    bookContent.saveReading = function () {
        var bookReading = {};
        bookReading.bookId = bookContent.bookId;
        bookReading.lastReadingChapter = bookContent.lastReadingChapter;
        bookReading.lastReadingChapterLink = bookContent.lastReadingChapterLink;

        //保存到本地缓存
        window.localStorage[bookContent.bookId] = JSON.stringify(bookReading);
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
        bookContent.lastReadingChapter = $('#hidden_book_content_reading_chapter').val();
        bookContent.lastReadingChapterLink = $('#hidden_book_content_reading_chapter_link').val();
        bookContent.tapScroll();

        bookContent.saveReading();
    };

    bookContent.init();
})(bookContent);


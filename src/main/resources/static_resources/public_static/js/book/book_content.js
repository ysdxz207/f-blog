var bookContent = {
    bookId: undefined,
    lastReadingChapter: '',
    lastReadingChapterLink: ''
};

(function (bookContent) {

    /**
     * 触摸滚动
     */
    bookContent.tapScroll = function () {

        $('.book-chapter-content').on('click', function (e) {

            var delay = 10;
            var lineHeight = 28;
            var isTop = $(document).scrollTop() < 10;
            var isBottom = ($(document).height() -
                ($(window).height() + $(document).scrollTop())) == 0;

            var tapX = e.clientX;

            var width = screen.width;
            var height = screen.height;

            //点击屏幕中央唤起菜单
            if (tapX < (width / 3 * 2) && tapX > (width / 3 * 1)) {
                bookMenu.toggle();
                return;
            }

            if (bookMenu.isShow()) {
                return;
            }

            if ((tapX > (width / 3 * 2))
                && !isBottom) {
                //向下滚动
                $('html,body')
                    .animate({scrollTop: $(document).scrollTop() + height - lineHeight}, delay);
                return;
            }

            if (tapX < (width / 3 * 1)
                && !isTop) {
                //向上滚动
                $('html,body')
                    .animate({scrollTop: $(document).scrollTop() - height + lineHeight}, delay);
                return;
            }

        });
    };


    bookContent.saveReading = function () {
        var bookReading = {};
        bookReading.bookId = bookContent.bookId;
        bookReading.lastReadingChapter = bookContent.lastReadingChapter;
        bookReading.lastReadingChapterLink = bookContent.lastReadingChapterLink;
        bookReading.sort = window.localStorage['sort_' + bookContent.bookId]
            ? window.localStorage['sort_' + bookReading.bookId] : 0;
        bookReading.fontSize = bookContent.fontSize;
        bookReading.bgColor = bookContent.bgColor;
        bookReading.fontSize = bookContent.fontSize;
        bookReading.lineHeight = bookContent.lineHeight;

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
    };

    bookContent.bind = function () {
        //章节列表
        $.ajax({
            url: "/book/chapters",
            data: {bookId: bookContent.bookId},
            method: "POST",
            dataType: "json",
            success: function (result) {
                if (result.statusCode == 200) {
                    result.data.forEach(function (chapter, number) {
                        var hasReadClass = (chapter.hasRead || chapter.title == bookContent.lastReadingChapter) ? 'has-read' : '';

                        var url = "/book/chapter?bookId=" + bookContent.bookId + "&link=" +
                            chapter.link + "&chapterName=" + chapter.title;
                        var li = $('<li class="' + hasReadClass + '"><a href="' + url + '" class="loading">'+ chapter.title + '</a></li>');

                        $('#book_chapters_ul').append(li);
                    });

                } else {
                    salert(result.message);
                }
            }
        });

    };

    bookContent.preloadNextPage = function () {
        $('.btn-book-content-next-chapter').preload();
    };
    bookContent.init = function () {

        bookContent.bookId = $('#hidden_book_content_book_id').val();
        bookContent.lastReadingChapter = $('#hidden_book_content_reading_chapter').val();
        bookContent.lastReadingChapterLink = $('#hidden_book_content_reading_chapter_link').val();


        bookContent.bind();
        bookContent.tapScroll();

        bookContent.saveReading();

        bookContent.preloadNextPage();
    };

    bookContent.init();
})(bookContent);


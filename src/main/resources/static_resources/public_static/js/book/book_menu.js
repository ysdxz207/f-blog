var bookMenu = {
    contentY: 0
};

(function (bookMenu) {

    /**
     */
    bookMenu.isShow = function () {
        return bookMenu.menuObj.css('display') != 'none';
    };

    bookMenu.toggle = function () {
        bookMenu.menuObj.toggle();
    };

    bookMenu.bind = function () {

        $('.btn-back-content').on('click', function (e) {
            //滚动回内容记录位置,因为章节列表菜单和章节内容在统一页面，所以直接是html,body
            $('html,body').scrollTop(bookMenu.contentY);

            $('#book_menu_chapters').hide();
            $('.book-content-main').show();
        });

        $('#btn_book_menu_font_setup').on('click', function () {
            // bookMenu.toggle();
        });
        $('#btn_book_menu_font_chapters').on('click', function () {
            //记录一下当前内容滚动位置，当从章节列表返回时再滚动到这个位置
            bookMenu.contentY = $(document).scrollTop();
            //进入章节列表时，把章节列表滚动到顶
            $('html,body').scrollTop(0);

            $('.book-content-main').hide();
            $('#book_menu').hide();
            $('#book_menu_chapters').show();
        });


    };

    /**
     * 预加载章节列表
     */
    bookMenu.preLoadChapters = function() {
        tools.createFloatPage(data);
    };

    bookMenu.init = function () {
        bookMenu.menuObj = $('#book_menu');
        bookMenu.bind();
    };

    bookMenu.init();
})(bookMenu);


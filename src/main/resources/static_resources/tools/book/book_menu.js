var bookMenu = {
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

        $('.btn-back').on('click', function (e) {
            history.back();
        });

        $('.btn-back-content').on('click', function (e) {
            $('#book_menu_chapters').hide();
            $('.book-content-main').show();
        });

        $('#btn_book_menu_font_setup').on('click', function () {
            // bookMenu.toggle();
        });
        $('#btn_book_menu_font_chapters').on('click', function () {
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


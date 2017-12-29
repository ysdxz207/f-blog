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

    bookMenu.makeMenu = function () {

    };

    bookMenu.bind = function () {

        $('.btn-back').on('touchend', function (e) {
            history.back();
        });

        $('.btn-back-content').on('touchend', function (e) {
            $('#book_menu_chapters').hide();
            $('.book-content-main').show(400);
        });

        $('#btn_book_menu_font_setup').on('touchend', function () {
            // bookMenu.toggle();
        });
        $('#btn_book_menu_font_chapters').on('touchend', function () {
            $('.book-content-main').hide();
            $('#book_menu_chapters').show(400);
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


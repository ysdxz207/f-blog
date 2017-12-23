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

        $('.btn-back').on('tap', function (e) {
            history.back();
        });

        $('#btn_book_menu_font_setup').on('tap', function () {
            // bookMenu.toggle();
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


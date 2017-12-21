var bookMenu = {
};

(function (bookMenu) {

    /**
     */
    bookMenu.isShow = function () {

    };

    bookMenu.show = function () {

    };

    bookMenu.hide = function () {

    };

    bookMenu.makeMenu = function () {

    };

    bookMenu.bind = function () {
        $('#btn_back').on('tap', function (e) {
            e.stopPropagation();    //  阻止事件冒泡
            history.back();
        });
    };

    bookMenu.init = function () {
        bookMenu.bind();
        bookMenu.makeMenu();
    };

    bookMenu.init();
})(bookMenu);


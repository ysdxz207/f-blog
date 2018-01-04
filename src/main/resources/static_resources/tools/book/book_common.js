var book = {};

(function (book) {

    book.bind = function () {
        $('.btn-back').on('click', function (e) {
            var url = window.localStorage['last_link'];
            window.localStorage['last_link'] = window.location.href;
            salert(window.location.href);
            location.href = url;
        });
    };

    book.init = function () {
        book.bind();
    };

    book.init();
})(book);


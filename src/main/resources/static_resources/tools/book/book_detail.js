var bookDetail = {
};

(function (bookDetail) {

    bookDetail.bind = function () {

        $('.btn-back').on('tap', function (e) {
            history.back();
        });

        $('.btn-add-book').on('tap', function () {
        });
        $('.btn-del-book').on('tap', function () {
        });
        $('.btn-read-book').on('tap', function () {

            location.href = "/book/chapter?bookId=" + bookDetail.bookId;
        });
    };

    bookDetail.init = function () {
        bookDetail.bookId = $('#hidden_book_detail_book_id').val();
        bookDetail.bind();
    };

    bookDetail.init();
})(bookDetail);


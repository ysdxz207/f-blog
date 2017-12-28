var bookDetail = {
};

(function (bookDetail) {

    bookDetail.bind = function () {

        $('.btn-back').on('touchend', function (e) {
            history.back();
        });

        $('.btn-add-book').on('touchend', function () {
        });
        $('.btn-del-book').on('touchend', function () {
        });
        $('.btn-read-book').on('touchend', function () {

            location.href = "/book/chapter?bookId=" + bookDetail.bookId;
        });
    };

    bookDetail.init = function () {
        bookDetail.bookId = $('#hidden_book_detail_book_id').val();
        bookDetail.bind();
    };

    bookDetail.init();
})(bookDetail);


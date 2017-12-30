var bookDetail = {
};

(function (bookDetail) {

    bookDetail.bind = function () {

        $('.btn-back').on('click', function (e) {
            history.back();
        });

        $('.btn-add-book').on('click', function () {
        });
        $('.btn-del-book').on('click', function () {
        });
        $('.btn-read-book').on('click', function () {

            location.href = "/book/chapter?bookId=" + bookDetail.bookId;
        });
    };

    bookDetail.init = function () {
        bookDetail.bookId = $('#hidden_book_detail_book_id').val();
        bookDetail.bind();
    };

    bookDetail.init();
})(bookDetail);


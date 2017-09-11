var fblog = {
    $tagTop: $('.fblog-tag-top'),
    $articleListContainer: $('#fblog_article_list_container'),
    pageCurrent: 1
};

(function (fblog) {

    fblog.loadTopTags = function () {
        $.getJSON("/tag/top?num=20", function (data) {
            if (!data) {
                return;
            }
            var arr = ["#23D160",
                "#FF3860",
                "#72D0EB",
                "#FFDD57",
                "#4A4A4A",
                "#DC6BDB",
                "#FFB11B",
                "#13CCAB",
                "#FF634B"];

            for (var i = 0; i < data.length; i++) {
                var randomIndex = Math.floor(Math.random() * arr.length);
                var color = arr[randomIndex];
                fblog.$tagTop.append('<span class="label label-default" style="background-color: '
                    + color + ';display: inline-block;border-radius: 1em;margin-left: 6px;margin-bottom: 4px;font-size: 100%;font-weight: 100;line-height: inherit;">' + data[i].name + '</span>');
            }
        })
    };

    fblog.loadArticleList = function () {
        $.getJSON("/article/list", {
            pageCurrent: fblog.pageCurrent
        },function (data) {

            if (data) {
                fblog.$articleListContainer.empty();
                fblog.pageCurrent = data.pageCurrent;
                var html = template('template_article_list', data);
                fblog.$articleListContainer.html(html);
            }
        });

    };

    fblog.bind = function () {
        $(document).on('click', '.pager .next a', function() {
            fblog.pageCurrent += 1;
            fblog.loadArticleList();
        });

        $(document).on('click', '.pager .previous a', function () {
            fblog.pageCurrent -= 1;
            fblog.loadArticleList();
        });
    };

    fblog.init = function () {
        fblog.loadTopTags();
        fblog.loadArticleList();
        fblog.bind();

    };


    fblog.init();
})(fblog);


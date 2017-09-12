var fblog = {
    $tagTop: $('.fblog-tag-top'),
    $articleListContainer: $('#fblog_article_list_container'),
    $containerWidgetTags: $('#container_widget_tags'),
    $containerWidgetCategories: $('#container_widget_categories'),
    PAGE_CURRENT_SESSION_KEY: 'fblog_page_current'
};

(function (fblog) {

    fblog.getPageCurrent = function () {
        var obj = $.session.get(fblog.PAGE_CURRENT_SESSION_KEY);
        return obj ? parseInt(obj) : 1;
    };

    fblog.setOrSumPageCurrent = function (sum, pageCurrent) {
        var page = sum ? fblog.getPageCurrent() + sum : pageCurrent;
        $.session.set(fblog.PAGE_CURRENT_SESSION_KEY, page);
    };

    fblog.loadTopTags = function () {
        $.getJSON("/tag/top?num=20", function (data) {
            if (!data) {
                return;
            }
            fblog.$containerWidgetTags.empty();
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
                data[i].color = color;
            }
            var html = template('template_widget_tags', {list:data});
            fblog.$containerWidgetTags.html(html);
        })
    };

    fblog.loadArticleList = function () {
        $.getJSON("/article/list", {
            pageCurrent: fblog.getPageCurrent()
        },function (data) {

            if (data) {
                fblog.$articleListContainer.empty();
                fblog.setOrSumPageCurrent(null, data.pageCurrent);
                var html = template('template_article_list', data);
                fblog.$articleListContainer.html(html);
            }
        });

    };

    fblog.bind = function () {
        $(document).on('click', '.pager .next a', function() {
            fblog.setOrSumPageCurrent(1);
            fblog.loadArticleList();
        });

        $(document).on('click', '.pager .previous a', function () {
            fblog.setOrSumPageCurrent(-1);
            fblog.loadArticleList();
        });
    };

    fblog.loadCategorys = function () {
        $.getJSON("/category/list", {
            pageCurrent: 1,
            pageSize: 10
        }, function (data) {

            if (data) {
                fblog.$containerWidgetCategories.empty();
                var html = template('template_widget_categories', data);
                fblog.$containerWidgetCategories.html(html);
            }
        });
    };
    fblog.init = function () {
        fblog.loadTopTags();
        fblog.loadCategorys();
        fblog.loadArticleList();
        fblog.bind();

    };


    fblog.init();
})(fblog);


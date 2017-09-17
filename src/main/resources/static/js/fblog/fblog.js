var fblog = {
    $tagTop: $('.fblog-tag-top'),
    $articleListContainer: $('#fblog_article_list_container'),
    $containerWidgetTags: $('#container_widget_tags'),
    $containerWidgetCategories: $('#container_widget_categories')
};

(function (fblog) {

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



    fblog.bind = function () {

        //搜索
        $(document).on('click', '#btn_fblog_search', function () {
            if (fblog.checkSearch()) {

                $('form[role=search]').submit();
            }
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
        fblog.bind();

    };

    fblog.checkSearch = function () {
        var search = $('#input_fblog_search').val();
        if (!search) {
            return false;
        }
        return true;
    }

    fblog.init();
})(fblog);


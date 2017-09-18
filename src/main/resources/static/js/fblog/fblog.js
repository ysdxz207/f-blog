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
            fblog.$containerWidgetTags.parent().delay('fast').fadeTo(500, 1);
            var arr = ["#23D160",
                "#FF3860",
                "#72D0EB",
                "#FFDD57",
                "#4A4A4A",
                "#DC6BDB",
                "#FFB11B",
                "#13CCAB",
                "#FF634B"];

           $.each(data, function (i, tag) {
               var randomIndex = Math.floor(Math.random() * arr.length);
               var color = arr[randomIndex];

               var $tag = $('<span class="label label-default" style="background-color: '
                   + color + ';display: inline-block;border-radius: 1em;margin-left: 6px;margin-bottom: 4px;font-size: 100%;font-weight: 100;line-height: inherit;">' +
                   '    <a href="/?tags=' + tag.name + '">' + tag.name + '</a>' +
                   '</span>');
               fblog.$containerWidgetTags.append($tag);
               $tag.fadeIn(700);
           });
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
                fblog.$containerWidgetCategories.parent().fadeTo(300,1);
                $.each(data.list, function (i, category) {
                    var $category = $('<a href="/?category='
                        + category.name
                        + '" class="list-group-item">' + category.name + '</a>');
                    $category.hide();
                    fblog.$containerWidgetCategories.append($category);
                    $category.fadeIn(500);
                });
            }
        });
    };
    fblog.init = function () {

        fblog.bind();
        fblog.loadTopTags();
        fblog.loadCategorys();

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


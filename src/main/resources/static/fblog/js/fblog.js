var fblog = {
    $tagTop: $('.fblog-tag-top'),
    $articleListContainer: $('#fblog_article_list_container'),
    $containerWidgetTags: $('#container_widget_tags'),
    $containerWidgetCategories: $('#container_widget_categories'),
    colors: ["#23D160",
            "#FF3860",
            "#72D0EB",
            "#FFDD57",
            "#4A4A4A",
            "#DC6BDB",
            "#FFB11B",
            "#13CCAB",
            "#FF634B"]
};

(function (fblog) {

    fblog.loadTopTags = function () {
        $.getJSON("/fblog/tag/top?num=20", function (data) {
            if (!data) {
                return;
            }
            fblog.$containerWidgetTags.empty();
            fblog.$containerWidgetTags.parent().delay('fast').fadeTo(500, 1);

           $.each(data, function (i, tag) {
               var color = fblog.getRandomColor();

               var $tag = $('<span class="label label-default" style="background-color: '
                   + color + ';display: inline-block;border-radius: 1em;margin-left: 6px;margin-bottom: 4px;font-size: 100%;font-weight: 100;line-height: inherit;">' +
                   '    <a href=' + tag.name + '"/fblog/?tags=">' + tag.name + '</a>' +
                   '</span>');
               fblog.$containerWidgetTags.append($tag);
               $tag.fadeIn(700);
           });
        })
    };

    /**
     * 获取随机颜色
     * @returns {*}
     */
    fblog.getRandomColor = function () {
        var randomIndex = Math.floor(Math.random() * fblog.colors.length);
        var color = fblog.colors[randomIndex];
        return color;
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
        $.getJSON("/fblog/category/list", {
            pageCurrent: 1,
            pageSize: 10
        }, function (data) {

            if (data) {
                fblog.$containerWidgetCategories.empty();
                fblog.$containerWidgetCategories.parent().fadeTo(300,1);
                $.each(data.list, function (i, category) {
                    var $category = $('<a href='
                        + category.name
                        + '"/fblog/?category=" class="list-group-item">' + category.name + '</a>');
                    $category.hide();
                    fblog.$containerWidgetCategories.append($category);
                    $category.fadeIn(500);
                });
            }
        });
    };

    fblog.loadArticleTags = function () {
        var $articleDetailTagsContent = $('#article_detail_tags_content');
        var articleId = $('#hidden_article_detail_article_id').val();
        if ($articleDetailTagsContent) {
            $.getJSON("/fblog/article/tags?articleId=" + articleId, function (data) {
                if (data.statusCode != 200) {
                    return;
                }
                $.each(data.data, function (i, tag) {
                    var color = fblog.getRandomColor();
                    var $tag = $('<span class="label label-default" style="background-color: '
                        + color + ';display: inline-block;border-radius: 1em;margin-left: 6px;margin-bottom: 4px;font-size: 100%;font-weight: 100;line-height: inherit;">' +
                        '    <a href=' + tag.name + '"/fblog/?tags=">' + tag.name + '</a>' +
                        '</span>');

                    $articleDetailTagsContent.append($tag);
                    $tag.fadeIn();
                });
            });
        }
    };
    fblog.init = function () {

        fblog.bind();
        fblog.loadTopTags();
        fblog.loadCategorys();

        //detail
        fblog.loadArticleTags();
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


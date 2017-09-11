var fblog = {
    $tagTop: $('.fblog-tag-top')
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
                "#DC9FB4",
                "#FFB11B",
                "#13CCAB",
                "#FF634B"];

            for (var i = 0; i < data.length; i++) {
                var randomIndex = Math.floor(Math.random() * arr.length);
                console.log(randomIndex);
                var color = arr[randomIndex];
                fblog.$tagTop.append('<span class="label label-default" style="background-color: '
                    + color + ';display: inline-block;border-radius: 1em;margin-left: 6px;margin-bottom: 4px;font-size: 100%;font-weight: 100;line-height: inherit;">' + data[i].name + '</span>');
            }
        })
    };

    fblog.init = function () {
        fblog.loadTopTags();
    };


    fblog.init();
})(fblog);


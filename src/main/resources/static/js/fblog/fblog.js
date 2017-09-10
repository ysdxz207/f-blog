var fblog = {
    $tagTop: $('.fblog-tag-top')
};

(function (fblog) {

    fblog.loadTopTags = function () {
       $.getJSON("/tag/top", function (data) {
           if (!data) {
               return;
           }
           var arr = ["#23D160",
               "#FF3860",
               "#72D0EB",
               "#FFDD57",
               "#4A4A4A",
               "#DC9FB4",
                "#FFB11B"];

           for (var i = 0; i < data.length; i ++) {
               var randomIndex = Math.floor(Math.random()*arr.length);
               var color = arr[randomIndex];
               fblog.$tagTop.append('<span class="label" style="background-color: '
                   + color + '">' + data[i].name + '</span>');
           }
       })
    };

    fblog.init = function () {
      fblog.loadTopTags();
    };


    fblog.init();
})(fblog);


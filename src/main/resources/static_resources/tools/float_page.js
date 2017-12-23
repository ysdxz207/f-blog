var floatPage = {

};

(function (floatPage) {

    floatPage.create = function (html) {
        var div = $('div').css('width', '100%')
            .css('height', '100%')
            .css('background-color', 'white')
            .css('position', 'absolute')
            .css('top', '0')
            .css('left', '0')
            .css('z-index', '100')
            .html(html);
        div.show().animate({"right":"0"},function(){
        });
    };

    floatPage.init = function () {

    };
    floatPage.init();
})(floatPage);

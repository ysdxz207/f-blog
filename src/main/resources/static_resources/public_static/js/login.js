/**
 * 登录
 * @author Moses
 * @date 2017-12-31
 */

;
$(function () {
    jQuery.fn.slogin = function(loginUrl, successPage, btn){

        var $btn = $(btn);
        $btn.attr("disabled", true).val('登录中..');
        var form = $($btn.parents('form'));
        $.ajax({
            url: loginUrl,
            data: form.serialize(),
            method: "POST",
            dataType: "json",
            success: function (result) {
                if (result.statusCode == 200) {
                    location.href = successPage;
                } else {
                    $('#error_msg').text(result.message);
                    $btn.attr("disabled", false).text('登录');
                }
            }
        });
    };

    jQuery.fn.slogout = function(redirectPage, btn){

        var $btn = $(btn);
        $btn.attr("disabled", true).val('退出中..');
        var form = $($btn.parents('form'));
        $.ajax({
            url: "/logout",
            data: form.serialize(),
            method: "GET",
            dataType: "json",
            success: function (result) {
                if (result.statusCode == 200) {
                    location.href = redirectPage;
                } else {
                    $('#error_msg').text(result.message);
                    $btn.attr("disabled", false).text('退出登录');
                }
            }
        });
    };




    slogin = jQuery.fn.slogin;
    slogout = jQuery.fn.slogout;

});
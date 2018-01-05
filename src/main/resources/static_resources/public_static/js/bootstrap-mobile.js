/**
 * 移动端
 * @author Moses
 * @date 2018-01-05
 */

;
jQuery.fn.sloading = function(){
    smask('<div style="color: #FFFFFF"><img src="/images/loading.gif" width="64"/><p>加载中...</p></div>');
};
sloading = jQuery.fn.sloading;
removeSloading = removeSmask;


/**
 *
 */

$(function(){

    //点透
    FastClick.attach(document.body);


    var mobile =false;
    if(/Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent)) {
        mobile = true;
    };


    if (mobile) {
        $('.loading').on('click', function (e) {
        	sloading();
        });
    }
});
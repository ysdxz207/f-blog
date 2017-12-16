/**
 * 遮罩层
 * @author Moses
 * @date 2017-12-16
 */

;
jQuery.fn.smask = function(html){
	var modalMain = $('<div>').attr('id', 'alert-modal').addClass('modal fade').addClass('text-center'),
		htmlObj = $(html),
		htmlObjWidth = htmlObj.width();

	if (!htmlObjWidth) {
        htmlObj.css('width', '90%');
	}
	htmlObj.appendTo(modalMain);
    htmlObj.css('margin', '50vh auto 0');
    htmlObj.css('transform', 'translateY(-50%)');

	modalMain.modal('show').on('hidden.bs.modal', function (e) {
		modalMain.remove();
	});
}
smask = jQuery.fn.smask;

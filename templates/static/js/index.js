/**
 * ================================================
 *   设置根元素font-size
 * 当设备宽度为375(iPhone6)时，根元素font-size=16px;
 × ================================================
 */
(function (doc, win) {
    var docEl = win.document.documentElement;
    var resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize';

    var refreshRem = function () {
        var clientWidth = win.innerWidth
            || doc.documentElement.clientWidth
            || doc.body.clientWidth;

        if (!clientWidth) return;
        var fz;
        var width = clientWidth > 750 ? 750 : clientWidth;
        console.log(width)
        fz = 100 * width / 750;
        docEl.style.fontSize = fz + 'px';//这样每一份也是16px,即1rem=16px
    };

    if (!doc.addEventListener) return;
    win.addEventListener(resizeEvt, refreshRem, false);
    doc.addEventListener('DOMContentLoaded', refreshRem, false);
    refreshRem();

})(document, window);

/**
 * 
 */

$(function () {
    var change = function() {
        var url = '/ical/' +  $("input[name='startWeeek']:checked").val();
        var keyword = $('#keyword').val();
        if (keyword) {
            url += '/' + encodeURI(keyword);
        }
        $('#ical').attr('href', url);
        $('#ical-webcal').attr('href', 'webcal://' + location.host + url);
        var host = location.host;
    };
    
    $("input[name='startWeeek']").change(change);
    $('#keyword').bind('textchange', change);
    
    change();
});
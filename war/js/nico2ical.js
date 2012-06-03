/**
 * 
 */

$(function () {
    $('.jq-placeholder').ahPlaceholder({
        placeholderColor : 'silver',
        placeholderAttr  : 'title',
        likeApple        : false
    });
    
    $('#logout').click(function() {
        return confirm('ログアウトしますか？');
    });
    
    $(".alert").alert();
    
    var change = function() {
        var url = '/ical/' +  $("input[name='startWeeek']:checked").val();
        var keyword = $('#keyword').val();
        if (keyword) {
            url += '/' + encodeURI(keyword);
        }
        $('#ical').attr('href', 'http://' + location.host + url);
        $('#ical-webcal').attr('href', 'webcal://' + location.host + url);
        var host = location.host;
    };
    
    $("input[name='startWeeek']").change(change);
    $('#keyword').bind('textchange', change);
    
    change();
    
    $('#mycalendar-tab a').click(function (e) {
        if ($(this).attr('data-disabled')) {
            return;
        }
        e.preventDefault();
        $(this).tab('show');
    });
    
    var CalendarSummaries = function(params) {
        var self = this;
        this.selectedValue = ko.observable(params.selectedValue);
        this.notifyErrorMail = ko.observable(params.notifyErrorMail);
        this.keywords = ko.observable(params.keywords);
        this.isSaveSuccess = ko.observable(false);
        this.isSavaError = ko.observable(false);
        this.isDisconnectError = ko.observable(false);
        this.isEnableButton = function() {
            return 0 < self.selectedValue().length;
        }.bind(this);
        this.updateCalendar = function() {
            self.isSaveSuccess(false);
            self.isSavaError(false);
            $('#save-calendar').button('loading');
            $('#mycalendar-tab a').attr('data-disabled', 'true');
            var disabledInputs = $('#form-settings input:not(disabled)');
            disabledInputs.attr('disabled', 'disabled');
            $.ajax(
                    '/myCalendar/save',
                    {
                        type: 'POST',
                        dataType: 'json',
                        data: {
                            'csrftoken': $.cookie('csrftoken'),
                            'notifyErrorMail': self.notifyErrorMail(),
                            'keywords': self.keywords().split(' '),
                            'calendarId': self.selectedValue()
                            }
                    }
            ).success(function(data) {
                $('#save-calendar').button('complete');
                self.isSaveSuccess(true);
            }).error(function(xhr, textStatus, errorThrown) {
                $('#save-calendar').button('error');
                self.isSavaError(true);
            }).complete(function() {
                $('#mycalendar-tab a').removeAttr('data-disabled');
                disabledInputs.removeAttr('disabled');
            });
        }.bind(this);
        this.disConnectCalendar = function() {
            if (confirm('実行すると連携が解除されます。よろしいですか？')) {
                self.isDisconnectError(false);
                $('#disconnect-calendar').button('loading');
                $('#mycalendar-tab a').attr('data-disabled', 'true');
                $.ajax(
                        '/myCalendar/disConnect',
                        {
                            type: 'POST',
                            dataType: 'json',
                            data: {
                                'csrftoken': $.cookie('csrftoken')
                                }
                        }
                ).success(function(data) {
                    $('#disconnect-calendar').hide();
                    $('#connect-calendar').removeClass('hidden');
                    $('#nav-mycalendar-settings').hide();
                }).error(function(xhr, textStatus, errorThrown) {
                    $('#disconnect-calendar').addClass('btn-danger').button('error');
                    self.isDisconnectError(true);
                }).complete(function() {
                    $('#mycalendar-tab a').removeAttr('data-disabled');
                });
            }
        }.bind(this);
    };
    ko.applyBindings(new CalendarSummaries(
            {
                'selectedValue': $('input[name=calendarSummaries]:checked').val() || '',
                'notifyErrorMail': $('#notify-error-email').is(':checked'),
                'keywords': $('#keywords').val()
                }
            )
    );
});
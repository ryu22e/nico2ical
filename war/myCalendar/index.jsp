<%@page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@page import="com.google.appengine.api.users.User"%>
<%@page import="org.slim3.util.ServletContextLocator"%>
<%@page pageEncoding="UTF-8" isELIgnored="false"%>
<%
  User user = UserServiceFactory.getUserService().getCurrentUser();
  request.setAttribute("user", user);
%>
<c:import url="/layout.jsp">
  <c:param name="subTitle" value="マイカレンダー"/>
  <c:param name="content">
    <header class="jumbotron subhead">
      <h1>マイカレンダー</h1>
      <p>
        「マイカレンダー」で、お使いのGoogle Calendarにニコニコ生放送の放送予定日をインポートできます。
      </p>
    </header>
    <c:if test="${isDisabled}">
      <div class="alert alert-block alert-error fade in">
        <a class="close" data-dismiss="alert" href="#">×</a>
        <h4 class="alert-heading">一時的にインポート機能を無効にしています</h4>
        <p>前回のインポートに失敗したため、一時的にインポート機能を無効にしています。</p>
        <p>以下の原因で失敗した可能性があります。</p>
        <ul>
        <li>（１）インポート対象のGoogle Calendarに書き込み権限がない。</li>
        <li>（２）Google Calendar APIの許可有効期限が終了した。</li>
        <li>（３）Google Calendar APIの１日の呼び出し上限をオーバーした。</li>
        <li>（４）nico2icalの動作環境であるGoogle App Engineに何らかの障害が発生した。</li>
        </ul>
        <p>お手数ですが、以下の手順でGoogle Calendarとの連携を再設定してください。</p>
        <p>（１）（２）が原因の場合は、これでインポートが再開されます。</p>
        <ul>
        <li>「連携のON/OFF」タブをクリックし、「Google Calendar連携OFF」ボタンでGoogle Calendarとの連携を一旦解除する。</li>
        <li>「Google Calendar連携ON」ボタンでGoogle Calendarとの連携を再び開始する。</li>
        </ul>
        <p>（３）（４）が原因の場合は、上記手順ではインポートは再開されません。</p>
        <p>管理人の方で、下記ブログにアナウンスを載せますので、しばらくお待ちください。</p>
        <p><a target="_blank" href="http://blog.livedoor.jp/ryu22e/archives/cat_60250685.html">http://blog.livedoor.jp/ryu22e/archives/cat_60250685.html</a></p>
      </div>
    </c:if>
    <ul class="nav nav-tabs" id="mycalendar-tab">
      <c:if test="${storedCredential}">
        <li id="nav-mycalendar-settings" class="active">
          <a data-target="#mycalendar-settings" href="#">設定</a>
        </li>
        <li>
          <a data-target="#mycalendar-on-off" href="#">連携のON/OFF</a>
        </li>
      </c:if>
      <c:if test="${!storedCredential}">
        <li class="active">
          <a data-target="#mycalendar-on-off" href="#">連携のON/OFF</a>
        </li>
      </c:if>
    </ul>
    
    <div class="tab-content">
      <c:if test="${storedCredential}">
        <div class="tab-pane<c:if test="${storedCredential}"> active</c:if>" id="mycalendar-settings">
          <form id="form-settings" class="well" data-bind="submit: updateCalendar">
            <fieldset>
              <legend>連携先</legend>
              <div class="control-group">
                <label class="control-label">連携するカレンダー名を選択して下さい（Google Calendar API利用上限の関係上、複数カレンダーの連携はできないようにしていますm(__)m）。</label>
                <div class="controls">
                  <c:if test="${calendars != null}">
                    <c:forEach var="c" items="${calendars}">
                      <label class="radio" for="calendarSummaries-${f:h(c.id)}">
                        <input type="radio" id="calendarSummaries-${f:h(c.id)}" ${f:radio("calendarSummaries", c.id)} data-bind="checked: selectedValue" />
                        ${f:h(c.summary)}
                      </label>
                    </c:forEach>
                  </c:if>
                </div><!-- /.controls -->
              </div><!-- /.control-group -->
            </fieldset>
            <fieldset>
              <legend>エラー通知</legend>
              <div class="control-group">
                <label class="control-label">インポート時にエラーが発生した場合にメールで通知します。</label>
                <div class="controls">
                  <label class="checkbox" for="notify-error-email">
                    <input id="notify-error-email" data-bind="checked: notifyErrorMail" type="checkbox" ${f:checkbox("notifyErrorMail")} />
                    通知する
                  </label>
                </div><!-- /.controls -->
              </div><!-- /.control-group -->
            </fieldset>
            <fieldset>
              <legend>インポート条件</legend>
                <div class="control-group">
                <label class="control-label">タイトルまたは説明文に含まれるキーワード</label>
                <div class="controls">
                <% 
                String[] str = {"aa","bb","cc"};
                pageContext.setAttribute("data",str);
                %>
                  <input id="keyword" 
                         class="input-xxlarge" 
                         type="text" 
                         maxlength="50" 
                         data-bind="value: keyword" 
                         placeholder="スペースで区切ると複数のキーワードを指定できます。"
                         value="${f:h(keyword)}" />
                </div><!-- /.controls -->
              </div><!-- /.control-group -->
            </fieldset>
            <div class="form-actions">
              <input id="save-calendar" 
                     class="btn" 
                     type="button" 
                     value="登録" 
                     data-loading-text="登録中..." 
                     data-complete-text="登録しました" 
                     data-error-text="登録に失敗しました" 
                     data-bind="css: { 'btn-primary': isEnableButton(), 'btn-success': isSaveSuccess(), 'btn-danger': isSavaError() }, enable: selectedValue().length > 0, click: updateCalendar"
                      />
            </div>
          </form>
        </div><!-- /#mycalendar-settings -->
      </c:if>
      <div class="tab-pane<c:if test="${!storedCredential}"> active</c:if>" id="mycalendar-on-off">
        <form class="well">
          <div class="control-group">
            <div class="controls">
              <c:if test="${storedCredential}">
                <a id="disconnect-calendar" 
                   href="#" 
                   class="btn" 
                   data-loading-text="連携解除中..." 
                   data-error-text="連携解除に失敗しました" 
                   data-bind="css: {'btn-danger': isDisconnectError()}, click: disConnectCalendar">
                   Google Calendar連携OFF
                </a>
              </c:if>
              <a id="connect-calendar" 
                 href="${f:url('connect')}" 
                 class="btn btn-primary<c:if test="${storedCredential}"> hidden</c:if>">
                 Google Calendar連携ON
              </a>
            </div>
          </div><!-- /.control-group -->
        </form>
      </div><!-- /#mycalendar-on-off -->
    </div><!-- /.tab-content -->
  </c:param>
</c:import>

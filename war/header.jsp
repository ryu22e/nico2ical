<%@page pageEncoding="UTF-8" isELIgnored="false"%>
<%@page import="com.google.appengine.api.users.UserServiceFactory"%>
<% boolean isUserLoggedIn = UserServiceFactory.getUserService().isUserLoggedIn(); %>
<div class="navbar navbar-fixed-top">
  <div class="navbar-inner">
    <div class="container">
      <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse"> <span class="icon-bar"></span>
        <span class="icon-bar"></span> <span class="icon-bar"></span>
      </a> <a class="brand" href="/">nico2ical</a>
      <div class="btn-group pull-right">
        <% if (isUserLoggedIn) { %>
            <a id="logout" class="btn" href="/Logout">ログアウト</a>
        <% } else { %>
            <a class="btn" href="/Login">Googleアカウントでログイン</a>
        <% } %>
      </div>
      <div class="nav-collapse">
        <ul class="nav">
          <% request.setAttribute("servletPath", request.getServletPath());%>
          <li class="<c:if test="${servletPath == '/Index.jsp'}">active</c:if>"><a href="/">TOP</a></li>
          <% if (isUserLoggedIn) { %>
            <li class="<c:if test="${servletPath == '/myCalendar/index.jsp'}">active</c:if>"><a href="/myCalendar/">マイカレンダー</a></li>
          <% } %>
          <li class="<c:if test="${servletPath == '/help/index.jsp'}">active</c:if>">
            <a href="/help/">ヘルプ</a>
          </li>
          <li>
            <a href="https://twitter.com/share" class="twitter-share-button" data-url="http://nico2ical.appspot.com/" data-text="nico2ical" data-via="ryu22e" data-lang="ja" data-hashtags="nico2ical">ツイート</a>
<script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0];if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src="//platform.twitter.com/widgets.js";fjs.parentNode.insertBefore(js,fjs);}}(document,"script","twitter-wjs");</script>
          </li>
        </ul>
      </div><!--/.nav-collapse -->
    </div>
  </div>
</div>

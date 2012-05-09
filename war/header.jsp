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
            <a id="logout" class="btn" href="/Logout?destinationURL=/">ログアウト</a>
        <% } else { %>
            <a class="btn" href="/Login?destinationURL=/">googleアカウントでログイン</a>
        <% } %>
      </div>
      <div class="nav-collapse">
        <ul class="nav">
          <% request.setAttribute("servletPath", request.getServletPath());%>
          <li class="<c:if test="${servletPath == '/Index.jsp'}">active</c:if>"><a href="/">TOP</a></li>
          <% if (isUserLoggedIn) { %>
            <li class="<c:if test="${servletPath == '/myCalendar/index.jsp'}">active</c:if>"><a href="/myCalendar/">マイカレンダー</a></li>
          <% } %>
          <li>
            <a href="http://twitter.com/share"
            class="twitter-share-button" data-url="${f:h(url)}"
            data-count="horizontal" data-via="ryu22e" data-lang="ja">ツイート</a>
            <script type="text/javascript"
              src="http://platform.twitter.com/widgets.js"></script>
          </li>
        </ul>
      </div><!--/.nav-collapse -->
    </div>
  </div>
</div>

<%@page pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="utf-8">
  <title>nico2ical - ${f:h(param.subTitle)}</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta name="description" content="ニコニコ生放送RSSをiCalendarファイルに変換するWebサービスです。">
  <meta name="keywords" content="ニコニコ生放送,iCalendar,カレンダー">
  <meta property="og:title" content="nico2ical">
  <meta property="og:type" content="website">
  <meta property="og:description" content="nico2icalは、ニコニコ生放送の公式放送日程をiCalendarファイルで配信するWebサービスです。">
  <meta property="og:url" content="${f:h(url)}">
  <meta property="og:image" content="${f:h(url)}image.png">
  <meta property="og:site_name" content="nico2ical">
  <meta property="og:email" content="ryu22e@gmail.com">
  <meta property="fb:admins" content="ryu22e">

  <!-- Le styles -->
  <link href="/assets/css/bootstrap.min.css?6094328250?5328067741" rel="stylesheet">
  <link href="/css/global.css?2747753671" rel="stylesheet">
  <link href="/assets/css/bootstrap-responsive.min.css" rel="stylesheet">

  <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
  <!--[if lt IE 9]>
    <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
  <![endif]-->
</head>
<body>
  <c:import url="/header.jsp"/>
  
  <div class="container">
  ${param.content}
  
  <c:import url="/footer.jsp"/>
  <script src="/assets/js/jquery-1.7.1.min.js"></script>
  <script src="/assets/js/jquery.textchange.min.js?2011-06-29"></script>
  <script src="/assets/js/jquery.ah-placeholder.js"></script>
  <script src="/assets/js/jquery.cookie.js?1542280969"></script>
  <script src="/assets/js/knockout-2.1.0.js"></script>
  <script src="/assets/js/bootstrap-collapse.js?3742199752"></script>
  <script src="/assets/js/bootstrap-button.js?3731199092"></script>
  <script src="/assets/js/bootstrap-tab.js?0731539092"></script>
  <script src="/assets/js/bootstrap-alert.js?3730639092"></script>
  <script src="/js/nico2ical.js?1251950016"></script>
  </div>
</body>
</html>

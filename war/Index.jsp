<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="utf-8">
  <title>nico2ical</title>
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
  <div class="navbar navbar-fixed-top">
    <div class="navbar-inner">
      <div class="container">
        <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse"> <span class="icon-bar"></span>
          <span class="icon-bar"></span> <span class="icon-bar"></span>
        </a> <a class="brand" href="/">nico2ical</a>
        <div class="nav-collapse">
          <ul class="nav">
            <li class="active"><a href="/">TOP</a></li>
            <li>
              <a href="http://twitter.com/share"
              class="twitter-share-button" data-url="${f:h(url)}"
              data-count="horizontal" data-via="ryu22e" data-lang="ja">ツイート</a>
              <script type="text/javascript"
                src="http://platform.twitter.com/widgets.js"></script>
            </li>
          </ul>
        </div>
        <!--/.nav-collapse -->
      </div>
    </div>
  </div>
  
  <div class="container">
    <header class="jumbotron subhead">
      <h1>nico2ical</h1>
      <p>
        nico2icalは、<a target="_blank" href="http://live.nicovideo.jp/">ニコニコ生放送</a>の公式放送日程をiCalendarファイルで配信するWebサービスです。
      </p>
      <p>
        ご意見、ご要望は<a target="_blank" href="https://twitter.com/ryu22e">https://twitter.com/ryu22e</a>、またはメールアドレス（ryu22e[at]gmail.com）まで。
      </p>
    </header>
    
    <fieldset>
      <legend>条件の指定</legend>
      <div class="control-group">
        <div class="control-group">
        <label class="control-label">過去分の放送をいつまで含める？</label>
          <div class="controls">
            <label class="radio inline" for="startWeeek-1"><input type="radio" id="startWeeek-1" name="startWeeek" value="1" checked="checked" />１週間前まで</label>
            <label class="radio inline" for="startWeeek-2"><input type="radio" id="startWeeek-2" name="startWeeek" value="2" />２週間前まで</label> 
            <label class="radio inline" for="startWeeek-3"><input type="radio" id="startWeeek-3" name="startWeeek" value="3" />３週間前まで</label> 
            <label class="radio inline" for="startWeeek-4"><input type="radio" id="startWeeek-4" name="startWeeek" value="4" />４週間前まで</label>
          </div>
        </div>
        <div class="control-group">
          <label class="control-label" for="keyword">タイトルまたは説明文に含まれるキーワード（β機能）</label>
          <div class="controls">
              <input class="input-xxlarge" type="text" id="keyword" maxlength="50" placeholder="スペースで区切ると複数のキーワードを指定できます。" />
          </div>
        </div>
      </div>
    </fieldset>
    <fieldset>
      <legend>iCalendarファイルのダウンロード</legend>
      <div class="control-group">
        <div class="controls">
          <a class="btn btn-primary" id="ical" href="/ical/1">iCalendarファイルをダウンロード</a>
          <a class="btn btn-primary" id="ical-webcal" href="">iCalendarファイルをダウンロード(webcal方式)</a>
        </div>
      </div>
    </fieldset>
  </div>
  <script type="text/javascript">
    window.___gcfg = {
      lang: 'ja'
    };
    
    (function() {
      var po = document.createElement('script'); po.type = 'text/javascript'; po.async = true;
      po.src = 'https://apis.google.com/js/plusone.js';
      var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(po, s);
    })();
  </script>
  <script src="/js/jquery-1.7.1.min.js"></script>
  <script src="/js/jquery.textchange.min.js?2011-06-29"></script>
  <script src="/js/jquery.ah-placeholder.js"></script>
  <script src="/assets/js/bootstrap-collapse.js?3742199752"></script>
  <script src="/js/nico2ical.js?1251950016"></script>
</body>
</html>
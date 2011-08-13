<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta property="og:title" content="nico2ical" />
<meta property="og:type" content="website" />
<meta property="og:description" content="nico2icalは、ニコニコ生放送の公式放送日程をiCalendarファイルで配信するWebサービスです。" />
<meta property="og:url" content="${f:h(url)}" />
<meta property="og:image" content="${f:h(url)}image.png" />
<meta property="og:site_name" content="nico2ical" />
<meta property="og:email" content="ryu22e@gmail.com" />
<meta property="fb:admins" content="ryu22e" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="description" content="ニコニコ生放送RSSをiCalendarファイルに変換するWebサービスです。" />
<meta name="keywords" content="ニコニコ生放送,iCalendar,カレンダー" />
<link rel="shortcut icon" href="/favicon.ico" />
<link rel="stylesheet" href="/css/global.css?2011-08-01" media="screen" />
<script type="text/javascript" charset="UTF-8"
    src="/js/jquery-1.6.1.min.js"></script>
<script type="text/javascript" charset="UTF-8"
    src="/js/jquery.textchange.min.js?2011-06-29"></script>
<script type="text/javascript" charset="UTF-8"
    src="/js/nico2ical.js?2011-06-29"></script>
<title>nico2ical</title>
</head>
<body>
    <h1 class="site-title">nico2ical</h1>
    <ul class="social-buttons">
        <li><a href="http://clip.livedoor.com/redirect?link=${f:h(url)}&title=nico2ical%20-%20nico2ical&ie=UTF-8" class="ldclip-redirect" title="この記事をクリップ！"><img src="http://parts.blog.livedoor.jp/img/cmn/clip_16_12_b.gif" width="16" height="12" alt="この記事をクリップ！" style="border: none;vertical-align: middle;" /></a></li>
        <li><a href="http://b.hatena.ne.jp/entry/${f:h(url)}" class="hatena-bookmark-button" data-hatena-bookmark-title="nico2ical" data-hatena-bookmark-layout="standard" title="このエントリーをはてなブックマークに追加"><img src="http://b.st-hatena.com/images/entry-button/button-only.gif" alt="このエントリーをはてなブックマークに追加" width="20" height="20" style="border: none;" /></a><script type="text/javascript" src="http://b.st-hatena.com/js/bookmark_button.js" charset="utf-8" async="async"></script></li>
        <li><a href="http://twitter.com/share" class="twitter-share-button" data-url="${f:h(url)}" data-count="horizontal" data-via="ryu22e" data-lang="ja">ツイート</a><script type="text/javascript" src="http://platform.twitter.com/widgets.js"></script></li>
        <li class="separete"><g:plusone size="medium"></g:plusone></li>
        <li><a href="http://mixi.jp/share.pl" class="mixi-check-button" data-key="602111f45d53927a2df968443cf01f0850315f1d" data-url="${f:h(url)}" data-button="button-1">mixiチェック</a><script type="text/javascript" src="http://static.mixi.jp/js/share.js"></script></li>
        <li><div id="fb-root"></div><script src="http://connect.facebook.net/ja_JP/all.js#appId=232314416808905&amp;xfbml=1"></script><fb:like href="${f:h(url)}" send="false" layout="button_count" width="450" show_faces="true" font="arial"></fb:like></li>
    </ul>
    <p class="clearLeft">
    nico2icalは、<a target="_blank" href="http://live.nicovideo.jp/">ニコニコ生放送</a>の公式放送日程をiCalendarファイルで配信するWebサービスです。
    </p>
    <p>ご意見、ご要望は<a target="_blank" href="https://twitter.com/ryu22e">https://twitter.com/ryu22e</a>、またはメールアドレス（ryu22e[at]gmail.com）まで。</p>
    <table class="condition">
    <tbody>
    <tr>
        <td class="condition-title">過去分</td>
        <td>
            <input type="radio" id="startWeeek-1" name="startWeeek" value="1" checked="checked" /><label for="startWeeek-1">１週間前まで</label>
            <input type="radio" id="startWeeek-2" name="startWeeek" value="2" /><label for="startWeeek-2">２週間前まで</label>
            <input type="radio" id="startWeeek-3" name="startWeeek" value="3" /><label for="startWeeek-3">３週間前まで</label>
            <input type="radio" id="startWeeek-4" name="startWeeek" value="4" /><label for="startWeeek-4">４週間前まで</label>
        </td>
    </tr>
    <tr>
        <td class="condition-title">キーワード（β機能）</td>
        <td><input type="text" id="keyword" maxlength="50" /><br />スペースで区切ると複数のキーワードを指定できます。</td>
    </tr>
    </tbody>
    </table>
    <a id="ical" href="/ical/1">iCalendar</a><a id="ical-webcal" href="">iCalendar(webcal方式)</a>
    <script type="text/javascript" src="https://apis.google.com/js/plusone.js">
    {lang: 'ja'}
    </script>
</body>
</html>
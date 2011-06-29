<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="description" content="ニコニコ生放送RSSをiCalendarファイルに変換するWebサービスです。" />
<meta name="keywords" content="ニコニコ生放送,iCalendar,カレンダー" />
<link rel="shortcut icon" href="/favicon.ico" />
<link rel="stylesheet" href="/css/global.css?2011-06-29" media="screen" />
<script type="text/javascript" charset="UTF-8"
    src="/js/jquery-1.6.1.min.js"></script>
<script type="text/javascript" charset="UTF-8"
    src="/js/jquery.textchange.min.js?2011-06-29"></script>
<script type="text/javascript" charset="UTF-8"
    src="/js/nico2ical.js?2011-06-29"></script>
<title>nico2cal</title>
</head>
<body>
    <h1>ニコニコ生放送RSS to iCalendar</h1>
    <p>
    <a target="_blank" href="http://live.nicovideo.jp/rss">ニコニコ生放送RSS</a>をiCalendarファイルに変換するWebサービスです。
    </p>
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
        <td><input type="text" id="keyword" maxlength="50" /></td>
    </tr>
    </tbody>
    </table>
    <a id="ical" href="/ical/1">iCalendar</a><a id="ical-webcal" href="">iCalendar(webcal方式)</a>
</body>
</html>
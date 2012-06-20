<%@page pageEncoding="UTF-8" isELIgnored="false"%>
<c:import url="/layout.jsp">
  <c:param name="subTitle" value="ヘルプ"/>
  <c:param name="content">
  <header class="jumbotron subhead" id="overview">
    <h1>ヘルプ</h1>
    <p>お使いの環境に合わせたご利用方法を解説しています。</p>
    <div class="subnav">
      <ul class="nav nav-pills">
        <li><a href="#ical">MacのiCalからnico2icalを利用したい</a></li>
        <li><a href="#thunderbird">Thunderbird/Lightingからnico2icalを利用したい</a></li>
        <li><a href="#googlecalendar">Google Calendarからnico2icalを利用したい</a></li>
      </ul>
    </div>
    <hr>
    <section id="ical">
      <h1>MacのiCalからnico2icalを利用したい</h1>
      <div class="row">
        <div class="span12">
          <p><a href="/">トップページ</a>から、以下の手順でiCalendarファイルをダウンロードします。</p>
          <div class="thumbnail">
            <img src="/img/help-example-for-ical.png" alt="利用例（MacのiCalの場合）" title="利用例（MacのiCalの場合）" />
          </div>
          <p>iCalが自動的に起動されて、以下の画面になります（Safari以外のブラウザをご利用の場合は、起動するアプリを選択するダイアログが表示される場合があります）。</p>
          <p>「照会」をクリックするとiCalでnico2icalから配信される放送予定日を受信できるようになります。</p>
          <div class="thumbnail">
            <img src="/img/help-ical.png" alt="利用例（MacのiCalの場合）" title="利用例（MacのiCalの場合）" />
          </div>
        </div>
      </div>
    </section>
    <hr>
    <section id="thunderbird">
      <h1>Thunderbird/Lightingからnico2icalを利用したい</h1>
      <div class="row">
        <div class="span12">
          <p>Thunderbird/Lightingのインストール方法については省略します。</p>
          <p><a href="/">トップページ</a>から、以下の手順でiCalendarファイルのURLをコピーします。</p>
          <div class="thumbnail">
            <img src="/img/help-example-for-thunderbird.png" alt="利用例（Thunderbird/Lightingの場合）" title="利用例（Thunderbird/Lightingの場合）" />
          </div>
          <p>Thunderbird/Lightingのメニューバーから[ファイル] - [新規作成] - [カレンダー...]を選んで、以下の手順で新規カレンダーを作成すると、Thunderbird/Lightingでnico2icalから配信される放送予定日を受信できるようになります。</p>
          <div class="thumbnail">
            <img src="/img/help-thunderbird-1.png" alt="Thunderbird/Lightingの設定手順1" title="Thunderbird/Lightingの設定手順1" />
            <img src="/img/help-thunderbird-2.png" alt="Thunderbird/Lightingの設定手順2" title="Thunderbird/Lightingの設定手順2" />
            <img src="/img/help-thunderbird-3.png" alt="Thunderbird/Lightingの設定手順3" title="Thunderbird/Lightingの設定手順3" />
          </div>
        </div>
      </div>
    </section>
    <hr>
    <section id="googlecalendar">
      <h1>Google Calendarからnico2icalを利用したい</h1>
      <p>Google Calendarをご利用の場合は、iCalendarファイルのURLを取り込む方法と、「マイカレンダー」機能を利用してGoogle Calendarに放送予定日をインポートする方法があります。</p>
      <p>それぞれ以下の特徴があります。</p>
      <table class="table table-bordered table-striped">
      <thead>
        <tr>
          <th>取り込む方法</th>
          <th>編集可・不可</th>
          <th>過去分放送の配信制限</th>
          <th>取り込む頻度</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td>iCalendarファイルのURLを取り込む方法</td>
          <td>不可</td>
          <td>1〜4週間まで配信される</td>
          <td>1時間に1回</td>
        </tr>
        <tr>
          <td>「マイカレンダー」機能を利用してGoogle Calendarに放送予定日をインポートする方法</td>
          <td>可（<a target="_blank" href="http://support.google.com/calendar/bin/answer.py?hl=ja&answer=99357">CalDavが利用できるカレンダーアプリからの編集もできる</a>）</td>
          <td>Google Calendarに直接書き込まれるため、配信制限はない</td>
          <td>12時間に1回</td>
        </tr>
      </tbody>
      </table>
      <h2>iCalendarファイルのURLを取り込む</h2>
      <div class="row">
        <div class="span12">
          <p><a href="/">トップページ</a>から、以下の手順でiCalendarファイルのURLをコピーします。</p>
          <div class="thumbnail">
            <img src="/img/help-example-for-googlecalendar.png" alt="利用例（Google Calendarの場合）" title="利用例（Google Calendarの場合）" />
          </div>
          <p><a target="_blank" href="https://www.google.com/calendar/render">Google Calendar</a>を開いて、左側のメニューから[他のカレンダー] - [URL で追加]を選択します。</p>
          <div class="thumbnail">
            <img src="/img/help-googlecalendar-1.png" alt="Google Calendarの設定手順1" title="Google Calendarの設定手順1" />
          </div>
          <p>iCalendarファイルのURLを貼り付けて「カレンダーを追加」を選択すると、Google Calendarでnico2icalから配信される放送予定日を受信できるようになります。</p>
          <div class="thumbnail">
            <img src="/img/help-googlecalendar-2.png" alt="Google Calendarの設定手順2" title="Google Calendarの設定手順2" />
          </div>
        </div>
      </div>
      <h2>「マイカレンダー」機能を利用してGoogle Calendarに放送予定日をインポートする</h2>
      <div class="row">
        <div class="span12">
          <p>nico2icalの画面右上から「Googleアカウントでログイン」を選択し、画面上メニューの「マイカレンダー」を選択します（Googleの）。</p>
          <div class="thumbnail">
            <img src="/img/help-mycalendar-1.png" alt="Goolge Calendar連携をONにする" title="Goolge Calendar連携をONにする" />
          </div>
          <p>Google Calendarへのアクセス許可を求める画面で「アクセスを許可」を選択します。</p>
          <div class="thumbnail">
            <img src="/img/help-mycalendar-2.png" alt="Goolge Calendarへのアクセスを許可する" title="Goolge Calendarへのアクセスを許可する" />
          </div>
          <p>マイカレンダーの「設定」画面で以下の手順で設定すると、Google Calendarでnico2icalから配信される放送予定日を受信できるようになります。</p>
          <div class="thumbnail">
            <img src="/img/help-mycalendar-3.png" alt="マイカレンダーの設定" title="マイカレンダーの設定" />
          </div>
        </div>
      </div>
    </section>
  </header>
  </c:param>
</c:import>

<c:import url="/layout.jsp">
  <c:param name="subTitle" value="Top"/>
  <c:param name="content">
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
  </c:param>
</c:import>
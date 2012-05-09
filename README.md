# igo-gaeのインストール

下記手順で[igo-gae](https://github.com/sile/igo-gae)をmavenローカルリポジトリにインストールする必要がある。

    git clone git://github.com/sile/igo-gae.git
    #$IGO_VERSIONには最新バージョンの番号を入れる
    mvn install:install-file -Dfile=igo-gae/gae-app/igo-morp/war/WEB-INF/lib/igo-$IGO_VERSION-gae.jar -DgroupId=net.reduls.igo -DartifactId=igo -Dversion=$IGO_VERSION -Dpackaging=jar

#ライセンス
----------
Copyright &copy; 2011 Ryuji TSUTSUI
Licensed under the [Apache License, Version 2.0][Apache]
 
 [Apache]: http://www.apache.org/licenses/LICENSE-2.0

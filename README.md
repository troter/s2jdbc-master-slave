マスタースレーブ構成用のS2JDBCのJdbcManager
===========================================

pom.xmlに追記する設定
---------------------

**mavenリポジトリを追加**

    <repositories>
      <repository>
        <id>troter.jp/release</id>
        <name>TROTER.JP Release Maven2 Repository</name>
        <url>http://troter.jp/maven2/release</url>
      </repository>
    </repositories>

**依存関係を追加**

    <dependencies>
      <dependency>
        <groupId>jp.troter</groupId>
        <artifactId>s2jdbc-master-slave</artifactId>
        <version>1.0.3-SNAPSHOT</version>
      </dependency>
    </dependencies>

設定
----

**s2jdbc.diconの設定例**

    <?xml version="1.0" encoding="UTF-8"?>
    <!DOCTYPE components PUBLIC "-//SEASAR//DTD S2Container 2.4//EN"
            "http://www.seasar.org/dtd/components24.dtd">
    <components>
        <include path="s2jdbc-master.dicon"/><!-- masterJdbcManagerの登録を行う -->
        <include path="s2jdbc-slave1.dicon"/><!-- slave1JdbcManagerの登録を行う -->
        <include path="s2jdbc-slave2.dicon"/><!-- slave2JdbcManagerの登録を行う -->
    
        <!-- AbstractServiceではこのjdbcManagerを利用する -->
        <component name="jdbcManager"
          class="jp.troter.seasar.extension.jdbc.manager.MasterSlaveJdbcManagerProxy">
          <property name="masterSlaveJdbcManagerFactoryName">"masterSlaveJdbcManagerFactory"</property>
        </component>
    
        <!-- ファクトリ。
         | マスタースレーブのJdbcManagerの作成を担当
         | アプリケーションでマスタースレーブの切り替えを指定する場合は
         | MasterSlaveContextとあわせて利用する。
         |-->
        <component name="masterSlaveJdbcManagerFactory"
          class="jp.troter.seasar.extension.jdbc.manager.MasterSlaveJdbcManagerFactoryImpl">
          <property name="masterJdbcManagerName">"master"</property>
          <initMethod name="addSlaveJdbcManagerName"><arg>"slave1"</arg></initMethod>
          <initMethod name="addSlaveJdbcManagerName"><arg>"slave2"</arg></initMethod>
        </component>

        <!-- コンテキストオブジェクト。
         | マスタースレーブ切り替えなどを担当
         |-->
        <component class="jp.troter.seasar.extension.jdbc.manager.MasterSlaveContextImpl">
          <property name="masterSlaveExceptionHanlderClass">
            @jp.troter.seasar.extension.jdbc.handler.DefaultMasterSlaveExceptionHandler@class
          </property>
          <initMethod name="addMasterSlaveJdbcManagerFactoryName">
            <arg>"masterSlaveJdbcManagerFactory"</arg>
          </initMethod>
        </component>
    </components>

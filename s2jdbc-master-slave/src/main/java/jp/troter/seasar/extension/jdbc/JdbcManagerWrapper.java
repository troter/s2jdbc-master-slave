package jp.troter.seasar.extension.jdbc;

import org.seasar.extension.jdbc.JdbcManager;

public interface JdbcManagerWrapper extends JdbcManager {

    /**
     * ラップしている<code>JdbcManager</code>を取得します。
     * @return
     */
    JdbcManager getJdbcManager();
}

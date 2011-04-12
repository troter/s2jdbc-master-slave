package jp.troter.seasar.extension.jdbc;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;

public interface JdbcManagerWrapper extends JdbcManager, JdbcManagerImplementor {

    /**
     * ラップしている<code>JdbcManager</code>を取得します。
     * @return
     */
    JdbcManager getJdbcManager();

    public static class Methods {
        public static JdbcManagerImplementor implementor(JdbcManager jdbcManager) {
            if (jdbcManager instanceof JdbcManagerImplementor) {
                return ((JdbcManagerImplementor)jdbcManager);
            }
            throw new UnsupportedOperationException();
        }
    }
}

package jp.troter.seasar.extension.jdbc;

import java.util.List;

import org.seasar.extension.jdbc.JdbcManager;

public interface MasterSlaveJdbcManagerFactory {

    /**
     * マスターとして使用する<code>JdbcManager</code>のコンポーネント名を取得します。
     * @return マスターとして使用する<code>JdbcManager</code>のコンポーネント名
     */
    String getMasterJdbcManagerName();

    /**
     * マスターとして使用する<code>JdbcManager</code>のコンポーネント名を設定します。
     * @param name マスターとして使用する<code>JdbcManager</code>のコンポーネント名
     */
    void setMasterJdbcManagerName(String name);

    /**
     * スレーブとして使用する<code>JdbcManager</code>のコンポーネント名一覧を追加します。
     * @return
     */
    List<String> getSlaveJdbcManagerNames();

    /**
     * スレーブとして使用する<code>JdbcManager</code>のコンポーネント名を追加します。
     * @param name スレーブとして使用する<code>JdbcManager</code>のコンポーネント名
     */
    void addSlaveJdbcManagerName(String name);

    /**
     * スレーブとして使用する<code>JdbcManager</code>のコンポーネント名を削除します。
     * @param name スレーブとして使用する<code>JdbcManager</code>のコンポーネント名
     */
    void removeSlaveJdbcManagerName(String name);

    /**
     * 優先して使用する<code>JdbcManager</code>のコンポーネント名を取得します。
     * @return
     */
    String getJdbcManagerName();

    /**
     * 優先して使用する<code>JdbcManager</code>のコンポーネント名を設定します。
     * @param name
     */
    void setJdbcManagerName(String name);

    /**
     * マスターとして使用する<code>JdbcManager</code>を取得します。 
     * @return
     */
    JdbcManager getMasterJdbcManager();

    /**
     * スレーブもしくはマスターとして使用する<code>JdbcManager</code>を取得します。 
     * @return
     */
    JdbcManager getSlaveOrMasterJdbcManager();

}

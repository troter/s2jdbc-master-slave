package jp.troter.seasar.extension.jdbc.manager;

import java.util.List;

import jp.troter.seasar.extension.jdbc.MasterSlaveJdbcManagerFactory;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.util.tiger.CollectionsUtil;

public class MasterSlaveJdbcManagerFactoryImpl implements
        MasterSlaveJdbcManagerFactory {

    /**
     * マスターの<code>JdbcManager</code>のコンポーネント名。
     */
    protected String masterJdbcManagerName;

    /**
     * スレーブの<code>JdbcManager</code>のコンポーネント名一覧を管理します。
     */
    protected List<String> slaveJdbcManagerNames = CollectionsUtil.newCopyOnWriteArrayList();

    /**
     * 動的な<code>JdbcManager</code>のコンポーネント名を管理します。
     */
    protected ThreadLocal<String> jdbcManagerName = new ThreadLocal<String>();

    /**
     * S2コンテナです。
     */
    protected S2Container container;

    @Override
    public String getMasterJdbcManagerName() {
        return masterJdbcManagerName;
    }

    @Override
    public void setMasterJdbcManagerName(String name) {
        masterJdbcManagerName = name;
    }

    @Override
    public List<String> getSlaveJdbcManagerNames() {
        return CollectionsUtil.newArrayList(slaveJdbcManagerNames);
    }

    @Override
    public void addSlaveJdbcManagerName(String name) {
        slaveJdbcManagerNames.add(name);
    }

    @Override
    public void removeSlaveJdbcManagerName(String name) {
        slaveJdbcManagerNames.remove(name);
    }

    @Override
    public String getJdbcManagerName() {
        String name = jdbcManagerName.get();
        if (! isValidSlaveJdbcManagerName(name)) {
            name = getMasterJdbcManagerName();
        }
        return name;
    }

    @Override
    public void setJdbcManagerName(String name) {
        jdbcManagerName.set(name);
    }

    @Override
    public JdbcManager getMasterJdbcManager() {
        String name = getMasterJdbcManagerName();

        JdbcManager jdbcManager
            = (JdbcManager) container.getRoot().getComponent(
                    getJdbcManagerComponentName(name));
        return new MasterJdbcManagerWrapper(jdbcManager);
    }

    @Override
    public JdbcManager getSlaveOrMasterJdbcManager() {
        String name = getJdbcManagerName();
        if (isMasterJdbcManagerName(name)) {
            return getMasterJdbcManager();
        }

        JdbcManager jdbcManager
            = (JdbcManager) container.getRoot().getComponent(
                    getJdbcManagerComponentName(name));
        return new SlaveJdbcManagerWrapper(jdbcManager);
    }

    /**
     * JdbcManagerのコンポーネント名を返します。
     * 
     * @param name
     *            dao.nameのようなdaoの後ろのサブパッケージ名
     * @return コンポーネント名
     */
    protected String getJdbcManagerComponentName(String name) {
        String jmName = name;
        if (StringUtil.isEmpty(jmName)) {
            return "jdbcManager";
        }
        return jmName + "JdbcManager";
    }

    /**
     * コンポーネント名はマスターとして利用する<code>JdbcManager</code>のコンポーネント名である
     * @param name
     * @return
     */
    protected boolean isMasterJdbcManagerName(String name) {
        return StringUtil.equals(name, getMasterJdbcManagerName());
    }

    /**
     * スレーブとして利用できる<code>JdbcManager</code>のコンポーネント名か。
     * @param name
     *            <code>JdbcManager</code>のコンポーネント名
     * @return スレーブとして利用できる<code>JdbcManager</code>のコンポーネント名の場合 <code>true</code><br>
     *         スレーブとして利用できない<code>JdbcManager</code>のコンポーネント名の場合 <code>false</code>
     */
    protected boolean isValidSlaveJdbcManagerName(String name) {
        return name != null && getSlaveJdbcManagerNames().contains(name);
    }

}

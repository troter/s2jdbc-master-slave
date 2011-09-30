package jp.troter.seasar.extension.jdbc.manager;

import java.util.List;
import java.util.Set;

import jp.troter.seasar.extension.jdbc.JdbcManagerWrapper;
import jp.troter.seasar.extension.jdbc.MasterSlaveContext;
import jp.troter.seasar.extension.jdbc.MasterSlaveJdbcManagerFactory;
import jp.troter.seasar.extension.jdbc.MasterSlaveJdbcManagerFactoryUtil;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.util.tiger.CollectionsUtil;

public class MasterSlaveContextImpl implements MasterSlaveContext {

    Set<String> masterSlaveJdbcManagerFactoryNames = CollectionsUtil.newCopyOnWriteArraySet();

    /**
     * S2コンテナです。
     */
    protected S2Container container;

    /**
     * @return S2コンテナ
     */
    public S2Container getContainer() {
        return container;
    }

    /**
     * @param container
     *            S2コンテナ
     */
    public void setContainer(S2Container container) {
        this.container = container;
    }

    @Override
    public void useMaster() {
        for (String name : masterSlaveJdbcManagerFactoryNames) {
            useMaster(name);
        }
    }

    @Override
    public void useMaster(String name) {
        if (! masterSlaveJdbcManagerFactoryNames.contains(name)) {
            throw new IllegalArgumentException("name");
        }
        MasterSlaveJdbcManagerFactoryUtil.useMaster(getMasterSlaveJdbcManagerFactory(name));
    }

    @Override
    public void useSlave() {
        for (String name : masterSlaveJdbcManagerFactoryNames) {
            useSlave(name);
        }
    }

    @Override
    public void useSlave(String name) {
        if (! masterSlaveJdbcManagerFactoryNames.contains(name)) {
            throw new IllegalArgumentException("name");
        }
        MasterSlaveJdbcManagerFactoryUtil.useSlave(getMasterSlaveJdbcManagerFactory(name));
    }

    @Override
    public boolean isMaster() {
        boolean master = true;
        for (String name : masterSlaveJdbcManagerFactoryNames) {
            master &= isMaster(name);
        }
        return master;
    }

    @Override
    public boolean isMaster(String name) {
        if (! masterSlaveJdbcManagerFactoryNames.contains(name)) {
            throw new IllegalArgumentException("name");
        }
        return MasterSlaveJdbcManagerFactoryUtil.isCurrentJdbcManagerNameMaster(getMasterSlaveJdbcManagerFactory(name));
    }

    @Override
    public boolean isSlave() {
        boolean slave = true;
        for (String name : masterSlaveJdbcManagerFactoryNames) {
            slave &= isSlave(name);
        }
        return slave;
    }

    @Override
    public boolean isSlave(String name) {
        if (! masterSlaveJdbcManagerFactoryNames.contains(name)) {
            throw new IllegalArgumentException("name");
        }
        return ! MasterSlaveJdbcManagerFactoryUtil.isCurrentJdbcManagerNameMaster(getMasterSlaveJdbcManagerFactory(name));
    }

    @Override
    public boolean isMix() {
        return ! isMaster() && ! isSlave();
    }

    @Override
    synchronized public void removeCurrentSlave() {
        for (String name : masterSlaveJdbcManagerFactoryNames) {
            if (isMaster(name)) {
                continue;
            }
            MasterSlaveJdbcManagerFactory factory = getMasterSlaveJdbcManagerFactory(name);
            JdbcManagerWrapper jdbcManager = factory.getSlaveOrMasterJdbcManager();
            try {
                jdbcManager.selectBySql(BeanMap.class, factory.getValidationQuery()).getResultList();
            } catch (Exception e) {
                factory.removeSlaveJdbcManagerName(jdbcManager.getName());
            }
        }
    }

    @Override
    public void addMasterSlaveJdbcManagerFactoryName(String name) {
        masterSlaveJdbcManagerFactoryNames.add(name);
    }

    @Override
    public List<MasterSlaveJdbcManagerFactory> getMasterSlaveJdbcManagerFactories() {
        List<MasterSlaveJdbcManagerFactory> factories = CollectionsUtil.newArrayList();
        for (String name : masterSlaveJdbcManagerFactoryNames) {
            factories.add(getMasterSlaveJdbcManagerFactory(name));
        }
        return factories;
    }

    protected MasterSlaveJdbcManagerFactory getMasterSlaveJdbcManagerFactory(String name) {
        MasterSlaveJdbcManagerFactory factory
            = (MasterSlaveJdbcManagerFactory) container.getRoot().getComponent(name);
        return factory;
    }

}

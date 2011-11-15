package jp.troter.seasar.extension.jdbc.manager;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;
import jp.troter.seasar.extension.jdbc.MasterSlaveExceptionHandler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seasar.extension.jdbc.SqlSelect;
import org.seasar.extension.jdbc.manager.JdbcManagerImpl;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.unit.Seasar2;
import org.seasar.framework.unit.TestContext;
import org.seasar.framework.unit.annotation.EasyMock;
import org.seasar.framework.unit.annotation.EasyMockType;

@RunWith(Seasar2.class)
public class MasterSlaveContextImplTest {

    public static class ShouldRemoveSlaveException extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }

    public static class Handler implements MasterSlaveExceptionHandler {
        @Override public boolean isShouldRemove(Throwable throwable) {
            if (throwable instanceof ShouldRemoveSlaveException) {
                return true;
            }
            return false;
        }
    }

    public static class MockJdbcManager extends JdbcManagerImpl {
        SqlSelect<BeanMap> sqlSelect;
        public void setSqlSelect(SqlSelect<BeanMap> sqlSelect) {
            this.sqlSelect = sqlSelect;
        }
        @SuppressWarnings("unchecked")
        @Override public <T> SqlSelect<T> selectBySql(Class<T> baseClass, String sql,
                Object... params) {
            if (baseClass.isAssignableFrom(BeanMap.class)) {
                return (SqlSelect<T>)sqlSelect;
            }
            return super.selectBySql(baseClass, sql, params);
        }
    }

    @EasyMock(EasyMockType.NICE)
    SqlSelect<BeanMap> exceptionSqlSelect;

    @EasyMock(EasyMockType.NICE)
    SqlSelect<BeanMap> nopSqlSelect;

    protected TestContext context;
    MasterSlaveContextImpl masterSlaveContext;
    MasterSlaveJdbcManagerFactoryImpl factory1;
    MasterSlaveJdbcManagerFactoryImpl factory2;
    MockJdbcManager master;
    MockJdbcManager slave1;
    MockJdbcManager slave2;

    public MasterSlaveJdbcManagerFactoryImpl createFactory() {
        MasterSlaveJdbcManagerFactoryImpl factory = new MasterSlaveJdbcManagerFactoryImpl();
        factory.setMasterJdbcManagerName("master");

        factory.container = (S2Container)context.getComponent("container");
        return factory;
    }

    public void before() {
        context.register(factory1 = createFactory(), "masterSlaveJdbcManagerFactory1");
        context.register(factory2 = createFactory(), "masterSlaveJdbcManagerFactory2");

        masterSlaveContext = new MasterSlaveContextImpl();
        masterSlaveContext.addMasterSlaveJdbcManagerFactoryName("masterSlaveJdbcManagerFactory1");
        masterSlaveContext.addMasterSlaveJdbcManagerFactoryName("masterSlaveJdbcManagerFactory2");
        masterSlaveContext.container = (S2Container)context.getComponent("container");
        masterSlaveContext.setMasterSlaveExceptionHanlderClass(Handler.class);

        context.register(master = new MockJdbcManager(), "masterJdbcManager");
        context.register(slave1 = new MockJdbcManager(), "slave1JdbcManager");
        context.register(slave2 = new MockJdbcManager(), "slave2JdbcManager");
    }

    @Test
    public void switch_master_slave() {
        factory1.addSlaveJdbcManagerName("slave1");
        factory2.addSlaveJdbcManagerName("slave1");

        masterSlaveContext.useSlave();
        assertTrue(masterSlaveContext.isSlave());
        assertTrue(masterSlaveContext.isSlave("masterSlaveJdbcManagerFactory1"));
        assertTrue(masterSlaveContext.isSlave("masterSlaveJdbcManagerFactory2"));

        assertFalse(masterSlaveContext.isMaster());
        assertTrue(masterSlaveContext.isSlave());
        assertFalse(masterSlaveContext.isMix());

        masterSlaveContext.useMaster();
        assertTrue(masterSlaveContext.isMaster());
        assertTrue(masterSlaveContext.isMaster("masterSlaveJdbcManagerFactory1"));
        assertTrue(masterSlaveContext.isMaster("masterSlaveJdbcManagerFactory2"));

        assertTrue(masterSlaveContext.isMaster());
        assertFalse(masterSlaveContext.isSlave());
        assertFalse(masterSlaveContext.isMix());

        masterSlaveContext.useMaster();
        masterSlaveContext.useSlave("masterSlaveJdbcManagerFactory2");
        assertTrue(masterSlaveContext.isMaster("masterSlaveJdbcManagerFactory1"));
        assertFalse(masterSlaveContext.isSlave("masterSlaveJdbcManagerFactory1"));
        assertFalse(masterSlaveContext.isMaster("masterSlaveJdbcManagerFactory2"));
        assertTrue(masterSlaveContext.isSlave("masterSlaveJdbcManagerFactory2"));

        assertFalse(masterSlaveContext.isMaster());
        assertFalse(masterSlaveContext.isSlave());
        assertTrue(masterSlaveContext.isMix());

        masterSlaveContext.useMaster();
        masterSlaveContext.useSlave("masterSlaveJdbcManagerFactory1");
        masterSlaveContext.useSlave("masterSlaveJdbcManagerFactory2");
        assertTrue(masterSlaveContext.isSlave("masterSlaveJdbcManagerFactory1"));
        assertTrue(masterSlaveContext.isSlave("masterSlaveJdbcManagerFactory2"));

        assertFalse(masterSlaveContext.isMaster());
        assertTrue(masterSlaveContext.isSlave());
        assertFalse(masterSlaveContext.isMix());
    }

    public void recordRemove_slave() throws Exception {
        expect(exceptionSqlSelect.getResultList())
            .andThrow(new ShouldRemoveSlaveException())
            .andThrow(new ShouldRemoveSlaveException());
    }

    @Test
    public void remove_slave() {
        factory1.addSlaveJdbcManagerName("slave1");
        factory2.addSlaveJdbcManagerName("slave2");

        masterSlaveContext.useSlave();
        assertTrue(masterSlaveContext.isSlave());
        assertTrue(masterSlaveContext.isSlave("masterSlaveJdbcManagerFactory1"));
        assertTrue(masterSlaveContext.isSlave("masterSlaveJdbcManagerFactory2"));

        // 片方だけ切り離す
        slave1.setSqlSelect(nopSqlSelect);
        slave2.setSqlSelect(exceptionSqlSelect);
        masterSlaveContext.useSlave();
        masterSlaveContext.removeCurrentSlave();
        assertTrue(masterSlaveContext.isMix());
        assertTrue(masterSlaveContext.isSlave("masterSlaveJdbcManagerFactory1"));
        assertTrue(masterSlaveContext.isMaster("masterSlaveJdbcManagerFactory2"));

        // 両方切り離す
        slave1.setSqlSelect(exceptionSqlSelect);
        slave2.setSqlSelect(exceptionSqlSelect);
        masterSlaveContext.useSlave();
        masterSlaveContext.removeCurrentSlave();
        assertTrue(masterSlaveContext.isMaster());
        assertFalse(masterSlaveContext.isSlave());
        assertFalse(masterSlaveContext.isMix());
        assertTrue(masterSlaveContext.isMaster("masterSlaveJdbcManagerFactory1"));
        assertTrue(masterSlaveContext.isMaster("masterSlaveJdbcManagerFactory2"));

        // 両方切り離すと、スレーブを利用するようにしてもマスターになる
        masterSlaveContext.useSlave();
        assertTrue(masterSlaveContext.isMaster());
        assertFalse(masterSlaveContext.isSlave());
    }
}

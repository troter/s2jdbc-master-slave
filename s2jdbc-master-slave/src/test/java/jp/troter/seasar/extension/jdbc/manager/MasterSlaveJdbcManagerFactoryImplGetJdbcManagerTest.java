package jp.troter.seasar.extension.jdbc.manager;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.manager.JdbcManagerImpl;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.unit.Seasar2;
import org.seasar.framework.unit.TestContext;

@RunWith(Seasar2.class)
public class MasterSlaveJdbcManagerFactoryImplGetJdbcManagerTest {

    public static class FakeJdbcManager extends JdbcManagerImpl { }

    protected TestContext context;
    MasterSlaveJdbcManagerFactoryImpl i;
    JdbcManager master;
    JdbcManager slave1;
    JdbcManager slave2;

    public void before() {
        i = new MasterSlaveJdbcManagerFactoryImpl();
        i.setMasterJdbcManagerName("master");

        i.container = (S2Container)context.getComponent("container");

        context.register(master = new FakeJdbcManager(), "masterJdbcManager");
        context.register(slave1 = new FakeJdbcManager(), "slave1JdbcManager");
        context.register(slave2 = new FakeJdbcManager(), "slave2JdbcManager");
    }

    @Test
    public void smoke() {
        assertThat(i.getMasterJdbcManager(), instanceOf(MasterJdbcManagerWrapper.class));
        assertThat(i.getMasterJdbcManager().getJdbcManager(), is(master));
        assertThat(i.getSlaveOrMasterJdbcManager(), instanceOf(MasterJdbcManagerWrapper.class));
        assertThat(i.getSlaveOrMasterJdbcManager().getJdbcManager(), is(master));
    }

    @Test
    public void slave() {
        i.addSlaveJdbcManagerName("slave1");

        i.setJdbcManagerName("slave1");
        assertThat(i.getMasterJdbcManager(), instanceOf(MasterJdbcManagerWrapper.class));
        assertThat(i.getMasterJdbcManager().getJdbcManager(), is(master));
        assertThat(i.getSlaveOrMasterJdbcManager(), instanceOf(SlaveJdbcManagerWrapper.class));
        assertThat(i.getSlaveOrMasterJdbcManager().getJdbcManager(), is(slave1));

        i.setJdbcManagerName("not exists");
        assertThat(i.getMasterJdbcManager(), instanceOf(MasterJdbcManagerWrapper.class));
        assertThat(i.getMasterJdbcManager().getJdbcManager(), is(master));
        assertThat(i.getSlaveOrMasterJdbcManager(), instanceOf(MasterJdbcManagerWrapper.class));
        assertThat(i.getSlaveOrMasterJdbcManager().getJdbcManager(), is(master));

        i.addSlaveJdbcManagerName("slave2");
        i.setJdbcManagerName("slave2");
        assertThat(i.getMasterJdbcManager(), instanceOf(MasterJdbcManagerWrapper.class));
        assertThat(i.getMasterJdbcManager().getJdbcManager(), is(master));
        assertThat(i.getSlaveOrMasterJdbcManager(), instanceOf(SlaveJdbcManagerWrapper.class));
        assertThat(i.getSlaveOrMasterJdbcManager().getJdbcManager(), is(slave2));

        i.setJdbcManagerName("master");
        assertThat(i.getMasterJdbcManager(), instanceOf(MasterJdbcManagerWrapper.class));
        assertThat(i.getMasterJdbcManager().getJdbcManager(), is(master));
        assertThat(i.getSlaveOrMasterJdbcManager(), instanceOf(MasterJdbcManagerWrapper.class));
        assertThat(i.getSlaveOrMasterJdbcManager().getJdbcManager(), is(master));
    }

}

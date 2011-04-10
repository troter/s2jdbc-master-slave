package jp.troter.seasar.extension.jdbc.manager;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.matchers.IsCollectionContaining.*;

import org.junit.Before;
import org.junit.Test;

public class MasterSlaveJdbcManagerFactoryImplTest {

    MasterSlaveJdbcManagerFactoryImpl i;

    @Before
    public void setUp() {
        i = new MasterSlaveJdbcManagerFactoryImpl();
        i.setMasterJdbcManagerName("master");
    }

    @Test
    public void getJdbcManagerComponentName() {
        assertThat(i.getJdbcManagerComponentName(null), is("jdbcManager"));
        assertThat(i.getJdbcManagerComponentName("master"), is("masterJdbcManager"));
        assertThat(i.getJdbcManagerComponentName("slave1"), is("slave1JdbcManager"));
    }

    @Test
    public void empty() {
        i = new MasterSlaveJdbcManagerFactoryImpl();

        assertThat(i.getMasterJdbcManagerName(), nullValue());
        assertThat(i.getJdbcManagerName(), nullValue());

        assertThat(i.getSlaveJdbcManagerNames().size(), is(0));
    }

    @Test
    public void add$remove$list() {
        assertThat(i.getMasterJdbcManagerName(), is("master"));

        i.addSlaveJdbcManagerName("slave1");
        i.addSlaveJdbcManagerName("slave2");
        i.addSlaveJdbcManagerName("slave3");
        assertThat(i.getSlaveJdbcManagerNames().size(), is(3));
        assertThat(i.getSlaveJdbcManagerNames(), hasItems("slave1", "slave2", "slave3"));

        i.removeSlaveJdbcManagerName("slave2");
        assertThat(i.getSlaveJdbcManagerNames().size(), is(2));
        assertThat(i.getSlaveJdbcManagerNames(), hasItems("slave1", "slave3"));
    }

    @Test
    public void getJdbcManagerName() {
        assertThat(i.getJdbcManagerName(), describedAs("スレーブが設定されていない場合はmasterを利用", is("master")));
        assertThat(i.getMasterJdbcManagerName(), is("master"));

        i.setJdbcManagerName("not exists");
        assertThat(i.getJdbcManagerName(), describedAs("スレーブに存在しないコンポーネント名の場合はmasterを利用", is("master")));
        assertThat(i.getMasterJdbcManagerName(), is("master"));
        
        i.addSlaveJdbcManagerName("slave1");
        i.addSlaveJdbcManagerName("slave2");
        i.addSlaveJdbcManagerName("slave3");

        i.setJdbcManagerName("slave2");
        assertThat(i.getJdbcManagerName(), describedAs("スレーブに存在するコンポーネント名の場合はそのコンポーネント名を利用", is("slave2")));
        assertThat(i.getMasterJdbcManagerName(), is("master"));

        i.setJdbcManagerName("master");
        assertThat(i.getJdbcManagerName(), describedAs("マスターを指定した場合はmasterを利用", is("master")));
        assertThat(i.getMasterJdbcManagerName(), is("master"));

        i.setJdbcManagerName("slave2");
        i.removeSlaveJdbcManagerName("slave2");
        assertThat(i.getJdbcManagerName(), describedAs("削除された場合はmasterを利用", is("master")));
        assertThat(i.getMasterJdbcManagerName(), is("master"));
    }

}

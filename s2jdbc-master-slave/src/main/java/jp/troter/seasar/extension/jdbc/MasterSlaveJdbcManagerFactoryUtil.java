package jp.troter.seasar.extension.jdbc;

import java.util.Date;
import java.util.List;
import java.util.Random;

public class MasterSlaveJdbcManagerFactoryUtil {
    
    protected static Random random = new Random(new Date().getTime());

    public static void useSlave(MasterSlaveJdbcManagerFactory factory) {
        List<String> names = factory.getSlaveJdbcManagerNames();
        if (names.isEmpty()) {
            useMaster(factory);
            return ;
        }

        String name = names.get(random.nextInt(names.size()));
        factory.setJdbcManagerName(name);
    }

    public static void useMaster(MasterSlaveJdbcManagerFactory factory) {
        factory.setJdbcManagerName(factory.getMasterJdbcManagerName());
    }

    /**
     * テスト用
     * @param _random
     */
    @Deprecated()
    public static void setRandom(Random _random) {
        random = _random;
    }
}

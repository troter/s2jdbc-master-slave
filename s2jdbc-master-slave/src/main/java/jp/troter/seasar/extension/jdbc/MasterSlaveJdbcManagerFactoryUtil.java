package jp.troter.seasar.extension.jdbc;

import java.util.Date;
import java.util.List;
import java.util.Random;

public class MasterSlaveJdbcManagerFactoryUtil {
    
    protected static Random random = new Random(new Date().getTime());

    /**
     * 以降このスレッドではマスターを利用します。
     * @param factory
     */
    public static void useMaster(MasterSlaveJdbcManagerFactory factory) {
        factory.setJdbcManagerName(factory.getMasterJdbcManagerName());
    }

    /**
     * 以降このスレッドでは(更新が発生しない限り)スレーブを利用します。
     * @param factory
     */
    public static void useSlave(MasterSlaveJdbcManagerFactory factory) {
        List<String> names = factory.getSlaveJdbcManagerNames();
        if (names.isEmpty()) {
            useMaster(factory);
            return ;
        }

        String name = names.get(random.nextInt(names.size()));
        factory.setJdbcManagerName(name);
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

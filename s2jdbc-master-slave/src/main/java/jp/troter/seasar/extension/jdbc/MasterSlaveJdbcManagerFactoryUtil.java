/*
 * Copyright 2011 Takumi IINO
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jp.troter.seasar.extension.jdbc;

import java.util.Date;
import java.util.List;
import java.util.Random;

import org.seasar.framework.util.StringUtil;

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
     * 現在選択されている<code>JdbcManager</code>のコンポーネント名はマスターとして使用する<code>JdbcManager</code>のコンポーネント名か。
     * @param factory
     * @return
     */
    public static boolean isCurrentJdbcManagerNameMaster(MasterSlaveJdbcManagerFactory factory) {
        return StringUtil.equals(factory.getMasterJdbcManagerName(), factory.getJdbcManagerName());
    }

    /**
     * 現在選択されているスレーブを削除します。
     * @param factory
     */
    public static void removeCurrentSlave(MasterSlaveJdbcManagerFactory factory) {
        String name = factory.getJdbcManagerName();
        factory.removeSlaveJdbcManagerName(name);
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

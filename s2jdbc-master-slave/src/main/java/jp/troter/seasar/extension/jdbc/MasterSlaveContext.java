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

import java.util.List;

public interface MasterSlaveContext {

    /**
     * コンテキスト管理下のMasterSlaveJdbcManagerFactoryすべてでマスターを利用します。
     */
    void useMaster();

    /**
     * 指定されたMasterSlaveJdbcManagerFactoryでマスターを利用します。
     * @param name MasterSlaveJdbcManagerFactory のコンポーネント名
     */
    void useMaster(String name);

    /**
     * コンテキスト管理下のMasterSlaveJdbcManagerFactoryすべてでスレーブを利用します。
     */
    void useSlave();

    /**
     * 指定されたMasterSlaveJdbcManagerFactoryでスレーブを利用します。
     * @param name MasterSlaveJdbcManagerFactory のコンポーネント名
     */
    void useSlave(String name);

    /**
     * コンテキスト管理下のMasterSlaveJdbcManagerFactoryすべてがマスターか
     */
    boolean isMaster();

    /**
     * 指定されたMasterSlaveJdbcManagerFactoryがマスターか
     * @param name MasterSlaveJdbcManagerFactory のコンポーネント名
     */
    boolean isMaster(String name);

    /**
     * コンテキスト管理下のMasterSlaveJdbcManagerFactoryすべてがスレーブか
     */
    boolean isSlave();

    /**
     * 指定されたMasterSlaveJdbcManagerFactoryがスレーブか
     * @param name MasterSlaveJdbcManagerFactory のコンポーネント名
     */
    boolean isSlave(String name);

    /**
     * コンテキスト管理下のMasterSlaveJdbcManagerFactoryはマスタースレーブ混在しているか
     */
    boolean isMix();

    /**
     * バリデーションのクエリに失敗したスレーブを削除します。
     */
    void removeCurrentSlave();

    /**
     * コンテキスト管理下に置く MasterSlaveJdbcManagerFactory のコンポーネント名を登録します。
     * @param name
     */
    void addMasterSlaveJdbcManagerFactoryName(String name);

    /**
     * コンテキスト管理下のMasterSlaveJdbcManagerFactoryをすべて取得します。
     * @return
     */
    List<MasterSlaveJdbcManagerFactory> getMasterSlaveJdbcManagerFactories();

    /**
     * 例外ハンドラを設定します。
     * @param handlerClass
     */
    void setMasterSlaveExceptionHanlderClass(Class<? extends MasterSlaveExceptionHandler> handlerClass);
}

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
    JdbcManagerWrapper getMasterJdbcManager();

    /**
     * スレーブもしくはマスターとして使用する<code>JdbcManager</code>を取得します。 
     * @return
     */
    JdbcManagerWrapper getSlaveOrMasterJdbcManager();

    /**
     * 接続のバリデーション用のクエリを設定します。
     * @param validationQuery
     */
    void setValidationQuery(String validationQuery);

    /**
     * 接続のバリデーション用のクエリを取得します。
     * @param validationQuery
     */
    String getValidationQuery();
}

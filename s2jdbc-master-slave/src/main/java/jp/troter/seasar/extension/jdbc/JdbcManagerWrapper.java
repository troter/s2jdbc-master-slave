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

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;

public interface JdbcManagerWrapper extends JdbcManager, JdbcManagerImplementor {

    /**
     * ラップしている<code>JdbcManager</code>を取得します。
     * @return
     */
    JdbcManager getJdbcManager();

    /**
     * コンポーネント名を返します
     * @return
     */
    String getName();

    public static class Methods {
        public static JdbcManagerImplementor implementor(JdbcManager jdbcManager) {
            if (jdbcManager instanceof JdbcManagerImplementor) {
                return ((JdbcManagerImplementor)jdbcManager);
            }
            throw new UnsupportedOperationException();
        }
    }
}

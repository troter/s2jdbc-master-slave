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
package jp.troter.seasar.extension.jdbc.handler;

import jp.troter.seasar.extension.jdbc.MasterSlaveExceptionHandler;

public class MysqlMasterSlaveExceptionHandler implements
        MasterSlaveExceptionHandler {

    private static final String JDBC_COMMUNICATIONS_EXCEPTION_NAME ="com.mysql.jdbc.CommunicationsException";

    private static final String JDBC_4_COMMUNICATIONS_EXCEPTION_NAME ="com.mysql.jdbc.exceptions.jdbc4.CommunicationsException";

    @Override
    public boolean isShouldRemove(Throwable throwable) {
        Throwable current = throwable;
        while (current != null) {
            if (current.getClass().getName().equals(JDBC_COMMUNICATIONS_EXCEPTION_NAME)) {
                return true;
            }
            if (current.getClass().getName().equals(JDBC_4_COMMUNICATIONS_EXCEPTION_NAME)) {
                return true;
            }
            current = current.getCause();
        }
        return false;
    }
}

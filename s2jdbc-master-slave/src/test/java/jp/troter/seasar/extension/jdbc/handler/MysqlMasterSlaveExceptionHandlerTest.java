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

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

public class MysqlMasterSlaveExceptionHandlerTest {

    MysqlMasterSlaveExceptionHandler handler;

    @Before
    public void setup() {
        handler = new MysqlMasterSlaveExceptionHandler();
    }

    @Test
    public void test() {
        com.mysql.jdbc.CommunicationsException e = new com.mysql.jdbc.CommunicationsException();
        assertTrue(handler.isShouldRemove(e));
        assertTrue(handler.isShouldRemove(new RuntimeException(e)));
        assertTrue(handler.isShouldRemove(new RuntimeException(new SQLException(e))));

        com.mysql.jdbc.exceptions.jdbc4.CommunicationsException e4 = new com.mysql.jdbc.exceptions.jdbc4.CommunicationsException();
        assertTrue(handler.isShouldRemove(e4));
        assertTrue(handler.isShouldRemove(new RuntimeException(e4)));
        assertTrue(handler.isShouldRemove(new RuntimeException(new SQLException(e4))));

        assertFalse(handler.isShouldRemove(new RuntimeException()));
        assertFalse(handler.isShouldRemove(new RuntimeException(new SQLException())));
    }
}

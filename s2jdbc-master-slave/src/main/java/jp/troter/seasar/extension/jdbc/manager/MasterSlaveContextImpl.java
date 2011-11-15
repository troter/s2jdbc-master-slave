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
package jp.troter.seasar.extension.jdbc.manager;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import jp.troter.seasar.extension.jdbc.JdbcManagerWrapper;
import jp.troter.seasar.extension.jdbc.MasterSlaveContext;
import jp.troter.seasar.extension.jdbc.MasterSlaveExceptionHandler;
import jp.troter.seasar.extension.jdbc.MasterSlaveJdbcManagerFactory;
import jp.troter.seasar.extension.jdbc.MasterSlaveJdbcManagerFactoryUtil;
import jp.troter.seasar.extension.jdbc.handler.DefaultMasterSlaveExceptionHanlder;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.tiger.CollectionsUtil;

public class MasterSlaveContextImpl implements MasterSlaveContext {

    private static final Logger logger = Logger
            .getLogger(MasterSlaveContextImpl.class);

    Set<String> masterSlaveJdbcManagerFactoryNames = CollectionsUtil.newCopyOnWriteArraySet();

    Class<? extends MasterSlaveExceptionHandler> handlerClass;

    ConcurrentHashMap<String, MasterSlaveExceptionHandler> handlerTable = CollectionsUtil.newConcurrentHashMap();

    /**
     * S2コンテナです。
     */
    protected S2Container container;

    /**
     * @return S2コンテナ
     */
    public S2Container getContainer() {
        return container;
    }

    /**
     * @param container
     *            S2コンテナ
     */
    public void setContainer(S2Container container) {
        this.container = container;
    }

    @Override
    public void useMaster() {
        for (String name : masterSlaveJdbcManagerFactoryNames) {
            useMaster(name);
        }
    }

    @Override
    public void useMaster(String name) {
        if (! masterSlaveJdbcManagerFactoryNames.contains(name)) {
            throw new IllegalArgumentException("name");
        }
        MasterSlaveJdbcManagerFactoryUtil.useMaster(getMasterSlaveJdbcManagerFactory(name));
    }

    @Override
    public void useSlave() {
        for (String name : masterSlaveJdbcManagerFactoryNames) {
            useSlave(name);
        }
    }

    @Override
    public void useSlave(String name) {
        if (! masterSlaveJdbcManagerFactoryNames.contains(name)) {
            throw new IllegalArgumentException("name");
        }
        MasterSlaveJdbcManagerFactoryUtil.useSlave(getMasterSlaveJdbcManagerFactory(name));
    }

    @Override
    public boolean isMaster() {
        boolean master = true;
        for (String name : masterSlaveJdbcManagerFactoryNames) {
            master &= isMaster(name);
        }
        return master;
    }

    @Override
    public boolean isMaster(String name) {
        if (! masterSlaveJdbcManagerFactoryNames.contains(name)) {
            throw new IllegalArgumentException("name");
        }
        return MasterSlaveJdbcManagerFactoryUtil.isCurrentJdbcManagerNameMaster(getMasterSlaveJdbcManagerFactory(name));
    }

    @Override
    public boolean isSlave() {
        boolean slave = true;
        for (String name : masterSlaveJdbcManagerFactoryNames) {
            slave &= isSlave(name);
        }
        return slave;
    }

    @Override
    public boolean isSlave(String name) {
        if (! masterSlaveJdbcManagerFactoryNames.contains(name)) {
            throw new IllegalArgumentException("name");
        }
        return ! MasterSlaveJdbcManagerFactoryUtil.isCurrentJdbcManagerNameMaster(getMasterSlaveJdbcManagerFactory(name));
    }

    @Override
    public boolean isMix() {
        return ! isMaster() && ! isSlave();
    }

    @Override
    synchronized public void removeCurrentSlave() {
        for (String name : masterSlaveJdbcManagerFactoryNames) {
            if (isMaster(name)) {
                continue;
            }
            MasterSlaveJdbcManagerFactory factory = getMasterSlaveJdbcManagerFactory(name);
            JdbcManagerWrapper jdbcManager = factory.getSlaveOrMasterJdbcManager();
            try {
                jdbcManager.selectBySql(BeanMap.class, factory.getValidationQuery()).getResultList();
            } catch (Throwable t) {
                if (getMasterSlaveExceptionHandler(name).isShouldRemove(t)) {
                    factory.removeSlaveJdbcManagerName(jdbcManager.getName());
                    logger.warn("ファクトリ["+name+"]のスレーブ["+jdbcManager.getName()+"]を切り離します。");
                }
            }
        }
    }

    @Override
    public void addMasterSlaveJdbcManagerFactoryName(String name) {
        masterSlaveJdbcManagerFactoryNames.add(name);
    }

    @Override
    public List<MasterSlaveJdbcManagerFactory> getMasterSlaveJdbcManagerFactories() {
        List<MasterSlaveJdbcManagerFactory> factories = CollectionsUtil.newArrayList();
        for (String name : masterSlaveJdbcManagerFactoryNames) {
            factories.add(getMasterSlaveJdbcManagerFactory(name));
        }
        return factories;
    }

    @Override
    public void setMasterSlaveExceptionHanlderClass(Class<? extends MasterSlaveExceptionHandler> handlerClass) {
        this.handlerClass = handlerClass;
    }

    public MasterSlaveExceptionHandler getMasterSlaveExceptionHandler(String name) {
        try {
            handlerTable.putIfAbsent(name, this.handlerClass.newInstance());
            return handlerTable.get(name);
        } catch (IllegalAccessException e) {
            logger.log(e);
            return new DefaultMasterSlaveExceptionHanlder();
        } catch (InstantiationException e) {
            logger.log(e);
            return new DefaultMasterSlaveExceptionHanlder();
        }
    }

    protected MasterSlaveJdbcManagerFactory getMasterSlaveJdbcManagerFactory(String name) {
        MasterSlaveJdbcManagerFactory factory
            = (MasterSlaveJdbcManagerFactory) container.getRoot().getComponent(name);
        return factory;
    }

}

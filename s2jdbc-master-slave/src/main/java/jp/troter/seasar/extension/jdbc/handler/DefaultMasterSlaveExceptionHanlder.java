package jp.troter.seasar.extension.jdbc.handler;

import jp.troter.seasar.extension.jdbc.MasterSlaveExceptionHandler;

public class DefaultMasterSlaveExceptionHanlder implements MasterSlaveExceptionHandler {

    @Override
    public boolean isShouldRemove(Throwable throwable) {
        return true;
    }

}

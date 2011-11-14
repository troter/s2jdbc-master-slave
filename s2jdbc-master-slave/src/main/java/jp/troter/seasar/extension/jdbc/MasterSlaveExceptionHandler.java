package jp.troter.seasar.extension.jdbc;

public interface MasterSlaveExceptionHandler {

    boolean isShouldRemove(Throwable throwable);

}

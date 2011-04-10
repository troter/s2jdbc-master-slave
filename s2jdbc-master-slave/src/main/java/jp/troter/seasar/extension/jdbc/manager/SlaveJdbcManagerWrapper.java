package jp.troter.seasar.extension.jdbc.manager;

import java.util.List;

import org.seasar.extension.jdbc.AutoBatchDelete;
import org.seasar.extension.jdbc.AutoBatchInsert;
import org.seasar.extension.jdbc.AutoBatchUpdate;
import org.seasar.extension.jdbc.AutoDelete;
import org.seasar.extension.jdbc.AutoFunctionCall;
import org.seasar.extension.jdbc.AutoInsert;
import org.seasar.extension.jdbc.AutoProcedureCall;
import org.seasar.extension.jdbc.AutoSelect;
import org.seasar.extension.jdbc.AutoUpdate;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.SqlBatchUpdate;
import org.seasar.extension.jdbc.SqlFileBatchUpdate;
import org.seasar.extension.jdbc.SqlFileFunctionCall;
import org.seasar.extension.jdbc.SqlFileProcedureCall;
import org.seasar.extension.jdbc.SqlFileSelect;
import org.seasar.extension.jdbc.SqlFileUpdate;
import org.seasar.extension.jdbc.SqlFunctionCall;
import org.seasar.extension.jdbc.SqlProcedureCall;
import org.seasar.extension.jdbc.SqlSelect;
import org.seasar.extension.jdbc.SqlUpdate;

public class SlaveJdbcManagerWrapper implements JdbcManager {

    protected JdbcManager jdbcManager;

    public SlaveJdbcManagerWrapper(JdbcManager jdbcManager) {
        this.jdbcManager = jdbcManager;
    }

    @Override
    public <T> AutoSelect<T> from(Class<T> baseClass) {
        return jdbcManager.from(baseClass);
    }

    @Override
    public <T> SqlSelect<T> selectBySql(Class<T> baseClass, String sql,
            Object... params) {
        return jdbcManager.selectBySql(baseClass, sql, params);
    }

    @Override
    public long getCountBySql(String sql, Object... params) {
        return jdbcManager.getCountBySql(sql, params);
    }

    @Override
    public <T> SqlFileSelect<T> selectBySqlFile(Class<T> baseClass, String path) {
        return jdbcManager.selectBySqlFile(baseClass, path);
    }

    @Override
    public <T> SqlFileSelect<T> selectBySqlFile(Class<T> baseClass,
            String path, Object parameter) {
        return jdbcManager.selectBySqlFile(baseClass, path, parameter);
    }

    @Override
    public long getCountBySqlFile(String path) {
        return jdbcManager.getCountBySqlFile(path);
    }

    @Override
    public long getCountBySqlFile(String path, Object parameter) {
        return jdbcManager.getCountBySqlFile(path, parameter);
    }

    @Override
    public <T> AutoInsert<T> insert(T entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> AutoBatchInsert<T> insertBatch(T... entities) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> AutoBatchInsert<T> insertBatch(List<T> entities) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> AutoUpdate<T> update(T entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> AutoBatchUpdate<T> updateBatch(T... entities) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> AutoBatchUpdate<T> updateBatch(List<T> entities) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SqlUpdate updateBySql(String sql, Class<?>... paramClasses) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SqlBatchUpdate updateBatchBySql(String sql, Class<?>... paramClasses) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SqlFileUpdate updateBySqlFile(String path) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SqlFileUpdate updateBySqlFile(String path, Object parameter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> SqlFileBatchUpdate<T> updateBatchBySqlFile(String path,
            T... params) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> SqlFileBatchUpdate<T> updateBatchBySqlFile(String path,
            List<T> params) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> AutoDelete<T> delete(T entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> AutoBatchDelete<T> deleteBatch(T... entities) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> AutoBatchDelete<T> deleteBatch(List<T> entities) {
        throw new UnsupportedOperationException();
    }

    @Override
    public AutoProcedureCall call(String procedureName) {
        return jdbcManager.call(procedureName);
    }

    @Override
    public AutoProcedureCall call(String procedureName, Object parameter) {
        return jdbcManager.call(procedureName, parameter);
    }

    @Override
    public SqlProcedureCall callBySql(String sql) {
        return jdbcManager.callBySql(sql);
    }

    @Override
    public SqlProcedureCall callBySql(String sql, Object parameter) {
        return jdbcManager.callBySql(sql, parameter);
    }

    @Override
    public SqlFileProcedureCall callBySqlFile(String path) {
        return jdbcManager.callBySqlFile(path);
    }

    @Override
    public SqlFileProcedureCall callBySqlFile(String path, Object parameter) {
        return jdbcManager.callBySqlFile(path, parameter);
    }

    @Override
    public <T> AutoFunctionCall<T> call(Class<T> resultClass,
            String functionName) {
        return jdbcManager.call(resultClass, functionName);
    }

    @Override
    public <T> AutoFunctionCall<T> call(Class<T> resultClass,
            String functionName, Object parameter) {
        return jdbcManager.call(resultClass, functionName, parameter);
    }

    @Override
    public <T> SqlFunctionCall<T> callBySql(Class<T> resultClass, String sql) {
        return jdbcManager.callBySql(resultClass, sql);
    }

    @Override
    public <T> SqlFunctionCall<T> callBySql(Class<T> resultClass, String sql,
            Object parameter) {
        return jdbcManager.callBySql(resultClass, sql, parameter);
    }

    @Override
    public <T> SqlFileFunctionCall<T> callBySqlFile(Class<T> resultClass,
            String path) {
        return jdbcManager.callBySqlFile(resultClass, path);
    }

    @Override
    public <T> SqlFileFunctionCall<T> callBySqlFile(Class<T> resultClass,
            String path, Object parameter) {
        return jdbcManager.callBySqlFile(resultClass, path, parameter);
    }

}

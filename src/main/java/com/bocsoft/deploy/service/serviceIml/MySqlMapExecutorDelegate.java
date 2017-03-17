
package main.java.com.bocsoft.deploy.service.serviceIml;

import com.ibatis.common.util.PaginatedList;
import com.ibatis.sqlmap.client.event.RowHandler;
import com.ibatis.sqlmap.engine.cache.CacheModel;
import com.ibatis.sqlmap.engine.exchange.DataExchangeFactory;
import com.ibatis.sqlmap.engine.execution.BatchException;
import com.ibatis.sqlmap.engine.execution.SqlExecutor;
import com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate;
import com.ibatis.sqlmap.engine.mapping.parameter.ParameterMap;
import com.ibatis.sqlmap.engine.mapping.result.ResultMap;
import com.ibatis.sqlmap.engine.mapping.result.ResultObjectFactory;
import com.ibatis.sqlmap.engine.mapping.statement.MappedStatement;
import com.ibatis.sqlmap.engine.scope.SessionScope;
import com.ibatis.sqlmap.engine.scope.StatementScope;
import com.ibatis.sqlmap.engine.transaction.Transaction;
import com.ibatis.sqlmap.engine.transaction.TransactionManager;
import com.ibatis.sqlmap.engine.type.TypeHandlerFactory;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class MySqlMapExecutorDelegate extends SqlMapExecutorDelegate {

	/**
	 * ԭ������
	 */
	private SqlMapExecutorDelegate delegate = null;

	/**
	 * @param delegate
	 */
	public MySqlMapExecutorDelegate(SqlMapExecutorDelegate delegate) {
		super();
		this.delegate = delegate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#addCacheModel(com
	 * .ibatis.sqlmap.engine.cache.CacheModel)
	 */
	@Override
	public void addCacheModel(CacheModel model) {
		this.delegate.addCacheModel(model);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#addMappedStatement
	 * (com.ibatis.sqlmap.engine.mapping.statement.MappedStatement)
	 */
	@Override
	public void addMappedStatement(MappedStatement ms) {
		try {
			this.delegate.getMappedStatement(ms.getId());
			Field field = this.delegate.getClass().getDeclaredField(
					"mappedStatements");
			field.setAccessible(true);
			Map map = (Map) field.get(this.delegate);
			if (map.containsKey(ms.getId())) {
				map.remove(ms.getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.delegate.addMappedStatement(ms);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#addParameterMap(
	 * com.ibatis.sqlmap.engine.mapping.parameter.ParameterMap)
	 */
	@Override
	public void addParameterMap(ParameterMap map) {
		this.delegate.addParameterMap(map);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#addResultMap(com
	 * .ibatis.sqlmap.engine.mapping.result.ResultMap)
	 */
	@Override
	public void addResultMap(ResultMap map) {
		this.delegate.addResultMap(map);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#autoCommitTransaction
	 * (com.ibatis.sqlmap.engine.scope.SessionScope, boolean)
	 */
	@Override
	protected void autoCommitTransaction(SessionScope sessionScope,
			boolean autoStart) throws SQLException {
		try {
			Method m = this.delegate.getClass().getDeclaredMethod(
					"autoCommitTransaction", SessionScope.class, Boolean.class);
			m.setAccessible(true);
			m.invoke(this.delegate, sessionScope, autoStart);
		} catch (Exception e) {
			if (e instanceof SQLException) {
				throw (SQLException) e;
			}
			throw new RuntimeException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#autoEndTransaction
	 * (com.ibatis.sqlmap.engine.scope.SessionScope, boolean)
	 */
	@Override
	protected void autoEndTransaction(SessionScope sessionScope,
			boolean autoStart) throws SQLException {
		try {
			Method m = this.delegate.getClass().getDeclaredMethod(
					"autoEndTransaction", SessionScope.class, Boolean.class);
			m.setAccessible(true);
			m.invoke(this.delegate, sessionScope, autoStart);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (e instanceof SQLException) {
				throw (SQLException) e;
			}
			throw new RuntimeException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#autoStartTransaction
	 * (com.ibatis.sqlmap.engine.scope.SessionScope, boolean,
	 * com.ibatis.sqlmap.engine.transaction.Transaction)
	 */
	@Override
	protected Transaction autoStartTransaction(SessionScope sessionScope,
			boolean autoStart, Transaction trans) throws SQLException {
		try {
			Method m = this.delegate.getClass().getDeclaredMethod(
					"autoStartTransaction", SessionScope.class, Boolean.class,
					Transaction.class);
			m.setAccessible(true);
			return (Transaction) m.invoke(this.delegate, sessionScope,
					autoStart);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (e instanceof SQLException) {
				throw (SQLException) e;
			}
			throw new RuntimeException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#beginSessionScope()
	 */
	@Override
	protected SessionScope beginSessionScope() {
		try {
			Method m = this.delegate.getClass().getDeclaredMethod(
					"beginSessionScope");
			m.setAccessible(true);
			return (SessionScope) m.invoke(this.delegate);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#beginStatementScope
	 * (com.ibatis.sqlmap.engine.scope.SessionScope,
	 * com.ibatis.sqlmap.engine.mapping.statement.MappedStatement)
	 */
	@Override
	protected StatementScope beginStatementScope(SessionScope sessionScope,
			MappedStatement mappedStatement) {
		try {
			Method m = this.delegate.getClass().getDeclaredMethod(
					"beginSessionScope", SessionScope.class,
					MappedStatement.class);
			m.setAccessible(true);
			return (StatementScope) m.invoke(this.delegate, sessionScope,
					mappedStatement);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#commitTransaction
	 * (com.ibatis.sqlmap.engine.scope.SessionScope)
	 */
	@Override
	public void commitTransaction(SessionScope sessionScope)
			throws SQLException {
		this.delegate.commitTransaction(sessionScope);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#delete(com.ibatis
	 * .sqlmap.engine.scope.SessionScope, java.lang.String, java.lang.Object)
	 */
	@Override
	public int delete(SessionScope sessionScope, String id, Object param)
			throws SQLException {
		return this.delegate.delete(sessionScope, id, param);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#endSessionScope(
	 * com.ibatis.sqlmap.engine.scope.SessionScope)
	 */
	@Override
	protected void endSessionScope(SessionScope sessionScope) {
		try {
			Method m = this.delegate.getClass().getDeclaredMethod(
					"endSessionScope", SessionScope.class);
			m.setAccessible(true);
			m.invoke(this.delegate, sessionScope);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#endStatementScope
	 * (com.ibatis.sqlmap.engine.scope.StatementScope)
	 */
	@Override
	protected void endStatementScope(StatementScope statementScope) {
		try {
			Method m = this.delegate.getClass().getDeclaredMethod(
					"endStatementScope", StatementScope.class);
			m.setAccessible(true);
			m.invoke(this.delegate, statementScope);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#endTransaction(com
	 * .ibatis.sqlmap.engine.scope.SessionScope)
	 */
	@Override
	public void endTransaction(SessionScope sessionScope) throws SQLException {
		this.delegate.endTransaction(sessionScope);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#equals(java.lang
	 * .Object)
	 */
	@Override
	public boolean equals(Object obj) {
		return this.delegate.equals(obj);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#executeBatch(com
	 * .ibatis.sqlmap.engine.scope.SessionScope)
	 */
	@Override
	public int executeBatch(SessionScope sessionScope) throws SQLException {
		return this.delegate.executeBatch(sessionScope);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#executeBatchDetailed
	 * (com.ibatis.sqlmap.engine.scope.SessionScope)
	 */
	@Override
	public List executeBatchDetailed(SessionScope sessionScope)
			throws SQLException, BatchException {
		return this.delegate.executeBatchDetailed(sessionScope);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#flushDataCache()
	 */
	@Override
	public void flushDataCache() {
		this.delegate.flushDataCache();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#flushDataCache(java
	 * .lang.String)
	 */
	@Override
	public void flushDataCache(String id) {
		this.delegate.flushDataCache(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#getCacheModel(java
	 * .lang.String)
	 */
	@Override
	public CacheModel getCacheModel(String id) {
		return this.delegate.getCacheModel(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#getCacheModelNames()
	 */
	@Override
	public Iterator getCacheModelNames() {
		return this.delegate.getCacheModelNames();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#getDataExchangeFactory
	 * ()
	 */
	@Override
	public DataExchangeFactory getDataExchangeFactory() {
		return this.delegate.getDataExchangeFactory();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#getDataSource()
	 */
	@Override
	public DataSource getDataSource() {
		return this.delegate.getDataSource();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#getMappedStatement
	 * (java.lang.String)
	 */
	@Override
	public MappedStatement getMappedStatement(String id) {
		return this.delegate.getMappedStatement(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#getMappedStatementNames
	 * ()
	 */
	@Override
	public Iterator getMappedStatementNames() {
		return this.delegate.getMappedStatementNames();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#getMaxTransactions()
	 */
	@Override
	public int getMaxTransactions() {
		return this.delegate.getMaxTransactions();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#getParameterMap(
	 * java.lang.String)
	 */
	@Override
	public ParameterMap getParameterMap(String id) {
		return this.delegate.getParameterMap(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#getParameterMapNames
	 * ()
	 */
	@Override
	public Iterator getParameterMapNames() {
		return this.delegate.getParameterMapNames();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#getResultMap(java
	 * .lang.String)
	 */
	@Override
	public ResultMap getResultMap(String id) {
		return this.delegate.getResultMap(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#getResultMapNames()
	 */
	@Override
	public Iterator getResultMapNames() {
		return this.delegate.getResultMapNames();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#getResultObjectFactory
	 * ()
	 */
	@Override
	public ResultObjectFactory getResultObjectFactory() {
		return this.delegate.getResultObjectFactory();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#getSqlExecutor()
	 */
	@Override
	public SqlExecutor getSqlExecutor() {
		return this.delegate.getSqlExecutor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#getTransaction(com
	 * .ibatis.sqlmap.engine.scope.SessionScope)
	 */
	@Override
	public Transaction getTransaction(SessionScope sessionScope) {
		return this.delegate.getTransaction(sessionScope);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#getTxManager()
	 */
	@Override
	public TransactionManager getTxManager() {
		return this.delegate.getTxManager();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#getTypeHandlerFactory
	 * ()
	 */
	@Override
	public TypeHandlerFactory getTypeHandlerFactory() {
		return this.delegate.getTypeHandlerFactory();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.delegate.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#insert(com.ibatis
	 * .sqlmap.engine.scope.SessionScope, java.lang.String, java.lang.Object)
	 */
	@Override
	public Object insert(SessionScope sessionScope, String id, Object param)
			throws SQLException {
		return this.delegate.insert(sessionScope, id, param);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#isCacheModelsEnabled
	 * ()
	 */
	@Override
	public boolean isCacheModelsEnabled() {
		return this.delegate.isCacheModelsEnabled();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#isEnhancementEnabled
	 * ()
	 */
	@Override
	public boolean isEnhancementEnabled() {
		return this.delegate.isEnhancementEnabled();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#
	 * isForceMultipleResultSetSupport()
	 */
	@Override
	public boolean isForceMultipleResultSetSupport() {
		return this.delegate.isForceMultipleResultSetSupport();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#isLazyLoadingEnabled
	 * ()
	 */
	@Override
	public boolean isLazyLoadingEnabled() {
		return this.delegate.isLazyLoadingEnabled();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#isStatementCacheEnabled
	 * ()
	 */
	@Override
	public boolean isStatementCacheEnabled() {
		return this.delegate.isStatementCacheEnabled();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#isUseColumnLabel()
	 */
	@Override
	public boolean isUseColumnLabel() {
		return this.delegate.isUseColumnLabel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#queryForList(com
	 * .ibatis.sqlmap.engine.scope.SessionScope, java.lang.String,
	 * java.lang.Object, int, int)
	 */
	@Override
	public List queryForList(SessionScope sessionScope, String id,
			Object paramObject, int skip, int max) throws SQLException {
		return this.delegate.queryForList(sessionScope, id, paramObject, skip,
				max);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#queryForList(com
	 * .ibatis.sqlmap.engine.scope.SessionScope, java.lang.String,
	 * java.lang.Object)
	 */
	@Override
	public List queryForList(SessionScope sessionScope, String id,
			Object paramObject) throws SQLException {
		return this.delegate.queryForList(sessionScope, id, paramObject);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#queryForMap(com.
	 * ibatis.sqlmap.engine.scope.SessionScope, java.lang.String,
	 * java.lang.Object, java.lang.String, java.lang.String)
	 */
	@Override
	public Map queryForMap(SessionScope sessionScope, String id,
			Object paramObject, String keyProp, String valueProp)
			throws SQLException {
		return this.delegate.queryForMap(sessionScope, id, paramObject,
				keyProp, valueProp);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#queryForMap(com.
	 * ibatis.sqlmap.engine.scope.SessionScope, java.lang.String,
	 * java.lang.Object, java.lang.String)
	 */
	@Override
	public Map queryForMap(SessionScope sessionScope, String id,
			Object paramObject, String keyProp) throws SQLException {
		return this.delegate
				.queryForMap(sessionScope, id, paramObject, keyProp);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#queryForObject(com
	 * .ibatis.sqlmap.engine.scope.SessionScope, java.lang.String,
	 * java.lang.Object, java.lang.Object)
	 */
	@Override
	public Object queryForObject(SessionScope sessionScope, String id,
			Object paramObject, Object resultObject) throws SQLException {
		return this.delegate.queryForObject(sessionScope, id, paramObject,
				resultObject);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#queryForObject(com
	 * .ibatis.sqlmap.engine.scope.SessionScope, java.lang.String,
	 * java.lang.Object)
	 */
	@Override
	public Object queryForObject(SessionScope sessionScope, String id,
			Object paramObject) throws SQLException {
		return this.delegate.queryForObject(sessionScope, id, paramObject);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#queryForPaginatedList
	 * (com.ibatis.sqlmap.engine.scope.SessionScope, java.lang.String,
	 * java.lang.Object, int)
	 */
	@Override
	public PaginatedList queryForPaginatedList(SessionScope sessionScope,
			String id, Object paramObject, int pageSize) throws SQLException {
		return this.delegate.queryForPaginatedList(sessionScope, id,
				paramObject, pageSize);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#queryWithRowHandler
	 * (com.ibatis.sqlmap.engine.scope.SessionScope, java.lang.String,
	 * java.lang.Object, com.ibatis.sqlmap.client.event.RowHandler)
	 */
	@Override
	public void queryWithRowHandler(SessionScope sessionScope, String id,
			Object paramObject, RowHandler rowHandler) throws SQLException {
		this.delegate.queryWithRowHandler(sessionScope, id, paramObject,
				rowHandler);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#setCacheModelsEnabled
	 * (boolean)
	 */
	@Override
	public void setCacheModelsEnabled(boolean cacheModelsEnabled) {
		this.delegate.setCacheModelsEnabled(cacheModelsEnabled);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#setEnhancementEnabled
	 * (boolean)
	 */
	@Override
	public void setEnhancementEnabled(boolean enhancementEnabled) {
		this.delegate.setEnhancementEnabled(enhancementEnabled);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#
	 * setForceMultipleResultSetSupport(boolean)
	 */
	@Override
	public void setForceMultipleResultSetSupport(
			boolean forceMultipleResultSetSupport) {
		this.delegate
				.setForceMultipleResultSetSupport(forceMultipleResultSetSupport);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#setLazyLoadingEnabled
	 * (boolean)
	 */
	@Override
	public void setLazyLoadingEnabled(boolean lazyLoadingEnabled) {
		this.delegate.setLazyLoadingEnabled(lazyLoadingEnabled);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#setResultObjectFactory
	 * (com.ibatis.sqlmap.engine.mapping.result.ResultObjectFactory)
	 */
	@Override
	public void setResultObjectFactory(ResultObjectFactory resultObjectFactory) {
		this.delegate.setResultObjectFactory(resultObjectFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#setStatementCacheEnabled
	 * (boolean)
	 */
	@Override
	public void setStatementCacheEnabled(boolean statementCacheEnabled) {
		this.delegate.setStatementCacheEnabled(statementCacheEnabled);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#setTxManager(com
	 * .ibatis.sqlmap.engine.transaction.TransactionManager)
	 */
	@Override
	public void setTxManager(TransactionManager txManager) {
		this.delegate.setTxManager(txManager);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#setUseColumnLabel
	 * (boolean)
	 */
	@Override
	public void setUseColumnLabel(boolean useColumnLabel) {
		this.delegate.setUseColumnLabel(useColumnLabel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#
	 * setUserProvidedTransaction(com.ibatis.sqlmap.engine.scope.SessionScope,
	 * java.sql.Connection)
	 */
	@Override
	public void setUserProvidedTransaction(SessionScope sessionScope,
			Connection userConnection) {
		this.delegate.setUserProvidedTransaction(sessionScope, userConnection);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#startBatch(com.ibatis
	 * .sqlmap.engine.scope.SessionScope)
	 */
	@Override
	public void startBatch(SessionScope sessionScope) {
		this.delegate.startBatch(sessionScope);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#startTransaction
	 * (com.ibatis.sqlmap.engine.scope.SessionScope, int)
	 */
	@Override
	public void startTransaction(SessionScope sessionScope,
			int transactionIsolation) throws SQLException {
		this.delegate.startTransaction(sessionScope, transactionIsolation);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#startTransaction
	 * (com.ibatis.sqlmap.engine.scope.SessionScope)
	 */
	@Override
	public void startTransaction(SessionScope sessionScope) throws SQLException {
		this.delegate.startTransaction(sessionScope);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate#update(com.ibatis
	 * .sqlmap.engine.scope.SessionScope, java.lang.String, java.lang.Object)
	 */
	@Override
	public int update(SessionScope sessionScope, String id, Object param)
			throws SQLException {
		return this.delegate.update(sessionScope, id, param);
	}
}

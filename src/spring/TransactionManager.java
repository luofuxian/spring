package spring;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

public class TransactionManager {
	private static DataSource dataSource;
	private Connection getConnection() throws SQLException {
		return SingleConnectHandler.getConnection(dataSource);
	}
	public TransactionManager(DataSource dataSource) {
		TransactionManager.dataSource = dataSource;
	}
	public void start() throws SQLException {
		Connection conn = getConnection();
		conn.setAutoCommit(false);
	}
	public void close() throws SQLException {
		Connection conn = getConnection();
		conn.close();
	}
	public void commit() throws SQLException {
		Connection conn = getConnection();
		conn.commit();
	}
	public void rollBack() throws SQLException {
		Connection conn = getConnection();
		conn.rollback();
	}
	public boolean isAutoCommit() throws SQLException {
		return getConnection().isClosed();
	}
	public void openConnection() throws SQLException{
		SingleConnectHandler.openConnection(dataSource);
	}
}
package spring;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

public class SingleConnectHandler {
	private static ThreadLocal<ConnectionHandler> localThread = new ThreadLocal<>();
	
	private static ConnectionHandler getConnectionHahdler() {
		ConnectionHandler ch = localThread.get();
		if(ch == null) {
			ch = new ConnectionHandler();
			localThread.set(ch);
		}
		return ch;
	}
	
	public static Connection getConnection(DataSource dataSource) throws SQLException {
		return getConnectionHahdler().getConnectionByDatabase(dataSource);
	}
	
	public static void openConnection(DataSource dataSource) throws SQLException {
		getConnectionHahdler().openConnection(dataSource);
	}
}

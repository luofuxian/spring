package spring;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

public class ConnectionHandler {
	private Map<DataSource, Connection> map = new HashMap<>();//将数据库连接保存在map中， 以DataSource为键
	
	public Connection getConnectionByDatabase(DataSource dataSource) throws SQLException{
		Connection conn = map.get(dataSource);
		if(conn == null) {
			conn = dataSource.getConnection();
			map.put(dataSource, conn);
		}
		return conn;
	}
	public void openConnection(DataSource dataSource) throws SQLException {
		Connection conn = map.get(dataSource);
		if(conn.isClosed()) {
			conn = dataSource.getConnection();
			map.put(dataSource, conn);
		}
	}
}

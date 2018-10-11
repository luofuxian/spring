package spring;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

public class ConnectionHandler {
	private Map<DataSource, Connection> map = new HashMap<>();//�����ݿ����ӱ�����map�У� ��DataSourceΪ��
	
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

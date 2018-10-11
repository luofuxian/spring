package spring;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

public class DBUtil {
	private static DataSource dataSource;
	
	public static DataSource getDataSourceInstance() throws SQLException{
		if(dataSource == null)
			dataSource = new PoolConnect();
		return dataSource;
	}
	
	public static Connection getConnection() throws SQLException {
		if(dataSource == null)
			dataSource = new PoolConnect();
		return dataSource.getConnection();
	}
}

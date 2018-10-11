package spring;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import javax.sql.DataSource;

public class UserDao implements UserService{
	private String sql = "insert into user(account,password) values(?,?)";
	private String sql1 = "select * from user ";
	private DataSource dataSource;
	public UserDao(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public void buy() throws SQLException {
		Connection conn = SingleConnectHandler.getConnection(dataSource);
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, Thread.currentThread().getName()+"buy");
		ps.setString(2, new Date().toString());
		ps.execute();
	}

	@Override
	public void addShops() throws SQLException {
		Connection conn = SingleConnectHandler.getConnection(dataSource);
		PreparedStatement ps = conn.prepareStatement(sql1);
		/*ps.setString(1, Thread.currentThread().getName()+"addShops");
		ps.setString(2, new Date().toString());*/
		ps.executeQuery();
	}
}

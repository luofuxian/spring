package spring;

import java.sql.SQLException;
import java.util.function.DoubleUnaryOperator;

import javax.sql.DataSource;

public class Test {
	public static void main(String[] args) throws SQLException, InterruptedException {
		DataSource b = DBUtil.getDataSourceInstance();
		for(int i=0;i<300;i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					UserService u = new UserDao(b);
					TransactionManager t = new TransactionManager(b);
					try {
						t.start();
						u.buy();
						u.addShops();
						t.commit();
						t.close();
					} catch (SQLException e) {
						e.printStackTrace();
						try {
							t.rollBack();
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						e.printStackTrace();
					}
				}
			}).start();
		}
	}
}

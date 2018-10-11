package spring;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Logger;

import javax.sql.DataSource;

public final class PoolConnect implements DataSource{
	private static String driverClassName;
	private static String password;
	private static String username;;
	private static String url;
	
	//最大连接数
	private static int maxAlive = 5;
	//初始化连接数
	private static int initAlive = 3;
	
	//存放连接池对象,使用线程安全的Vector
	private static List<ConnectionManager> conns = new Vector<ConnectionManager>(maxAlive);
	
	static {
		try {
			//初始化配置文件
			InputStream inStream = new FileInputStream("./conf/jdbc.properties");
			Properties pro = new Properties();
			pro.load(inStream);
			driverClassName = pro.getProperty("driverClassName");
			password = pro.getProperty("password");
			username = pro.getProperty("username");
			url = pro.getProperty("url");
			initAlive = Util.toInt(pro.getProperty("initAlive")) == 0 ? initAlive:Util.toInt(pro.getProperty("initAlive"));
			maxAlive = Util.toInt(pro.getProperty("maxAlive")) == 0 ? initAlive:Util.toInt(pro.getProperty("maxAlive"));
			//初始化数据库连接池
			Class.forName(driverClassName);
			for(int i=0;i<initAlive;i++) {
				Connection conn = DriverManager.getConnection(url, username, password);
				ConnectionManager myConn = new ConnectionManager(conn, false);
				conns.add(myConn);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Connection getConnection() throws SQLException {
		final ConnectionManager arg3 = getFreeConnection();
		final Connection proxyConnection = arg3.getConn();
		return (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(), new Class[] { Connection.class }, new InvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				if("close".equals(method.getName())) {
					conns.get(conns.indexOf(arg3)).setBusy(false);//修改连接为可用
					return null;
				} //当执行关闭时并不是真正关闭
				return method.invoke(proxyConnection, args);
			}
		});
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	//循环连接池查询是否有可用连接，没有的话则等待其他线程把连接释放后获取。
	private synchronized ConnectionManager getFreeConnection() throws SQLException {
		while (true) {
			if(conns.size()>0) {//先检查池中是否还有可用连接
				for(ConnectionManager arg0:conns) {
					if(!arg0.isBusy()) {
						arg0.setBusy(true);
						return arg0;
					}
				}
			}
			
			if(conns.size()<maxAlive) {
				try {
					Class.forName(driverClassName);
					Connection arg1 = DriverManager.getConnection(url, username, password);
					ConnectionManager arg2 = new ConnectionManager(arg1, false);
					conns.add(arg2);
					return arg2;
				} catch (ClassNotFoundException e) {
					throw new SQLException(e.getMessage());
				}
			} else {
				try {
					Thread.sleep(100);
					System.out.println("池中连接数为："+conns.size()+",无可用连接，等待中...");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}





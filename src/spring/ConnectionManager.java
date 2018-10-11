package spring;

import java.sql.Connection;

public class ConnectionManager{
	private Connection conn;
	private boolean busy;
	public ConnectionManager(Connection conn,boolean busy) {
		this.conn = conn;
		this.busy = busy;
	}
	public Connection getConn() {
		return conn;
	}
	public void setConn(Connection conn) {
		this.conn = conn;
	}
	public boolean isBusy() {
		return busy;
	}
	public void setBusy(boolean busy) {
		this.busy = busy;
	}
	
}

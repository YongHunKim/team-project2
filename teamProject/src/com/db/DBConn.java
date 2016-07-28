package com.db;

import java.sql.*;

public class DBConn {
	private static Connection conn = null;
	
	private DBConn(){
		
	}
	
	public static Connection getConnection(){
		String url ="jdbc:oracle:thin:@localhost:1521:ORCL";
		String user = "kyh";
		String pw = "tlatla";
		
		if(conn == null){
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				conn = DriverManager.getConnection(url, user, pw);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return conn;
	}
	
	public void close(){
		if(conn == null){
			return;
		}
		
		try {
			if(conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		conn = null;
	}
}

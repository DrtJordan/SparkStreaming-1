package org.hna.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.hna.utils.Config;

public class MysqlDao {
	private static Logger log = LoggerFactory.getLogger(MysqlDao.class);
	private static Connection con = null;
//	private static ObjectMapper mapper  = new ObjectMapper();
	public MysqlDao(){
		init();
	}
	public void init(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(Config.url, Config.userName, Config.userPasswd);
		} catch (Exception e) {
			log.error("mysql connection error!!!");
			e.printStackTrace();
		}
	}
	public boolean save(String sql) {
		PreparedStatement prepareStatement = null;
		try{
			prepareStatement = con.prepareStatement(sql);
			prepareStatement.executeUpdate();
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			try{
				prepareStatement.close();
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		return true;
	}
	
}

package database;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;

import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteConfig.SynchronousMode;

import auth.Auth;
import logger.WriteLog;
import parser.HtmlParser;

public class DbConnection {
	public Connection iniConnection() {
		Connection connection = null; 
		try {
			Class.forName("org.sqlite.JDBC");
			SQLiteConfig sqlConfig = new SQLiteConfig();
			sqlConfig.setEncoding(SQLiteConfig.Encoding.UTF8);
			sqlConfig.setPageSize(5120);
			sqlConfig.setSynchronous(SynchronousMode.NORMAL);
			File file = new File("./anime.db");
			if(file.exists()) {
				connection = DriverManager.getConnection("jdbc:sqlite:anime.db", sqlConfig.toProperties());
			} else {
				connection = DriverManager.getConnection("jdbc:sqlite:" + new Auth().getAuth(), sqlConfig.toProperties());
			}
		} catch(Exception e) {
			WriteLog.writeErrorLog(e.getMessage().toString());
			e.printStackTrace();
		}

		return connection;
	}

	public boolean closeConnection(Connection connection) {
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			WriteLog.writeErrorLog(e.getMessage().toString());
			e.printStackTrace();
		}

		return true;
	}

	public boolean insertEmailVal(Connection connection, String emailAddr) {
		boolean result = true;
		try {
			connection.setAutoCommit(false);
			if(this.checkMailExist(connection, emailAddr)) {
				result = false;
			} else {
				String sql = "INSERT OR IGNORE INTO "+
						"email(EMAIL, CREATEDATE) "+
						"VALUES(?, ?)";
				PreparedStatement stmt = connection.prepareStatement(sql);
				stmt.setString(1, emailAddr);
				stmt.setString(2, HtmlParser.getTodayDat());
				stmt.executeUpdate();
				stmt.close();
				connection.commit();
			}
		} catch(SQLException e) {
			e.printStackTrace();
			WriteLog.writeErrorLog(e.getMessage().toString());
		}

		return result;
	}

	public boolean insertValue(Connection connection, String []values) {
		try {
			connection.setAutoCommit(false);
			String sql = "INSERT OR IGNORE INTO "+
					"anime(TITLE, LINK, THEDATE) "+
					"VALUES(?, ?, ?)";
			PreparedStatement stat = connection.prepareStatement(sql);
			stat.setString(1, values[0]);
			stat.setString(2, values[1]);
			stat.setString(3, values[2]);
			stat.executeUpdate();
			stat.close();
			connection.commit();
		} catch(SQLException e) {
			e.printStackTrace();
			WriteLog.writeErrorLog(e.getMessage().toString());
		}

		return true;
	}

	public ArrayList<String>getEmailList(Connection connection) {
		ArrayList<String> mailList = new ArrayList<String>();
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
		    ResultSet rs = stmt.executeQuery("SELECT * FROM email;");
		    while(rs.next()) {
		    	String emailAddr = rs.getString("EMAIL");
		    	mailList.add(emailAddr);
		    }

		    rs.close();
		    stmt.close();
		} catch(SQLException e) {
			WriteLog.writeErrorLog(e.getMessage().toString());
			e.printStackTrace();

			return mailList;
		}
		return mailList;
	}

	public ArrayList<String> selectValue(Connection connection, String type) {
		ArrayList<String> resultList = new ArrayList<String>();
		Statement stat = null;
		try {
			stat = connection.createStatement();

			ResultSet rs = null;
			if(type.equals("hot")) {
				rs = stat.executeQuery("SELECT * FROM anime WHERE THEDATE = date('now');");
			} else if(type.equals("weekly")) {
				rs = stat.executeQuery("SELECT * FROM anime WHERE (julianday('now') - julianday(THEDATE)) <= 7;");
			} else {
				rs = stat.executeQuery("SELECT * FROM anime;");
			}

			while(rs.next()) {
		    	String id = String.valueOf(rs.getInt("ID"));
		        String title = rs.getString("TITLE");
		        String link = rs.getString("LINK");
		        if(link.contains("NewsArea")) {
		        	id = "Animen";
		        	link = "https://www.animen.com.tw" + link;
		        } else if(link.contains("gamme")){
		        	id = "宅宅新聞";
		        } else if(link.contains("gamer")) {
		        	id = "巴哈姆特";
		        } else {
		        }

		        String dat = rs.getString("THEDATE");

		        resultList.add(id);
		        resultList.add(title);
		        resultList.add(link);
		        resultList.add(dat);
		    }
		    rs.close();
		    stat.close();
		} catch(SQLException e) {
			WriteLog.writeErrorLog(e.getMessage().toString());
			e.printStackTrace();

			return resultList;
		}

		return resultList;
	}

	public String delEmailVal(Connection connection, String emailAddr) {
		String result = "";
		try {
			boolean isMailExist = this.checkMailExist(connection, emailAddr);

			if(isMailExist == false) {
				result = "email-address-non-exist";
			} else {
				connection.setAutoCommit(false);
				String sql = "DELETE FROM email WHERE EMAIL = ?;";
				PreparedStatement stat = connection.prepareStatement(sql);
				stat.setString(1, emailAddr);
				stat.executeUpdate();
				stat.close();
				connection.commit();

				result = "delete-email-address-success";
			}
		} catch(SQLException e) {
			e.printStackTrace();
			WriteLog.writeErrorLog(e.getMessage().toString());
		}

		return result;
	}

	private boolean checkMailExist(Connection connection, String emailAddr) {
		boolean res = false;
		try {
			ResultSet rs = null;
			String sql = "SELECT * FROM EMAIL WHERE EMAIL = ?;";
			PreparedStatement stat = connection.prepareStatement(sql);
			stat.setString(1, emailAddr);
			rs = stat.executeQuery();
			String []email = {"", ""};
			while(rs.next()) {
				email[0] = rs.getString("EMAIL");
				email[1] = rs.getString("CREATEDATE");
			}

			rs.close();
			stat.close();

			if(email[0].length() != 0) {
				res = true;
			}

		} catch(SQLException e) {
			e.printStackTrace();
			WriteLog.writeErrorLog(e.getMessage().toString());
		}

		return res;
	}

	public boolean createEmailTable(Connection connection) {
		try {
			Statement stat = connection.createStatement();
			/*
			 * EMAIL 情報標題
			 * CREATEDATE 開始訂閱日期
			 */
			String sql = "CREATE TABLE IF NOT EXISTS email " +
				"(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " EMAIL        TEXT    NOT NULL UNIQUE, " +
                " CREATEDATE   DATE    NOT NULL);";
			stat.executeUpdate(sql);
			stat.close();
		} catch(SQLException e) {
			WriteLog.writeErrorLog(e.getMessage().toString());
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean createTable(Connection connection) {
		try {
			Statement stat = connection.createStatement();
			/*
			 * TITLE 情報標題
			 * DESCRIPTION 情報大綱
			 * LINK 情報連結
			 */
			String sql = "CREATE TABLE IF NOT EXISTS anime " +
				"(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " TITLE        TEXT    NOT NULL UNIQUE, " +
                " THEDATE      DATE    NOT NULL, " +
                " LINK         TEXT    NOT NULL UNIQUE);";
			stat.executeUpdate(sql);
			stat.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			WriteLog.writeErrorLog(e.getMessage().toString());
			e.printStackTrace();
			return false;
		}

		return true;
	}
}

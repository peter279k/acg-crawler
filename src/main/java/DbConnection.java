import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;

public class DbConnection {
	
	public Connection iniConnection() {
		Connection connection = null; 
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:anime.db");
			
		} catch(Exception e) {
			e.printStackTrace();
		}

		return connection;
	}

	public boolean closeConnection(Connection connection) {
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
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

			DbConnection.writeLog(values);
		}

		return true;
	}

	public ArrayList<String> selectValue(Connection connection) {
		ArrayList<String> resultList = new ArrayList<String>();
		Statement stat = null;
		try {
			stat = connection.createStatement();
		      ResultSet rs = stat.executeQuery("SELECT * FROM anime;");
		      int index = 0;
		      while(rs.next()) {
		         String id = String.valueOf(rs.getInt("ID"));
		         String title = rs.getString("TITLE");
		         String link = rs.getString("LINK");
		         String dat = rs.getString("THEDATE");

		         resultList.set(index, id);
		         index++;
		         resultList.set(index, title);
		         index++;
		         resultList.set(index, link);
		         index++;
		         resultList.set(index, dat);
		         index++;
		      }
		      rs.close();
		      stat.close();
		} catch(SQLException e) {
			e.printStackTrace();

			return resultList;
		}

		return resultList;
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
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	private static void writeLog(String []val) {
		ArrayList<String> lines = new ArrayList<String>();
		Path logFile = Paths.get("./error.log");
		lines.add(val[2]);
		lines.add(val[0]);
		lines.add(val[1]);
		lines.add("\r\n");

		try {
			Files.write(logFile, lines, Charset.forName("UTF-8"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("write error log is failed!");
			e.printStackTrace();
		}
	}
}
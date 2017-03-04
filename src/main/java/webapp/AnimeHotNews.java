package webapp;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import database.DbConnection;

@SuppressWarnings("serial")
public class AnimeHotNews extends HttpServlet{
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
    		throws ServletException, IOException {
		
		DbConnection dbConn = new DbConnection();
		Connection conn = dbConn.iniConnection();

		ArrayList<String> resList = dbConn.selectValue(conn, "hot");
		if(resList.size() == 0) {
			resList.add("empty");
		}

		String json = new Gson().toJson(resList);
		
		resp.setHeader("Access-Control-Allow-Origin", "*");
		resp.setHeader("Accept", "application/json");
		resp.setHeader("Content-Type", "application/json; charset=utf-8");
        resp.getWriter().println(json);
    }
}

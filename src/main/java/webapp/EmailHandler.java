package webapp;

import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import database.DbConnection;

@SuppressWarnings("serial")
public class EmailHandler extends HttpServlet{
	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
    		throws ServletException, IOException {
		
		String emailAddr = req.getParameter("email-addr");
		String emailAction = req.getParameter("action");

		Map<String, String> resList = new HashMap<String, String>();

		if(emailAddr.length() == 0) {
			resList.put("result", "請輸入email！");
		} else if(this.checkMail(emailAddr) == false) {
			resList.put("result", "email錯誤！");
		} else {
			DbConnection dbConn = new DbConnection();
			Connection conn = dbConn.iniConnection();
			if(emailAction.equals("subscribe")) {
				dbConn.createEmailTable(conn);
				dbConn.insertEmailVal(conn, emailAddr);
				resList.put("result", "訂閱成功！");
			} else {
				String result = dbConn.delEmailVal(conn, emailAddr);
				if(result.contains("success")) {
					resList.put("result", "退訂成功！");
				} else if(result.contains("non-exist")) {
					resList.put("result", "退訂失敗，原因" + result);
				} else {
					resList.put("result", "退訂失敗");
				}
			}
			
			dbConn.closeConnection(conn);
		}

		String json = new Gson().toJson(resList);

		resp.setHeader("Access-Control-Allow-Origin", "*");
		resp.setHeader("Accept", "application/json");
		resp.setHeader("Content-Type", "application/json; charset=utf-8");
        resp.getWriter().println(json);
    }
	
	private boolean checkMail(String emailAddr) {
		boolean result = true;
		   try {
		      InternetAddress addr = new InternetAddress(emailAddr);
		      addr.validate();
		   } catch (AddressException ex) {
		      result = false;
		   }
		   return result;
	}
}

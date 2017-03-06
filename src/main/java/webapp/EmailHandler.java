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

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.security.web.csrf.CsrfToken;

import com.google.gson.Gson;

import database.DbConnection;
import security.TokenGenerator;

@SuppressWarnings("serial")
public class EmailHandler extends HttpServlet{
	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
    		throws ServletException, IOException {
		Map<String, String> resList = new HashMap<String, String>();
		TokenGenerator csrf = new TokenGenerator();
		CsrfToken checkToken = csrf.loadToken(req);
		if(checkToken == null) {
			resList.put("result", "missing or invalid csrf-token!"); 
		} else {
			String emailAddr = req.getParameter("email-addr");
			String emailAction = req.getParameter("action");

			emailAddr = StringEscapeUtils.escapeHtml4(emailAddr);
			emailAction = StringEscapeUtils.escapeHtml4(emailAction);

			if(emailAddr.length() == 0) {
				resList.put("result", "請輸入email！");
			} else if(this.checkMail(emailAddr) == false) {
				resList.put("result", "email錯誤！");
			} else {
				DbConnection dbConn = new DbConnection();
				Connection conn = dbConn.iniConnection();
				if(emailAction.equals("subscribe")) {
					dbConn.createEmailTable(conn);
					boolean result = dbConn.insertEmailVal(conn, emailAddr);

					if(result) {
						resList.put("result", "訂閱成功！");
					} else {
						resList.put("result", "郵件地址已經存在！");
					}
				} else {
					String result = dbConn.delEmailVal(conn, emailAddr);
					if(result.contains("success")) {
						resList.put("result", "退訂成功！");
					} else if(result.contains("non-exist")) {
						resList.put("result", "退訂失敗，原因：email地址不存在！");
					} else {
						resList.put("result", "退訂失敗");
					}
				}

				dbConn.closeConnection(conn);
			}
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

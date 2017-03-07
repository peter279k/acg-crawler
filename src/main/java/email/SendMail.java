package email;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import auth.Auth;
import database.DbConnection;
import logger.ReadLog;
import logger.WriteLog;
import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;
import parser.HtmlParser;

public class SendMail {
	private static MailGunMsg msgList = new MailGunMsg();
	private static long randSleep = Math.round(Math.random() * 10 * 2);
	private static Map<String, String> authList = new Auth().getAuth();
	private static Authenticator authenticator = new Authenticator() {

		@Override
		public Request authenticate(Route route, okhttp3.Response response) throws IOException {
			// TODO Auto-generated method stub
			String credential = Credentials.basic("api", SendMail.authList.get("api-key"));
			return response.request().newBuilder()
                .header("Authorization", credential)
                .build();
		}
	};
	private static String message = new EmailTemplate().generate();
	private static ArrayList<String> mailList = SendMail.getMailList();
	private static Request buildMaiGunReq(int index) {
		FormBody.Builder requestBody = new FormBody.Builder();
		String toAddr = "", subjStr = "";
		if(index == -1) {
			toAddr = SendMail.authList.get("from-email-address");
			subjStr = "ACG-Cgawler-log-" + HtmlParser.getTodayDat();
		} else {
			toAddr = SendMail.mailList.get(index);
			subjStr = "Anime 動漫電子報_" + HtmlParser.getTodayDat();
		}
		RequestBody reqBody = requestBody
			.add("from", SendMail.authList.get("from-email-account"))
			.add("to", toAddr)
			.add("subject", subjStr)
			.add("text", "You have to let email support the HTML strings!")
			.add("html", SendMail.message)
			.build();
		Request.Builder request = new Request.Builder();
		Request req = request
			.url(SendMail.authList.get("api-base-url"))
			.post(reqBody)
			.build();
		return req;
	}
	private static void buildMailGunResp(Request req) {
		OkHttpClient client = new OkHttpClient().newBuilder()
			.authenticator(SendMail.authenticator).build();
		try {
			Response response = client.newCall(req).execute();
			String respStr = response.body().string();
			if(response.isSuccessful() == false) {
				WriteLog.writeErrorLog(respStr);
			} else if(response.code() >= 300 && response.code() <= 600) {
				WriteLog.writeErrorLog(respStr);
			} else {
				Gson gson = new GsonBuilder().create();
				String jsonStr = response.body().string().toString();
				SendMail.msgList = gson.fromJson(jsonStr, MailGunMsg.class);
				System.out.println(SendMail.msgList);
			}
			response.close();
		} catch(IOException e) {
			e.printStackTrace();
			WriteLog.writeErrorLog(e.getMessage().toString());
		}
	}

	public static MailGunMsg sendMailGunMsg() {
		if(SendMail.mailList.size() == 0) {
			System.out.println("empty mail list...");
			System.exit(0);
		}
		if(SendMail.authList.containsKey("result")) {
			System.out.println(authList.get("result"));
			System.exit(0);
		}

		for(int index=0;index<SendMail.mailList.size();index++) {
			Request req = SendMail.buildMaiGunReq(index);
			try {
				SendMail.buildMailGunResp(req);
				TimeUnit.SECONDS.sleep(SendMail.randSleep);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				WriteLog.writeErrorLog(e.getMessage().toString());
				e.printStackTrace();
			}
		}

	    return SendMail.msgList;
	}

	public static String sendGmailMsg() {
		return null;
	}

	public static String sendGmailHtml() {
		return null;
	}

	public static void sendErrorLog() {
		String logString = ReadLog.getLog();
		if(logString.equals("empty-string")) {
			System.out.println("today is no error log...");
			System.exit(0);
		}

		SendMail.message = logString;
		Request req = SendMail.buildMaiGunReq(-1);
		try {
			SendMail.buildMailGunResp(req);
			TimeUnit.SECONDS.sleep(SendMail.randSleep);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			WriteLog.writeErrorLog(e.getMessage().toString());
			e.printStackTrace();
		}
	}

	private static ArrayList<String> getMailList() {
		DbConnection conn = new DbConnection();
		Connection c = conn.iniConnection();

		return conn.getEmailList(c);
	}
}

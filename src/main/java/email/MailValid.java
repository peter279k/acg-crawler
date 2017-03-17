package email;

import java.io.IOException;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import auth.Auth;
import logger.WriteLog;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MailValid {
	private static ValidResp msgList = new ValidResp();
	private static MxResp mxResList = new MxResp();
	private OkHttpClient client = new OkHttpClient();
	private String emailAddr = "";
	private Map<String, String> authList = new Auth().getAuth();
	public MailValid(String emailAddr) {
		this.emailAddr = emailAddr;
	}

	public boolean emailValidate() {
		boolean result = false;
		Request.Builder request = new Request.Builder();
		StringBuilder builder = new StringBuilder();
		builder.append("https://api.mailgun.net/v3/address/validate");
		builder.append("?api_key=" + authList.get("api-pub-key"));
		builder.append("&address=" + this.emailAddr);
		Request req = request
			.url(builder.toString())
			.build();
		result = this.buildResp(req);

		return result;
	}

	public boolean mxToolBoxCheck() {
		boolean result = false;
		String []strArr = emailAddr.split("@");
		String domainName = strArr[1];
		Request.Builder request = new Request.Builder();
		StringBuilder builder = new StringBuilder();
		builder.append("https://api.mxtoolbox.com/api/v1/lookup/mx/");
		builder.append(domainName);
		String credential = authList.get("mx-api-key");
		Request req = request
			.url(builder.toString())
			.get()
			.addHeader("authorization", credential)
			.addHeader("cache-control", "no-cache")
			.build();
		result = this.buildMxResp(req);

		return result;
	}

	private boolean buildMxResp(Request req) {
		boolean isValid = false;
		try {
			Response response = this.client.newCall(req).execute();
			String respStr = response.body().string();
			if(response.isSuccessful() == false) {
				WriteLog.writeErrorLog(respStr);
			} else if(response.code() >= 300 && response.code() <= 600) {
				WriteLog.writeErrorLog(respStr);
			} else {
				Gson gson = new GsonBuilder().create();
				String jsonStr = respStr;
				MailValid.mxResList = gson.fromJson(jsonStr, MxResp.class);
				if(MailValid.mxResList.getInformation().isEmpty() == false) {
					isValid = true;
				}
			}
			response.close();
		} catch(IOException e) {
			WriteLog.writeErrorLog(e.getMessage().toString());
			System.out.println(e.getMessage().toString());
		}

		return isValid;
	}

	private boolean buildResp(Request req) {
		boolean isValid = false;
		try {
			Response response = this.client.newCall(req).execute();
			String respStr = response.body().string();
			if(response.isSuccessful() == false) {
				WriteLog.writeErrorLog(respStr);
			} else if(response.code() >= 300 && response.code() <= 600) {
				WriteLog.writeErrorLog(respStr);
			} else {
				Gson gson = new GsonBuilder().create();
				String jsonStr = respStr;
				MailValid.msgList = gson.fromJson(jsonStr, ValidResp.class);
				if(MailValid.msgList.getIs_valid()) {
					isValid = true;
				}
			}
			response.close();
		} catch(IOException e) {
			e.printStackTrace();
			WriteLog.writeErrorLog(e.getMessage().toString());
		}

		return isValid;
	}
}

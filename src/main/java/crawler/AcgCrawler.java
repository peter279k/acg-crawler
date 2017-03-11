package crawler;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import logger.WriteLog;
import parser.*;

public class AcgCrawler {
	final String requestUrls[] = {
		"https://gnn.gamer.com.tw/rss.xml",
		"http://www.animen.com.tw/NewsArea/NewsList",
		"http://news.gamme.com.tw/feed",
		"http://news.gamme.com.tw/category/anime/feed"
	};
	private static OkHttpClient client = new OkHttpClient();
	private static int index = 0;

    public void httpClient() throws IOException {
    	for(int i=0;i<requestUrls.length;i++) {
    		AcgCrawler.index = i;
    		String content;

			try {
				content = AcgCrawler.httpRequest(requestUrls[AcgCrawler.index]);
				if(content.equals("false")) {
	    			WriteLog.writeErrorLog(requestUrls[AcgCrawler.index]);
	    		} else {
	    			if(requestUrls[AcgCrawler.index].contains("gamme")) {
	    				XmlParser.parse(content);
	    			} else if(requestUrls[AcgCrawler.index].contains("rss.xml")) {
	    				XmlParser.parseGammer(content);
	    			} else {
	    				HtmlParser.parse(content);
	    			}
	    			
	    			TimeUnit.SECONDS.sleep(Math.round(Math.random() * 10 * 2));
	    		}
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				WriteLog.writeErrorLog(e.getMessage().toString());
			}
    	}
    }

    private static String httpRequest(String reqUrl) throws IOException {
    	String responseStr = "";
    	Request request = new Request.Builder()
    			.url(reqUrl)
    			.build();
    	Response response = AcgCrawler.client.newCall(request).execute();

    	if(response.isSuccessful() == false) {
    		return String.valueOf(response.isSuccessful());
    	}

    	if(reqUrl.contains("rss.xml")) {
    		responseStr = new String(response.body().bytes(), "big5");
    	} else {
    		responseStr = response.body().string();
    	}

    	response.body().close();

    	return responseStr;
    }
}

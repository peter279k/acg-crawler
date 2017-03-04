import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import logger.WriteLog;
import parser.*;

public class AcgCrawler {
	final String requestUrls[] = {
		"http://www.animen.com.tw/NewsArea/NewsList",
		"http://news.gamme.com.tw/feed",
		"http://news.gamme.com.tw/category/anime/feed"
	};

	final String loopUrl[] = {
		"https://www.facebook.com/HKACGerZ",
		"https://www.facebook.com/PlayStationTaiwan/",
		"https://news.gamme.com.tw/category/anime/page",
		"https://www.facebook.com/taipeigameshow/",
		"https://www.facebook.com/Ubisoft.TWN/",
		"https://www.facebook.com/gamebase.gb/",
		"https://www.facebook.com/sppbuy/"
	};
	
	private static int index = 0;

    public void httpClient() throws IOException {
    	for(int i=0;i<requestUrls.length;i++) {
    		AcgCrawler.index = i;
    		String content;
			try {
				content = AcgCrawler.httpRequest(requestUrls[AcgCrawler.index]);
				if(content.equals("false")) {
	    			System.out.println(requestUrls[AcgCrawler.index]);
	    			AcgCrawler.parseContent(content, requestUrls[AcgCrawler.index]);
	    		} else {
	    			if(requestUrls[AcgCrawler.index].contains("gamme")) {
	    				XmlParser.parse(content);
	    				TimeUnit.SECONDS.sleep(Math.round(Math.random() * 10));
	    			} else {
	    				HtmlParser.parse(content, requestUrls[AcgCrawler.index]);
	    			}
	    		}
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				WriteLog.writeErrorLog(e.getMessage().toString());
			}
    	}
    }
    
    private static String httpRequest(String reqUrl) throws IOException {
    	OkHttpClient client = AcgCrawler.unsafeHttpClient();
    	Request request = new Request.Builder()
    			.url(reqUrl)
    			.build();
    	Response response = client.newCall(request).execute();
    	
    	if(response.isSuccessful() == false) {
    		return String.valueOf(response.isSuccessful());
    	}

    	return response.body().string();
    }
    
    private static void parseContent(String content, String url) {
    	HtmlParser.parse(content, url);
    }
    
    private static OkHttpClient unsafeHttpClient() {
    	try {
    		final TrustManager []trustAllCert = new TrustManager[] {
    			new X509TrustManager() {

					@Override
					public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
						// TODO Auto-generated method stub
						
					}

					@Override
					public X509Certificate[] getAcceptedIssuers() {
						// TODO Auto-generated method stub
						return new java.security.cert.X509Certificate[]{};
					}
    			}
    		};

    		// Install the all-trusting trust manager
    	    final SSLContext sslContext = SSLContext.getInstance("SSL");
    	    sslContext.init(null, trustAllCert, new java.security.SecureRandom());
    	    // Create an ssl socket factory with our all-trusting manager
    	    final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

    	    OkHttpClient.Builder builder = new OkHttpClient.Builder();
    	    builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCert[0]);
    	    builder.hostnameVerifier(new HostnameVerifier() {
    	    	@Override
    	    	public boolean verify(String hostname, SSLSession session) {
    	    		return true;
    	    	}
    	    });

    	    OkHttpClient okHttpClient = builder.build();

    	    return okHttpClient;
    	} catch(Exception e) {
    		WriteLog.writeErrorLog(e.getMessage().toString());
    		throw new RuntimeException(e);
    	}
    }
}

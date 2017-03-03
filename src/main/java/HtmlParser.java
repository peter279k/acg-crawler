import java.sql.Connection;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class HtmlParser {
	
	public static void parse(String content, String url) {
		Document doc = Jsoup.parse(content);
		DbConnection conn = new DbConnection();
		Connection c = conn.iniConnection();
		conn.createTable(c);
		
		Elements title = doc.select("div.news-item-title");
		Elements link = doc.select("div.news-item-title > a");
		Elements dat = doc.select("div.news-item-date");
		for(int index=0;index<title.size();index++) {
			String []currDat = dat.get(index).text().split(" ");
			String theDat = currDat[0];
			String val[] = {
				title.get(index).text().toString(),
				link.get(index).attr("href").toString(),
				theDat
			};
			System.out.println(val[0]);
			System.out.println(val[1]);
			System.out.println(val[2]);
			conn.insertValue(c, val);
		}

		conn.closeConnection(c);
	}
}

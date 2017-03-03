import java.sql.Connection;
import java.time.LocalDate;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class XmlParser {
	public static void parse(String contents) {
		DbConnection conn = new DbConnection();
		Connection c = conn.iniConnection();
		conn.createTable(c);
		Document doc = Jsoup.parse(contents);
		Elements pubDate = doc.select("item > pudDate");
		Elements title = doc.select("item > title");
		Elements link = doc.select("item > link");
		
		//puDate: Sat, 04 Mar 2017 04:40:08 +0800
		for(int index=0;index<pubDate.size();index++) {
			String theDat = XmlParser.convertPubDate(pubDate.get(index).text().toString());
			String val[] = {
				title.get(index).text().toString(),
				link.get(index).text().toString(),
				theDat
			};

			String todayDat = HtmlParser.getTodayDat();
			if(theDat.equals(todayDat)) {
				conn.insertValue(c, val);
			}
		}
	}
	
	private static String convertPubDate(String dates) {
		StringBuilder buildDate = new StringBuilder();
		LocalDate localDate = LocalDate.parse(dates);
		buildDate.append(localDate.getYear() + "/");
		buildDate.append(localDate.getMonthValue() + "/");
		buildDate.append(localDate.getDayOfMonth());

		return buildDate.toString();
	}
}

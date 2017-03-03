import java.sql.Connection;
import java.time.LocalDate;
import java.time.ZoneId;
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

			String todayDat = HtmlParser.getTodayDat();
			if(theDat.equals(todayDat)) {
				conn.insertValue(c, val);
			}
		}

		conn.closeConnection(c);
	}

	public static String getTodayDat() {
		ZoneId zoneId = ZoneId.of("Asia/Taipei");
		LocalDate localDate = LocalDate.now(zoneId);
		String date = "";
		int thisYear = localDate.getYear();
		int thisMonth = localDate.getMonthValue();
		int thisDay = localDate.getDayOfMonth();
		date += thisYear;
		date += "/";
		date += thisMonth;
		date += "/";
		date += thisDay;
		
		return date;
	}
}

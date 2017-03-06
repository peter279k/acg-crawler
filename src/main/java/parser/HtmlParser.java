package parser;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.ZoneId;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import database.DbConnection;

public class HtmlParser {
	public static void parse(String content) {
		Document doc = Jsoup.parse(content);
		DbConnection conn = new DbConnection();
		Connection c = conn.iniConnection();
		conn.createTable(c);

		Elements title = doc.select("div.news-item-title");
		Elements link = doc.select("div.news-item-title > a");
		Elements dat = doc.select("div.news-item-date");
		for(int index=0;index<title.size();index++) {
			String []currDat = dat.get(index).text().split(" ");
			String []dates = currDat[0].split("/");
			if(dates[1].length() == 1) {
				dates[1] = "0" + dates[1];
			}
			if(dates[2].length() == 1) {
				dates[2] = "0" + dates[2];
			}
			String theDat = dates[0] + "-" + dates[1] + "-" + dates[2];
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
		date += "-";
		if(thisMonth < 10) {
			date += "0" + thisMonth;
		} else {
			date += thisMonth;
		}

		date += "-";
		if(thisDay < 10) {
			date += "0" + thisDay;
		} else {
			date += thisDay;
		}

		return date;
	}
}

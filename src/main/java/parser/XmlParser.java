package parser;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import logger.WriteLog;
import database.DbConnection;

public class XmlParser {
	public static void parseGammer(String contents) {
		DbConnection conn = new DbConnection();
		Connection c = conn.iniConnection();
		conn.createEmailTable(c);
		Document doc = Jsoup.parse(contents, "big5", Parser.xmlParser());

		Elements title = doc.select("title");
		Elements pubDate = doc.select("pubDate");
		Elements link = doc.select("link");

		int dateIndex = 0;
		for(int index=2;index<title.size();index++) {
			String theDat = XmlParser.convertPubDate(pubDate.get(dateIndex).text().toString());
			String []val = {
				title.get(index).text().toString(),
				link.get(index).text().toString(),
				theDat
			};

			String todayDat = HtmlParser.getTodayDat();
			if(theDat.equals(todayDat)) {
				conn.insertValue(c, val);
			}

			dateIndex += 1;
		}

		conn.closeConnection(c);
	}

	public static void parse(String contents) {
		DbConnection conn = new DbConnection();
		Connection c = conn.iniConnection();
		conn.createTable(c);
		Document doc = Jsoup.parse(contents, "", Parser.xmlParser());
		Elements pubDate = doc.select("pubDate");
		Elements title = doc.select("title");
		Elements link = doc.select("link");

		//e.g. puDate: Sat, 04 Mar 2017 04:40:08 +0800
		for(int index=1;index<pubDate.size();index++) {
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

		conn.closeConnection(c);
	}

	private static String convertPubDate(String dates) {
		StringBuilder buildDate = new StringBuilder();
		String dat = dates.replace("+0800", "GMT");
		try {
			Date localDate = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH).parse(dat);
			LocalDate local = new java.sql.Date(localDate.getTime()).toLocalDate();
			buildDate.append(local.getYear() + "-");
			if(local.getMonthValue() >= 10) {
				buildDate.append(local.getMonthValue() + "-");
			} else {
				buildDate.append("0" + local.getMonthValue() + "-");
			}

			if(local.getDayOfMonth() >= 10) {
				buildDate.append(local.getDayOfMonth());
			} else {
				buildDate.append("0" + local.getDayOfMonth());
			}
		} catch(Exception e) {
			WriteLog.writeErrorLog(e.getMessage().toString());
			e.printStackTrace();
		}

		return buildDate.toString();
	}
}

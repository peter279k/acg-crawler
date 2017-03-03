import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class HtmlParser {
	
	public static void parse(String content, String url) {
		Document doc = Jsoup.parse(content);
		Elements link = doc.select("a[href]");
	}
	
	public static void parseJson(String content, String url) {
	}
}

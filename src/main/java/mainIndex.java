import java.io.IOException;

public class mainIndex {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AcgCrawler crawler = new AcgCrawler();

		try {
			crawler.httpClient();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

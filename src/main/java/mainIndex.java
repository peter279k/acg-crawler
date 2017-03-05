import java.io.IOException;

import crawler.AcgCrawler;

/*
 * 流程
 * 發送電子報：在每個禮拜四 (以PHP發電子報)
 * 發送完之後，就 DROP TABLE
 */
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

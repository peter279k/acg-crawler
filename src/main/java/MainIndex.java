import java.io.IOException;
import crawler.AcgCrawler;
import email.MailGunMsg;
import email.SendMail;
import logger.WriteLog;

/*
 * 流程
 * 發送電子報：在每個禮拜四 (以JAVA via MailGun發電子報)
 * 發送完之後，就 DROP TABLE
 */
public class MainIndex {
	private static void unknownAction() {
		StringBuilder builder = new StringBuilder();
		builder.append("Unknown action.");
		builder.append("Please set the action parameter.");
		WriteLog.writeErrorLog(builder.toString());
		System.out.println(builder.toString());
		System.exit(0);
	}
	private static void doCrawl() {
		AcgCrawler crawler = new AcgCrawler();
		try {
			crawler.httpClient();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			WriteLog.writeErrorLog(e.getMessage().toString());
		}
	}
	private static void doSendMail(String []args) {
		String serviceName = "";
		if(args.length > 2 && args[1].equals("GMAIL")) {
			serviceName = args[1];
		} else {
			serviceName = "MAILGUN";
		}

		if(serviceName.equals("GMAIL")) {
			SendMail.sendGmailHtml();
		} else {
			MailGunMsg respList = SendMail.sendMailGunMsg();
			if(respList.getId() != null && respList.getMessage() != null) {
				System.out.println(respList.getMessage());
			} else {
				WriteLog.writeErrorLog("No response json...");
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// Crawl page: java acg-crawler.jar crawler
		// Send newsletter: java acg-crawler.jar send-email
		// Send error log email: java acg-crawler.jar send-error-log
		if(args.length > 0) {
			if(args[0].equals("crawler")) {
				MainIndex.doCrawl();
			} else if(args[0].equals("send-email")) {
				MainIndex.doSendMail(args);
			} else if(args[0].equals("send-error-log")) {
				SendMail.sendErrorLog();;
			}
		} else {
			MainIndex.unknownAction();
		}
	}
}

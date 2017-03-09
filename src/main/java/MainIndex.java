import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;

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
	private static int setHour = 21;
	private static boolean checkHour = MainIndex.checkSpeciTime(MainIndex.setHour);
	private static String mailService = "MAILGUN";

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
	private static void doSendMail() {
		String serviceName = MainIndex.mailService;

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

	private static boolean checkSpeciTime(int hour) {
		boolean isSpeci = false;
		ZoneId zoneId = ZoneId.of("Asia/Taipei");
		LocalDateTime localDateTime = LocalDateTime.now(zoneId);
		int nowHour = localDateTime.getHour();
		if(nowHour == hour) {
			return isSpeci = true;
		}

		return isSpeci;
	}

	private static void doDaemonEmail() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				MainIndex.doSendMail();
			}
		});
		thread.start();
	}
	
	private static void doDaemonLog() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				SendMail.sendErrorLog();
			}
		});
		thread.start();
	}

	private static void doDaemonCrawl() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				MainIndex.doCrawl();
			}
		});
		thread.start();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// Crawl page: java acg-crawler.jar crawler
		// Send newsletter: java acg-crawler.jar send-email
		// Send error log email: java acg-crawler.jar send-error-log
		MainIndex.doDaemonCrawl();
		if(MainIndex.checkHour) {
			MainIndex.doDaemonEmail();
			MainIndex.doDaemonLog();
		}
	}
}

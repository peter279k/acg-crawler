package logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import parser.HtmlParser;

public class ReadLog {
	private static String currHome = System.getProperty("user.home");
	private static String filePathStr = ReadLog.currHome + "/" + HtmlParser.getTodayDat() + "_error.log";

	public static String getLog() {
		String resStr = "";
		File file = new File(ReadLog.filePathStr);
		Path filePath = Paths.get(ReadLog.filePathStr);
		if(file.exists()) {
			try {
				String str = "<html><head></head><body>";
				List<String> strList = Files.readAllLines(filePath);
				for(int index=0;index<strList.size();index++) {
					str += "<p>" + strList.get(index) + "</p>";
				}
				str += "</body></html>";
				resStr = str;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				WriteLog.writeErrorLog(e.getMessage().toString());
				e.printStackTrace();
			}
			file.delete();
		} else {
			resStr = "empty-string";
		}

		return resStr;
	}
}

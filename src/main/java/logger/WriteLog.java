package logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import parser.HtmlParser;

public class WriteLog {
	private static String prefixPath = "/home/tomcat7/";
	public static void writeErrorLog(String errorMsg) {
		ArrayList<String> lines = new ArrayList<String>();
		File logFilePath = new File(prefixPath + HtmlParser.getTodayDat() + "_error.log");
		if(logFilePath.exists()) {
			WriteLog.writeAppendLog(errorMsg);
		} else {
			Path logFile = Paths.get(prefixPath + HtmlParser.getTodayDat() + "_error.log");
			lines.add(errorMsg);
			try {
				Files.write(logFile, lines, Charset.forName("UTF-8"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("write error log is failed!");
				e.printStackTrace();
			}
		}
	}

	private static void writeAppendLog(String errorMsg) {
		try {
			File file = new File(prefixPath + HtmlParser.getTodayDat() + "_error.log");

			FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(errorMsg);
			bw.write("\r\n");

			bw.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("append error log is failed!");
			e.printStackTrace();
		}
	}
}

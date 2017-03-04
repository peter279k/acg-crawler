package logger;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class WriteLog {
	public static void writeErrorLog(String errorMsg) {
		ArrayList<String> lines = new ArrayList<String>();
		Path logFile = Paths.get("./error.log");
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

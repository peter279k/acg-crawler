package auth;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;

@SuppressWarnings("serial")
public class Auth extends HttpServlet {
	private List<String> authList = this.getAuthInfo();
	private Map<String, String> list = new HashMap<String, String>();
	private String stat = "";
	private String []arr = null;

	public Map<String,String> getAuth() {
		if(this.authList == null || this.authList.isEmpty()) {
			this.list.put("result", "auth.ini is not existed...");
		} else {
			for(int index=0;index<this.authList.size();index++) {
				if(this.authList.get(index).isEmpty()
					|| this.authList.get(index).equals("")
					|| this.authList.get(index).length() == 0) {
					break;
				} else if(this.authList.get(index).contains("[GMAIL]")) {
					this.stat = "GMAIL";
				} else if(stat.equals("GMAIL")) {
					this.arr = this.authList.get(index).split("=");
					if(arr.length != 2) {
						continue;
					}
					this.parse(index);
				} else if(this.authList.get(index).contains("[SQLite]")) {
					this.stat = "SQLite";
				} else if(this.stat.equals("SQLite")) {
					this.arr = this.authList.get(index).split("=");
					if(this.arr.length != 2) {
						continue;
					}
					this.parse(index);
				} else if(this.stat.equals("home")) {
					this.arr = this.authList.get(index).split("=");
					if(this.arr.length != 2) {
						continue;
					}
					this.parse(index);
				}
			}
		}

		return this.list;
	}

	private void parse(int index) {
		this.list.put(this.arr[0], this.arr[1]);
		if(this.authList.size() < (index+1)
			&& this.authList.get(index+1).isEmpty() == false
			&& this.authList.get(index+1).contains("=") == false) {
			this.stat = "";
		}
	}

	private List<String> getAuthInfo() {
		String path = "/home/peter/acgcrawler/auth.ini";
		File authFile = new File(path);
		List<String>listStr = null;
		if(authFile.exists()) {
			Path authPath = Paths.get(path);
			try {
				listStr = Files.readAllLines(authPath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println("The auth.ini is not existed...");
		}

		return listStr;
	}
}

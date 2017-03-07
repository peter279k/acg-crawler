package auth;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Auth {
	public Map<String,String> getAuth() {
		List<String> authList = this.getAuthInfo();
		Map<String, String> list = new HashMap<String, String>();
		if(authList == null || authList.isEmpty()) {
			list.put("result", "auth.ini is not existed...");
		} else {
			String stat = "";
			for(int index=0;index<authList.size();index++) {
				if(authList.get(index).isEmpty()
					|| authList.get(index).equals("")
					|| authList.get(index).length() == 0) {
					break;
				} else if(authList.get(index).contains("[GMAIL]")) {
					stat = "GMAIL";
				} else if(stat.equals("GMAIL")) {
					String []arr = authList.get(index).split("=");
					if(arr.length != 2) {
						continue;
					}
					list.put(arr[0], arr[1]);
					if(authList.size() < (index+1)
						&& authList.get(index+1).isEmpty() == false
						&& authList.get(index+1).contains("=") == false) {
						stat = "";
					}
				} else {
					String []arr = authList.get(index).split("=");
					if(arr.length != 2) {
						continue;
					}
					list.put(arr[0], arr[1]);
				}
			}
		}

		return list;
	}

	private List<String> getAuthInfo() {
		String path = "./auth.ini";
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

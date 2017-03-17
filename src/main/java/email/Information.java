package email;

public class Information {

	private String Pref;
	private String Hostname;
	private String IPAddress;
	private String TTL;

	public String getPref() {
		return this.Pref;
	}

	public void setPref(String Pref) {
		this.Pref = Pref;
	}

	public String getHostName() {
		return this.Hostname;
	}

	public void setHostName(String Hostname) {
		this.Hostname = Hostname;
	}

	public String getIpAddress() {
		return this.IPAddress;
	}

	public void setIpAddress(String IPAddress) {
		this.IPAddress = IPAddress;
	}

	public String getTtl() {
		return this.TTL;
	}

	public void setTtl(String TTL) {
		this.TTL = TTL;
	}
}

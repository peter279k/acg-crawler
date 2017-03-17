package email;

public class ValidResp {
	private boolean is_valid;
	private String address;
	private String did_you_mean;

	public boolean getIs_valid() {
		return this.is_valid;
	}

	public void setIs_valid(boolean is_valid) {
		this.is_valid = is_valid;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDid_you_Mean() {
		return this.did_you_mean;
	}

	public void setDid_you_mean(String did_you_mean) {
		this.did_you_mean = did_you_mean;
	}
}

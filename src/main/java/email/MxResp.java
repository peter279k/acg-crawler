package email;

import java.util.List;

public class MxResp {
	private List<Information> Information;
	private String Command;

	public List<Information> getInformation() {
		return this.Information;
	}

	public void setInformation(List<Information> Information) {
		this.Information = Information;
	}

	public String getCommand() {
		return this.Command;
	}

	public void setCommand(String Command) {
		this.Command = Command;
	}
}

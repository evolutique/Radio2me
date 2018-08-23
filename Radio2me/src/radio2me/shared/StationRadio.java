package radio2me.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class StationRadio implements IsSerializable {

	private String friendlyName = "";
	private String url = "";

	public StationRadio() {
	}

	public StationRadio(String pFriendlyName, String pUrl) {
		friendlyName = pFriendlyName;
		url = pUrl;
	}

	public String getFriendlyName() {
		return friendlyName;
	}

	public void setFriendlyName(String friendlyName) {
		this.friendlyName = friendlyName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}

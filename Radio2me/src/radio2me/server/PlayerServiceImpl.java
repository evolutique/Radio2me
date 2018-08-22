package radio2me.server;

import java.net.URL;
import java.net.URLConnection;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import javazoom.jl.player.Player;
import radio2me.client.PlayerService;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class PlayerServiceImpl extends RemoteServiceServlet implements PlayerService {

	private static Player player;

	public Boolean playUrl(String pUrl) throws Exception {
		Boolean result = true;
		try {
			URLConnection urlConnection = new URL(pUrl).openConnection();
			urlConnection.connect();
			if (null != player) {
				player.close();
			}
			player = new Player(urlConnection.getInputStream());
			// instruction bloquante
			player.play();
		} catch (Exception e) {
			System.out.println(e.toString());
			throw e;
		}

		return result;
	}

	public Boolean stop() {
		Boolean result = false;
		if (null != player) {
			player.close();
			player = null;
			result = true;
		}

		return result;
	}

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}
}

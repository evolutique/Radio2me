package radio2me.client;

import com.google.gwt.core.client.GWT;

/**
 * Model principal du programme côté client. Singleton.
 * 
 * @author admin
 *
 */
public class MainModel {

	private final PlayerServiceAsync playerService;

	private static MainModel _instance = null;

	private MainModel() {
		playerService = GWT.create(PlayerService.class);
	}

	public static MainModel getInstance() {
		if (null == _instance) {
			_instance = new MainModel();
		}

		return _instance;
	}

	public PlayerServiceAsync queryPlayerService() {
		return playerService;
	}
}

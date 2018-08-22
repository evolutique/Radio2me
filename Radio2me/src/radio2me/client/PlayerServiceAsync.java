package radio2me.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface PlayerServiceAsync {
	
	/**
	 * joue l'url passé en paramètre. retourne true si la lecture à pu commencer
	 * @param pUrl
	 * @param callback
	 * @throws Exception
	 */
	public void playUrl(String pUrl, AsyncCallback<Boolean> callback) throws Exception;
}

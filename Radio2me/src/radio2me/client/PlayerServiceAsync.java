package radio2me.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface PlayerServiceAsync {
	
	/**
	 * joue l'url pass� en param�tre. retourne true si la lecture � pu commencer
	 * @param pUrl
	 * @param callback
	 * @throws Exception
	 */
	public void playUrl(String pUrl, AsyncCallback<Boolean> callback);

	public void stop(AsyncCallback<Boolean> callback);
}

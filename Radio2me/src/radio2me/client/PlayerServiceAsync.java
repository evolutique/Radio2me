package radio2me.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import radio2me.shared.StationRadio;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface PlayerServiceAsync {

	/**
	 * joue l'url pass� en param�tre. retourne true si la lecture � pu commencer
	 * 
	 * @param pUrl
	 * @param callback
	 * @throws Exception
	 */
	public void playUrl(String pUrl, AsyncCallback<Boolean> callback);

	public void stop(AsyncCallback<Boolean> callback);

	public void isPlaying(AsyncCallback<Boolean> callback);

	public void addNewStation(StationRadio pNewStation, AsyncCallback<Void> callback);

	public void deleteStation(StationRadio toDelete, AsyncCallback<Void> callback);

	public void getListStations(AsyncCallback<ArrayList<StationRadio>> callback);

	public void getVolume(AsyncCallback<Float> callback);

	public void setVolume(float value, AsyncCallback<Void> callback);
}

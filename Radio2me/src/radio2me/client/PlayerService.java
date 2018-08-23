package radio2me.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import radio2me.shared.StationRadio;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("player")
public interface PlayerService extends RemoteService {
	public Boolean playUrl(String pUrl) throws Exception;

	public Boolean stop();

	public Boolean isPlaying();
	
	public ArrayList<StationRadio> getListStations();
	
	public void addNewStation(StationRadio pNewStation);
	
	public void deleteStation(StationRadio toDelete);
	
	public float getVolume();

	public void setVolume(float value);
}

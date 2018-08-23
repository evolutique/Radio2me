package radio2me.client;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import radio2me.shared.StationRadio;

/**
 * Model principal du programme c�t� client. Singleton.
 * 
 * @author admin
 *
 */
public class MainModel {

	private final PlayerServiceAsync playerService;

	private ArrayList<StationRadio> listStations;

	private static MainModel _instance = null;

	private MainModel() {
		playerService = GWT.create(PlayerService.class);
		//initListStations();
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

//	private void initListStations() {
//		playerService.getListStations(new AsyncCallback<ArrayList<StationRadio>>() {
//
//			@Override
//			public void onSuccess(ArrayList<StationRadio> result) {
//				listStations = result;
//
//			}
//
//			@Override
//			public void onFailure(Throwable caught) {
//				// TODO Auto-generated method stub
//
//			}
//		});
//	}
//
//	public ArrayList<StationRadio> getListStations() {
//		return listStations;
//	}
}

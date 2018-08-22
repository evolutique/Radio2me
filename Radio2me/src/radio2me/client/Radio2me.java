package radio2me.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

import gwt.material.design.client.constants.IconPosition;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialContainer;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import radio2me.shared.StationsCH;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Radio2me implements EntryPoint {
	private boolean playing = false;
	private PlayerServiceAsync service = MainModel.getInstance().queryPlayerService();

	private MaterialTextBox tbURL = new MaterialTextBox();
	private MaterialButton btPlay = new MaterialButton("", IconType.PLAY_CIRCLE_FILLED);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		MaterialContainer container = new MaterialContainer();
		// container.setHeight("200px");

		container.add(buildMainPanel());

		// on ajoute tout au DOM
		RootPanel.get().add(container);
	}

	private MaterialPanel buildMainPanel() {
		// panel
		MaterialPanel mainPanel = new MaterialPanel();
		mainPanel.setHeight("300px");

		// ligne des préenregistré
		MaterialRow favorisRow = buildFavorisRow();
		mainPanel.add(favorisRow);

		// ligne de l'url et des boutons
		MaterialRow urlRow = buildURLRow();
		mainPanel.add(urlRow);

		return mainPanel;
	}

	private MaterialRow buildFavorisRow() {
		MaterialRow mainRow = new MaterialRow();
		MaterialColumn mainCol = new MaterialColumn();
		mainCol.setGrid("s12");
		mainRow.add(mainCol);
		for (StationsCH currentStation : StationsCH.values()) {
			MaterialButton newStationButton = new MaterialButton(currentStation.name());
			newStationButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					tbURL.setText(currentStation.toString());
					playAction();
				}
			});
			newStationButton.setGrid("s2");
			mainCol.add(newStationButton);
		}

		return mainRow;
	}

	private MaterialRow buildURLRow() {
		MaterialRow mainRow = new MaterialRow();
		MaterialColumn mainCol = new MaterialColumn();
		mainCol.setGrid("s12");
		mainRow.add(mainCol);

		// composants
		MaterialLabel lbURL = new MaterialLabel("URL");
		lbURL.setGrid("s2");
		mainCol.add(lbURL);

		tbURL = new MaterialTextBox();
		tbURL.setText("http://energybern.ice.infomaniak.ch/energybern-high.mp3");
		tbURL.setGrid("s8");
		mainCol.add(tbURL);

		// bouton play
		btPlay = new MaterialButton("", IconType.PLAY_CIRCLE_FILLED);
		btPlay.setIconPosition(IconPosition.NONE);
		btPlay.setWidth("40px");
		btPlay.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				playAction();
			}

		});
		btPlay.setGrid("s1");
		mainCol.add(btPlay);

		MaterialButton btAdd = new MaterialButton("", IconType.ADD_CIRCLE);

		btAdd.setIconPosition(IconPosition.NONE);
		btAdd.setWidth("40px");
		btAdd.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

			}
		});
		btAdd.setGrid("s1");
		mainCol.add(btAdd);

		return mainRow;
	}

	private void playAction() {
		if (!playing) {
			playing = true;
			btPlay.setIconType(IconType.PAUSE_CIRCLE_FILLED);
			MaterialToast.fireToast("lecture démarée");

			service.playUrl(tbURL.getText(), new AsyncCallback<Boolean>() {

				@Override
				public void onSuccess(Boolean result) {
					// pas de retour tant que le player tourne coté serveur
				}

				@Override
				public void onFailure(Throwable caught) {
					MaterialToast.fireToast("Un problème lors du retour du serveur : " + caught.toString());

				}
			});
		} else {
			playing = false;
			btPlay.setIconType(IconType.PLAY_CIRCLE_FILLED);
			service.stop(new AsyncCallback<Boolean>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onSuccess(Boolean result) {
					// TODO Auto-generated method stub

				}
			});
		}
	}
}

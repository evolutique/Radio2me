package radio2me.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

import gwt.material.design.client.constants.IconPosition;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialContainer;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRange;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import radio2me.shared.StationRadio;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Radio2me implements EntryPoint {
	private boolean playing = false;
	private PlayerServiceAsync service;

	private MaterialTextBox tbURL = new MaterialTextBox();
	private MaterialTextBox tbFavorisName = new MaterialTextBox();
	private MaterialButton btPlay = new MaterialButton("", IconType.PLAY_CIRCLE_FILLED);

	private MaterialPanel mainPanel;
	private MaterialRow favorisRow;

	private ArrayList<StationRadio> listStations;
	private StationRadio selectedStation;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		service = MainModel.getInstance().queryPlayerService();
		MaterialContainer container = new MaterialContainer();
		// container.setHeight("200px");

		listStations = new ArrayList<StationRadio>();
		MainModel.getInstance().queryPlayerService().getListStations(new AsyncCallback<ArrayList<StationRadio>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(ArrayList<StationRadio> result) {
				listStations = result;
				container.add(buildMainPanel());

				// on ajoute tout au DOM
				RootPanel.get().add(container);
			}

		});

	}

	private MaterialPanel buildMainPanel() {
		// panel
		mainPanel = new MaterialPanel();
		mainPanel.setHeight("300px");

		// ligne de l'url et des boutons
		MaterialRow urlRow = buildURLRow();
		mainPanel.add(urlRow);

		// ligne des control
		MaterialRow controlRow = buildControlRow();
		mainPanel.add(controlRow);

		// ligne des préenregistré
		favorisRow = buildFavorisRow();
		mainPanel.add(favorisRow);

		return mainPanel;
	}

	private MaterialRow buildFavorisRow() {
		MaterialRow mainRow = new MaterialRow();
		MaterialColumn mainCol = new MaterialColumn();
		mainCol.setGrid("s12");
		mainRow.add(mainCol);
		Collections.sort(listStations, new Comparator<StationRadio>() {

			@Override
			public int compare(StationRadio o1, StationRadio o2) {
				return o1.getFriendlyName().compareToIgnoreCase(o2.getFriendlyName());
			}
		});
		for (StationRadio currentStation : listStations) {
			MaterialButton newStationButton = new MaterialButton(currentStation.getFriendlyName());
			newStationButton.setMargin(2);
			newStationButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					tbURL.setText(currentStation.getUrl());
					tbFavorisName.setText(currentStation.getFriendlyName());
					selectedStation = currentStation;
					playAction(true);
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
		lbURL.setGrid("s1");
		mainCol.add(lbURL);

		tbURL = new MaterialTextBox();
		// tbURL.setText("http://energybern.ice.infomaniak.ch/energybern-high.mp3");
		tbURL.setGrid("s8");
		mainCol.add(tbURL);

		// bouton play
		btPlay = new MaterialButton("", IconType.PLAY_CIRCLE_FILLED);
		btPlay.setIconPosition(IconPosition.NONE);
		btPlay.setWidth("40px");
		btPlay.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				playAction(false);
			}

		});
		btPlay.setGrid("s1");
		initPlayButton();
		mainCol.add(btPlay);

		// volume
		MaterialRange rVolume = new MaterialRange();
		rVolume.setMin(0);
		rVolume.setMax(10);
		rVolume.setValue(5);
		rVolume.addValueChangeHandler(new ValueChangeHandler<Integer>() {

			@Override
			public void onValueChange(ValueChangeEvent<Integer> event) {
				service.setVolume(new Float((float) event.getValue() / 10f), new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(Void result) {
						// TODO Auto-generated method stub

					}
				});
			}
		});

		rVolume.setGrid("s2");
//		service.getVolume(new AsyncCallback<Float>() {
//
//			@Override
//			public void onFailure(Throwable caught) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void onSuccess(Float result) {
//				rVolume.setValue(new Float(result * 10f).intValue());
//			}
//		});

		mainCol.add(rVolume);

		return mainRow;
	}

	private MaterialRow buildControlRow() {
		MaterialRow mainRow = new MaterialRow();
		MaterialColumn mainCol = new MaterialColumn();
		mainCol.setGrid("s12");
		mainRow.add(mainCol);

		// label favoris
		MaterialLabel lbFavoris = new MaterialLabel();
		lbFavoris.setText("Nom de la radio");
		lbFavoris.setGrid("s2");
		mainCol.add(lbFavoris);

		// nom du favoris sélectionné
		tbFavorisName = new MaterialTextBox();
		tbFavorisName.setGrid("s8");
		mainCol.add(tbFavorisName);

		// bouton ajouter
		MaterialButton btAdd = new MaterialButton("", IconType.SAVE);
		btAdd.setMargin(2);
		btAdd.setIconPosition(IconPosition.NONE);
		btAdd.setWidth("40px");
		btAdd.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addNewStationAction();
			}
		});
		btAdd.setGrid("s1");
		mainCol.add(btAdd);

		// bouton supprimer
		MaterialButton btDel = new MaterialButton("", IconType.DELETE);
		btDel.setMargin(2);
		btDel.setIconPosition(IconPosition.NONE);
		btDel.setWidth("40px");
		btDel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				deleteStationAction();
			}
		});
		btDel.setGrid("s1");
		mainCol.add(btDel);
		return mainRow;
	}

	private void deleteStationAction() {
		if (null != selectedStation) {
			service.deleteStation(selectedStation, new AsyncCallback<Void>() {

				@Override
				public void onFailure(Throwable caught) {
					MaterialToast.fireToast("Echec de la suppression");
				}

				@Override
				public void onSuccess(Void result) {
					MaterialToast.fireToast("Supprimer avec succès");
					listStations.remove(selectedStation);
					refreshFavorisRow();

				}
			});
		}
	}

	private void addNewStationAction() {
		StationRadio newStation = new StationRadio(tbFavorisName.getText(), tbURL.getText());
		service.addNewStation(newStation, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				MaterialToast.fireToast("Echec de l'ajout : " + caught.toString());

			}

			@Override
			public void onSuccess(Void result) {
				MaterialToast.fireToast("Ajouter avec succès");
				listStations.add(newStation);
				selectedStation = newStation;
				refreshFavorisRow();
			}
		});
	}

	private void refreshFavorisRow() {
		mainPanel.remove(favorisRow);
		favorisRow = buildFavorisRow();
		mainPanel.add(favorisRow);
	}

	/**
	 * détermine si une musique est en train d'etre jouée coté serveur
	 */
	private void initPlayButton() {
		service.isPlaying(new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(Boolean result) {
				if (result) {
					btPlay.setIconType(IconType.PAUSE_CIRCLE_FILLED);
					playing = true;
				} else {
					btPlay.setIconType(IconType.PLAY_CIRCLE_FILLED);
					playing = false;
				}
			}
		});
	}

	private void playAction(boolean forcePlay) {
		if (forcePlay) {
			playing = false;
			// on arrete coté serveur dans tous les cas pour lancé la nouvelle musique
			service.stop(new AsyncCallback<Boolean>() {

				@Override
				public void onFailure(Throwable caught) {
				}

				@Override
				public void onSuccess(Boolean result) {
				}
			});
		}
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

package radio2me.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.media.client.Audio;
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

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Radio2me implements EntryPoint {
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);
	boolean playing = false;
	Audio audio = Audio.createIfSupported();

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		MaterialContainer container = new MaterialContainer();

		// panel
		MaterialPanel mainPanel = new MaterialPanel();
		container.add(mainPanel);
		MaterialRow mainRow = new MaterialRow();
		mainPanel.add(mainRow);
		MaterialColumn mainCol = new MaterialColumn();
		mainCol.setGrid("s10");
		mainRow.add(mainCol);

		// composants
		MaterialLabel lbURL = new MaterialLabel("URL");
		lbURL.setGrid("s2");
		mainCol.add(lbURL);

		MaterialTextBox tbURL = new MaterialTextBox();
		tbURL.setText("http://energybern.ice.infomaniak.ch/energybern-high.mp3");
		tbURL.setGrid("s8");
		mainCol.add(tbURL);

		// bouton play
		MaterialButton btPlay = new MaterialButton("", IconType.PLAY_CIRCLE_FILLED);
		btPlay.setIconPosition(IconPosition.NONE);
		btPlay.setWidth("40px");
		btPlay.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (!playing) {
					playing = true;
					btPlay.setIconType(IconType.PAUSE_CIRCLE_FILLED);
					if (null != audio) {
						audio.setSrc(tbURL.getText());
						audio.setEnabled(true);
						audio.setVolume(0.9);
						audio.play();
						if (null != audio.getError()) {
							MaterialToast.fireToast("Erreur de lecture");
						}
					}
				} else {
					playing = false;
					btPlay.setIconType(IconType.PLAY_CIRCLE_FILLED);
					audio.pause();
				}
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

		// on ajoute tout au DOM
		RootPanel.get().add(container);
	}
}

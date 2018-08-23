package radio2me.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.servlet.ServletException;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import javazoom.jl.player.Player;
import radio2me.client.PlayerService;
import radio2me.shared.StationRadio;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class PlayerServiceImpl extends RemoteServiceServlet implements PlayerService {

	private static Thread tPlayer;
	private RunnablePlayer rPlayer;
	private ArrayList<StationRadio> listStations;

	@Override
	public void init() throws ServletException {
		super.init();
		initStationsRadio();
	}

	public Boolean playUrl(String pUrl) throws Exception {
		Boolean result = true;
		if (null != tPlayer) {
			rPlayer.stop();
		}
		rPlayer = new RunnablePlayer(pUrl);
		tPlayer = new Thread(rPlayer);
		tPlayer.start();
		if (rPlayer.getError().equals("")) {
			result = true;
		} else {
			result = false;
			throw new Exception(rPlayer.getError());
		}
		return result;

	}

	@Override
	public Boolean stop() {
		Boolean result = false;
		if (null != rPlayer) {
			rPlayer.stop();
			result = true;
		}
		return result;
	}

	public Boolean isPlaying() {
		Boolean result = false;
		result = (null != rPlayer && rPlayer.isPlaying());

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

	@Override
	public ArrayList<StationRadio> getListStations() {
		return listStations;
	}

	@Override
	public void addNewStation(StationRadio pNewStation) {
		File f = new File(getFileStations());
		if (null != f && f.exists()) {
			try {
				// FileWriter fw = new FileWriter(f);
				StringBuilder strNewStation = new StringBuilder();
				strNewStation.append(pNewStation.getFriendlyName());
				strNewStation.append(";");
				strNewStation.append(pNewStation.getUrl());
				strNewStation.append("\n");
				Files.write(Paths.get(f.getAbsolutePath()), strNewStation.toString().getBytes(),
						StandardOpenOption.APPEND);
//				fw.append(strNewStation.toString());
//				fw.close();
				listStations.add(pNewStation);
				Collections.sort(listStations, new Comparator<StationRadio>() {

					@Override
					public int compare(StationRadio o1, StationRadio o2) {
						return o1.getFriendlyName().compareToIgnoreCase(o2.getFriendlyName());
					}
				});
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	}

	@Override
	public void deleteStation(StationRadio toDelete) {
		try {
			listStations.remove(toDelete);
			File inputFile = new File(getFileStations());
			File tempFile = new File(getFileStations() + ".tmp");

			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

			String lineToRemove = toDelete.getFriendlyName() + ";" + toDelete.getUrl();
			String currentLine;

			while ((currentLine = reader.readLine()) != null) {
				// trim newline when comparing with lineToRemove
				String trimmedLine = currentLine.trim();
				if (trimmedLine.equals(lineToRemove))
					continue;
				writer.write(currentLine + System.getProperty("line.separator"));
			}
			
			
			writer.flush();
			writer.close();
			reader.close();
			boolean deleted = inputFile.delete();
			boolean successful = tempFile.renameTo(inputFile);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		initStationsRadio();
	}

	private void initStationsRadio() {
		listStations = new ArrayList<StationRadio>();
		String confFileStations = getFileStations();
		try (BufferedReader br = new BufferedReader(new FileReader(confFileStations))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] currentStation = line.split(";");
				listStations.add(new StationRadio(currentStation[0], currentStation[1]));
			}
			Collections.sort(listStations, new Comparator<StationRadio>() {

				@Override
				public int compare(StationRadio o1, StationRadio o2) {
					return o1.getFriendlyName().compareToIgnoreCase(o2.getFriendlyName());
				}
			});
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String getFileStations() {
		String result = "";
		try {
			javax.naming.Context ctx = new javax.naming.InitialContext();
			javax.naming.Context envCtx = (javax.naming.Context) ctx.lookup("java:comp/env");
			String confFolder = (String) envCtx.lookup("confFolder");
			result = confFolder + File.separator + "stations.properties";
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return result;
	}

	public float getVolume() {
		return rPlayer.getVolume();
	}

	public void setVolume(float value) {
		rPlayer.setVolume(value);
	}

	private class RunnablePlayer implements Runnable {
		private String url = "";
		private String errorMsg = "";
		private Player player = null;

		// mixeur pour le volume
		private Port lineOut;
		private FloatControl volControl;
		private Mixer mixer;

		public RunnablePlayer(String pUrl) {
			url = pUrl;
			initVolumeControl();
		}

		@Override
		public void run() {
			try {
				URLConnection urlConnection = new URL(url).openConnection();
				urlConnection.connect();
				if (null != player) {
					player.close();
				}
				player = new Player(urlConnection.getInputStream());
				// instruction bloquante
				player.play();
				close();
			} catch (Exception e) {
				errorMsg = e.toString();
			}
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

		public Boolean isPlaying() {
			return (null != player && !player.isComplete());
		}

		public String getError() {
			return errorMsg;
		}

		// mixeur pour le volume
		public void initVolumeControl() {
			lineOut = null;
			volControl = null;

			// It gets everyone of the System's Mixers
			Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
			try {
				// it Looks for those Mixers that suport the OutPut SPEAKER
				for (int i = 0; i < mixerInfo.length; i++) {
					mixer = AudioSystem.getMixer(mixerInfo[i]);

					// If the SPEAKER is supported, then it gets a line
					if (mixer.isLineSupported(Port.Info.SPEAKER)) {
						lineOut = (Port) mixer.getLine(Port.Info.SPEAKER);

						lineOut.open();

						// Once we have the line, we request the Volumen control as a FloatControl
						volControl = (FloatControl) lineOut.getControl(FloatControl.Type.VOLUME);
						// Everything is done
						volControl.setValue((float) 0.5);
					}
				}
			} catch (Exception error) {
				error.printStackTrace();
			}
		}

		public float getVolume() {
			return volControl.getValue();
		}

		public void setVolume(float value) {
			volControl.setValue(value);
		}

		public boolean isControlValid() {
			return (volControl == null) ? false : true;
		}

		public void close() {
			lineOut.close();
		}
	}
}

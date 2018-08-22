package radio2me.shared;

public enum StationsCH {
	COULEUR3, NRGBERN, ROUGEFM;

	@Override
	public String toString() {
		switch (this) {
		case COULEUR3:
			return "http://stream.srg-ssr.ch/m/couleur3/mp3_128";
		case NRGBERN:
			return "http://energybern.ice.infomaniak.ch/energybern-high.mp3";
		case ROUGEFM:
			return "http://rougefm.ice.infomaniak.ch/rougefm-high.mp3";
		default:
			return "";
		}
	}

}

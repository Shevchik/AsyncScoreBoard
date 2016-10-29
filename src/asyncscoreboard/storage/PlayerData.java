package asyncscoreboard.storage;

public class PlayerData {

	private int kills;
	private int deaths;
	private boolean show;

	public int getKills() {
		return kills;
	}

	public void setKills(int kills) {
		this.kills = kills;
	}

	public int getDeaths() {
		return deaths;
	}

	public void incKills() {
		this.kills++;
	}

	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}

	public void incDeaths() {
		this.deaths++;
	}

	public boolean show() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

}

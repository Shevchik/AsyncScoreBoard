package packetscoreboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.minecraft.server.v1_10_R1.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_10_R1.ScoreboardTeam;

public class PacketScoreboardTeam {

	private String name;
	private String prefix;
	private List<String> entries = new ArrayList<String>();
	public PacketScoreboardTeam(String name, String prefix, String... entries) {
		this.name = name;
		this.prefix = prefix;
		this.entries.addAll(Arrays.asList(entries));
	}

	public String getName() {
		return name;
	}

	public void setPrefix(String prefix) {
		if (this.prefix.equals(prefix)) {
			return;
		}
		this.prefix = prefix;
		packetscoreboard.getPlayerConnection().sendPacket(new PacketPlayOutScoreboardTeam(new PacketTeam(name, prefix, entries), 2));
	}

	public String getPrefix() {
		return prefix;
	}

	public List<String> getEntries() {
		return entries;
	}

	private PacketScoreboard packetscoreboard;
	protected void setScoreboard(PacketScoreboard packetscoreboard) {
		this.packetscoreboard = packetscoreboard;
	}

	protected static class PacketTeam extends ScoreboardTeam {

		private final String prefix;
		private final List<String> entries;
		public PacketTeam(String name, String prefix, List<String> entries) {
			super(null, name);
			this.prefix = prefix;
			this.entries = entries;
		}

		@Override
		public String getPrefix() {
			return prefix;
		}

		@Override
		public Collection<String> getPlayerNameSet() {
			return entries;
		}

	}

}

package packetscoreboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.minecraft.server.v1_12_R1.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_12_R1.ScoreboardTeam;

public class PacketScoreboardTeam {

	private String name;
	private String prefix;
	private String suffix;
	private List<String> entries = new ArrayList<>();
	public PacketScoreboardTeam(String name, String prefix, String suffix, String... entries) {
		this.name = name;
		this.prefix = prefix;
		this.suffix = suffix;
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
		packetscoreboard.getPlayerConnection().sendPacket(new PacketPlayOutScoreboardTeam(new PacketTeam(name, prefix, suffix, entries), 2));
	}

	public void setSuffix(String suffix) {
		if (this.suffix.equals(suffix)) {
			return;
		}
		this.suffix = suffix;
		packetscoreboard.getPlayerConnection().sendPacket(new PacketPlayOutScoreboardTeam(new PacketTeam(name, prefix, suffix, entries), 2));
	}

	public String getPrefix() {
		return prefix;
	}

	public String getSuffix() {
		return suffix;
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
		private final String suffix;
		private final List<String> entries;
		public PacketTeam(String name, String prefix, String suffix, List<String> entries) {
			super(null, name);
			this.prefix = prefix;
			this.suffix = suffix;
			this.entries = entries;
		}

		@Override
		public String getPrefix() {
			return prefix;
		}

		@Override
		public String getSuffix() {
			return suffix;
		}

		@Override
		public Collection<String> getPlayerNameSet() {
			return entries;
		}

	}

}

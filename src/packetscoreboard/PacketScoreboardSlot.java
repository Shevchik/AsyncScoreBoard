package packetscoreboard;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.server.v1_10_R1.PacketPlayOutScoreboardScore;
import net.minecraft.server.v1_10_R1.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_10_R1.ScoreboardScore;
import packetscoreboard.PacketScoreboard.PointedScoreboardObjective;
import packetscoreboard.PacketScoreboardTeam.PacketTeam;

public class PacketScoreboardSlot {

	private final HashSet<PacketScoreboardTeam> teams = new HashSet<>();
	private final HashMap<String, Integer> entryToValue = new HashMap<>();

	private final PacketScoreboard packetscoreboard;
	private final PointedScoreboardObjective objective;
	public PacketScoreboardSlot(PacketScoreboard packetscoreboard, String objName) {
		this.packetscoreboard = packetscoreboard;
		this.objective = new PointedScoreboardObjective(objName);
	}

	public Set<String> getEntries() {
		return entryToValue.keySet();
	}

	public int getValue(String entry) {
		return entryToValue.get(entry);
	}

	public void setEntry(String entry, int value) {
		entryToValue.put(entry, value);
		packetscoreboard.getPlayerConnection().sendPacket(new PacketPlayOutScoreboardScore(new PacketScore(objective, entry, value)));
	}

	public void removeEntry(String entry) {
		entryToValue.remove(entry);
		packetscoreboard.getPlayerConnection().sendPacket(new PacketPlayOutScoreboardScore(entry, objective));
	}

	public Set<PacketScoreboardTeam> getTeams() {
		return Collections.unmodifiableSet(teams);
	}

	public void addTeam(PacketScoreboardTeam team) {
		teams.add(team);
		team.setScoreboard(packetscoreboard);
		packetscoreboard.getPlayerConnection().sendPacket(new PacketPlayOutScoreboardTeam(new PacketTeam(team.getName(), team.getPrefix(), team.getSuffix(), team.getEntries()), 0));
	}

	public void removeTeam(PacketScoreboardTeam team) {
		teams.remove(team);
		packetscoreboard.getPlayerConnection().sendPacket(new PacketPlayOutScoreboardTeam(new PacketTeam(team.getName(), "", "", Collections.emptyList()), 1));
	}

	private static class PacketScore extends ScoreboardScore {

		private final int score;
		public PacketScore(PointedScoreboardObjective objective, String displayName, int value) {
			super(null, objective, displayName);
			this.score = value;
		}

		@Override
		public int getScore() {
			return score;
		}

	}

}

package packetscoreboard;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.server.v1_13_R2.PacketPlayOutScoreboardScore;
import net.minecraft.server.v1_13_R2.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_13_R2.ScoreboardServer;
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
		packetscoreboard.getPlayerConnection().sendPacket(new PacketPlayOutScoreboardScore(ScoreboardServer.Action.CHANGE, objective.getName(), entry, value));
	}

	public void removeEntry(String entry) {
		entryToValue.remove(entry);
		packetscoreboard.getPlayerConnection().sendPacket(new PacketPlayOutScoreboardScore(ScoreboardServer.Action.REMOVE, objective.getName(), entry, 0));
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

}

package packetscoreboard;

import java.util.ArrayList;
import java.util.EnumMap;

import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import net.minecraft.server.v1_10_R1.IScoreboardCriteria;
import net.minecraft.server.v1_10_R1.PacketPlayOutScoreboardDisplayObjective;
import net.minecraft.server.v1_10_R1.PacketPlayOutScoreboardObjective;
import net.minecraft.server.v1_10_R1.PlayerConnection;
import net.minecraft.server.v1_10_R1.Scoreboard;
import net.minecraft.server.v1_10_R1.ScoreboardBaseCriteria;
import net.minecraft.server.v1_10_R1.ScoreboardObjective;

public class PacketScoreboard {

	private final PlayerConnection connection;
	public PacketScoreboard(Player player) {
		this.connection = ((CraftPlayer) player).getHandle().playerConnection;
	}

	private final EnumMap<DisplaySlot, PacketScoreboardSlot> slots = new EnumMap<>(DisplaySlot.class);

	public boolean hasSlot(DisplaySlot slot) {
		return slots.containsKey(slot);
	}

	public PacketScoreboardSlot createSlot(DisplaySlot slot, String title) {
		String objName = "PSB"+slot.toString();
		PacketScoreboardSlot pslots = new PacketScoreboardSlot(this, objName);
		slots.put(slot, pslots);
		connection.sendPacket(new PacketPlayOutScoreboardObjective(new NamedScoreboardObjective(objName, title), 0));
		connection.sendPacket(new PacketPlayOutScoreboardDisplayObjective(getSlotId(slot), new PointedScoreboardObjective(objName)));
		return pslots;
	}

	public PacketScoreboardSlot getSlot(DisplaySlot slot) {
		return slots.get(slot);
	}

	public void removeSlot(DisplaySlot slot) {
		PacketScoreboardSlot rslot = slots.remove(slot);
		if (rslot != null) {
			for (PacketScoreboardTeam team : new ArrayList<>(rslot.getTeams())) {
				rslot.removeTeam(team);
			}
			String intName = "PSB"+slot.toString();
			connection.sendPacket(new PacketPlayOutScoreboardObjective(new PointedScoreboardObjective(intName), 1));
		}
	}

	protected PlayerConnection getPlayerConnection() {
		return connection;
	}

	private static int getSlotId(DisplaySlot slot) {
		switch (slot) {
			case PLAYER_LIST: {
				return 0;
			}
			case SIDEBAR: {
				return 1;
			}
			case BELOW_NAME: {
				return 2;
			}
		}
		return -1;
	}

	private static final Scoreboard fakeScoreboard = new Scoreboard();
	private static final IScoreboardCriteria fakeCriteria = new ScoreboardBaseCriteria("dummy");

	protected static class PointedScoreboardObjective extends ScoreboardObjective {

		public PointedScoreboardObjective(String name) {
			super(fakeScoreboard, name, fakeCriteria);
		}

	}

	protected static class NamedScoreboardObjective extends PointedScoreboardObjective {

		public NamedScoreboardObjective(String name, String title) {
			super(name);
			setDisplayName(title);
		}

	}

}

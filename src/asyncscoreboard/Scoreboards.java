package asyncscoreboard;

import java.util.HashMap;
import java.util.stream.Stream;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import me.clip.placeholderapi.PlaceholderAPI;
import packetscoreboard.PacketScoreboard;
import packetscoreboard.PacketScoreboardSlot;

public class Scoreboards {

	private static final Scoreboards instance = new Scoreboards();
	public static final Scoreboards getInstance() {
		return instance;
	}

	private final HashMap<String, PacketScoreboard> boards = new HashMap<String, PacketScoreboard>();
	private final Object lock = new Object();

	public void updateSlot(Player player, DisplaySlot slot, String displayName, SlotUpdateFunc func) {
		synchronized (lock) {
			PacketScoreboard scoreboard = boards.get(player.getName());
			if (scoreboard == null) {
				scoreboard = new PacketScoreboard(player);
				boards.put(player.getName(), scoreboard);
			}
			PacketScoreboardSlot pslot = null;
			if (!scoreboard.hasSlot(slot)) {
				pslot = scoreboard.createSlot(slot, displayName);
			}
			pslot = scoreboard.getSlot(slot);
			func.update(pslot);
		}
	}

	public void updateAll(BoardUpdateFunc func) {
		synchronized (lock) {
			boards.values().forEach(func::update);
		}
	}

	public void clearScoreboard(Player player) {
		synchronized (lock) {
			PacketScoreboard scoreboard = boards.get(player.getName());
			if (scoreboard != null) {
				Stream.of(DisplaySlot.values()).filter(scoreboard::hasSlot).forEach(scoreboard::removeSlot);
				boards.remove(player.getName());
			}
		}
	}

	public static String getSidebarTitle(Player player) {
		return formatLine(Main.cfgSBSidebarTitle, player);
	}

	public static String[] getSidebarLines(Player player) {
		return Stream.of(Main.cfgSBSidebarLines).map(row -> formatLine(row, player)).toArray(String[]::new);
	}

	public static String getBelowNameString() {
		return ChatColor.translateAlternateColorCodes('&', Main.cfgSBBelowNameString);
	}

	public static int getBelowNameValue(Player player) {
		try {
			return Integer.parseInt(PlaceholderAPI.setPlaceholders(player, Main.cfgSBBelowNameValue));
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	public static int getPlayerListValue(Player player) {
		try {
			return Integer.parseInt(PlaceholderAPI.setPlaceholders(player, Main.cfgSBPlayerListValue));
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	private static String formatLine(String string, Player player) {
		string = PlaceholderAPI.setPlaceholders(player, string);
		string = string.substring(0, Math.min(string.length(), 16));
		return string;
	}

	@FunctionalInterface
	public static interface SlotUpdateFunc {
		public void update(PacketScoreboardSlot slot);
	}

	@FunctionalInterface
	public static interface BoardUpdateFunc {
		public void update(PacketScoreboard board);
	}

}

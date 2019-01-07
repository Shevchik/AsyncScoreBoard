package asyncscoreboard.slots;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import asyncscoreboard.Scoreboards;
import packetscoreboard.PacketScoreboardTeam;

public class SidebarScoreboard {

	private static final char[] colorCodes = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

	public static void updateScoreBoard(Player player) {
		Scoreboards.getInstance().updateSlot(player, DisplaySlot.SIDEBAR, Scoreboards.getSidebarTitle(player), slot -> {
			String[] newRows = Scoreboards.getSidebarLines(player);
			Set<String> oldRows = slot.getEntries();
			if (oldRows.isEmpty()) {
				for (int rowNumber = 0; rowNumber < newRows.length; rowNumber++) {
					String entryName = ChatColor.getByChar(colorCodes[rowNumber]).toString() + ChatColor.RESET.toString();
					slot.setEntry(entryName, newRows.length - rowNumber);
					PrefixSuffix ps = splitLine(newRows[rowNumber]);
					PacketScoreboardTeam team = new PacketScoreboardTeam(entryName, ps.prefix, ps.suffix, entryName);
					slot.addTeam(team);
				}
			} else {
				slot.getTeams().forEach(team -> {
					PrefixSuffix ps = splitLine(newRows[newRows.length - slot.getValue(team.getEntries().get(0))]);
					team.setPrefix(ps.prefix);
					team.setSuffix(ps.suffix);
				});
			}
		});
	}

	private static PrefixSuffix splitLine(String line) {
		if (line.length() > 16) {
			String lastFormat = ChatColor.getLastColors(line);
			return new PrefixSuffix(line.substring(0, 16), lastFormat + line.substring(16));
		} else {
			return new PrefixSuffix(line);
		}
	}

	private static class PrefixSuffix {
		private final String prefix;
		private final String suffix;
		public PrefixSuffix(String prefix, String suffix) {
			this.prefix = prefix;
			this.suffix = suffix;
		}
		public PrefixSuffix(String prefix) {
			this(prefix, "");
		}
	}

}

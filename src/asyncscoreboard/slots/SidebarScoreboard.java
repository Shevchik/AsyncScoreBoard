package asyncscoreboard.slots;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import asyncscoreboard.Main;
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
					PacketScoreboardTeam team = new PacketScoreboardTeam(entryName, newRows[rowNumber], entryName);
					slot.addTeam(team);
				}
			} else {
				slot.getTeams().forEach(team -> {
					String oldRow = team.getPrefix();
					String entryName = team.getEntries().get(0);
					int value = slot.getValue(entryName);
					String newRow = newRows[newRows.length - value];
					if (!newRow.equals(oldRow)) {
						team.setPrefix(newRow);
					}
				});
			}
		});
	}

}

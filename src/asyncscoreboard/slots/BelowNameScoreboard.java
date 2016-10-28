package asyncscoreboard.slots;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import asyncscoreboard.Scoreboards;

public class BelowNameScoreboard {

	public static void updateScoreBoard(Player player) {
		Scoreboards.getInstance().updateSlot(
			player, DisplaySlot.BELOW_NAME, Scoreboards.getBelowNameString(),
			slot -> Bukkit.getOnlinePlayers().forEach(oplayer -> slot.setEntry(oplayer.getName(), Scoreboards.getBelowNameValue(oplayer)))
		);
	}

}

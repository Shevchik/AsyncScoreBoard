package asyncscoreboard.slots;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import asyncscoreboard.Scoreboards;

public class PlayerListScoreboard {

	public static void updateScoreBoard(Player player) {
		Scoreboards.getInstance().updateSlot(
			player, DisplaySlot.PLAYER_LIST, "dummy",
			slot -> Bukkit.getOnlinePlayers().forEach(oplayer -> {
				int value = Scoreboards.getPlayerListValue(oplayer);
				slot.setEntry(oplayer.getPlayerListName(), value);
				slot.setEntry(oplayer.getName(), value);
			})
		);
	}

}

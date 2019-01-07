package placeholders;

import org.bukkit.entity.Player;

import asyncscoreboard.Main;
import asyncscoreboard.storage.PlayerData;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderHook;

public class KillsDeathsPlaceHolder extends PlaceholderHook {

	private static final String hook_name = "asbkillsdeaths";


	@Override
	public String onPlaceholderRequest(Player player, String identifier) {
		if (player == null) {
			return null;
		}
		PlayerData stat = Main.getInstance().getStorage().getPlayerData(player.getUniqueId());
		if (stat == null) {
			return "0";
		}
		switch (identifier) {
			case "kills": {
				return Integer.toString(stat.getKills());
			}
			case "deaths": {
				return Integer.toString(stat.getDeaths());
			}
			case "kdr": {
				return String.valueOf(stat.getDeaths() == 0 ? 1f : ((float) stat.getKills()) / stat.getDeaths());
			}
		}
		return null;
	}

	public static void hook() {
		PlaceholderAPI.registerPlaceholderHook(hook_name, new KillsDeathsPlaceHolder());
	}

	public static void unhook() {
		PlaceholderAPI.unregisterPlaceholderHook(hook_name);
	}

}

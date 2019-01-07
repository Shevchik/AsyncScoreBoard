package placeholders;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderHook;
import net.milkbowl.vault.economy.Economy;

public class MoneyPlaceholder extends PlaceholderHook {

	private static final String hook_name = "asbmoney";

	@Override
	public String onPlaceholderRequest(Player player, String indetifier) {
		if (player == null) {
			return null;
		}
		Economy economy = Bukkit.getServicesManager().getRegistration(Economy.class).getProvider();
		switch (indetifier) {
			case "exact": {
				return String.valueOf(economy.getBalance(player));
			}
			case "round": {
				return String.valueOf((int) Math.round(economy.getBalance(player)));
			}
			case "floor": {
				return String.valueOf((int) Math.floor(economy.getBalance(player)));
			}
			case "ceil": {
				return String.valueOf((int) Math.ceil(economy.getBalance(player)));
			}
			default: {
				return null;
			}
		}
	}

	public static void hook() {
		PlaceholderAPI.registerPlaceholderHook(hook_name, new MoneyPlaceholder());
	}

	public static void unhook() {
		PlaceholderAPI.unregisterPlaceholderHook(hook_name);
	}

}

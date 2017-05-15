package placeholders;

import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderHook;

public class PingPlaceholder extends PlaceholderHook {

	private static final String hook_name = "asbplayer";

	@Override
	public String onPlaceholderRequest(Player player, String indetifier) {
		if (player == null) {
			return null;
		}
		if (indetifier.equals("ping")) {
			return Integer.toString(((CraftPlayer) player).getHandle().ping);
		}
		return null;
	}

	public static void hook() {
		PlaceholderAPI.registerPlaceholderHook(hook_name, new PingPlaceholder());
	}

	public static void unhook() {
		PlaceholderAPI.unregisterPlaceholderHook(hook_name);
	}

}

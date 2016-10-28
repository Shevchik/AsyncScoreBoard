package placeholders;

import org.bukkit.entity.Player;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;

import asyncscoreboard.Main;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderHook;

public class WorldGuardPlaceholder extends PlaceholderHook {

	private static final String hook_name = "asbworldguard";

	@Override
	public String onPlaceholderRequest(Player player, String identifier) {
		if (player == null) {
			return null;
		}
		if (identifier.equals("current_region")) {
			ApplicableRegionSet ars = WGBukkit.getRegionManager(player.getWorld()).getApplicableRegions(player.getLocation());
			if (ars.size() == 0) {
				return Main.cfgPHWGNoRegion;
			}
			LocalPlayer localplayer = WGBukkit.getPlugin().wrapPlayer(player, true);
			String region = ars.getRegions().iterator().next().getId();
			if (ars.isOwnerOfAll(localplayer) || ars.isMemberOfAll(localplayer)) {
				return Main.cfgPHWGOwnRegionPrefix + region;
			} else {
				return Main.cfgPHWGNotOwnRegionPrefix + region;
			}
		}
		return null;
	}

	public static void hook() {
		PlaceholderAPI.registerPlaceholderHook(hook_name, new WorldGuardPlaceholder());
	}

	public static void unhook() {
		PlaceholderAPI.unregisterPlaceholderHook(hook_name);
	}

}

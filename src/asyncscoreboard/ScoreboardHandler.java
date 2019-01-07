package asyncscoreboard;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;

import asyncscoreboard.slots.BelowNameScoreboard;
import asyncscoreboard.slots.PlayerListScoreboard;
import asyncscoreboard.slots.SidebarScoreboard;
import me.clip.placeholderapi.PlaceholderAPI;

public class ScoreboardHandler implements Listener {

	private BukkitTask sbUpdateTask;

	protected final HashMap<String, Long> blLastUpdateTime = new HashMap<>();
	protected final HashMap<String, Long> ssLastUpdateTime = new HashMap<>();
	protected final HashMap<String, Long> plLastUpdateTime = new HashMap<>();

	public void start() {
		Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
		sbUpdateTask = Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getInstance(), () -> {
			long curTime = System.currentTimeMillis();
			Bukkit.getOnlinePlayers().forEach(player -> {

				if (!Main.getInstance().getStorage().getPlayerData(player.getUniqueId()).show()) {
					return;
				}
				if (Main.cfgSBDisabledWorlds.contains(player.getWorld().getName())) {
					return;
				}

				if (Main.cfgSBBelowNameEnabled) {
					Long blLastUpdate = blLastUpdateTime.get(player.getName());
					if ((blLastUpdate == null) || ((curTime - blLastUpdate) > Main.cfgSBBelowNameInterval)) {
						BelowNameScoreboard.updateScoreBoard(player);
						blLastUpdateTime.put(player.getName(), curTime);
					}
				}

				if (Main.cfgSBSiderbarEnabled) {
					Long ssLastUpdate = ssLastUpdateTime.get(player.getName());
					if ((ssLastUpdate == null) || ((curTime - ssLastUpdate) > Main.cfgSBSidebarInterval)) {
						SidebarScoreboard.updateScoreBoard(player);
						ssLastUpdateTime.put(player.getName(), curTime);
					}
				}

				if (Main.cfgSBPlayerListEnabled) {
					Long plLastUpdate = plLastUpdateTime.get(player.getName());
					if ((plLastUpdate == null) || ((curTime - plLastUpdate) > Main.cfgSBPlayerListInterval)) {
						PlayerListScoreboard.updateScoreBoard(player);
						plLastUpdateTime.put(player.getName(), curTime);
					}
				}

			});
		}, 0L, 1L);
	}

	public void stop() {
		sbUpdateTask.cancel();
		Bukkit.getOnlinePlayers().forEach(Scoreboards.getInstance()::clearScoreboard);
		HandlerList.unregisterAll(this);
	}

	@EventHandler(priority=EventPriority.LOWEST)
	public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
		Scoreboards.getInstance().clearScoreboard(event.getPlayer());
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Scoreboards.getInstance().clearScoreboard(event.getPlayer());
		String playerName = event.getPlayer().getName();
		Scoreboards.getInstance().updateAll(board -> {
			if (board.hasSlot(DisplaySlot.BELOW_NAME)) {
				board.getSlot(DisplaySlot.BELOW_NAME).removeEntry(playerName);
			}
			if (board.hasSlot(DisplaySlot.PLAYER_LIST)) {
				board.getSlot(DisplaySlot.PLAYER_LIST).removeEntry(event.getPlayer().getPlayerListName());
				board.getSlot(DisplaySlot.PLAYER_LIST).removeEntry(playerName);
			}
		});
		ssLastUpdateTime.remove(playerName);
		plLastUpdateTime.remove(playerName);
		blLastUpdateTime.remove(playerName);
	}

	public static String replaceVariables(String string, Player player) {
		return PlaceholderAPI.setPlaceholders(player, string);
	}


}
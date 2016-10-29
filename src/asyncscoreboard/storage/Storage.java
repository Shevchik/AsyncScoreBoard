package asyncscoreboard.storage;

import java.io.File;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import asyncscoreboard.Main;

public class Storage implements Listener {

	private final ConcurrentHashMap<UUID, PlayerData> storage = new ConcurrentHashMap<>();

	public PlayerData getPlayerData(UUID uuid) {
		return storage.get(uuid);
	}

	private File getDataFile() {
		return new File(Main.getInstance().getDataFolder(), "playerdata.yml");
	}

	public void start() {
		Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
		YamlConfiguration config = YamlConfiguration.loadConfiguration(getDataFile());
		for (String uuidString : config.getKeys(false)) {
			try {
				UUID uuid = UUID.fromString(uuidString);
				if (Bukkit.getOfflinePlayer(uuid).hasPlayedBefore()) {
					ConfigurationSection section = config.getConfigurationSection(uuidString);
					PlayerData data = new PlayerData();
					data.setKills(section.getInt("Kills"));
					data.setDeaths(section.getInt("Deaths"));
					data.setShow(section.getBoolean("Show"));
					storage.put(uuid, data);
				}
			} catch (IllegalArgumentException e) {
			}
		}
	}

	public void stop() {
		YamlConfiguration config = new YamlConfiguration();
		for (Entry<UUID, PlayerData> entry : storage.entrySet()) {
			ConfigurationSection section = config.createSection(entry.getKey().toString());
			section.set("Kills", entry.getValue().getKills());
			section.set("Deaths", entry.getValue().getDeaths());
			section.set("Show", entry.getValue().show());
		}
		try {
			config.save(getDataFile());
		} catch (IOException e) {
		}
		HandlerList.unregisterAll(this);
	}

	@EventHandler
	public void onLogin(PlayerLoginEvent event) {
		if (getPlayerData(event.getPlayer().getUniqueId()) == null) {
			storage.put(event.getPlayer().getUniqueId(), new PlayerData());
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		if (player.getHealth() > 0.0f) {
			return;
		}
		getPlayerData(player.getUniqueId()).incDeaths();
		Player killer = player.getKiller();
		if (killer != null) {
			getPlayerData(player.getUniqueId()).incKills();
		}
	}

}

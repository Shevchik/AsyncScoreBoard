package asyncscoreboard;

import java.util.HashSet;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import asyncscoreboard.storage.Storage;
import placeholders.KillsDeathsPlaceHolder;
import placeholders.PingPlaceholder;
import placeholders.WorldGuardPlaceholder;

public class Main extends JavaPlugin {

	private static Main instance;
	public Main() {
		instance = this;
	}

	public static Main getInstance() {
		return instance;
	}

	public static HashSet<String> cfgSBDisabledWorlds;

	public static boolean cfgSBSiderbarEnabled;
	public static int cfgSBSidebarInterval;
	public static String cfgSBSidebarTitle;
	public static String[] cfgSBSidebarLines;

	public static boolean cfgSBBelowNameEnabled;
	public static int cfgSBBelowNameInterval;
	public static String cfgSBBelowNameString;
	public static String cfgSBBelowNameValue;

	public static boolean cfgSBPlayerListEnabled;
	public static int cfgSBPlayerListInterval;
	public static String cfgSBPlayerListValue;

	public static String cfgPHWGNoRegion;
	public static String cfgPHWGOwnRegionPrefix;
	public static String cfgPHWGNotOwnRegionPrefix;

	private final ScoreboardHandler handler = new ScoreboardHandler();
	private final Storage storage = new Storage();

	public Storage getStorage() {
		return storage;
	}

	@Override
	public void onEnable() {
		getConfig().options().copyDefaults(true);
		saveConfig();
		reloadConfig();
		storage.start();

		PingPlaceholder.hook();
		WorldGuardPlaceholder.hook();
		KillsDeathsPlaceHolder.hook();

		FileConfiguration config = getConfig();
		cfgSBDisabledWorlds = new HashSet<>(config.getStringList("Scoreboard.DisabledWorlds"));

		cfgSBSiderbarEnabled = config.getBoolean("Scoreboard.Sidebar.Enabled");
		cfgSBSidebarInterval = config.getInt("Scoreboard.Sidebar.Interval");
		cfgSBSidebarTitle = config.getString("Scoreboard.Sidebar.Title");
		cfgSBSidebarLines = config.getStringList("Scoreboard.Sidebar.Rows").toArray(new String[0]);

		cfgSBBelowNameEnabled = config.getBoolean("Scoreboard.BelowName.Enabled");
		cfgSBBelowNameInterval = config.getInt("Scoreboard.BelowName.Interval");
		cfgSBBelowNameString = config.getString("Scoreboard.BelowName.String");
		cfgSBBelowNameValue = config.getString("Scoreboard.BelowName.Value");

		cfgSBPlayerListEnabled = config.getBoolean("Scoreboard.PlayerList.Enabled");
		cfgSBPlayerListInterval = config.getInt("Scoreboard.PlayerList.Interval");
		cfgSBPlayerListValue = config.getString("Scoreboard.PlayerList.Value");

		cfgPHWGNoRegion = config.getString("Placeholders.WorldGuard.NoRegion");
		cfgPHWGOwnRegionPrefix = config.getString("Placeholders.WorldGuard.OwnRegionPrefix");
		cfgPHWGNotOwnRegionPrefix = config.getString("Placeholders.WorldGuard.NotOwnRegionPrefix");

		getCommand("asb").setExecutor(new Commands());

		handler.start();
	}

	@Override
	public void onDisable() {
		handler.stop();
		WorldGuardPlaceholder.unhook();
		PingPlaceholder.unhook();
		KillsDeathsPlaceHolder.unhook();
		storage.stop();
	}

}

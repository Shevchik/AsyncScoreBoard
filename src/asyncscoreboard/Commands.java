package asyncscoreboard;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import asyncscoreboard.storage.PlayerData;

public class Commands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only for players");
			return true;
		}
		Player player = (Player) sender;
		if (args.length == 1 && args[0].equalsIgnoreCase("toggle")) {
			PlayerData data = Main.getInstance().getStorage().getPlayerData(player.getUniqueId());
			if (data.show()) {
				data.setShow(false);
				Scoreboards.getInstance().clearScoreboard(player);
				player.sendMessage(ChatColor.YELLOW + "Scoreboards disabled");
			} else {
				data.setShow(true);
				player.sendMessage(ChatColor.YELLOW + "Scoreboards enabled");
			}
			return true;
		}
		return false;
	}

}

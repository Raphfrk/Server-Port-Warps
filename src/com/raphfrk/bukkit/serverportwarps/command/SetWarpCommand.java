package com.raphfrk.bukkit.serverportwarps.command;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.raphfrk.bukkit.serverportwarps.ServerPortWarps;

public class SetWarpCommand implements CommandExecutor {

	ServerPortWarps p;

	public SetWarpCommand(ServerPortWarps p) {
		this.p = p;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label,
			String[] args) {

		if(args.length < 1) {
			return false;
		}

		if(!(sender instanceof Player)) {
			sender.sendMessage("Only players can set warps");
			return true;
		}
		
		Player player = (Player)sender;
		
		String prefix = "p." + player.getName();
		
		String warpName = args[0];
		
		Location currentLoc = player.getLocation();
		
		boolean success = p.warpManager.setLocation(warpName, currentLoc, prefix, 10);
		
		if(success) {
			sender.sendMessage("Warp " + args[0] + " set to this location");
			return true;
		} else {
			sender.sendMessage("Unable to set warp");
			return true;
		}
	}

}


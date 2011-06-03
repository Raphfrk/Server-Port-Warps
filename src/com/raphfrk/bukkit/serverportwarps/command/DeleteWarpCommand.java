package com.raphfrk.bukkit.serverportwarps.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.raphfrk.bukkit.serverportwarps.ServerPortWarps;
import com.raphfrk.bukkit.serverportwarps.event.ServerPortWarpDeleteEvent;

public class DeleteWarpCommand implements CommandExecutor {

	ServerPortWarps p;

	public DeleteWarpCommand(ServerPortWarps p) {
		this.p = p;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label,
			String[] args) {

		if(args.length < 0) {
			return false;
		}

		if(!(sender instanceof Player)) {
			sender.sendMessage("Only players can delete warps");
			return true;
		}
		
		Player player = (Player)sender;

		String prefix = "p." + player.getName();
		
		String warpName = args[0];
		
		if(p.warpManager.deleteWarp(warpName, prefix)) {
			sender.sendMessage("Warp " + warpName + " has been deleted");
			return true;
		} else {
			
			String serverName = p.eventLink.getEntryLocation("serverport.warp." + prefix, warpName);
			
			if(serverName != null) {
				p.eventLink.sendEvent(serverName, new ServerPortWarpDeleteEvent(player.getName(), prefix, warpName));
				return true;
			}
			
			sender.sendMessage("Unable to find warp " + prefix + " " + warpName);
			return true;
		}
	}
}
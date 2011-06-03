package com.raphfrk.bukkit.serverportwarps.command;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.raphfrk.bukkit.serverportwarps.ServerPortWarps;
import com.raphfrk.bukkit.serverportwarps.event.ServerPortWarpSummonEvent;

public class UseWarpCommand implements CommandExecutor {

	ServerPortWarps p;

	public UseWarpCommand(ServerPortWarps p) {
		this.p = p;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label,
			String[] args) {

		if(args.length < 1) {
			return false;
		}

		if(!(sender instanceof Player)) {
			sender.sendMessage("Only players can use warps");
			return true;
		}

		Player player = (Player)sender;

		String prefix = "p." + player.getName();
		boolean personal = true;
		String targetServer = p.eventLink.getEntryLocation("serverport.warp." + prefix, args[0]);
		if(targetServer == null) {
			String[] groups = p.permissions.getGroups(player.getWorld().getName(), player.getName());
			if(groups != null) {
				for(String group : groups) {
					prefix = "g." + group;
					targetServer = p.eventLink.getEntryLocation("serverport.warp.g." + group , args[0]);
					if(targetServer != null) {
						break;
					}
				}
			}
			if(targetServer == null) {
				prefix = "a.";
				targetServer = p.eventLink.getEntryLocation("serverport.warp.a", args[0]);
			}
		}

		String warpName = args[0];

		if(targetServer == null) {
			sender.sendMessage(args[0] + " is an unknown warp target");
			return true;
		}

		String worldName = player.getWorld().getName();
		String playerName = player.getName();

		if(targetServer.equals(p.serverPortCore.getLocalServerName())) {
			Location loc = p.warpManager.getLocation(prefix, warpName);

			if(loc == null) {
				sender.sendMessage(args[0] + " is a lost warp target");
				return true;
			}

			String targetWorldName = loc.getWorld().getName();

			boolean local = targetWorldName.equals(worldName);

			if(!p.permissions.has(worldName, playerName, "serverport.warp.use.warp." + args[0])) {
				if(!local && (!p.permissions.has(worldName, playerName, "serverport.warp.use.from"))) {
					player.sendMessage("You can't leave this world with warps");
					return true;
				}

				if(local && (!p.permissions.has(worldName, playerName, "serverport.warp.use.local"))) {
					player.sendMessage("You can't move within this world with warps");
					return true;
				}
			}

			if(!p.permissions.has(targetWorldName, playerName, "serverport.warp.use.warp." + args[0])) {
				player.sendMessage("You do not have permission to travel to " + targetWorldName);
			} else {
				player.teleport(loc);
			}

			return true;
		} else if( targetServer != null) {
			if(!p.permissions.has(worldName, playerName, "serverport.warp.use.warp." + args[0])) {
				if(!p.permissions.has(worldName, playerName, "serverport.warp.use.from")) {
					player.sendMessage("You can't leave this world with warps");
					return true;
				}
			}
			p.eventLink.sendEvent(targetServer, new ServerPortWarpSummonEvent(playerName, prefix, warpName));
			return true;
		}
		
		return false;
	}

}

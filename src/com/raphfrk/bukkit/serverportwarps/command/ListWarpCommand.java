package com.raphfrk.bukkit.serverportwarps.command;

import java.util.LinkedList;
import java.util.Set;
import java.util.TreeMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.raphfrk.bukkit.serverportwarps.ServerPortWarps;

public class ListWarpCommand implements CommandExecutor {

	ServerPortWarps p;

	public ListWarpCommand(ServerPortWarps p) {
		this.p = p;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label,
			String[] args) {

		if(args.length < 0) {
			return false;
		}

		if(!(sender instanceof Player)) {
			sender.sendMessage("Only players can list warps");
			return true;
		}

		String playerName = ((Player)sender).getName();

		String tableName = "serverport.warp.p." + playerName;

		Set<String> warps = p.eventLink.copyEntries(tableName);

		TreeMap<String,LinkedList<String>> warpsSorted = new TreeMap<String,LinkedList<String>>();

		for(String current : warps) {
			String serverName = p.eventLink.getEntryLocation(tableName, current);

			if(serverName == null) {
				serverName = "unknown";
			}

			LinkedList<String> l = warpsSorted.get(serverName);
			if(l == null) {
				l = new LinkedList<String>();
				warpsSorted.put(serverName, l);
			}
			l.add(current);
		}

		String localServerName = p.serverPortCore.getLocalServerName();
		LinkedList<String> localWarps = warpsSorted.get(localServerName);

		if(localWarps != null) {
			sender.sendMessage("Local Warps");
			for(String warp : localWarps) {
				sender.sendMessage(warp);
			}
			warpsSorted.remove(localServerName);
		}

		for(String serverName : warpsSorted.keySet()) {
			
			sender.sendMessage("Server: " + serverName);
			
			LinkedList<String> serverWarps = warpsSorted.get(serverName);
			
			for(String warp : serverWarps) {
				sender.sendMessage(warp);
			}
			
		}

		return true;
	}
}

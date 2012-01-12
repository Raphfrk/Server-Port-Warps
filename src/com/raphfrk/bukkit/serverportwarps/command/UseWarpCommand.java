/*******************************************************************************
 * Copyright (C) 2012 Raphfrk
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
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

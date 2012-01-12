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

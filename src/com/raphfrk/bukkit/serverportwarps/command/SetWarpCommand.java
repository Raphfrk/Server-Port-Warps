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


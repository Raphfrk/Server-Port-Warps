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
package com.raphfrk.bukkit.serverportwarps;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class MyPermissions {

	// serverport.warps.use
	// serverport.warps.del.personal
	// serverport.warps.del.group.<group-name>
	// serverport.warps.create.personal
	// serverport.warps.create.group.<group-name>
	//                     .warp.<warp-name> -> a particular warp
	//                     .from             -> can use warps leaving this world
	//                     .to               -> can use warps to this world
	//                     .local            -> can use warps within this world
	// 
	
	private PermissionHandler permissionHandler;

	ServerPortWarps p;
	MyPermissions (ServerPortWarps p) {
		this.p = p;
	}

	void init() {
		Plugin permissionsPlugin = p.getServer().getPluginManager().getPlugin("Permissions");
		if (this.permissionHandler == null) {
			if (permissionsPlugin != null) {
				this.permissionHandler = ((Permissions) permissionsPlugin).getHandler();
			} else {
				p.log("Permission system not detected, defaulting to OP");
			}
		}
	}
	
	private boolean isOp(String playerName) {
		Player playerObj = p.getServer().getPlayer(playerName);
		return playerObj == null ? false : playerObj.isOp();
	}

	public boolean has(String world, String player, String permission) {
		
		if(permissionHandler == null) {
			return isOp(player);
		} else {
			p.log(world + " " + player + " " + permission);
			return isOp(player) || permissionHandler.has(world, player, permission);
		}
	}
	
	public int getValue(String world, String player, String permission) {
		int min = isOp(player) ? 10 : -1;
		if (this.permissionHandler == null) {
			return min;
		} else {
			return permissionHandler.getPermissionInteger(world, player, permission);
		}
	}
	
	public String[] getGroups(String world, String player) {
		if (this.permissionHandler == null) {
			return null;
		} else {
			return permissionHandler.getGroups(world, player);
		}
	}
}

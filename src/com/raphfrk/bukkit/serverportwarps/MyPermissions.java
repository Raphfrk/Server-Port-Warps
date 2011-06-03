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

package com.raphfrk.bukkit.serverportwarps.listeners;

import org.bukkit.Location;
import org.bukkit.event.CustomEventListener;
import org.bukkit.event.Event;

import com.raphfrk.bukkit.serverportwarps.ServerPortLocationMirror;
import com.raphfrk.bukkit.serverportwarps.ServerPortWarps;
import com.raphfrk.bukkit.serverportwarps.event.ServerPortWarpDeleteEvent;
import com.raphfrk.bukkit.serverportwarps.event.ServerPortWarpSummonEvent;

public class SPWCustomListener extends CustomEventListener {

	final ServerPortWarps p;

	public SPWCustomListener(ServerPortWarps p) {
		this.p = p;
	}

	public void onCustomEvent(Event event) {
		if(event instanceof ServerPortWarpSummonEvent) {
			onCustomEvent((ServerPortWarpSummonEvent)event);
		} else if(event instanceof ServerPortWarpDeleteEvent) {
			onCustomEvent((ServerPortWarpDeleteEvent)event);
		}
	}

	public void onCustomEvent(ServerPortWarpSummonEvent summonEvent) {
		
		String playerName = summonEvent.getPlayerName();
		String warpName = summonEvent.getWarpName();
		String prefix = summonEvent.getPrefix();
		
		Location loc = p.warpManager.getLocation(prefix, warpName);
		
		if(loc == null) {
			p.eventLink.sendMessage(playerName, "Unknown warp target " + warpName);
		}
		
		String targetWorldName = loc.getWorld().getName();
		String permission = "serverport.warp.use.warp.to";
		p.log(targetWorldName + " " + playerName + " " + permission);
		if(!p.permissions.has(targetWorldName, playerName, "serverport.warp.use.warp.to")) {
			p.eventLink.sendMessage(playerName, "You do not have permission to travel to " + targetWorldName);
		} else {
			p.serverPortCore.teleport(playerName, new ServerPortLocationMirror(p.serverPortCore.getLocalServerName(), loc));
		}
	}
	
	public void onCustomEvent(ServerPortWarpDeleteEvent deleteEvent) {
		
		String playerName = deleteEvent.getPlayerName();
		String warpName = deleteEvent.getWarpName();
		String prefix = deleteEvent.getPrefix();
		
		if(p.warpManager.deleteWarp(warpName, prefix)) {
			p.eventLink.sendMessage(playerName, "Warp " + warpName + " has been remotely deleted");
		} else {
			p.eventLink.sendMessage(playerName, "Unable to remotely detete warp " + warpName);
		}
	}

}
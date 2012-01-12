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

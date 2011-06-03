package com.raphfrk.bukkit.serverportwarps.event;

import org.bukkit.event.Event;

public class ServerPortWarpSummonEvent extends Event {

	private static final long serialVersionUID = 1L;

	public ServerPortWarpSummonEvent(String playerName, String prefix, String warpName) {
		super("ServerPortWarpSummonEvent");
		this.playerName = playerName;
		this.prefix = prefix;
		this.warpName = warpName;
	}
	
	final private String playerName;
	final private String warpName;
	final private String prefix;
	
	public String getPlayerName() {
		return playerName;
	}
	
	public String getWarpName() {
		return warpName;
	}
	
	public String getPrefix() {
		return prefix;
	}

}

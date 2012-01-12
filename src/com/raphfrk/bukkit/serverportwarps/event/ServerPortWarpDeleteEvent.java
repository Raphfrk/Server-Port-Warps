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
package com.raphfrk.bukkit.serverportwarps.event;

import org.bukkit.event.Event;

public class ServerPortWarpDeleteEvent extends Event {

	private static final long serialVersionUID = 1L;

	public ServerPortWarpDeleteEvent(String playerName, String prefix, String warpName) {
		super("ServerPortWarpDeleteEvent");
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

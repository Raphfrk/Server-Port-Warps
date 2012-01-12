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

import java.util.List;

import org.bukkit.Location;

public class WarpManager {

	ServerPortWarps p;

	WarpManager(ServerPortWarps p) {
		this.p = p;
	}

	public Location getLocation(String prefix, String warpName) {

		String fullName = "serverport.warp." + prefix + "." + warpName;

		try {
			p.eb.beginTransaction();
			ServerPortLocationMirror spLoc = p.eb.find(ServerPortLocationMirror.class).where().ieq("name", fullName).findUnique();

			if(spLoc == null) {
				return null;
			}

			if(!spLoc.getServer().equals(p.serverPortCore.getLocalServerName())) {
				return null;
			}

			Location loc = spLoc.getLocation(p.serverPortCore);

			return loc;

		} finally {
			p.eb.endTransaction();
		}

	}
	
	public boolean setLocation(String warpName, Location loc, String prefix, int max) {

		ServerPortLocationMirror spLoc = new ServerPortLocationMirror(p.serverPortCore.getLocalServerName(), loc);
		p.eb.beginTransaction();
		try {

			if(prefix != null) {
				int number = p.eb.find(ServerPortLocationMirror.class).where().istartsWith("name", "serverport.warp." + prefix).findRowCount();
				if(number >= max) {
					return false;
				}
			}

			ServerPortLocationMirror toSave = new ServerPortLocationMirror(spLoc);

			String fullName = "serverport.warp." + prefix + "." + warpName;

			toSave.setName(fullName);

			p.eb.save(toSave);
			return true;

		} finally {
			p.eb.commitTransaction();
			p.eventLink.addRouteEntry("serverport.warp." + prefix, warpName);
		}


	}
	
	public boolean deleteWarp(String warpName, String prefix) {

		p.eb.beginTransaction();
		try {

			String fullName = "serverport.warp." + prefix + "." + warpName;
			
			ServerPortLocationMirror spLoc = p.eb.find(ServerPortLocationMirror.class).where().ieq("name", fullName).findUnique();

			if(spLoc != null) {
				p.eb.delete(spLoc);
				p.eventLink.deleteRouteEntry("serverport.warp." + prefix, warpName);
				return true;
			} else {
				return false;
			}

		} finally {
			p.eb.commitTransaction();
		}


	}

	public boolean registerWarps() {

		p.eb.beginTransaction();
		try {
			List<ServerPortLocationMirror> warps = p.eb.find(ServerPortLocationMirror.class).where().istartsWith("name", "serverport.warp").findList();
			
			for(ServerPortLocationMirror current : warps) {

				String fullName = current.getName();
				
				int index = fullName.lastIndexOf(".");
				if(index <= 0) {
					continue;
				}
				String warpName = fullName.substring(index+1);
				String prefix = fullName.substring(0,index);
				
				p.eventLink.addRouteEntry(prefix, warpName);

			}
			
			return true;

		} finally {
			p.eb.commitTransaction();
		}
	}
	
	

}

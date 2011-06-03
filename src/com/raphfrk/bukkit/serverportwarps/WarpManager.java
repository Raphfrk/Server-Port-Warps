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

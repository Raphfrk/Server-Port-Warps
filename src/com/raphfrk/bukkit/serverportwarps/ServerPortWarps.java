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

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Server;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.avaje.ebean.EbeanServer;
import com.raphfrk.bukkit.eventlinkapi.EventLinkAPI;
import com.raphfrk.bukkit.serverportcoreapi.ServerPortCoreAPI;
import com.raphfrk.bukkit.serverportwarps.command.DeleteWarpCommand;
import com.raphfrk.bukkit.serverportwarps.command.ListWarpCommand;
import com.raphfrk.bukkit.serverportwarps.command.SetWarpCommand;
import com.raphfrk.bukkit.serverportwarps.command.UseWarpCommand;
import com.raphfrk.bukkit.serverportwarps.database.DatabaseManager;
import com.raphfrk.bukkit.serverportwarps.listeners.SPWCustomListener;

public class ServerPortWarps extends JavaPlugin {

	File pluginDirectory;

	static final String slash = System.getProperty("file.separator");

	final HashSet<String> admins = new HashSet<String>();

	Server server;

	PluginManager pm;
	ServicesManager sm;

	DependencyManager dm;

	EbeanServer eb;

	public SPWCustomListener customListener = new SPWCustomListener(this);

	public WarpManager warpManager = new WarpManager(this);

	static MiscUtils.LogInstance logger = MiscUtils.getLogger("[ServerPortWarps]");

	public EventLinkAPI eventLink;
	public ServerPortCoreAPI serverPortCore;
	PluginDescriptionFile pdfFile;

	public MyPermissions permissions = new MyPermissions(this);

	public void onEnable() {

		String name = "Server Port Warps";

		pm = getServer().getPluginManager();
		server = getServer();

		log(name + " initialized");

		pluginDirectory = this.getDataFolder();

		pdfFile = this.getDescription();

		permissions.init();

		sm = getServer().getServicesManager();

		dm = new DependencyManager(this);

		if(dm.connectEventLink() && dm.connectServerPortCore() && pm.isPluginEnabled(this)) {
			log(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!");
		}

		DatabaseManager.setupDatabaseManual(this, "[ServerPortWarps]");
		eb = getDatabase();

		getCommand("spw").setExecutor(new UseWarpCommand(this));
		getCommand("spwset").setExecutor(new SetWarpCommand(this));
		//		getCommand("spwsetg").setExecutor(new SetWarpGroupCommand());
		getCommand("spwdel").setExecutor(new DeleteWarpCommand(this));
		getCommand("spwlist").setExecutor(new ListWarpCommand(this));

		pm.registerEvent(Type.CUSTOM_EVENT, customListener, Priority.Normal, this);

		warpManager.registerWarps();
		
		log("Initialised");

	}

	public void onDisable() {
	}


	public void log(String message) {
		logger.log(message);
	}

	public void onLoad() {
		new ServerPortLocationMirror();
	}

	@Override
	public List<Class<?>> getDatabaseClasses() {
		List<Class<?>> list = new ArrayList<Class<?>>();
		list.add(ServerPortLocationMirror.class);
		return list;
	}
}


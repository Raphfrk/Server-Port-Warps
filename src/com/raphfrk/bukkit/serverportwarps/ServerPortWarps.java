package com.raphfrk.bukkit.serverportwarps;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.raphfrk.bukkit.eventlink.EventLink;
import com.raphfrk.bukkit.eventlink.MiscUtils;
import com.raphfrk.bukkit.eventlink.MyPropertiesFile;
import com.raphfrk.bukkit.serverportcore.ServerPortCore;

public class ServerPortWarps extends JavaPlugin {

	File pluginDirectory;

	static final String slash = System.getProperty("file.separator");

	final HashSet<String> admins = new HashSet<String>();

	Server server;

	PluginManager pm;

	static MiscUtils.LogInstance logger = MiscUtils.getLogger("[ServerPortWarps]");
	
	EventLink eventLink;
	ServerPortCore serverPortCore;

	public void onEnable() {

		String name = "Server Port Warps";

		pm = getServer().getPluginManager();
		server = getServer();

		log(name + " initialized");

		pluginDirectory = this.getDataFolder();

		if( !readConfig() ) {
			log("Unable to read configs or create dirs, ServerPort warps failed to start");
			return;
		}

		//		pm.registerEvent(Type.CUSTOM_EVENT, customListener, Priority.Normal, this);
		//		pm.registerEvent(Type.PLAYER_JOIN, playerListener, Priority.Normal, this);
		//		pm.registerEvent(Type.PLAYER_QUIT, playerListener, Priority.Normal, this);
		//		pm.registerEvent(Type.WORLD_LOAD, worldListener, Priority.Normal, this);

		PluginDescriptionFile pdfFile = this.getDescription();
		
		Plugin plugin = pm.getPlugin("EventLink");
		if(plugin != null && !(plugin instanceof EventLink)) {
			eventLink = (EventLink)plugin;
			log("Unable to connect to EventLink - disabling plugin");
			pm.disablePlugin(this);
		}
		plugin = pm.getPlugin("Server Port Core");
		if(plugin != null && !(plugin instanceof ServerPortCore)) {
			serverPortCore = (ServerPortCore)plugin;
			log("Unable to connect to Server Port Core - disabling plugin");
			pm.disablePlugin(this);
		}
		if(pm.isPluginEnabled(this)) {
			log(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!");
		}

	}

	public void onDisable() {
	}


	private boolean readConfig() {

		if( !pluginDirectory.exists() ) {
			pluginDirectory.mkdirs();
		}

		MyPropertiesFile pf;
		try {
			pf = new MyPropertiesFile(new File( pluginDirectory , "serverportwarps.txt").getCanonicalPath());
		} catch (IOException e) {
			return false;
		}

		pf.load();

		pf.save();

		return true;

	}

	public void log(String message) {
		logger.log(message);
	}

	public boolean isAdmin(Player player) {
		return eventLink != null && eventLink.isAdmin(player);
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String commandLabel, String[] args) {

		if(!commandSender.isOp()) {
			commandSender.sendMessage("You do not have sufficient user level for this command");
			return true;
		}

		if(command.getName().equals("serverport")) {

			commandSender.sendMessage("No commands are implemented yet for this plugin");

		}

		return false;

	}

}


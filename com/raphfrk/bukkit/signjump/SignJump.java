package com.raphfrk.bukkit.signjump;

import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;
import org.getspout.spoutapi.SpoutManager;

public class SignJump extends JavaPlugin {

	int trigger;
	
	SpoutManager sm;
	
	private final SignJumpPlayerListener playerListener = new SignJumpPlayerListener(this);
	
	@Override
	public void onDisable() {
	}

	@Override
	public void onEnable() {
		
		setupConfig();
		
		sm = SpoutManager.getInstance();
		
		getServer().getPluginManager().registerEvent(Type.PLAYER_INTERACT, playerListener, Priority.Normal, this);
		
		getServer().getLogger().info("Sign Jump Enabled");
		
	}
	
	private void setupConfig() {
		Configuration config = this.getConfiguration();

		config.load();
		config.setHeader("# Sign Jump Config");
		trigger = config.getInt("trigger", 20);
		config.save();
	}

}

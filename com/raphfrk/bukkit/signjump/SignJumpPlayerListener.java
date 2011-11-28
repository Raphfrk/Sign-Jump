package com.raphfrk.bukkit.signjump;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.getspout.spoutapi.block.SpoutBlock;
import org.getspout.spoutapi.player.SpoutPlayer;

public class SignJumpPlayerListener extends PlayerListener {

	private SignJump p;

	SignJumpPlayerListener(SignJump p) {
		this.p = p;
	}

	public void onPlayerInteract(PlayerInteractEvent event) {
		
		if (event.isCancelled()) {
			return;
		}
		
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		Material type = event.getMaterial();

		setSignTarget(block, null, type, player, p);

	}


	public static void setSignTarget(Block block, String longHostname, Material type, Player player, SignJump p) {
		if (block.getType().equals(Material.WALL_SIGN) || block.getType().equals(Material.SIGN_POST)) {
			if (type == null || (type.getId() == p.trigger && player.isOp())) {
				String destination = (String)((SpoutBlock)block).getData("com.raphfrk.signjump.destination");
				if (destination != null) {
					((SpoutBlock)block).removeData("com.raphfrk.signjump.destination");
					player.sendMessage("[SignJump] Sign link broken");
				} else {
					Sign sign = (Sign)block.getState();
					String hostname = sign.getLine(1);
					if (longHostname != null) {
						hostname = longHostname;
					}
					if (hostname.contains(":")) {
						player.sendMessage("[SignJump] Unable to enable link, hostname may not contain : symbol");
						return;
					}
					String portString = sign.getLine(2);
					int portnum = -1;
					if (portString.equals("")) {
						portnum = 25565;
					} else {
						try {
							portnum = Integer.parseInt(portString);
						} catch (NumberFormatException nfe) {
							player.sendMessage("[SignJump] Unable to enable link, port number can't be parsed");
							return;
						}
					}
					player.sendMessage("[SignJump] Connected sign to " + hostname + ":" + portnum);
					((SpoutBlock)block).setData("com.raphfrk.signjump.destination", hostname + ":" + portnum);
				}
			} else {
				String destination = (String)((SpoutBlock)block).getData("com.raphfrk.signjump.destination");
				if (destination != null) {
					String[] split = destination.split(":");
					if (split.length != 2) {
						player.sendMessage("[SignJump] bad destination data");
						((SpoutBlock)block).removeData("com.raphfrk.signjump.destination");
					} else {
						int portnum = -1;
						String hostname = split[0];
						try {
							portnum = Integer.parseInt(split[1]);
						} catch (NumberFormatException nfe) {
							player.sendMessage("[SignJump] bad destination data");
							((SpoutBlock)block).removeData("com.raphfrk.signjump.destination");
							return;
						}
						((SpoutPlayer)player).reconnect(hostname, portnum);
					}
				}
			}
		} else if (longHostname != null) {
			player.sendMessage("[SignJump] target is not a sign");
		}
	}

}

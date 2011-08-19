package com.raphfrk.bukkit.signjump;

import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetTargetCommand implements CommandExecutor {

	SignJump p;
	
	public SetTargetCommand(SignJump p) {
		this.p = p;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String text, String[] args) {

		if (!sender.isOp()) {
			sender.sendMessage("[SignJump] You do not have permission to use this command");
			return true;
		}
		
		if (!(sender instanceof Player)) {
			sender.sendMessage("[SignJump] Only players can set signs");
			return true;
		}
		
		if (args.length == 0) {
			return false;
		}
		
		Player player = (Player)sender;
		
		Block block = player.getTargetBlock(null, 100);
		
		SignJumpPlayerListener.setSignTarget(block, args[0], null, player, p);
		
		return true;
		
	}

}

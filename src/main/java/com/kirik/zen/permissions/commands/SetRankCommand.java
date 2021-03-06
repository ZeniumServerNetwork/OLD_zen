package com.kirik.zen.permissions.commands;

import java.util.Arrays;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.zen.commands.system.ICommand;
import com.kirik.zen.commands.system.ICommand.Help;
import com.kirik.zen.commands.system.ICommand.Names;
import com.kirik.zen.commands.system.ICommand.Permission;
import com.kirik.zen.commands.system.ICommand.Usage;
import com.kirik.zen.core.util.PlayerNotFoundException;
import com.kirik.zen.main.ZenCommandException;

@Names("setrank")
@Help("Changes or resets the tag of a player")
@Usage("/setrank <player> <rank>")
@Permission("zen.setrank")
public class SetRankCommand extends ICommand {
	
	@Override
	public void run(final CommandSender commandSender, String[] args, String argStr, String commandName) throws ZenCommandException {
		if(args.length < 2)
			throw new ZenCommandException(this.getUsage());
		
		if(!(commandSender instanceof Player)) {
			Player target = playerHelper.matchPlayerSingle(args[0]);
			
			String rank = args[1];
			String[] groups = plugin.permission.getGroups();
			if(Arrays.asList(groups).contains(rank)){
				playerHelper.setPlayerRank(target, rank);
			}else{
				throw new ZenCommandException("Rank \"" + rank + "\" not found!");
			}
			playerHelper.setPlayerRank(target, rank);
			playerHelper.sendServerMessage("CONSOLE set rank of " + target.getName() + " to " + rank);
			return;
		}
		
		String targetName = playerHelper.completePlayerName(args[0], false);
		
		if(targetName == null)
			throw new PlayerNotFoundException();
		
		Player targetPlayer = playerHelper.matchPlayerSingle(args[0]);
		
		String rank = args[1];
		String[] groups = plugin.permission.getGroups();
		
		int targetPlayerLevel = playerHelper.getPlayerLevel(targetPlayer);
		int commandSenderLevel = playerHelper.getPlayerLevel((Player)commandSender);
		//playerHelper.setPlayerPrefix(targetPlayer, newNick + " ");
		if(commandSenderLevel <= playerHelper.getLevelOfRank(rank)){
			throw new ZenCommandException("You cannot set a player to the rank of \"" + rank + "\"!");
		}
		if(commandSenderLevel <= targetPlayerLevel){
			throw new ZenCommandException("You are not able to set the rank of " + targetName + " to \"" + rank + "\"!");
		}
		if(Arrays.asList(groups).contains(rank)){
			playerHelper.setPlayerRank(targetPlayer, rank);
		}else{
			throw new ZenCommandException("Rank \"" + rank + "\" not found!");
		}
		
		playerHelper.sendServerMessage(commandSender.getName() + " set rank of " + targetName + " to " + rank + "\u00a7f!");
	}
	
}
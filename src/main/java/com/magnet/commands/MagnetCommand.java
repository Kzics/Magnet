package com.magnet.commands;

import com.magnet.Magnet;
import com.magnet.items.MagnetItem;
import com.magnet.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MagnetCommand implements CommandExecutor, TabCompleter {

    private final Main main;
    public MagnetCommand(final Main main){
        this.main = main;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if(!(sender instanceof Player)){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"You must be a player to use this command"));
            return false;
        }
        if(args.length != 2){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&cUsage: /magnet <magnetId> <player>"));
            return false;
        }

        if(sender.hasPermission("magnet.use") || sender.isOp()) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&aYou have been given a magnet"));
            ItemStack itemStack = new MagnetItem(main.getMagnetsManager().getMagnet(args[0]));
            Player target = main.getServer().getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&cPlayer not found"));
                return false;
            }
            target.getInventory().addItem(itemStack);
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"You do not have permission to use this command"));
            return false;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (sender.hasPermission("magnet.use") || sender.isOp()) {
            if (args.length == 1) {
                List<String> magnetIds = new ArrayList<>();
                for (Magnet magnet : main.getMagnetsManager().getMagnet()) {
                    magnetIds.add(magnet.getId());
                }
                return magnetIds;
            }
        }
        return null;
    }
}

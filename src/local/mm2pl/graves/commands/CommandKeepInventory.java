package local.mm2pl.graves.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import local.mm2pl.graves.GraveMain;

public class CommandKeepInventory implements CommandExecutor{
    @Override
    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
        if(args.length == 0)
        {
            if(sender instanceof Player)
            {
                GraveMain.keepinventory.put(sender.getName(), true);
                sender.sendMessage(GraveMain.lang.getString("keepinventory.enabled.self"));
                return true;
            }else {
                sender.sendMessage(GraveMain.lang.getString("keepinventory.error.console"));
                return true;
            }
        }else {
            if(args[0] == "*")
            {
                for (Player pl : Bukkit.getOnlinePlayers())
                {
                    GraveMain.keepinventory.put(pl.getName(), true);
                }
                return true;
            }
            Player pl;
            try
            {
                pl = Bukkit.getPlayer(args[0]);
            } catch (Exception e)
            {
                sender.sendMessage(String.format(GraveMain.lang.getString("command.argplr"), 1));
                return true;
            }
            GraveMain.keepinventory.put(pl.getName(), true);
            sender.sendMessage(String.format(GraveMain.lang.getString("keepinventory.enabled.other"), args[0]));
            return true;
        }
    }
}

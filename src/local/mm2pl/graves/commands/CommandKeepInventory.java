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
                sender.sendMessage("§6Temporarly enabled keepinventory for §aself§r");
                return true;
            }else {
                sender.sendMessage("§4Cannot enable keepinventory for §aCONSOLE§4: §aCONSOLE§4 is not an in-game player§r");
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
                sender.sendMessage("§4Argument 1 must be an in-game player.§r");
                return true;
            }
            GraveMain.keepinventory.put(pl.getName(), true);
            sender.sendMessage("§6Temporarly enabled keepinventory for §a"+args[0]+"§r");
            return true;
        }
    }
}

package local.mm2pl.graves.commands;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import local.mm2pl.graves.GraveMain;

public class CommandSoulbind implements CommandExecutor{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        if(sender instanceof Player)
        {
            boolean removeEnch = false;
            if(args.length >= 1)
            {
                if(args[0].equalsIgnoreCase("-r") || args[0].equalsIgnoreCase("--remove"))
                {
                    removeEnch = true;
                }
                if(args[0].equalsIgnoreCase("-h") || args[0].equalsIgnoreCase("--help") || args[0].equalsIgnoreCase("-?") || args[0].equalsIgnoreCase("?")|| args[0].equalsIgnoreCase("help"))
                {
                    return false;
                }
            }
            Player pl = (Player) sender;
            ItemStack item = pl.getEquipment().getItemInMainHand();
            if(item == null)
            {
                pl.sendMessage(GraveMain.lang.getString("soulbind.error.holditem"));
                return true;
            }
            ItemMeta meta = item.getItemMeta();
            List<String> lore;
            if(meta.hasLore())
            {
                lore = meta.getLore();
            }else {
                lore = new LinkedList<String>();
            }
            if(!removeEnch)
            {
                for (String string : lore)
                {
                    if(string.equalsIgnoreCase("§0gSoulbound"))
                    {
                        pl.sendMessage(GraveMain.lang.getString("soulbind.error.holditem"));
                        return true;
                    }
                }
                lore.add(GraveMain.lang.getString("soulbound.ench_name"));
                lore.add("§0gSoulbound");
                meta.setLore(lore);
                item.setItemMeta(meta);
                pl.sendMessage(GraveMain.lang.getString("soulbind.success.soulbound_added"));
            }else {
                List<String> newlore = new LinkedList<String>();
                for (String string : lore)
                {
                    if(GraveMain.checkSoulbound(string))
                    {
                        continue;
                    }
                    newlore.add(string);
                }
                meta.setLore(newlore);
                item.setItemMeta(meta);
                pl.sendMessage(GraveMain.lang.getString("soulbind.success.soulbound_removed"));
            }
            return true;
        }else {
            sender.sendMessage(GraveMain.lang.getString("command.ingame"));
            return true;
        }
    }
}

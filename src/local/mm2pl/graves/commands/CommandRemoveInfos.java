package local.mm2pl.graves.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import local.mm2pl.graves.GraveMain;

public class CommandRemoveInfos implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
        if(sender instanceof Player)
        {
            Player pl = (Player) sender;
            Inventory inv = pl.getInventory();
            for (ItemStack item : inv)
            {
                if(item == null)
                {
                    continue;
                }
                String coords = GraveMain.lang.getString("deathinfo.coords");
                if(item.hasItemMeta())
                {
                    ItemMeta meta = item.getItemMeta();
                    if(meta.hasDisplayName() && meta.hasLore())
                    {
                        if(meta.getDisplayName().matches(GraveMain.lockRegex) && meta.getLore().contains(coords) && meta.getLore().contains("§0GRDeathinfo"))
                        {
                            inv.remove(item);
                        }
                    }
                }
            }
            sender.sendMessage("§aRemoved all the death infos.§r");
            return true;
        }else {
            sender.sendMessage("§4This command can only be run by players in-game.§r");
            return true;
        }
    }
}

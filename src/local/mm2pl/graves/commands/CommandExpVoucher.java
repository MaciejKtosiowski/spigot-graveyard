package local.mm2pl.graves.commands;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CommandExpVoucher implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
        if(sender instanceof Player)
        {
            if(!(args.length >= 1))
            {
                return false;
            }
            if(!(args[0].matches("[0-9]+")))
            {
                sender.sendMessage("§4Argument 1: must be numerical§r");
                return true;
            }
            Player pl = (Player) sender;
            ItemStack voucher = new ItemStack(Material.PAPER, 1);
            ItemMeta meta = voucher.getItemMeta();
            meta.setDisplayName("EXP voucher.");
            List<String> lore = new LinkedList<String>();
            lore.add("§0g"+args[0]);
            lore.add("§aCreated: "+new Date(System.currentTimeMillis()).toString());
            meta.setLore(lore);
            voucher.setItemMeta(meta);
            pl.getInventory().addItem(voucher);
            return true;
        }else {
            sender.sendMessage("§4This command can only be run by players in-game.§r");
            return true;
        }
    }
    
}

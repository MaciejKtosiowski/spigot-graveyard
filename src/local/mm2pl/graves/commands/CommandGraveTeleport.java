package local.mm2pl.graves.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import local.mm2pl.graves.GraveMain;

public class CommandGraveTeleport implements CommandExecutor{
    @Override
    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
        if(sender instanceof Player)
        {
            Player pl = (Player) sender;
            ItemStack item = pl.getEquipment().getItemInMainHand();
            if(!(item.hasItemMeta()))
            {
                sender.sendMessage(GraveMain.lang.getString("gtp.not_a_deathinfo"));
                return true;
            }
            ItemMeta meta = item.getItemMeta();
            if(!(meta.hasDisplayName()))
            {
                sender.sendMessage(GraveMain.lang.getString("gtp.not_a_deathinfo"));
                return true;
            }
            if(!(meta.hasLore()))
            {
                sender.sendMessage(GraveMain.lang.getString("gtp.not_a_deathinfo"));
                return true;
            }
            boolean isNextLine = false;
            for (String line : meta.getLore())
            {
                if(isNextLine)
                {
                    int x;
                    int y;
                    int z;
                    String templine = line.replaceAll(GraveMain.lang.getString("regex.deathinfo"), "");
                    String[] spl = templine.split(" ");
                    if(!(spl.length == 3))
                    {
                        return false;
                    }
                    x = Integer.valueOf(spl[0]);
                    y = Integer.valueOf(spl[1]);
                    z = Integer.valueOf(spl[2]);
                    pl.teleport(new Location(pl.getWorld(), x, y, z), TeleportCause.COMMAND);
                    return true;
                }
                if(line.equalsIgnoreCase(GraveMain.lang.getString("deathinfo.coords")))
                {
                    isNextLine = true;
                }
            }
        }
        return false;
    }
}

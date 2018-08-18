package local.mm2pl.graves.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import local.mm2pl.graves.GraveMain;

public class CommandForceOpenGrave implements CommandExecutor{
    @Override
    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
        if(sender instanceof Player)
        {
            boolean isForced = false;
            if(args.length >= 1)
            {
                if(args[0].equalsIgnoreCase("-f") || args[0].equalsIgnoreCase("--force"))
                {
                    if(!GraveMain.config.getBoolean("opengrave.enable_force"))
                    {
                        sender.sendMessage(GraveMain.lang.getString("opengrave.force.disabled"));
                    }
                    if(sender.isOp() || sender.hasPermission("graveyard.opengrave.force"))
                    {
                        isForced = true;
                        sender.sendMessage(GraveMain.lang.getString("opengrave.force.disabled"));
                        
                    }else {
                        sender.sendMessage(GraveMain.lang.getString("opengrave.force.no_permission"));
                        return true;
                    }
                    
                }
            }
            Player pl = (Player) sender;
//            Location loc = pl.getLocation();
            Block block = pl.getTargetBlock(null, 6);
            Block block2 = block.getRelative(BlockFace.EAST);
            if(block2.getType() != Material.CHEST)
            {
                block2 = block.getRelative(BlockFace.WEST);
                if(block2.getType() != Material.CHEST)
                {
                    pl.sendMessage(GraveMain.lang.getString("opengrave.error.not_a_grave"));
                    return true;
                }
            }
            if(block.getType() == Material.CHEST)
            {
                Chest chest = (Chest) block.getState();
                Chest chest2 = (Chest) block2.getState();
                if(chest.getCustomName() == null)
                {
                    pl.sendMessage(GraveMain.lang.getString("opengrave.error.other"));
                    return true;
                }
                String name = chest.getCustomName();
                boolean personal = name.matches(pl.getName()+GraveMain.graveRegex);
                boolean isGrave = name.matches(".*"+GraveMain.graveRegex);
                if(personal || (isGrave&&isForced))
                {
                    if(chest.getLock() == null)
                    {
                        pl.sendMessage(GraveMain.lang.getString("opengrave.error.not_locked"));
                        return true;
                    }
                    if(chest.getLock().equals(""))
                    {
                        pl.sendMessage(GraveMain.lang.getString("opengrave.error.not_locked"));
                        return true;
                    }
                    pl.sendMessage(String.format(GraveMain.lang.getString("opengrave.success.changing"), chest.getLock()));
                    chest.setLock(null);
                    chest2.setLock(null);
                    chest.update();
                    chest2.update();
                    if(isForced)
                    {
                        Bukkit.broadcast(String.format(GraveMain.lang.getString("opengrave.force.admin_msg"), sender.getName()), "graveyard.msgs");
                    }
                    return true;
                }else {
                    if(!personal && isGrave && !isForced)
                    {
                        pl.sendMessage(GraveMain.lang.getString("opengrave.error.not_owned"));
                        if(pl.hasPermission("graveyard.opengrave.force"))
                        {
                            pl.sendMessage(GraveMain.lang.getString("opengrave.error.not_owned_force"));
                        }
                    }else {
                        pl.sendMessage(GraveMain.lang.getString("opengrave.error.other"));
//                        pl.sendMessage("§4The block you are looking at is not a grave.");/
                    }
                }
            }
        }
        return true;
    }
}

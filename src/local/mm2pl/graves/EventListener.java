package local.mm2pl.graves;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.data.type.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EventListener implements Listener{
    @EventHandler
    public void onPlayerItemUse(PlayerInteractEvent e)
    {
        if(e.getItem() == null)
        {
            return;
        }
        Action ac = e.getAction();
        if(ac.equals(Action.RIGHT_CLICK_AIR) || ac.equals(Action.RIGHT_CLICK_BLOCK))
        {
            ItemStack item = e.getItem();
            if(!(item.hasItemMeta()))
            {
                return;
            }
            ItemMeta meta = item.getItemMeta();
            if(!(meta.hasLore()))
            {
                return;
            }
            List<String> lore = meta.getLore();
            for (String string : lore)
            {
                if(string.matches("§0g[0123456789]+"))
                {
                    int exp = 0;
                    try
                    {
                        exp = Integer.valueOf(string.substring(3));
                    } catch (Exception e2)
                    {
                        exp = Integer.MAX_VALUE;
                    }
                    
                    e.getPlayer().giveExp(exp);
                    if(item.getAmount() == 1)
                    {
                        ItemStack air = new ItemStack(Material.AIR, 1);
                        e.getPlayer().getInventory().setItemInMainHand(air);
                        e.setCancelled(true);
                    }else {
                        ItemStack other_vouchers = new ItemStack(item);
                        other_vouchers.setItemMeta(meta);
                        other_vouchers.setAmount(item.getAmount()-1);
                        e.getPlayer().getInventory().setItemInMainHand(other_vouchers);
                    }
                    break;
                }
            }
        }
    }
    @EventHandler
    public void onPlayerChestClose(InventoryCloseEvent e)
    {
//        Bukkit.broadcastMessage("invcloseevent");
        Inventory inv = e.getInventory();
        Player pl = (Player) e.getPlayer();
        InventoryHolder holder = inv.getHolder();
        if(holder instanceof Chest || holder instanceof DoubleChest){
//            Bukkit.broadcastMessage("holder = chest");
            Location loc = inv.getLocation();
            org.bukkit.block.Chest chest2 = (org.bukkit.block.Chest) loc.getBlock().getState();
            if(chest2.getCustomName() == null)
            {
                return;
            }
            if(chest2.getCustomName().matches(pl.getName()+"'s grave\\. \\([0-9][0-9]:[0-9][0-9] [0-9]+\\.[0-9]+\\.[0-9]+\\)"))
            {
                boolean isEmpty = true;
                for (ItemStack item : inv)
                {
                    if(item == null)
                    {
                        continue;
                    }
                    if(item.getType() != Material.AIR)
                    {
//                        Bukkit.broadcastMessage(item.getType().toString()+" is not AIR");
                        isEmpty = false;
                        break;
                    }
                }
                if(isEmpty)
                {
                    pl.sendMessage("§aThe grave disappears...");
                    loc.getBlock().getRelative(BlockFace.EAST).setType(Material.AIR);
                    loc.getBlock().setType(Material.AIR);
                    EntityEquipment eq = pl.getEquipment();
                    ItemStack info = eq.getItemInMainHand();
                    if(info == null)
                    {
                        return;
                    }
                    if(info.hasItemMeta())
                    {
                        ItemMeta meta = info.getItemMeta();
                        if(!meta.hasLore())
                        {
                            return;
                        }
                        List<String> lore = meta.getLore();
                        if(lore.contains("§0GRDeathinfo"))
                        {
                            eq.setItemInMainHand(null);
                        }
                    }
                }
            }
        }
        
    }
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e)
    {
        
        e.setKeepInventory(true);
        e.setKeepLevel(true);
        Player pl = (Player) e.getEntity();
        if(GraveMain.keepinventory.getOrDefault(pl.getName(), false))
        {
            GraveMain.keepinventory.put(pl.getName(), false);
            pl.sendTitle("§k.§r", "§aKeep inventory was enabled for you§r", 20, 60, 20);
            return;
        }
        Inventory inv = pl.getInventory();
        
//        Bukkit.broadcastMessage(String.valueOf(items.length));
//        List<ItemStack> items = e.getDrops();
        Location loc = pl.getLocation();
//        Bukkit.broadcastMessage(String.valueOf(loc.getBlockX()));
//        Bukkit.broadcastMessage(String.valueOf(loc.getBlockY()));
//        Bukkit.broadcastMessage(String.valueOf(loc.getBlockZ()));
        
        Block block = loc.getWorld().getBlockAt(loc);
        Block block2 = block.getRelative(BlockFace.EAST);
        int iters = 0;
        while(!block.isEmpty() || !block2.isEmpty())
        {
            iters++;
            if(iters >= 1024)
            {
                //Failed to find a place for the grave, prevent a hang and return.
                Bukkit.broadcast("§4[ERR] Failed to place a grave for §a"+pl.getName()+"§r", "graveyard.msgs");
                pl.sendTitle("§4Failed to place the grave.", "", 20, 60, 20);
                return;
            }
            loc = loc.add(0, 1, 0);
            block = loc.getWorld().getBlockAt(loc);
            block2 = block.getRelative(BlockFace.EAST);
        }
        
        block.setType(Material.CHEST, false);
        block2.setType(Material.CHEST, false);
        Chest chest1 = (Chest) block.getBlockData();
        chest1.setFacing(BlockFace.NORTH);
        chest1.setType(Chest.Type.LEFT);
        block2.setBlockData(chest1);
        
        Chest chest2 = (Chest) block2.getBlockData();
        chest2.setFacing(BlockFace.NORTH);
        chest2.setType(Chest.Type.RIGHT);
        block2.setBlockData(chest2);
        
        org.bukkit.block.Chest chest3 = (org.bukkit.block.Chest) block.getState();
        Inventory binv = chest3.getInventory();
        ItemStack[] items = inv.getContents();
        List<ItemStack> savedItems = new LinkedList<ItemStack>();
        boolean wasAdded = false;
        for (ItemStack item : items)
        {
            if(item == null)
            {
                continue;
            }
            wasAdded = false;
            if(item.hasItemMeta())
            {
                ItemMeta meta = item.getItemMeta();
                if(!meta.hasLore())
                {
                    continue;
                }
                List<String> lore = meta.getLore();
                if(lore.contains("§0gSoulbound"))
                {
                    ItemStack clone = new ItemStack(item);
                    clone.setItemMeta(meta);
                    savedItems.add(item);
                    wasAdded = true;
                }
            }
            if(!wasAdded)
            {
                binv.addItem(item);
            }
        }
//        binv.setContents(items);
        
        
        int exp = 0;
        exp = pl.getTotalExperience();
        if(GraveMain.config.getBoolean("expvoucher.spawn_on_death"))
        {
//            Bukkit.broadcastMessage(String.valueOf(pl.getTotalExperience()));
//            Bukkit.broadcastMessage(String.valueOf(pl.getExp()));
//            Bukkit.broadcastMessage(String.valueOf(pl.getLevel()));
            pl.setTotalExperience(0);
            pl.setExp(0.0f);
            pl.setLevel(0);
            e.setDroppedExp(0);
            ItemStack voucher = new ItemStack(Material.PAPER, 1);
            ItemMeta meta2 = voucher.getItemMeta();
            meta2.setDisplayName("EXP voucher. (death)");
            List<String> lore = new LinkedList<String>();
            lore.add("Right-click to reedem the experience");
            String value = String.valueOf(Math.round(exp*GraveMain.config.getDouble("expvoucher.value")));
            lore.add("Value: "+value);
            lore.add("§aCreated: "+new Date(System.currentTimeMillis()).toString());
            if(GraveMain.config.getBoolean("expvoucher.soulbind"))
            {
                lore.add("§o§6Soulbound§r");
                lore.add("§0gSoulbound");
            }
            lore.add("§0g"+value);
            meta2.setLore(lore);
            voucher.setItemMeta(meta2);
            binv.addItem(voucher);
        }
        ItemStack deathInfocard = new ItemStack(Material.PAPER, 1);
        ItemMeta meta = deathInfocard.getItemMeta();
        List<String> deathInfoList = new LinkedList<String>();
//        deathInfoList.add("Name: "+pl.getName());
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm d.M.y");
        String today = formatter.format(date);
        deathInfoList.add(e.getDeathMessage());
        deathInfoList.add(today);
        deathInfoList.add("Coords:");
        deathInfoList.add("X: "+String.valueOf(loc.getBlockX())+" Y: "+String.valueOf(loc.getBlockY())+" Z: "+String.valueOf(loc.getBlockZ()));
        deathInfoList.add("EXP: "+String.valueOf(exp));
        deathInfoList.add("§o§6Soulbound§r");
        deathInfoList.add("§0gSoulbound");
        deathInfoList.add("§0GRDeathinfo");
        meta.setLore(deathInfoList);
        
        String lockstr = "Death info - "+String.valueOf(System.currentTimeMillis());
        meta.setDisplayName(lockstr);
        chest3.setLock(lockstr);
//        Bukkit.broadcastMessage(String.valueOf(chest3.isLocked()));
//        Bukkit.broadcastMessage(String.valueOf(chest3.getLock()));
        chest3.setCustomName(pl.getName()+"'s grave. ("+today+")");
        chest3.update();
        deathInfocard.setItemMeta(meta);
        inv.clear();
        inv.addItem(deathInfocard);
        for (ItemStack item : savedItems)
        {
            inv.addItem(item);
        }
//        ItemStack[] invcontents = inv.getContents();
    }
}

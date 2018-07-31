package local.mm2pl.graves;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import local.mm2pl.graves.commands.CommandExpVoucher;
import local.mm2pl.graves.commands.CommandForceOpenGrave;
import local.mm2pl.graves.commands.CommandGraveTeleport;
import local.mm2pl.graves.commands.CommandKeepInventory;
import local.mm2pl.graves.commands.CommandRemoveInfos;
import local.mm2pl.graves.commands.CommandSoulbind;
import local.mm2pl.graves.commands.DisabledCommand;

public class GraveMain extends JavaPlugin{
    public static GraveMain instance;
    public static FileConfiguration config;
    public static File configf;
    public static Map<String, Boolean> keepinventory;
    public static final String graveRegex = "'s grave\\. \\([0-9][0-9]:[0-9][0-9] [0-9]+\\.[0-9]+\\.[0-9]+\\)";
//    public void saveConfig() {
//        config = new YamlConfiguration();
//        configf = new File(getDataFolder(), "config.yml");
//        if (!configf.exists()) {
//            configf.getParentFile().mkdirs();
//            saveResource("config.yml", false);
//            try{
//                config.load(configf);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }else {
//        }
//        try{
//            config.save(configf);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        
//    }
    public static boolean checkSoulbound(String string)
    {
        return string.equalsIgnoreCase("§6Soulbound") || string.equalsIgnoreCase("§0gSoulbound") || string.equalsIgnoreCase("§6Soulbound§r") || string.equalsIgnoreCase("§6Soulbound§r");
    }
    @Override
    public void onEnable() {
        keepinventory = new HashMap<String, Boolean>();
        instance = this;
//        saveConfig();
        config = this.getConfig();
        config.addDefault("expvoucher.spawn_on_death", true);
        config.addDefault("expvoucher.value", 1.0d);
        config.addDefault("expvoucher.soulbind", true);
        config.addDefault("expvoucher.enable_command", true);
        
        config.addDefault("removeinfos.enable_command",true);
        
        config.addDefault("gtp.enable_command", true);
        
        config.addDefault("keepinventory.enable_command", true);
        
        config.addDefault("opengrave.enable_command", true);
        config.addDefault("opengrave.enable_force", true);
        
        config.addDefault("soulbound.enable_command", true);
        config.addDefault("soulbound.prevent_dropping", false);
        config.options().copyDefaults(true);
        saveConfig();
        
        getServer().getPluginManager().registerEvents(new EventListener(), this);
        if(config.getBoolean("keepinventory.enable_command"))
        {
            this.getCommand("keepinventory").setExecutor(new CommandKeepInventory());
        }else {
            this.getCommand("keepinventory").setExecutor(new DisabledCommand());
        }
        if(config.getBoolean("gtp.enable_command"))
        {
            this.getCommand("gtp").setExecutor(new CommandGraveTeleport());
        }else {
            this.getCommand("gtp").setExecutor(new DisabledCommand());
        }
        if(config.getBoolean("expvoucher.enable_command"))
        {
            this.getCommand("giveexpvoucher").setExecutor(new CommandExpVoucher());
        }else {
            this.getCommand("giveexpvoucher").setExecutor(new DisabledCommand());
        }
        if(config.getBoolean("removeinfos.enable_command"))
        {
            this.getCommand("removeinfos").setExecutor(new CommandRemoveInfos());
        }else {
            this.getCommand("removeinfos").setExecutor(new DisabledCommand());
        }
        if(config.getBoolean("opengrave.enable_command"))
        {
            this.getCommand("opengrave").setExecutor(new CommandForceOpenGrave());
        }else {
            this.getCommand("opengrave").setExecutor(new DisabledCommand());
        }
        if(config.getBoolean("soulbound.enable_command"))
        {
            this.getCommand("soulbind").setExecutor(new CommandSoulbind());
        }else {
            this.getCommand("soulbind").setExecutor(new DisabledCommand());
        }
    }
    public void onDisable() {
        
    }
}

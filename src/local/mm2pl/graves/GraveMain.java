package local.mm2pl.graves;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import local.mm2pl.graves.commands.CommandExpVoucher;
import local.mm2pl.graves.commands.CommandGraveTeleport;
import local.mm2pl.graves.commands.CommandKeepInventory;
import local.mm2pl.graves.commands.CommandRemoveInfos;
import local.mm2pl.graves.commands.DisabledCommand;

public class GraveMain extends JavaPlugin{
    public static GraveMain instance;
    public static FileConfiguration config;
    public static File configf;
    public static Map<String, Boolean> keepinventory;
    public void saveConfig() {
        configf = new File(getDataFolder(), "config.yml");
        if (!configf.exists()) {
            configf.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }
        config = new YamlConfiguration();
    }
    @Override
    public void onEnable() {
        keepinventory = new HashMap<String, Boolean>();
        instance = this;
        saveConfig();
        getServer().getPluginManager().registerEvents(new EventListener(), this);
        if(!config.getBoolean("keepinventory.enable_command"))
        {
            this.getCommand("keepinventory").setExecutor(new CommandKeepInventory());
        }else {
            this.getCommand("keepinventory").setExecutor(new DisabledCommand());
        }
        if(!config.getBoolean("gtp.enable_command"))
        {
            this.getCommand("gtp").setExecutor(new CommandGraveTeleport());
        }else {
            this.getCommand("gtp").setExecutor(new DisabledCommand());
        }
        if(!config.getBoolean("expvoucher.enable_command"))
        {
            this.getCommand("giveexpvoucher").setExecutor(new CommandExpVoucher());
        }else {
            this.getCommand("giveexpvoucher").setExecutor(new DisabledCommand());
        }
        if(!config.getBoolean("removeinfos.enable_command"))
        {
            this.getCommand("removeinfos").setExecutor(new CommandRemoveInfos());
        }else {
            this.getCommand("removeinfos").setExecutor(new DisabledCommand());
        }
    }
    public void onDisable() {
        
    }
}

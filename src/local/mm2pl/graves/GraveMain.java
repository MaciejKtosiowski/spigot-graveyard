package local.mm2pl.graves;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
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
    public static FileConfiguration lang;
    public static File langf;
    static Logger logger;
    public static Map<String, Boolean> keepinventory;
    public static String lockRegex = "Death info - [0-9]+";
    public static String graveRegex = "'s grave\\. \\([0-9][0-9]:[0-9][0-9] [0-9]+\\.[0-9]+\\.[0-9]+\\)";
    public static final String KEEP_INVENTORY = "keep_inventory";
    public static final String NORMAL = "normal";
    public static final String OPEN = "open";
    public static final String NO_OVERRIDE = "no_override";
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
    public void saveLang()
    {
        lang = new YamlConfiguration();
        langf = new File(getDataFolder(), "lang.yml");
        if (!langf.exists())
        {
            langf.getParentFile().mkdirs();
            saveResource("lang.yml", true);
            langf = new File(getDataFolder(), "lang.yml");
        } // else:  lang.yml exists
//        try{
//            config.save(langf);
//        } catch (IOException e) {
//            e.printStackTrace();
//            }
        
        try
        {
            lang.load(langf);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onEnable() {
        logger = getLogger();
        keepinventory = new HashMap<String, Boolean>();
        instance = this;
//        saveConfig();
        saveLang();
        graveRegex = lang.getString("regex.grave", graveRegex);
        lockRegex = lang.getString("regex.lock", lockRegex);
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
        
        config.addDefault("unbreakable_graves", true);
        
        config.addDefault("death_cause.pvp", NO_OVERRIDE);
        config.addDefault("death_cause.BLOCK_EXPLOSION", NORMAL);
        config.addDefault("death_cause.CONTACT", NORMAL);
        config.addDefault("death_cause.CRAMMING", NORMAL);
        config.addDefault("death_cause.CUSTOM", NORMAL);
        config.addDefault("death_cause.DRAGON_BREATH", NORMAL);
        config.addDefault("death_cause.DROWNING", NORMAL);
        config.addDefault("death_cause.DRYOUT", NORMAL);
        config.addDefault("death_cause.ENTITY_ATTACK", NORMAL);
        config.addDefault("death_cause.ENTITY_EXPLOSION", NORMAL);
        config.addDefault("death_cause.ENTITY_SWEEP_ATTACK", NORMAL);
        config.addDefault("death_cause.FALL", NORMAL);
        config.addDefault("death_cause.FALLING_BLOCK", NORMAL);
        config.addDefault("death_cause.FIRE", NORMAL);
        config.addDefault("death_cause.FIRE_TICK", NORMAL);
        config.addDefault("death_cause.FLY_INTO_WALL", NORMAL);
        config.addDefault("death_cause.HOT_FLOOR", NORMAL);
        config.addDefault("death_cause.LAVA", NORMAL);
        config.addDefault("death_cause.LIGHTNING", NORMAL);
        config.addDefault("death_cause.MAGIC", NORMAL);
        config.addDefault("death_cause.POISON", NORMAL);
        config.addDefault("death_cause.PROJECTILE", NORMAL);
        config.addDefault("death_cause.STARVATION", NORMAL);
        config.addDefault("death_cause.SUFFOCATION", NORMAL);
        config.addDefault("death_cause.SUICIDE", NORMAL);
        config.addDefault("death_cause.THORNS", NORMAL);
        config.addDefault("death_cause.VOID", NORMAL);
        config.addDefault("death_cause.WITHER", NORMAL);
        config.options().copyDefaults(true);
        saveConfig();
        
        boolean errers = false;
        for (String i : config.getConfigurationSection("death_cause").getKeys(false))
        {
            String k = config.getString("death_cause."+i);
            if(!(k.equals(NORMAL) || k.equals(KEEP_INVENTORY) || k.equals(OPEN)))
            {
                if(i.equals("pvp"))
                {
                    if(k.equals(NO_OVERRIDE))
                    {
                        continue;
                    }
                    logger.log(Level.SEVERE, "Config option "+i+" is not normal || keep_inventory || open || no_override");
                    errers = true;
                    this.setEnabled(false);
                    continue;
                }
                logger.log(Level.SEVERE, "Config option "+i+" is not normal || keep_inventory || open");
                errers = true;
                this.setEnabled(false);
            }
        }
        if(errers)
        {
            DisabledCommand exec = new DisabledCommand(true);
            this.getCommand("keepinventory").setExecutor(exec);
            this.getCommand("gtp").setExecutor(exec);
            this.getCommand("giveexpvoucher").setExecutor(exec);
            this.getCommand("removeinfos").setExecutor(exec);
            this.getCommand("opengrave").setExecutor(exec);
            this.getCommand("soulbind").setExecutor(exec);
            return;
        }
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

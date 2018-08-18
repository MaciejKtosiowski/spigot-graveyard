package local.mm2pl.graves.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import local.mm2pl.graves.GraveMain;

public class DisabledCommand implements CommandExecutor{
    private String msg = GraveMain.lang.getString("disabled.config");
    public DisabledCommand(boolean hasConfigErrors) {
        msg = GraveMain.lang.getString("disabled.config_err");
    }
    public DisabledCommand() {
        
    }
    @Override
    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
        sender.sendMessage(msg);
        return true;
    }
}

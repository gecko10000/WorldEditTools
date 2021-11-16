package gecko10000.WorldEditTools;

import gecko10000.WorldEditTools.guis.ToolGetGUI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import redempt.redlib.commandmanager.CommandHook;
import redempt.redlib.commandmanager.CommandParser;

public class CommandHandler {

    private final WorldEditTools plugin;

    public CommandHandler(WorldEditTools plugin) {
        this.plugin = plugin;
        new CommandParser(plugin.getResource("command.rdcml"))
                .parse().register(plugin.getName(), this);
    }

    @CommandHook("reload")
    public void reload(CommandSender sender) {
        plugin.reload();
        sender.sendMessage(plugin.makeReadableComponent("&aConfig reloaded!"));
    }

    @CommandHook("get")
    public void get(Player player) {
        new ToolGetGUI(plugin, player);
    }

}

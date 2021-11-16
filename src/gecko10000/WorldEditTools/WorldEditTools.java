package gecko10000.WorldEditTools;

import gecko10000.WorldEditTools.guis.ToolGetGUI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import redempt.redlib.configmanager.ConfigManager;

public class WorldEditTools extends JavaPlugin {

    public ConfigManager config;
    public ItemManager itemManager = new ItemManager(this);

    public void onEnable() {
        new CommandHandler(this);
        new Listeners(this);
        reload();
    }

    public void reload() {
        config = new ConfigManager(this)
                .addConverter(ItemManager.ToolType.class, ItemManager.ToolType::valueOf, ItemManager.ToolType::toString)
                .register(ToolGetGUI.class)
                .saveDefaults().load();
    }

    public static Component makeReadableComponent(String input) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(input);
    }

    public static String makeReadableString(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

}

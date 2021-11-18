package gecko10000.WorldEditTools;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.SessionManager;
import com.sk89q.worldedit.util.formatting.text.TextComponent;
import gecko10000.WorldEditTools.guis.FillGUI;
import gecko10000.WorldEditTools.guis.ToolGetGUI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.event.player.PlayerInteractEvent;
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
                .register(ToolGetGUI.class, FillGUI.class, itemManager)
                .saveDefaults().load();
        itemManager.addDefaults();
    }

    public static Component makeReadableComponent(String input) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(input);
    }

    public static String makeReadableString(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public LocalSession getSession(Player player) {
        return WorldEdit.getInstance()
                .getSessionManager()
                .get(player);
    }

    public Region getSelection(Player player) {
        try {
            return WorldEdit.getInstance()
                    .getSessionManager()
                    .get(player)
                    .getSelection();
        } catch (IncompleteRegionException e) {
            return null;
        }
    }

}

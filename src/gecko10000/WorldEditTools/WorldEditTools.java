package gecko10000.WorldEditTools;

import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import gecko10000.WorldEditTools.guis.ToolGetGUI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import redempt.redlib.configmanager.ConfigManager;

public class WorldEditTools extends JavaPlugin {

    public ConfigManager config;
    public ItemManager itemManager;

    public void onEnable() {
        itemManager = new ItemManager(this);
        new CommandHandler(this);
        new Listeners(this);
        reload();
    }

    public void reload() {
        config = new ConfigManager(this)
                .addConverter(ItemManager.ToolType.class, ItemManager.ToolType::valueOf, ItemManager.ToolType::toString)
                .register(ToolGetGUI.class, itemManager)
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

    public boolean isInAllowedIsland(org.bukkit.entity.Player player, BlockVector3 bv1, BlockVector3 bv2) {
        IridiumSkyblockAPI api = IridiumSkyblockAPI.getInstance();
        Location l1 = new Location(player.getWorld(), bv1.getX(), bv1.getY(), bv1.getZ());
        Location l2 = new Location(player.getWorld(), bv2.getX(), bv2.getY(), bv2.getZ());
        Island i1 = api.getIslandViaLocation(l1).orElse(null);
        Island i2 = api.getIslandViaLocation(l2).orElse(null);
        return i1 != null && i2 != null && i1.getId() == i2.getId() && i1.getMembers().contains(api.getUser(player));
    }

}

package gecko10000.WorldEditTools;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import redempt.redlib.configmanager.ConfigManager;
import redempt.redlib.configmanager.annotations.ConfigValue;
import redempt.redlib.itemutils.ItemBuilder;

import java.util.Map;

public class ItemManager {

    private final WorldEditTools plugin;
    private static final NamespacedKey KEY = NamespacedKey.fromString("worldedittools:tool");

    @ConfigValue
    private final Map<ToolType, ItemStack> tools = ConfigManager.map(ToolType.class, ItemStack.class);

    public ItemManager(WorldEditTools plugin) {
        this.plugin = plugin;
        addDefaults();
    }

    public ItemStack getTool(ToolType type) {
        return new ItemBuilder(tools.get(type))
                .addPersistentTag(KEY, PersistentDataType.STRING, type.toString());
    }

    public ToolType getType(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return null;
        }
        try {
            return ToolType.valueOf(item.getItemMeta().getPersistentDataContainer().get(KEY, PersistentDataType.STRING));
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    public void addDefaults() {
        tools.put(ToolType.SELECTION_STICK, new ItemBuilder(Material.STICK)
                .setName(ChatColor.GREEN + "Selection Tool")
                .setLore(ChatColor.YELLOW + "Left click to select position 1",
                        ChatColor.YELLOW + "Right click to select position 2")
                .addEnchant(Enchantment.DURABILITY, 1)
                .addItemFlags(ItemFlag.HIDE_ENCHANTS));
        tools.put(ToolType.FILL_TOOL, new ItemBuilder(Material.BUCKET)
                .setName(ChatColor.GREEN + "Fill Tool")
                .setLore(ChatColor.YELLOW + "Right click after making a selection",
                        ChatColor.YELLOW + "to fill it with the block of your choice.")
                .addEnchant(Enchantment.DURABILITY, 1)
                .addItemFlags(ItemFlag.HIDE_ENCHANTS));
        tools.put(ToolType.CUT_TOOL, new ItemBuilder(Material.SHEARS)
                .setName(ChatColor.GREEN + "Cut Tool")
                .setLore(ChatColor.YELLOW + "Right click after making a",
                        ChatColor.YELLOW + "selection to cut it.")
                .addEnchant(Enchantment.DURABILITY, 1)
                .addItemFlags(ItemFlag.HIDE_ENCHANTS));
        tools.put(ToolType.COPY_TOOL, new ItemBuilder(Material.PAPER)
                .setName(ChatColor.GREEN + "Copy Tool")
                .setLore(ChatColor.YELLOW + "Right click after making a",
                        ChatColor.YELLOW + "selection to copy it.")
                .addEnchant(Enchantment.DURABILITY, 1)
                .addItemFlags(ItemFlag.HIDE_ENCHANTS));
        tools.put(ToolType.PASTE_TOOL, new ItemBuilder(Material.WRITABLE_BOOK)
                .setName(ChatColor.GREEN + "Paste Tool")
                .setLore(ChatColor.YELLOW + "Right click after copying or",
                        ChatColor.YELLOW + "cutting a selection to paste it.")
                .addEnchant(Enchantment.DURABILITY, 1)
                .addItemFlags(ItemFlag.HIDE_ENCHANTS));
        tools.put(ToolType.UNDO_TOOL, new ItemBuilder(Material.SPECTRAL_ARROW)
                .setName(ChatColor.GREEN + "Undo Tool")
                .setLore(ChatColor.YELLOW + "Right click to undo.")
                .addEnchant(Enchantment.DURABILITY, 1)
                .addItemFlags(ItemFlag.HIDE_ENCHANTS));
    }

    public enum ToolType {
        SELECTION_STICK,
        FILL_TOOL,
        CUT_TOOL,
        COPY_TOOL,
        PASTE_TOOL,
        UNDO_TOOL
    }

}

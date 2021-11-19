package gecko10000.WorldEditTools;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import redempt.redlib.configmanager.ConfigManager;
import redempt.redlib.configmanager.annotations.ConfigValue;
import redempt.redlib.itemutils.ItemBuilder;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ItemManager {

    private final WorldEditTools plugin;
    private static final NamespacedKey KEY = NamespacedKey.fromString("worldedittools:tool");

    @ConfigValue
    private Map<ToolType, ItemStack> tools = ConfigManager.map(ToolType.class, ItemStack.class);

    public ItemManager(WorldEditTools plugin) {
        this.plugin = plugin;
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
        addTool(ToolType.SELECT, Material.STICK, "&aSelection Tool",
                List.of("&eLeft click to select position 1", "&eRight click to select position 2"));
        addTool(ToolType.FILL, Material.BUCKET, "&aFill Tool",
                List.of("&eRight click after making a selection", "&eto fill it with the block of your choice."));
        addTool(ToolType.CUT, Material.SHEARS, "&aCut Tool",
                List.of("&eRight click after making a", "&eselection to cut it."));
        addTool(ToolType.COPY, Material.PAPER, "&aCopy Tool",
                List.of("&eRight click after making a", "&eselection to copy it."));
        addTool(ToolType.PASTE, Material.WRITTEN_BOOK, "&aPaste Tool",
                List.of("&eRight click after copying or", "&ecutting a selection to paste it."));
        addTool(ToolType.UNDO, Material.SPECTRAL_ARROW, "&aUndo Tool",
                List.of("&eRight click to undo."));
        plugin.config.save();
    }

    private void addTool(ToolType type, Material material, String name, List<String> lore) {
        tools.putIfAbsent(type, new ItemBuilder(material)
                .setName(WorldEditTools.makeReadableString(name))
                .setLore(lore.stream()
                        .map(WorldEditTools::makeReadableString)
                        .collect(Collectors.toList()).toArray(new String[0]))
                .addEnchant(Enchantment.DURABILITY, 1)
                .addItemFlags(ItemFlag.values()).clone());
    }

    public enum ToolType {
        SELECT,
        FILL,
        CUT,
        COPY,
        PASTE,
        UNDO
    }

}

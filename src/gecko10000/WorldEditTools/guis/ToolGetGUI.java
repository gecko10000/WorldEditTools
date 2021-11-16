package gecko10000.WorldEditTools.guis;

import gecko10000.WorldEditTools.ItemManager;
import gecko10000.WorldEditTools.WorldEditTools;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import redempt.redlib.configmanager.annotations.ConfigValue;
import redempt.redlib.inventorygui.InventoryGUI;
import redempt.redlib.itemutils.ItemBuilder;

public class ToolGetGUI {

    private static final int SIZE = 45;

    private final WorldEditTools plugin;
    private final InventoryGUI gui;

    @ConfigValue("gui.name")
    public static String guiName = "&aWorldEdit Tools";

    public ToolGetGUI(WorldEditTools plugin, Player player) {
        this.plugin = plugin;
        gui = new InventoryGUI(Bukkit.createInventory(null, SIZE, plugin.makeReadableComponent(guiName)));
        gui.setReturnsItems(false);
        gui.fill(0, SIZE, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(""));
        setOpenItem(10, plugin.itemManager.getTool(ItemManager.ToolType.SELECTION_STICK));
        setOpenItem(13, plugin.itemManager.getTool(ItemManager.ToolType.FILL_TOOL));
        setOpenItem(16, plugin.itemManager.getTool(ItemManager.ToolType.CUT_TOOL));
        setOpenItem(28, plugin.itemManager.getTool(ItemManager.ToolType.COPY_TOOL));
        setOpenItem(31, plugin.itemManager.getTool(ItemManager.ToolType.PASTE_TOOL));
        setOpenItem(34, plugin.itemManager.getTool(ItemManager.ToolType.UNDO_TOOL));
        gui.open(player);
    }

    private void setOpenItem(int slot, ItemStack item) {
        gui.openSlot(slot);
        gui.getInventory().setItem(slot, item);
    }

}

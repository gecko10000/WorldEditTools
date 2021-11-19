package gecko10000.WorldEditTools.guis;

import gecko10000.WorldEditTools.ItemManager;
import gecko10000.WorldEditTools.WorldEditTools;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import redempt.redlib.configmanager.annotations.ConfigValue;
import redempt.redlib.inventorygui.InventoryGUI;
import redempt.redlib.inventorygui.ItemButton;
import redempt.redlib.itemutils.ItemBuilder;
import redempt.redlib.itemutils.ItemUtils;

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
        gui.fill(0, SIZE, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(WorldEditTools.makeReadableString("&r")));
        setClickableItem(10, plugin.itemManager.getTool(ItemManager.ToolType.SELECT));
        setClickableItem(13, plugin.itemManager.getTool(ItemManager.ToolType.FILL));
        setClickableItem(16, plugin.itemManager.getTool(ItemManager.ToolType.CUT));
        setClickableItem(28, plugin.itemManager.getTool(ItemManager.ToolType.COPY));
        setClickableItem(31, plugin.itemManager.getTool(ItemManager.ToolType.PASTE));
        setClickableItem(34, plugin.itemManager.getTool(ItemManager.ToolType.UNDO));
        gui.open(player);
    }

    private void setClickableItem(int slot, ItemStack item) {
        gui.addButton(slot, ItemButton.create(item, evt -> ItemUtils.give((Player) evt.getWhoClicked(), item)));
    }

}

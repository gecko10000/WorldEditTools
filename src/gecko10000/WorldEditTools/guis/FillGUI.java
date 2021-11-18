package gecko10000.WorldEditTools.guis;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.util.formatting.text.TextComponent;
import com.sk89q.worldedit.world.block.BlockTypes;
import gecko10000.WorldEditTools.WorldEditTools;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import redempt.redlib.configmanager.annotations.ConfigValue;
import redempt.redlib.inventorygui.InventoryGUI;
import redempt.redlib.inventorygui.ItemButton;
import redempt.redlib.itemutils.ItemBuilder;
import redempt.redlib.misc.FormatUtils;
import redempt.redlib.misc.Task;

public class FillGUI {

    @ConfigValue("fill-gui.name")
    public static String name = "&cFill Selection";

    private static final int SIZE = 27;

    private final WorldEditTools plugin;
    private final InventoryGUI gui;

    public FillGUI(WorldEditTools plugin, Player player) {
        this.plugin = plugin;
        gui = new InventoryGUI(Bukkit.createInventory(null, SIZE, WorldEditTools.makeReadableComponent(name)));
        gui.fill(0, SIZE, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(WorldEditTools.makeReadableString("&r")));
        gui.getInventory().setItem(13, null);
        gui.openSlot(13);
        gui.addButton(10, ItemButton.create(new ItemBuilder(Material.RED_STAINED_GLASS_PANE)
                .setName(WorldEditTools.makeReadableString("&cCancel")), evt -> player.closeInventory()));
        gui.addButton(16, ItemButton.create(null,
                evt -> fill(player, gui.getInventory().getItem(13))));
        setName();
        gui.setOnClickOpenSlot(evt -> Task.syncDelayed(this::setName));
        gui.setOnDragOpenSlot(evt -> Task.syncDelayed(this::setName));
        gui.open(player);
    }

    private void setName() {
        Inventory inv = gui.getInventory();
        ItemStack selected = inv.getItem(13);
        String type = FormatUtils.toTitleCase((selected == null ? Material.AIR : selected.getType()).toString().replace('_', ' '));
        inv.setItem(16, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE)
                .setName(WorldEditTools.makeReadableString("&aConfirm filling with " + type)));
    }

    @ConfigValue("max-blocks.fill")
    private static int maxFillBlocks = 1000;

    private void fill(Player player, ItemStack item) {
        Material type = item == null ? Material.AIR : item.getType();
        com.sk89q.worldedit.entity.Player wePlayer = BukkitAdapter.adapt(player);
        if (!type.isBlock()) {
            wePlayer.printError(TextComponent.of("This is not a valid block!"));
            return;
        }
        Region selection = plugin.getSelection(wePlayer);
        EditSession session = WorldEdit.getInstance().newEditSessionBuilder()
                .world(selection.getWorld())
                .actor(wePlayer)
                .maxBlocks(maxFillBlocks)
                .build();
        try {
            session.setBlocks(selection, BlockTypes.get(type.getKey().toString()).getDefaultState());
            session.commit();
            plugin.getSession(wePlayer).remember(session);
            session.close();
        } catch (MaxChangedBlocksException e) {
            wePlayer.printError(TextComponent.of("Too many blocks! (" + selection.getVolume() + "/" + maxFillBlocks + ")"));
            return;
        }
        player.closeInventory();
    }

}

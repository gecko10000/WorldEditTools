package gecko10000.WorldEditTools;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extension.platform.permission.ActorSelectorLimits;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.RegionSelector;
import com.sk89q.worldedit.regions.selector.limit.SelectorLimits;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.util.formatting.text.TextComponent;
import gecko10000.WorldEditTools.guis.FillGUI;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import redempt.redlib.configmanager.annotations.ConfigValue;

public class Listeners implements Listener {

    private final WorldEditTools plugin;

    @ConfigValue("max-blocks")
    public static int max = 1000;

    public Listeners(WorldEditTools plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent evt) {
        Action action = evt.getAction();
        if (action == Action.PHYSICAL) {
            return;
        }
        ItemManager.ToolType type = plugin.itemManager.getType(evt.getItem());
        if (type == null) {
            return;
        }
        evt.setCancelled(true);
        if (type == ItemManager.ToolType.SELECT && evt.getClickedBlock() != null) {
            select(evt);
        }
        if (action.isLeftClick()) {
            return;
        }
        switch (type) {
            case FILL -> fill(evt);
            case CUT -> cut(evt);
            case COPY -> copy(evt, false);
            case PASTE -> paste(evt);
            case UNDO -> undo(evt);
        }
    }

    private void select(PlayerInteractEvent evt) {
        Player player = BukkitAdapter.adapt(evt.getPlayer());
        LocalSession session = plugin.getSession(player);
        RegionSelector selector = session.getRegionSelector(player.getWorld());
        BlockVector3 clicked = BukkitAdapter.asBlockVector(evt.getClickedBlock().getLocation());
        SelectorLimits limits = ActorSelectorLimits.forActor(player);
        if (evt.getAction() == Action.LEFT_CLICK_BLOCK) {
            selector.selectPrimary(clicked, limits);
            selector.explainPrimarySelection(player, session, clicked);
        } else {
            selector.selectSecondary(clicked, limits);
            selector.explainSecondarySelection(player, session, clicked);
        }
    }

    private void fill(PlayerInteractEvent evt) {
        org.bukkit.entity.Player player = evt.getPlayer();
        Player wePlayer = BukkitAdapter.adapt(player);
        if (!verifySelection(wePlayer)) {
            return;
        }
        new FillGUI(plugin, evt.getPlayer());
    }

    private void cut(PlayerInteractEvent evt) {
        copy(evt, true);
        FillGUI.fill(plugin, evt.getPlayer(), null, max);
    }

    private void copy(PlayerInteractEvent evt, boolean silent) {
        Player wePlayer = BukkitAdapter.adapt(evt.getPlayer());
        if (!verifySelection(wePlayer)) {
            return;
        }
        Region selection = plugin.getSelection(wePlayer);
        if (selection.getVolume() > max) {
            if (!silent) {
                wePlayer.printError(TextComponent.of("Too many blocks! (" + selection.getVolume() + "/" + max + ")"));
            }
            return;
        }
        Clipboard clipboard = new BlockArrayClipboard(selection);
        clipboard.setOrigin(wePlayer.getLocation().toVector().toBlockPoint());
        LocalSession session = plugin.getSession(wePlayer);
        EditSession editSession = session.createEditSession(wePlayer);
        Operation copy = new ForwardExtentCopy(editSession, selection, clipboard, selection.getMinimumPoint());
        try {
            Operations.complete(copy);
            session.setClipboard(new ClipboardHolder(clipboard));
            wePlayer.printInfo(copy.getStatusMessages().iterator().next());
        } catch (WorldEditException e) {
            e.printStackTrace();
            wePlayer.printError(TextComponent.of("An error was encountered."));
        }

    }

    private void paste(PlayerInteractEvent evt) {
        Player wePlayer = BukkitAdapter.adapt(evt.getPlayer());
        LocalSession session = plugin.getSession(wePlayer);
        ClipboardHolder holder;
        try {
            holder = session.getClipboard();
        } catch (EmptyClipboardException e) {
            wePlayer.printError(TextComponent.of("Your clipboard is empty!"));
            return;
        }
        EditSession editSession = WorldEdit.getInstance().newEditSessionBuilder()
                .world(wePlayer.getWorld())
                .actor(wePlayer)
                .build();
        Operation paste = holder.createPaste(editSession)
                .to(wePlayer.getLocation().toVector().toBlockPoint())
                .build();
        try {
            Operations.complete(paste);
            wePlayer.printInfo(paste.getStatusMessages().iterator().next());
        } catch (WorldEditException e) {
            e.printStackTrace();
            wePlayer.printError(TextComponent.of("An error was encountered."));
            return;
        }
        editSession.commit();
        session.remember(editSession);
        editSession.close();
    }

    private void undo(PlayerInteractEvent evt) {
        Player wePlayer = BukkitAdapter.adapt(evt.getPlayer());
        LocalSession session = plugin.getSession(wePlayer);
        EditSession editSession = session.undo(null, wePlayer);
        wePlayer.printInfo(TextComponent.of(editSession == null ? "Nothing to undo." : "Undid edit."));
    }

    private boolean verifySelection(Player player) {
        Region selection = plugin.getSelection(player);
        if (selection == null) {
            player.printError(TextComponent.of("Make a selection first!"));
            return false;
        }
        return true;
    }

}

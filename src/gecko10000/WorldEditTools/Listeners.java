package gecko10000.WorldEditTools;

import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extension.platform.permission.ActorSelectorLimits;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.RegionSelector;
import com.sk89q.worldedit.regions.selector.limit.SelectorLimits;
import com.sk89q.worldedit.session.SessionManager;
import com.sk89q.worldedit.world.World;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class Listeners implements Listener {

    private final WorldEditTools plugin;

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
        if (type == ItemManager.ToolType.SELECTION_STICK && evt.getClickedBlock() != null) {
            select(evt);
        }
        if (action.isLeftClick()) {
            return;
        }
        switch (type) {
            case FILL_TOOL -> fill(evt);
            case CUT_TOOL -> cut(evt);
            case COPY_TOOL -> copy(evt);
            case PASTE_TOOL -> paste(evt);
            case UNDO_TOOL -> undo(evt);
        }
    }

    private void select(PlayerInteractEvent evt) {
        LocalSession session = getSession(evt);
        Player player = BukkitAdapter.adapt(evt.getPlayer());
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

    }

    private void cut(PlayerInteractEvent evt) {

    }

    private void copy(PlayerInteractEvent evt) {

    }

    private void paste(PlayerInteractEvent evt) {

    }

    private void undo(PlayerInteractEvent evt) {

    }

    private LocalSession getSession(PlayerInteractEvent evt) {
        SessionManager manager = WorldEdit.getInstance().getSessionManager();
        return manager.get(BukkitAdapter.adapt(evt.getPlayer()));
    }

}

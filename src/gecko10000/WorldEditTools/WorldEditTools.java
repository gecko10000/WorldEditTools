package gecko10000.WorldEditTools;

import org.bukkit.plugin.java.JavaPlugin;

public class WorldEditTools extends JavaPlugin {

    public void onEnable() {
        new CommandHandler(this);
    }

}

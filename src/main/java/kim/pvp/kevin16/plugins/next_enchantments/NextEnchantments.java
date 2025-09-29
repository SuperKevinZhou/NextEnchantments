package kim.pvp.kevin16.plugins.next_enchantments;

import org.bukkit.plugin.java.JavaPlugin;

public final class NextEnchantments extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("NextEnchantments Plugin has been enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("NextEnchantments Plugin has been disabled!");
    }
}

package kim.pvp.kevin16.plugins.next_enchantments;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class NextEnchantments extends JavaPlugin {

    private static final Map<Integer, String> ENCHANTMENTS_MAP;

    static {
        Map<Integer, String> tempMap = new HashMap<>();
        tempMap.put(0, "Rage");
//        tempMap.put(1, "Vampire");
//        tempMap.put(2, "BloodDrinking");
//        tempMap.put(3, "PeaceMaker");
        ENCHANTMENTS_MAP = Collections.unmodifiableMap(tempMap);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("NextEnchantments Plugin has been enabled!");
        saveResource("lang.yml", false);
        saveResource("enchantments.yml", false);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("NextEnchantments Plugin has been disabled!");
    }
}

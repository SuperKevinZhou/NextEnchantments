package kim.pvp.kevin16.plugins.next_enchantments;

import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class NextEnchantmentsApi {
    private static NextEnchantments plugin;

    public static NextEnchantmentsApi getApi() {
        if (plugin == null) {
            plugin = NextEnchantments.getInstance();
        }

        return new NextEnchantmentsApi();
    }

    private NextEnchantmentsApi() {}

    public ItemStack getEnchantmentBook(Enchantment enchantment) {
        return plugin.getEnchantmentBook(enchantment);
    }

    public Map<String, Object> getEnchantmentParameters(String enchantKey, int level) {
        return plugin.getEnchantmentParameters(enchantKey, level);
    }
}

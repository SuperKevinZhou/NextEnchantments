package kim.pvp.kevin16.plugins.next_enchantments;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class NextEnchantments extends JavaPlugin {

    public static final Map<Integer, String> ENCHANTMENTS_MAP;
    public static String lang;

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
        saveDefaultConfig();
        lang = getConfig().getString("lang");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("NextEnchantments Plugin has been disabled!");
    }

    public ItemStack getEnchantmentBook(int id, int level) {
        // Get enchantment name
        String enchantKey = ENCHANTMENTS_MAP.get(id);
        if (enchantKey == null) {
            getLogger().warning("Unknown enchantment id: " + id);
            return new ItemStack(Material.AIR);
        }

        // Create enchanted book item
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = book.getItemMeta();

        // Get enchantment info from the config.yml
        String path = "enchantments." + enchantKey + ".levels." + level;
        if (!getConfig().contains(path)) {
            getLogger().warning("Cannot found enchantment config: " + path);
            return new ItemStack(Material.AIR);
        }

        // Get enchantment parameters
        int category = getConfig().getInt(path + ".category");
        int x = getConfig().getInt(path + ".parameters.x");
        double y = getConfig().getDouble(path + ".parameters.y");
        double z = getConfig().getDouble(path + ".parameters.z");
        int cd = getConfig().getInt(path + ".parameters.cd");

        // Localization
        String enchantName = getConfig().getString("translations." + lang + ".enchantments." + enchantKey + ".name", enchantKey);
        String categoryName = getConfig().getString("translations." + lang + ".category." + category, String.valueOf(category));

        // Build lore
        List<String> descriptionLines = getConfig().getStringList("translations." + lang + ".enchantments." + enchantKey + ".description");
        List<Component> lore = new ArrayList<>();

        // Create text component
        Component categoryComponent = Component.text("分类: " + categoryName)
                .color(TextColor.color(0xAAAAAA))
                .decoration(TextDecoration.ITALIC, false);
        lore.add(categoryComponent);

        // Add blank line
        lore.add(Component.text(""));

        // Replace placeholders
        for (String line : descriptionLines) {
            String processedLine = line
                    .replace("{x}", String.valueOf(x))
                    .replace("{y}", String.valueOf(y))
                    .replace("{z}", String.valueOf(z))
                    .replace("{cd}", String.valueOf(cd));

            Component lineComponent = Component.text(processedLine)
                    .color(TextColor.color(0x7F7F7F))
                    .decoration(TextDecoration.ITALIC, false);
            lore.add(lineComponent);
        }

        // Set lore
        Component displayName = Component.text(enchantName + " " + toRoman(level))
                .color(NamedTextColor.LIGHT_PURPLE)
                .decoration(TextDecoration.ITALIC, false)
                .decoration(TextDecoration.BOLD, true);
        meta.displayName(displayName);
        meta.lore(lore);

        // Add ItemFlag
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        // Add PersistentDataContainer
        NamespacedKey enchantKeyObj = new NamespacedKey(this, "custom_enchant");
        meta.getPersistentDataContainer().set(enchantKeyObj, PersistentDataType.STRING, enchantKey + ":" + level);

        book.setItemMeta(meta);
        return book;
    }

    // Number to Roman Number
    private String toRoman(int number) {
        return switch (number) {
            case 1 -> "I";
            case 2 -> "II";
            case 3 -> "III";
            case 4 -> "IV";
            case 5 -> "V";
            case 6 -> "VI";
            case 7 -> "VII";
            case 8 -> "VIII";
            case 9 -> "IX";
            case 10 -> "X";
            default -> String.valueOf(number);
        };
    }
}

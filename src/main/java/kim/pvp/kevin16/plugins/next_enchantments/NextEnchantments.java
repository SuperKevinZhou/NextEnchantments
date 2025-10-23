package kim.pvp.kevin16.plugins.next_enchantments;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@SuppressWarnings("UnstableApiUsage")
public final class NextEnchantments extends JavaPlugin {

    private static NextEnchantments instance;
    public static Map<Integer, String> enchantments_map;
    public static String lang;

    static {
        Map<Integer, String> tempMap = new HashMap<>();
        tempMap.put(0, "Rage");
//        tempMap.put(1, "Vampire");
//        tempMap.put(2, "BloodDrinking");
//        tempMap.put(3, "PeaceMaker");
        enchantments_map = tempMap;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        getLogger().info("NextEnchantments Plugin has been enabled!");
        saveDefaultConfig();
        lang = getConfig().getString("lang");
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(NextEnchantmentsCommands.build(), List.of("nextenchantments", "ne", "next_en"));
        });
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("NextEnchantments Plugin has been disabled!");
    }

    public static NextEnchantments getInstance() {
        if (instance == null) {
            throw new IllegalStateException("The plugin hasn't been initialized yet!");
        }
        return instance;
    }

    private ItemStack getEnchantmentBook(int id, int level) {
        // Get enchantment name
        String enchantKey = enchantments_map.get(id);
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

        // Get enchantment parameters dynamically
        int category = getConfig().getInt(path + ".category");

        // Dynamically load all parameters from config
        Map<String, Object> parameters = new HashMap<>();
        String parametersPath = path + ".parameters";
        if (getConfig().contains(parametersPath)) {
            ConfigurationSection paramsSection = getConfig().getConfigurationSection(parametersPath);
            if (paramsSection != null) {
                for (String paramKey : paramsSection.getKeys(false)) {
                    parameters.put(paramKey, paramsSection.get(paramKey));
                }
            }
        }

        // Localization
        String enchantName = getConfig().getString("translations." + lang + ".enchantments." + enchantKey + ".name", enchantKey);
        String categoryName = getConfig().getString("translations." + lang + ".category." + category, String.valueOf(category));

        // Build lore
        List<String> descriptionLines = getConfig().getStringList("translations." + lang + ".enchantments." + enchantKey + ".description");
        List<Component> lore = new ArrayList<>();

        // Create text component
        Component categoryComponent = Component.text(getConfig().getString("translations." + lang + ".ui.CATEGORY") + categoryName)
                .color(TextColor.color(0xAAAAAA))
                .decoration(TextDecoration.ITALIC, false);
        lore.add(categoryComponent);

        // Add blank line
        lore.add(Component.text(""));

        // Dynamically replace placeholders based on actual parameters
        for (String line : descriptionLines) {
            Component lineComponent = getComponent(line, parameters);
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

        // Add PersistentDataContainer with all parameters
        NamespacedKey enchantKeyObj = new NamespacedKey(this, "ne_enchant");
        int enchantId = ((id - 1) << 12) | (level - 1);

        // Store enchantment ID and level in PDC
        meta.getPersistentDataContainer().set(enchantKeyObj, PersistentDataType.INTEGER,
                enchantId);

        book.setItemMeta(meta);
        return book;
    }

    private static @NotNull Component getComponent(String line, Map<String, Object> parameters) {
        String processedLine = line;

        // Replace all parameter placeholders dynamically
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            String placeholder = "{" + entry.getKey() + "}";
            processedLine = processedLine.replace(placeholder, String.valueOf(entry.getValue()));
        }

        return Component.text(processedLine)
                .color(TextColor.color(0x7F7F7F))
                .decoration(TextDecoration.ITALIC, false);
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

    private Map<String, Object> getEnchantmentParameters(String enchantKey, int level) {
        Map<String, Object> parameters = new HashMap<>();
        String path = "enchantments." + enchantKey + ".levels." + level;

        if (getConfig().contains(path + ".parameters")) {
            ConfigurationSection paramsSection = getConfig().getConfigurationSection(path + ".parameters");
            if (paramsSection != null) {
                for (String paramKey : paramsSection.getKeys(false)) {
                    parameters.put(paramKey, paramsSection.get(paramKey));
                }
            }
        }
        return parameters;
    }
}

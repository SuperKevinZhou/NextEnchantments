package kim.pvp.kevin16.plugins.next_enchantments;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
        // 获取附魔名称
        String enchantKey = ENCHANTMENTS_MAP.get(id);
        if (enchantKey == null) {
            getLogger().warning("未知的附魔ID: " + id);
            return new ItemStack(Material.AIR);
        }

        // 创建附魔书物品
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = book.getItemMeta();

        // 从配置获取附魔信息
        String path = "enchantments." + enchantKey + ".levels." + level;
        if (!getConfig().contains(path)) {
            getLogger().warning("找不到附魔配置: " + path);
            return new ItemStack(Material.AIR);
        }

        // 获取附魔参数
        int category = getConfig().getInt(path + ".category");
        int x = getConfig().getInt(path + ".parameters.x");
        double y = getConfig().getDouble(path + ".parameters.y");
        double z = getConfig().getDouble(path + ".parameters.z");
        int cd = getConfig().getInt(path + ".parameters.cd");

        // 获取本地化文本
        String enchantName = getConfig().getString("translations." + lang + ".enchantments." + enchantKey + ".name", enchantKey);
        String categoryName = getConfig().getString("translations." + lang + ".category." + category, String.valueOf(category));

        // 构建描述
        List<String> descriptionLines = getConfig().getStringList("translations." + lang + ".enchantments." + enchantKey + ".description");
        List<Component> lore = new ArrayList<>();

        // 使用Paper的Adventure API创建更漂亮的文本组件
        Component categoryComponent = Component.text("分类: " + categoryName)
                .color(TextColor.color(0xAAAAAA))
                .decoration(TextDecoration.ITALIC, false);
        lore.add(categoryComponent);

        // 添加空行
        lore.add(Component.text(""));

        // 替换描述中的占位符并添加到lore
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

        // 设置显示名称和描述（使用Paper的Adventure API）
        Component displayName = Component.text(enchantName + " " + toRoman(level))
                .color(NamedTextColor.LIGHT_PURPLE)
                .decoration(TextDecoration.ITALIC, false)
                .decoration(TextDecoration.BOLD, true);
        meta.displayName(displayName);
        meta.lore(lore);

        // 添加物品标志
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        // 使用持久化数据容器存储附魔信息
        NamespacedKey enchantKeyObj = new NamespacedKey(this, "custom_enchant");
        meta.getPersistentDataContainer().set(enchantKeyObj, PersistentDataType.STRING, enchantKey + ":" + level);

        book.setItemMeta(meta);
        return book;
    }

    // 将数字转换为罗马数字（用于显示附魔等级）
    private String toRoman(int number) {
        switch (number) {
            case 1: return "I";
            case 2: return "II";
            case 3: return "III";
            case 4: return "IV";
            case 5: return "V";
            case 6: return "VI";
            case 7: return "VII";
            case 8: return "VIII";
            case 9: return "IX";
            case 10: return "X";
            default: return String.valueOf(number);
        }
    }
}

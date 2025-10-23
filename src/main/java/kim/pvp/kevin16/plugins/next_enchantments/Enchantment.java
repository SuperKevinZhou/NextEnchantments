package kim.pvp.kevin16.plugins.next_enchantments;

public class Enchantment {
    private int id;
    private int level;
    private String name;

    public Enchantment(int enchant) {
        this.id = enchant >> 12;
        this.level = enchant & 0xFFF;
        this.name = NextEnchantments.reverse_enchantments_map.get(this.id);
    }

    public Enchantment(int id, int level) {
        this.id = id;
        this.level = level;
        this.name = NextEnchantments.reverse_enchantments_map.get(this.id);
    }

    public Enchantment(String name, int level) {
        this.id = NextEnchantments.enchantments_map.get(name);
        this.level = level;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
        this.name = NextEnchantments.reverse_enchantments_map.get(id);
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setName(String name) {
        this.name = name;
        this.id = NextEnchantments.enchantments_map.get(this.name);
    }

    public int toInt() {
        return (id << 12) | level;
    }
}

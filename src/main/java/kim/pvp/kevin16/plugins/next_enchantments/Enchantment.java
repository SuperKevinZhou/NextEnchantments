package kim.pvp.kevin16.plugins.next_enchantments;

public class Enchantment {
    private int id;
    private int level;

    public Enchantment(int enchant) {
        this.id = (enchant >> 12) + 1;
        this.level = (enchant & 0xFFF) + 1;
    }

    public Enchantment(int id, int level) {
        this.id = id;
        this.level = level;
    }

    public int getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int toInt() {
        return ((id - 1) << 12) | (level - 1);
    }
}

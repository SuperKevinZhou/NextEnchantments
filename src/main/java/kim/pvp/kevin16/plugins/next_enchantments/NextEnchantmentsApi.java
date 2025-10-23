package kim.pvp.kevin16.plugins.next_enchantments;

public class NextEnchantmentsApi {
    private static NextEnchantments plugin;

    public static NextEnchantmentsApi getApi() {
        if (plugin == null) {
            plugin = NextEnchantments.getInstance();
        }

        return new NextEnchantmentsApi();
    }

    private NextEnchantmentsApi() {}
}

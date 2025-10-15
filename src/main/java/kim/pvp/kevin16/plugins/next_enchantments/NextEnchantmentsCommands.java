package kim.pvp.kevin16.plugins.next_enchantments;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;

@SuppressWarnings("UnstableApiUsage")
public class NextEnchantmentsCommands {
    public static LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("nextenchantments")
                .then(Commands.literal("book")
                        .then(Commands.literal("give")
                                .then(Commands.argument("id", IntegerArgumentType.integer(1, 4096))
                                        .then(Commands.argument("level", IntegerArgumentType.integer(1, 128))
                                                .executes(NextEnchantmentsCommands::giveBookToHimself)
                                                .then(Commands.argument("player", ArgumentTypes.player())
                                                        .executes(NextEnchantmentsCommands::giveBookToPlayer)
                                                )
                                        )
                                )
                        )
                )
                .then(Commands.literal("enchant"))
                .build();
    }

    public static int giveBookToHimself(CommandContext<CommandSourceStack> ctx) {

        return Command.SINGLE_SUCCESS;
    }

    public static int giveBookToPlayer(CommandContext<CommandSourceStack> ctx) {

        return Command.SINGLE_SUCCESS;
    }
}

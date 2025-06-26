package wiam.wiamspammer;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import wiam.wiamspammer.automessage.AutoMessage;
import wiam.wiamspammer.config.AutoMessageConfig;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class WiamSpammerClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {

		Spammer.initialize();
		AutoMessage.initialize();
		Util.initialize();

		ClientCommandRegistrationCallback.EVENT.register((dispatcher , registryAccess ) -> dispatcher.register(literal("spammer")
                .then(argument("value", FloatArgumentType.floatArg(0.1F))
                        .then(argument("Content", StringArgumentType.greedyString()).executes( x -> {
                            Spammer.startSpamming(x.getArgument("value", Float.class), x.getArgument("Content", String.class), x.getSource().getClient().getNetworkHandler());
                            if(x.getSource().getClient().player != null) {
                                x.getSource().getClient().player.sendMessage(Text.translatable("message.wiamspammer.spammer.started").formatted(Formatting.BLUE), false);
                            }
                            return 1;
                        })))
                .then(literal("stop").executes(x -> {
                    Spammer.stopSpamming();
                    if(x.getSource().getClient().player != null) {
                        x.getSource().getClient().player.sendMessage(Text.translatable("message.wiamspammer.spammer.stopped").formatted(Formatting.BLUE), false);
                    }
                    return 1;
                }))
        ));

		ClientCommandRegistrationCallback.EVENT.register((dispatcher , registryAccess ) -> dispatcher.register(literal("loadspammerconfig").executes(x ->{
            AutoMessageConfig.load();
            if(x.getSource().getClient().player != null) {
                x.getSource().getClient().player.sendMessage(Text.translatable("message.wiamspammer.config.loaded").formatted(Formatting.BLUE), false);
            }
            return 1;
        })));
	}
}
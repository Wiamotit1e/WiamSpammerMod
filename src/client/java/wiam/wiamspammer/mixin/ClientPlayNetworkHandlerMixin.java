package wiam.wiamspammer.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wiam.wiamspammer.automessage.AutoMessage;
import wiam.wiamspammer.automessage.MessagePacketProcessor;
import wiam.wiamspammer.automessage.ProcessedMessage;
import wiam.wiamspammer.config.AutoMessageConfig;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    @Inject( at = @At("TAIL"), method = "onGameMessage")
    public void onGameMessage(GameMessageS2CPacket packet, CallbackInfo ci) {
        wiam$send(packet);
    }

    @Inject( at = @At("TAIL"), method = "onChatMessage")
    public void onChatMessage(ChatMessageS2CPacket packet, CallbackInfo ci) {
        wiam$send(packet);
    }

    @Unique
    private void wiam$send(Packet<?> packet) {
        ProcessedMessage processedMessage = MessagePacketProcessor.processMessage(packet);
        AutoMessageConfig.toAutoMessage(MinecraftClient.getInstance().getNetworkHandler());
        if (processedMessage != null) processedMessage.sendToAutoMessage();
    }
}

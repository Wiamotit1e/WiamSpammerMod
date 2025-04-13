package wiam.wiamspammer.mixin;

import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.network.packet.s2c.common.DisconnectS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wiam.wiamspammer.Spammer;
import wiam.wiamspammer.Util;

@Mixin(ClientCommonNetworkHandler.class)
public class ClientCommonNetworkHandlerMixin {

    @Inject(at = @At("TAIL"), method = "onDisconnect")
    public void onDisconnect(DisconnectS2CPacket packet, CallbackInfo info) {
        Spammer.stopSpamming();
        Util.removeAllMessages();
    }

}

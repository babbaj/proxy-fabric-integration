package dev.babbaj.proxyintegration.mixin;

import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MultiplayerScreen.class)
public interface IMPScreen extends IScreen {
    @Accessor("serverListWidget")
    MultiplayerServerListWidget serverListWidget0();
}

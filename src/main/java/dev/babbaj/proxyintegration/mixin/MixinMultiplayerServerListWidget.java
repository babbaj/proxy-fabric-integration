package dev.babbaj.proxyintegration.mixin;

import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(MultiplayerServerListWidget.class)
public class MixinMultiplayerServerListWidget {

    @ModifyVariable(method = "<init>", argsOnly = true, at = @At(value = "LOAD", opcode = Opcodes.ILOAD), ordinal = 3)
    private static int modifyHeight(int h) {
        // 36 pixel original height
        // 17 pixel height of checkboxes
        // 4 pixel extra padding
        return 36 + 17 + 4;
    }
}

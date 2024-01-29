package dev.babbaj.proxyintegration.mixin;

import dev.babbaj.proxyintegration.CheckboxRow;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.text.Text;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(MultiplayerServerListWidget.class)
public class MixinMultiplayerServerListWidget {

    @ModifyVariable(method = "<init>", argsOnly = true, at = @At(value = "LOAD", opcode = Opcodes.ILOAD), ordinal = 3)
    private static int modifyHeight(int h) {
        // 36 pixel original height
        // 17 pixel height of checkboxes (CheckBoxWidget.getSize)
        // 4 pixel extra padding
        return 36 + 17 + 4;
    }
}

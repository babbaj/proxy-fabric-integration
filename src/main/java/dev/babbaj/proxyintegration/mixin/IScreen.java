package dev.babbaj.proxyintegration.mixin;

import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Screen.class)
public interface IScreen {
    @Invoker("addDrawableChild")
    <T extends Element & Drawable & Selectable> T addDrawableChild0(T drawableElement);
}

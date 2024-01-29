package dev.babbaj.proxyintegration.mixin;

import dev.babbaj.proxyintegration.CheckboxRow;
import dev.babbaj.proxyintegration.CheckboxWithCallback;
import dev.babbaj.proxyintegration.ProxyIp;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiplayerServerListWidget.ServerEntry.class)
public class MixinMultiplayerServerListWidget$ServerEntry {
    @Shadow
    private ServerInfo server;
    @Shadow
    private MinecraftClient client;
    @Unique
    private CheckboxRow row;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void postInit(CallbackInfo ci) {
        ProxyIp originalIp = ProxyIp.parseIp(server.address).orElse(null);
        if (originalIp != null) {
            CheckboxWidget[] widgets = ProxyIp.ALL_OPTIONS.stream().map(opt -> {
                    int width = client.textRenderer.getWidth(opt) + 24;
                    return new CheckboxWithCallback(0, 0, width, 20, Text.of(opt), originalIp.options().contains(opt), checked -> {
                        ProxyIp ip = ProxyIp.parseIp(server.address).orElseThrow(IllegalStateException::new);
                        server.address = (checked ? ip.withOption(opt) : ip.withoutOption(opt)).toString();
                        var entry = (MultiplayerServerListWidget.ServerEntry) (Object) this;
                        entry.saveFile();
                    });
                })
                .toArray(CheckboxWidget[]::new);
            this.row = new CheckboxRow(server, widgets);
        } else {
            this.row = new CheckboxRow(server, new CheckboxWidget[0]);
        }

    }

    @Inject(method = "render", at = @At("RETURN"))
    private void postRender(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta, CallbackInfo ci) {
        int offset = 0;
        for (int i = 0; i < row.checkboxes().length; i++) {
            row.checkboxes()[i].setPosition(x + offset, y + 36);
            row.checkboxes()[i].render(context, mouseX, mouseY, tickDelta);
            offset += row.checkboxes()[i].getWidth() + 2;
        }
    }

    @Inject(method = "mouseClicked", at = @At("RETURN"))
    private void postClick(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        for (int i = 0; i < row.checkboxes().length; i++) {
            if (row.checkboxes()[i].mouseClicked(mouseX, mouseY, button)) {
                return;
            }
        }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void fixArrowCheck(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        for (int i = 0; i < row.checkboxes().length; i++) {
            if (row.checkboxes()[i].mouseClicked(mouseX, mouseY, button)) {
                cir.setReturnValue(true);
                return;
            }
        }
    }
}

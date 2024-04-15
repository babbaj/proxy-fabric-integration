package dev.babbaj.proxyintegration.mixin;

import dev.babbaj.proxyintegration.ProxyAPI;
import dev.babbaj.proxyintegration.ProxyIp;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.gui.widget.AxisGridWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Set;
import java.util.TreeSet;

@Mixin(MultiplayerScreen.class)
public class MixinMultiplayerScreen {
    @Unique
    private ButtonWidget proxySyncButton;

    @ModifyArg(method = "init",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/widget/AxisGridWidget;<init>(IILnet/minecraft/client/gui/widget/AxisGridWidget$DisplayAxis;)V",
                    ordinal = 0
            ),
            index = 0
    )
    private int rowWidth(int i) {
        return i + 100;
    }

    @Inject(method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;refreshPositions()V"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void addToRow(CallbackInfo ci, ButtonWidget buttonWidget, ButtonWidget buttonWidget2, ButtonWidget buttonWidget3, ButtonWidget buttonWidget4, DirectionalLayoutWidget directionalLayoutWidget, AxisGridWidget axisGridWidget, AxisGridWidget axisGridWidget2) {
        IScreen screen = ((IScreen) this);
        this.proxySyncButton = screen.addDrawableChild0(ButtonWidget.builder(Text.literal("Sync Proxy"), button -> {
            try {
                ProxyAPI.AccountList accounts = ProxyAPI.getActiveAccounts();
                updateEntries((MultiplayerScreen) (Object) this, accounts);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).width(100).build());
        axisGridWidget.add(this.proxySyncButton);
    }

    private static void updateEntries(MultiplayerScreen screen, ProxyAPI.AccountList result) {
        var list = screen.getServerList();
        Set<String> exists = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        for (int i = 0; i < list.size(); i++) {
            ProxyIp ip = ProxyIp.parseIp(list.get(i).address).orElse(null);
            if (ip != null) {
                exists.add(ip.account());
            }
        }
        for (String account : result.accounts()) {
            if (!exists.contains(account)) {
                String address = account + ".proxy." + result.domain();
                list.add(new ServerInfo(account, address, ServerInfo.ServerType.REALM), false);
            }
        }
        list.saveFile();
        MultiplayerServerListWidget widget = ((IMPScreen) screen).serverListWidget0();
        widget.setServers(list);
    }

}

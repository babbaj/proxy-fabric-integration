package dev.babbaj.proxyintegration;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;

public class CheckboxRow extends MultiplayerServerListWidget.Entry {
    public final ServerInfo parent;

    public final CheckboxWidget[] checkboxes;

    public CheckboxRow(ServerInfo parent, CheckboxWidget[] checkboxes) {
        this.parent = parent;
        this.checkboxes = checkboxes;
    }

    @Override
    public Text getNarration() {
        return Text.empty();
    }

    @Override
    public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {

    }
}

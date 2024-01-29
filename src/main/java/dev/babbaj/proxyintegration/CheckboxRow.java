package dev.babbaj.proxyintegration;

import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.network.ServerInfo;

public record CheckboxRow(ServerInfo parent, CheckboxWidget[] checkboxes) { }

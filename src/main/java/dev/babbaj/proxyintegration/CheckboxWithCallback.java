package dev.babbaj.proxyintegration;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.text.Text;

public class CheckboxWithCallback extends CheckboxWidget {
    private final BooleanConsumer callback;
    public CheckboxWithCallback(int x, int y, int width, int height, Text message, boolean checked, BooleanConsumer callback) {
        super(x, y, width, height, message, checked);
        this.callback = callback;
    }

    @Override
    public void onPress() {
        super.onPress();
        this.callback.accept(this.isChecked());
    }
}

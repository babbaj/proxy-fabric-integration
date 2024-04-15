package dev.babbaj.proxyintegration;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.text.Text;

public class CheckboxWithCallback extends CheckboxWidget {
    private final Callback callback;
    public CheckboxWithCallback(int x, int y, Text message, TextRenderer renderer, boolean checked, Callback callback) {
        super(x, y, message, renderer, checked, callback);
        this.callback = callback;
    }

    @Override
    public void onPress() {
        super.onPress();
        this.callback.onValueChange(this, this.isChecked());
    }
}

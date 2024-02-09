package net.darkhax.eplus.gui;


import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.ParametersAreNonnullByDefault;


// this is only button with no background
@ParametersAreNonnullByDefault
public abstract class AbstractVerticalButtonScroller extends Widget {
    protected final int buttonHeight;
    protected final int barHeight;
    protected final IValueChangeHandler onValueChange;
    protected final IScrollHandler onScrolled;
    protected double value;

    /**
     * constructs this widget
     * @param onValueChange callback, guaranteed to be only called when active and visible
     * @param onScrolled callback, guaranteed to be only called when active and visible
     */
    public AbstractVerticalButtonScroller(
            int x, int y, int width, int buttonHeight, int barHeight,
            double initialValue, IValueChangeHandler onValueChange, IScrollHandler onScrolled
    ) {
        super(x, y, width, barHeight, new StringTextComponent(""));
        this.buttonHeight = buttonHeight;
        this.barHeight = barHeight;
        this.value = initialValue;
        this.onValueChange = onValueChange;
        this.onScrolled = onScrolled;
    }

    /**
     * implementations should override this to render themselves
     */
    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {}

    /**
     * get scroll value, nothing special
     * @return you guess
     */
    public double getValue() {
        return this.value;
    }

    /**
     * set scroll value, callback will be triggered if value changed
     * @param value you guess
     */
    public void setValue(double value) {
        double valuePrev = this.value;
        this.value = MathHelper.clamp(value, 0.0D, 1.0D);
        if (valuePrev != this.value) { this.onValueChange.onValueChange(this); }
    }

    /**
     * increase scroll value, callback will be triggered if value changed
     * @param delta you guess
     */
    public void incValue(double delta) {
        this.setValue(this.getValue() + delta);
    }

    /**
     * implementations should define how this method calls `setValue`
     * @param mouseX x coordinate
     * @param mouseY y coordinate
     */
    public abstract void setValueFromMouse(double mouseX, double mouseY);

    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (!this.active || !this.visible) return false;
        this.onScrolled.onScrolled(this, mouseX, mouseY, delta);
        return true;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (!this.active || !this.visible) return;
        this.setValueFromMouse(mouseX, mouseY);
    }

    @Override
    public void onDrag(double mouseX, double mouseY, double dragX, double dragY) {
        if (!this.active || !this.visible) return;
        this.setValueFromMouse(mouseX, mouseY);
    }

    public interface IValueChangeHandler {
        void onValueChange(AbstractVerticalButtonScroller scroller);
    }

    public interface IScrollHandler {
        void onScrolled(AbstractVerticalButtonScroller scroller, double mouseX, double mouseY, double delta);
    }
}

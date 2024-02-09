package net.darkhax.eplus.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;

import javax.annotation.ParametersAreNonnullByDefault;


// this is only button with no background
@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
public class GuiButtonScroller extends AbstractVerticalButtonScroller {
    public GuiButtonScroller(int x, int y, int barHeight, IValueChangeHandler onValueChange, IScrollHandler onScrolled) {
        super(x, y, 12, 15, barHeight, 0, onValueChange, onScrolled);
    }

    // render button at specified position
    private void blitButton(MatrixStack matrixStack, int x, int y, boolean isPressed) {
        this.blit(matrixStack, x, y, isPressed ? this.width : 0, 182, this.width, this.buttonHeight);
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        mc.getTextureManager().bind(GuiAdvancedTablePure.TEXTURE);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();

        int yOffset = (int) (this.value * (this.barHeight - this.buttonHeight));
        this.blitButton(matrixStack, this.x, this.y + yOffset, this.isHovered);
    }

    /**
     * set scroll value according to mouse position, callback will be triggered if value changed
     * @param mouseX x coordinate
     * @param mouseY y coordinate
     */
    public void setValueFromMouse(double mouseX, double mouseY) {
        int yStart = this.y + this.buttonHeight / 2;
        double scrollableLength = this.barHeight - this.buttonHeight;
        double yOffset = mouseY - yStart;
        this.setValue(yOffset / scrollableLength);
    }
}
/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 * <p>
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */

package net.darkhax.eplus.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class GuiItemButton extends Button {
    /**
     * The ItemStack that is drawn over the button. By default, nothing is rendered.
     * <br/>This stack can be changed after construction.
     */
    public ItemStack renderStack = ItemStack.EMPTY;

    /**
     * Constructs a new 20x20 graphic button showing an item.
     * @param xPosition The X coordinate to position the button at.
     * @param yPosition The Y coordinate to position the button at.
     * @param onPress Called when button is pressed.
     * @param onTooltip Called when button should render a tooltip.
     */
    public GuiItemButton(int xPosition, int yPosition, Button.IPressable onPress, Button.ITooltip onTooltip) {
        super(xPosition, yPosition, 20, 20, new StringTextComponent(""), onPress, onTooltip);
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        super.renderButton(matrixStack, mouseX, mouseY, partialTicks);
        ItemRenderer itemRenderer = mc.getItemRenderer();
        FontRenderer fontRenderer = mc.gui.getFont();
        itemRenderer.renderGuiItemDecorations(fontRenderer, this.renderStack, this.x + 2, this.y + 2, "");
        itemRenderer.renderGuiItem(this.renderStack, this.x + 2, this.y + 2);
    }
}

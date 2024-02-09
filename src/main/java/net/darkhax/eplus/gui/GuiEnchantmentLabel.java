package net.darkhax.eplus.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;


@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
public class GuiEnchantmentLabel extends Widget {
    private static final int COLOR_NAME_TEXT = 0x55aaff00;
    private static final int COLOR_BACKGROUND_LOCKED = 0x44d10841;
    private static final int COLOR_BACKGROUND_AVAILABLE = 0x445aaeae;
    protected final ITooltipHandler onTooltip;
    protected final IClickHandler onClicked;

    @Nullable public EnchantmentData info;
    public boolean selected = false;
    public boolean locked = false;

    public GuiEnchantmentLabel (int x, int y, ITooltipHandler onTooltip, IClickHandler onClicked) {
        super(x, y, 142, 16, new StringTextComponent(""));
        this.clear(); this.onTooltip = onTooltip; this.onClicked = onClicked;
    }

    /**
     * clear the content of this label
     */
    public void clear() {
        this.info = null;
        this.selected = false;
    }

    /**
     * check if this label is empty
     * @return you guess
     */
    public boolean isEmpty() {
        return this.info == null;
    }

    /**
     * Used to get the translated name of the enchantment. If the enchantment is of level 0,
     * the level bit is cut off.
     * @return The name to display for the label.
     */
    public String getDisplayName() {
        if (this.isEmpty()) return "";
        assert this.info != null;
        String s = I18n.get(this.info.enchantment.getDescriptionId());
        if (this.info.enchantment.isCurse()) { s = TextFormatting.RED + s; }
        return this.info.level <= 0 ? s : s + " " + I18n.get("enchantment.level." + this.info.level);
    }

    // render a single piece of level bar
    private void blitLevelBarUnit(MatrixStack matrixStack, int x, int y, boolean isSelected) {
        this.blit(matrixStack, x, y, isSelected ? 5 : 0, 197, 5, 16);
    }

    // render the whole level bar
    private void blitLevelBar(MatrixStack matrixStack, int x, int y, boolean isSelected, int nLevel) {
        for (int i = 0; i < nLevel; ++i) {
            this.blitLevelBarUnit(matrixStack, x + i * 5, y, isSelected);
        }
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (this.isEmpty()) return; // render only when there is an enchantment given

        assert this.info != null;
        Minecraft mc = Minecraft.getInstance();
        fill(matrixStack, this.x, this.y, this.x + this.width, this.y + this.height,
                this.locked ? COLOR_BACKGROUND_LOCKED : COLOR_BACKGROUND_AVAILABLE);

        mc.getTextureManager().bind(GuiAdvancedTablePure.TEXTURE);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();

        this.blitLevelBar(matrixStack, this.x, this.y, this.selected, this.info.level);
        String enchantmentName = this.getDisplayName();
        if (this.selected) enchantmentName = "-> " + enchantmentName;
        drawString(matrixStack, mc.gui.getFont(), enchantmentName, this.x + mc.font.lineHeight / 2,
                this.y + (this.height - mc.font.lineHeight) / 2, COLOR_NAME_TEXT);

        if (this.isHovered()) {
            this.renderToolTip(matrixStack, mouseX, mouseY);
        }
    }

    @Override
    public void renderToolTip(MatrixStack poseStack, int mouseX, int mouseY) {
        this.onTooltip.onTooltip(this, poseStack, mouseX, mouseY);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (!this.locked) {
            this.onClicked.onClicked(this, mouseX, mouseY);
        }
    }

    public interface ITooltipHandler {
        void onTooltip(GuiEnchantmentLabel label, MatrixStack poseStack, int mouseX, int mouseY);
    }

    public interface IClickHandler {
        void onClicked(GuiEnchantmentLabel label, double mouseX, double mouseY);
    }
}
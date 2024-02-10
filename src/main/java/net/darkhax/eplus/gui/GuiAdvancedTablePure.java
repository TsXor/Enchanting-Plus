package net.darkhax.eplus.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.darkhax.eplus.inventory.ContainerAdvancedTable;
import net.darkhax.eplus.util.EnchantmentUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraftforge.fml.client.gui.GuiUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;


/**
 * This class is as "pure GUI" as possible.
 * It cares little about enchantment logic and pay attention to managing child widgets and rendering.
 * <br/>TODO: enable adding multiple enchantments a time
 */
@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
public abstract class GuiAdvancedTablePure extends ContainerScreen<ContainerAdvancedTable> {

    public record PostponedHoveringText(
            @Nonnull List<? extends ITextProperties> textLines,
            int mouseX, int mouseY, int screenWidth, int screenHeight, int maxTextWidth,
            net.minecraft.client.gui.FontRenderer font, int zLevel
    ) {
        public void draw(MatrixStack matrixStack) {
            GuiUtils.drawHoveringText(
                    matrixStack, textLines, mouseX, mouseY, screenWidth, screenHeight, maxTextWidth, font
            );
        }
    }

    protected static final ItemStack ITEM_SPOOKY_BONE = new ItemStack(Items.BONE);
    static { ITEM_SPOOKY_BONE.enchant(Enchantments.PROJECTILE_PROTECTION, 1); }
    protected static final ItemStack ITEM_ENCHANTED_BOOK = new ItemStack(Items.ENCHANTED_BOOK);

    protected static final Random RANDOM = new Random();
    public static final ResourceLocation TEXTURE = new ResourceLocation("eplus", "textures/gui/enchant.png");
    protected static final KeyBinding KEYBINDING_SNEAK = Minecraft.getInstance().options.keyShift;
    protected static String[] TIP_KEYS = { "description", "books", "treasure", "curse", "storage", "inventory", "armor", "delight" };

    // child widgets
    private GuiItemButton enchantButton;
    private GuiButtonScroller scrollbar;
    private final List<GuiEnchantmentLabel> labels = new ArrayList<>();
    protected final List<PostponedHoveringText> hoveringTextList = new ArrayList<>();

    // properties
    protected final String tipKey;
    private int selectedEnchantmentIdx = -1;

    public GuiAdvancedTablePure(ContainerAdvancedTable container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
        this.tipKey = TIP_KEYS[RANDOM.nextInt(TIP_KEYS.length)];
    }

    @Override
    public void init() {
        this.imageWidth = 235; this.imageHeight = 182;
        super.init();

        for (int i = 0; i < 4; ++i) {
            GuiEnchantmentLabel label = new GuiEnchantmentLabel(this.leftPos + 62, this.topPos + 17 + i * 18,
                    this::renderLabelToolTip, this::handleEnchantmentLabelClick);
            this.labels.add(label);
            this.addButton(label);
        }

        this.scrollbar = new GuiButtonScroller(this.leftPos + 206, this.topPos + 17, 70,
                this::handleScrollerValueChange, this::handleScrollerScroll);
        this.addButton(this.scrollbar);

        this.enchantButton = new GuiItemButton(this.leftPos + 35, this.topPos + 38,
                this::handleEnchantButtonClick, this::renderEnchantButtonTooltip);
        this.addButton(this.enchantButton);
    }

    protected abstract ItemStack getEnchantButtonStack();

    protected abstract List<String> getInfoBoxContent();

    protected abstract List<String> getEnchantButtonTip();

    protected abstract ContainerAdvancedTable.EnchantmentList getEnchantmentList();

    protected abstract boolean shouldInfoBeLocked(EnchantmentData info);

    private static List<ITextComponent> textToComponent(List<String> text) {
        return text.stream().map(ITextComponent::nullToEmpty).toList();
    }

    protected void addTopHoveringText(
            @Nonnull List<? extends ITextProperties> textLines,
            int mouseX, int mouseY, int screenWidth, int screenHeight, int maxTextWidth,
            net.minecraft.client.gui.FontRenderer font, int zLevel
    ) {
        this.hoveringTextList.add(new PostponedHoveringText(
                textLines, mouseX, mouseY, screenWidth, screenHeight, maxTextWidth, font, zLevel
        ));
    }

    protected void renderTopHoveringText(MatrixStack matrixStack) {
        this.hoveringTextList.sort(Comparator.comparingInt(text -> text.zLevel));
        for (PostponedHoveringText text : this.hoveringTextList) { text.draw(matrixStack); }
        this.hoveringTextList.clear();
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        // pre-render net.darkhax.eplus.setup
        ContainerAdvancedTable.EnchantmentList availableEnchantments = getEnchantmentList();
        if (availableEnchantments.isDirty) {
            this.selectedEnchantmentIdx = -1;
            this.scrollbar.visible = availableEnchantments.size() > this.labels.size();
            this.scrollbar.setValue(0);
            // make sure GUI is updated
            this.updateLabelContents();
            this.updateLabelSelectionState();
            availableEnchantments.isDirty = false;
        }
        this.enchantButton.renderStack = getEnchantButtonStack();

        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTopHoveringText(matrixStack);
    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) {
        this.font.draw(matrixStack, I18n.get("block.eplus.advanced_table"), 32, 5, 0x404040);
        // this is not tooltip, so just render immediately
        GuiUtils.drawHoveringText(matrixStack, textToComponent(this.getInfoBoxContent()),
                -100, this.topPos, this.width, this.height,
                80, this.font);
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1, 1, 1, 1);
        Minecraft mc = Minecraft.getInstance();
        mc.getTextureManager().bind(TEXTURE);
        this.blit(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    private void updateLabelContents() {
        ContainerAdvancedTable.EnchantmentList availableEnchantments = getEnchantmentList();
        if (availableEnchantments.size() > this.labels.size()) {
            int startIdx = (int) MathHelper.lerp(this.scrollbar.value,
                    0, availableEnchantments.size() - this.labels.size());
            for (int i = 0; i < this.labels.size(); ++i) {
                GuiEnchantmentLabel label = this.labels.get(i);
                label.info = availableEnchantments.get(startIdx + i);
                label.locked = shouldInfoBeLocked(label.info);
            }
        } else {
            int i = 0;
            for (; i < availableEnchantments.size(); ++i) {
                GuiEnchantmentLabel label = this.labels.get(i);
                label.info = availableEnchantments.get(i);
                label.locked = shouldInfoBeLocked(label.info);
            }
            for (; i < this.labels.size(); ++i) {
                this.labels.get(i).clear();
            }
        }
    }

    private void updateLabelSelectionState() {
        ContainerAdvancedTable.EnchantmentList availableEnchantments = getEnchantmentList();
        if (availableEnchantments.size() > this.labels.size()) {
            int startIdx = (int) MathHelper.lerp(this.scrollbar.value,
                    0, availableEnchantments.size() - this.labels.size());
            int labelIdx = this.selectedEnchantmentIdx - startIdx;
            for (int i = 0; i < this.labels.size(); ++i) {
                this.labels.get(i).selected = i == labelIdx;
            }
        } else {
            int i = 0;
            int labelIdx = this.selectedEnchantmentIdx;
            for (; i < availableEnchantments.size(); ++i) {
                this.labels.get(i).selected = i == labelIdx;
            }
        }
    }

    public void renderEnchantButtonTooltip(Button button, MatrixStack poseStack, int mouseX, int mouseY) {
        this.addTopHoveringText(textToComponent(this.getEnchantButtonTip()), mouseX, mouseY, this.width, this.height,
                this.width / 4, this.font, 0);
    }

    public void renderLabelToolTip(GuiEnchantmentLabel label, MatrixStack poseStack, int mouseX, int mouseY) {
        if (label.isEmpty()) return;
        assert label.info != null;
        Minecraft mc = Minecraft.getInstance();
        if (InputMappings.isKeyDown(mc.getWindow().getWindow(), KEYBINDING_SNEAK.getKey().getValue())) {
            ITextComponent desc = ITextComponent.nullToEmpty(EnchantmentUtils.getDescription(label.info.enchantment));
            this.addTopHoveringText(Collections.singletonList(desc), mouseX, mouseY, this.width, this.height,
                    this.width / 3, mc.font, 0);
        }
    }

    public void handleScrollerValueChange(AbstractVerticalButtonScroller scroller) {
        this.updateLabelContents();
        this.updateLabelSelectionState();
    }

    public void handleScrollerScroll(AbstractVerticalButtonScroller scroller, double mouseX, double mouseY, double delta) {
        ContainerAdvancedTable.EnchantmentList availableEnchantments = getEnchantmentList();
        if (availableEnchantments.size() > this.labels.size()) {
            int scrollableCols = availableEnchantments.size() - this.labels.size();
            scroller.incValue(-delta / 2.0D / scrollableCols);
        } else {
            scroller.setValue(0);
        }
    }

    public abstract void handleEnchantButtonClick(Button button);

    public abstract void handleEnchantmentSelection(@Nullable EnchantmentData info);

    public void handleEnchantmentLabelClick(GuiEnchantmentLabel clickedLabel, double mouseX, double mouseY) {
        if (clickedLabel.isEmpty()) return; // do nothing if label has nothing
        assert clickedLabel.info != null;

        // the length of this.labels is short enough, so I use this O(n) lookup here
        // instead of pre-building a hashmap
        int clickedLabelIdx = this.labels.indexOf(clickedLabel);
        int clickedEnchantmentIdx;
        ContainerAdvancedTable.EnchantmentList availableEnchantments = getEnchantmentList();
        if (availableEnchantments.size() > this.labels.size()) {
            int startIdx = (int) MathHelper.lerp(this.scrollbar.value,
                    0, availableEnchantments.size() - this.labels.size());
            clickedEnchantmentIdx = startIdx + clickedLabelIdx;
        } else {
            clickedEnchantmentIdx = clickedLabelIdx;
        }
        EnchantmentData info;
        if (this.selectedEnchantmentIdx == clickedEnchantmentIdx) {
            this.setSelectedEnchantmentIdx(-1); // unselect
            info = null;
        } else {
            this.setSelectedEnchantmentIdx(clickedEnchantmentIdx);
            info = clickedLabel.info;
        }
        this.handleEnchantmentSelection(info);
    }

    /**
     * Get value of field <code>selectedEnchantmentIdx</code>, -1 means none selected.
     * @return you guess
     */
    public int getSelectedEnchantmentIdx() {
        return this.selectedEnchantmentIdx;
    }

    /**
     * Set value of field <code>selectedEnchantmentIdx</code>, GUI will be updated if it changed.
     * Set it to -1 if you want to unselect.
     * @param value you guess
     */
    public void setSelectedEnchantmentIdx(int value) {
        if (this.selectedEnchantmentIdx != value) {
            this.selectedEnchantmentIdx = value;
            this.updateLabelSelectionState();
        }
    }
}
package net.darkhax.eplus.api.event;

import net.darkhax.eplus.gui.GuiAdvancedTable;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.Event;

import java.util.List;

/**
 * This event is fired on the client when the info box to the left of the advanced table gui is
 * rendered. This event can be used to add/remove content from this box.
 */
@OnlyIn(Dist.CLIENT)
public class InfoBoxEvent extends Event {
    public final GuiAdvancedTable gui;
    public final List<String> info;

    public InfoBoxEvent (GuiAdvancedTable gui, List<String> info) {
        super();
        this.gui = gui;
        this.info = info;
    }
}

package us.ichun.mods.tabula.client.gui.window;

import net.minecraft.util.StatCollector;
import us.ichun.mods.tabula.client.gui.GuiWorkspace;
import us.ichun.mods.tabula.client.gui.Theme;
import us.ichun.mods.tabula.client.gui.window.element.*;
import us.ichun.mods.tabula.common.Tabula;

public class WindowSettings extends Window
{
    public WindowSettings(GuiWorkspace parent, int x, int y, int w, int h, int minW, int minH)
    {
        super(parent, x, y, w, h, minW, minH, "window.settings.title", true);

        elements.add(new ElementButton(this, width / 2 - 30, height - 25, 60, 16, -1, false, 2, 1, "element.button.ok"));

        elements.add(new ElementCheckBox(this, 11, 20, 0, 0, 0, false, 0, 0, "window.settings.renderRotationPoint", Tabula.config.getInt("renderRotationPoint") == 1));
    }

    @Override
    public void draw(int mouseX, int mouseY)
    {
        super.draw(mouseX, mouseY);
        if(!minimized)
        {
            workspace.getFontRenderer().drawString(StatCollector.translateToLocal("window.settings.renderRotationPoint"), posX + 25, posY + 21, Theme.getAsHex(Theme.instance.font), false);
        }
    }

    @Override
    public void elementTriggered(Element element)
    {
        if(element.id == 0)
        {
            Tabula.config.get("renderRotationPoint").set(((ElementCheckBox)element).toggledState ? 1 : 0);
            Tabula.config.save();
        }
        if(element.id == -1)
        {
            workspace.removeWindow(this, true);
        }
    }
}
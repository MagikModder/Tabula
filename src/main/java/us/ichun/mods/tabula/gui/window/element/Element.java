package us.ichun.mods.tabula.gui.window.element;

import us.ichun.mods.tabula.gui.window.Window;

public abstract class Element
{
    public final Window parent;
    public int posX;
    public int posY;
    public int width;
    public int height;
    public int id;
    public boolean ignoreMinimized;

    public Element(Window window, int x, int y, int w, int h, int ID, boolean igMin)
    {
        parent = window;
        posX = x;
        posY = y;
        width = w;
        height = h;
        id = ID;
        ignoreMinimized = igMin;
    }

    public void update(){}

    public abstract void draw(int mouseX, int mouseY, boolean hover);

    public boolean onClick(int mouseX, int mouseY, int id)
    {
        return false;
    }

    public void selected(){}

    public void deselected(){}

    public void resized(){}

    public void keyInput(char c, int key){}

    public String tooltip()
    {
        return null; //return null for no tooltip. This is localized.
    }

    public int getPosX()
    {
        return parent.posX + posX;
    }

    public int getPosY()
    {
        return parent.posY + posY;
    }
}

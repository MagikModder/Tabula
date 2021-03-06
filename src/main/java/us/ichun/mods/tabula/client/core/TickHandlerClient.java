package us.ichun.mods.tabula.client.core;

import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.mojang.util.UUIDTypeAdapter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import us.ichun.mods.ichunutil.common.module.tabula.common.project.ProjectInfo;
import us.ichun.mods.tabula.client.gui.GuiWorkspace;
import us.ichun.mods.tabula.client.gui.Theme;
import us.ichun.mods.tabula.client.mainframe.Mainframe;
import us.ichun.mods.tabula.client.mainframe.core.ProjectHelper;
import us.ichun.mods.tabula.common.Tabula;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TickHandlerClient
{
    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event)
    {
        if(event.phase == TickEvent.Phase.END)
        {
            Minecraft mc = Minecraft.getMinecraft();

            if(mc.currentScreen instanceof GuiMainMenu)
            {
                if(Keyboard.isKeyDown(Keyboard.KEY_T) && !keyTDown)
                {
                    initializeMainframe(null, -1, -1, -1);
                }
                keyTDown = Keyboard.isKeyDown(Keyboard.KEY_T);
            }

            for(int i = ProjectHelper.texturesToClear.size() - 1; i >= 0; i--)
            {
                Integer id = ProjectHelper.projectTextureIDs.get(ProjectHelper.texturesToClear.get(i));
                if(id != null)
                {
                    TextureUtil.deleteTexture(id);
                }
                ProjectHelper.texturesToClear.remove(i);
            }
            for(int i = ProjectHelper.projectsToDestroy.size() - 1; i >= 0; i--)
            {
                ProjectHelper.projectsToDestroy.get(i).destroy();
                ProjectHelper.projectsToDestroy.remove(i);
            }
            for(Map.Entry<String, ProjectInfo> e : projectsToUpdate.entrySet())
            {
                ProjectHelper.addProjectToManager(ProjectHelper.createProjectFromJsonHost(e.getKey(), e.getValue().getAsJson()));
            }
            for(Map.Entry<String, BufferedImage> e : projectImagesToUpdate.entrySet())
            {
                ProjectHelper.updateProjectTexture(e.getKey(), e.getValue());
            }
            projectsToUpdate.clear();
            projectImagesToUpdate.clear();
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if(event.phase == TickEvent.Phase.END)
        {
            if(mainframe != null)
            {
                mainframe.tick();
            }
        }
    }

    public void initializeMainframe(String name, int i, int j, int k)
    {
        Minecraft mc = Minecraft.getMinecraft();
        mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));

        int oriScale = mc.gameSettings.guiScale;
        mc.gameSettings.guiScale = mc.gameSettings.guiScale == 1 ? 1 : 2;
        mainframe = new Mainframe();
        UUID uuid;
        try
        {
            uuid = UUIDTypeAdapter.fromString(mc.getSession().getPlayerID());
        }
        catch(IllegalArgumentException e)
        {
            uuid = UUIDTypeAdapter.fromString("deadbeef-dead-beef-dead-beefdeadbeef");
        }
        mainframe.addListener(mc.getSession().getUsername(), true);

        FMLClientHandler.instance().showGuiScreen(new GuiWorkspace(oriScale, false, true, name, i, j, k));
    }

    public HashMap<String, ProjectInfo> projectsToUpdate = new HashMap<String, ProjectInfo>();
    public HashMap<String, BufferedImage> projectImagesToUpdate = new HashMap<String, BufferedImage>();

    public Mainframe mainframe;

    private boolean keyTDown;
}

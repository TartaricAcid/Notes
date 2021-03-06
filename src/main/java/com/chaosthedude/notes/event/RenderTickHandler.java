package com.chaosthedude.notes.event;

import com.chaosthedude.notes.Notes;
import com.chaosthedude.notes.config.ConfigHandler;
import com.chaosthedude.notes.util.RenderUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;

public class RenderTickHandler {

	private static final Minecraft mc = Minecraft.getMinecraft();

	@SubscribeEvent
	public void onRenderTick(RenderTickEvent event) {
		if (event.phase == Phase.END && !mc.gameSettings.hideGUI && (mc.currentScreen == null || mc.currentScreen instanceof GuiChat)) {
			if (Notes.pinnedNote != null && Notes.pinnedNote.isValidScope()) {
				Notes.pinnedNote.update();

				final ScaledResolution res = new ScaledResolution(mc);
				final String text = Notes.pinnedNote.getFilteredText();
				final int maxWidth = MathHelper.floor(res.getScaledWidth() * ConfigHandler.pinnedWidthScale);
				final int maxHeight = MathHelper.floor(res.getScaledHeight() * ConfigHandler.pinnedHeightScale);
				final int renderWidth = RenderUtils.getSplitStringWidth(text, maxWidth);
				final int renderHeight = RenderUtils.getSplitStringHeight(text, maxWidth);
				final int width = res.getScaledWidth() - renderWidth;
				final int height = (res.getScaledHeight() / 2) - (renderHeight / 2);

				final int fixedRenderWidth = RenderUtils.getRenderWidth(ConfigHandler.pinnedNotePosition, renderWidth, res);
				final int fixedRenderHeight = RenderUtils.getRenderHeight(ConfigHandler.pinnedNotePosition, renderHeight, res);

				final float opacity = mc.gameSettings.chatOpacity * 0.9F + 0.1F;
				final int color = (int) (255.0F * opacity);

				RenderUtils.drawRect(fixedRenderWidth - 10, fixedRenderHeight - 5, fixedRenderWidth + renderWidth, fixedRenderHeight + renderHeight + 5, color / 2 << 24);
				RenderUtils.drawSplitStringOnHUD(text, fixedRenderWidth - 5, fixedRenderHeight, maxWidth);
			}
		}
	}

}

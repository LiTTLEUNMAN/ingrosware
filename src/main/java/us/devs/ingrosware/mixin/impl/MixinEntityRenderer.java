package us.devs.ingrosware.mixin.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GLAllocation;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import us.devs.ingrosware.IngrosWare;
import us.devs.ingrosware.event.impl.render.Render2DEvent;
import us.devs.ingrosware.event.impl.render.Render3DEvent;
import us.devs.ingrosware.util.render.GLUProjection;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
@Mixin(EntityRenderer.class)
public class MixinEntityRenderer {

    @Inject(method = "renderWorldPass", at = @At(value = "INVOKE_STRING", target = "net/minecraft/profiler/Profiler.endStartSection(Ljava/lang/String;)V", args = {"ldc=hand"}))
    private void onStartHand(int pass, float partialTicks, long finishTimeNano, CallbackInfo ci) {
        final GLUProjection projection = GLUProjection.getInstance();
        final IntBuffer viewPort = GLAllocation.createDirectIntBuffer(16);
        final FloatBuffer modelView = GLAllocation.createDirectFloatBuffer(16);
        final FloatBuffer projectionPort = GLAllocation.createDirectFloatBuffer(16);
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelView);
        GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projectionPort);
        GL11.glGetInteger(GL11.GL_VIEWPORT, viewPort);
        final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        projection.updateMatrices(viewPort, modelView, projectionPort, scaledResolution.getScaledWidth() / (double) Minecraft.getMinecraft().displayWidth,
                scaledResolution.getScaledHeight() / (double) Minecraft.getMinecraft().displayHeight);
        IngrosWare.INSTANCE.getBus().post(new Render3DEvent(partialTicks));
    }

    @Redirect(method = "updateCameraAndRender", at = @At(value = "INVOKE", target = "net/minecraft/client/gui/GuiIngame.renderGameOverlay(F)V"))
    private void updateCameraAndRender$renderGameOverlay(GuiIngame guiIngame, float partialTicks) {
        guiIngame.renderGameOverlay(partialTicks);
        IngrosWare.INSTANCE.getBus().post(new Render2DEvent(partialTicks, new ScaledResolution(Minecraft.getMinecraft())));
    }
}

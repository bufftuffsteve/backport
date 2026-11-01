package com.buffsteve.autumnbackport.client;

import com.buffsteve.autumnbackport.entity.Cushion;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

/** Draws a cushion with its color's texture (textures/entity/cushion/<color>_cushion.png). */
public class CushionRenderer extends EntityRenderer<Cushion> {
    private static final Map<DyeColor, ResourceLocation> TEXTURES = new EnumMap<>(DyeColor.class);

    static {
        for (DyeColor color : DyeColor.values()) {
            TEXTURES.put(color, new ResourceLocation("textures/entity/cushion/" + color.getName() + "_cushion.png"));
        }
    }

    private final CushionModel model;

    public CushionRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new CushionModel(context.bakeLayer(CushionModel.LAYER));
    }

    @Override
    public void render(Cushion cushion, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffers, int light) {
        poseStack.pushPose();
        Direction facing = Direction.fromYRot(cushion.getYRot());
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - facing.toYRot()));
        poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
        poseStack.translate(0.0, -0.25, 0.0);
        model.renderToBuffer(poseStack, buffers.getBuffer(model.renderType(getTextureLocation(cushion))), light, OverlayTexture.NO_OVERLAY,
                1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
        super.render(cushion, entityYaw, partialTick, poseStack, buffers, light); // name tag
    }

    @Override
    public ResourceLocation getTextureLocation(Cushion cushion) {
        return TEXTURES.get(cushion.getColor());
    }
}

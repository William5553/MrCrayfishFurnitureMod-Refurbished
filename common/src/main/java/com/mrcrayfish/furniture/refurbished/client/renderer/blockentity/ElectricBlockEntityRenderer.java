package com.mrcrayfish.furniture.refurbished.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mrcrayfish.furniture.refurbished.blockentity.ElectricBlockEntity;
import com.mrcrayfish.furniture.refurbished.core.ModItems;
import com.mrcrayfish.furniture.refurbished.electric.Connection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.Set;

/**
 * Author: MrCrayfish
 */
public class ElectricBlockEntityRenderer implements BlockEntityRenderer<ElectricBlockEntity>
{
    private static final Set<Connection> DRAWN_CONNECTIONS = new HashSet<>();
    private static final int HIGHLIGHT_COLOUR = 0xFFAAFFAA;

    public ElectricBlockEntityRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(ElectricBlockEntity electric, float partialTick, PoseStack poseStack, MultiBufferSource source, int light, int overlay)
    {
        Minecraft mc = Minecraft.getInstance();
        if(mc.player == null || !mc.player.getItemInHand(InteractionHand.MAIN_HAND).is(ModItems.WRENCH.get()))
            return;

        AABB box = electric.getInteractBox().move(electric.getPosition().multiply(-1));
        boolean maxed = electric.getConnections().size() >= 5; // TODO config
        boolean isLookingAt = this.isLookingAtNode(electric, partialTick);
        int color = isLookingAt ? HIGHLIGHT_COLOUR : 0xFFFFFFFF;
        float red = FastColor.ARGB32.red(color) / 255F;
        float green = FastColor.ARGB32.green(color) / 255F;
        float blue = FastColor.ARGB32.blue(color) / 255F;
        DebugRenderer.renderFilledBox(poseStack, source, box.inflate(isLookingAt ? 0.03125 : 0), red, green, blue, 1.0F);
        poseStack.pushPose();
        poseStack.translate(0.5F, 0.5F, 0.5F);
        for(Connection connection : electric.getConnections())
        {
            if(!DRAWN_CONNECTIONS.contains(connection))
            {
                poseStack.pushPose();
                Vec3 delta = Vec3.atLowerCornerOf(connection.getPosB().subtract(connection.getPosA()));
                double yaw = Math.atan2(-delta.z, delta.x) + Math.PI;
                double pitch = Math.atan2(delta.horizontalDistance(), delta.y) + Mth.HALF_PI;
                poseStack.mulPose(Axis.YP.rotation((float) yaw));
                poseStack.mulPose(Axis.ZP.rotation((float) pitch));
                DebugRenderer.renderFilledBox(poseStack, source, new AABB(0, -0.03125, -0.03125, delta.length(), 0.03125, 0.03125), 1.0F, 1.0F, 1.0F, 0.5F);
                DRAWN_CONNECTIONS.add(connection);
                poseStack.popPose();
            }
        }
        poseStack.popPose();
    }

    private boolean isLookingAtNode(ElectricBlockEntity electric, float partialTick)
    {
        Minecraft mc = Minecraft.getInstance();
        if(mc.player != null && mc.gameMode != null)
        {
            double entityReach = mc.gameMode.getPickRange();
            Vec3 start = mc.player.getEyePosition(partialTick);
            Vec3 look = mc.player.getViewVector(partialTick);
            Vec3 end = start.add(look.x * entityReach, look.y * entityReach, look.z * entityReach);
            AABB nodeBox = electric.getInteractBox().move(electric.getPosition());
            return nodeBox.clip(start, end).isPresent();
        }
        return false;
    }

    @Override
    public boolean shouldRenderOffScreen(ElectricBlockEntity entity)
    {
        return true;
    }

    @Override
    public int getViewDistance()
    {
        return 128;
    }

    @Override
    public boolean shouldRender(ElectricBlockEntity $$0, Vec3 $$1)
    {
        return true;
    }

    public static void clearDrawn()
    {
        DRAWN_CONNECTIONS.clear();
    }
}

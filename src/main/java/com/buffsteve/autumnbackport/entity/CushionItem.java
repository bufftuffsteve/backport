package com.buffsteve.autumnbackport.entity;

import com.buffsteve.autumnbackport.registry.ModEntities;
import com.buffsteve.autumnbackport.registry.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

/** Places a cushion on the top face of a block, facing the way the player looks. Ported from 26.3. */
public class CushionItem extends Item {
    private final DyeColor color;

    public CushionItem(DyeColor color, Properties properties) {
        super(properties);
        this.color = color;
    }

    public DyeColor getColor() {
        return color;
    }

    @Override
    public InteractionResult useOn(UseOnContext original) {
        UseOnContext context = useCollisionShapeIfNeeded(original);
        Level level = context.getLevel();
        if (context.getClickedFace() != Direction.UP) {
            return InteractionResult.FAIL;
        }
        BlockPlaceContext placeContext = new BlockPlaceContext(context);
        BlockPos pos = placeContext.getClickedPos();
        // Centred on the block, at the exact height of the surface you clicked (slabs, carpets...).
        Vec3 spot = new Vec3(pos.getX() + 0.5, context.getClickLocation().y, pos.getZ() + 0.5);
        AABB box = ModEntities.CUSHION.get().getDimensions().makeBoundingBox(spot);
        if (!Cushion.canBePlacedAt(level, box)) {
            return InteractionResult.FAIL;
        }
        ItemStack stack = context.getItemInHand();
        if (level instanceof ServerLevel serverLevel) {
            if (!serverLevel.getEntitiesOfClass(Cushion.class, box).isEmpty()) {
                return InteractionResult.FAIL; // one cushion per spot
            }
            Player player = context.getPlayer();
            Cushion cushion = ModEntities.CUSHION.get().create(serverLevel,
                    EntityType.createDefaultStackConfig(serverLevel, stack, player), pos, MobSpawnType.SPAWN_EGG, true, true);
            if (cushion == null) {
                return InteractionResult.FAIL;
            }
            cushion.setColor(color);
            float yaw = Direction.fromYRot(placeContext.getRotation()).toYRot();
            cushion.moveTo(spot.x, spot.y, spot.z, yaw, 0.0F);
            serverLevel.addFreshEntity(cushion);
            cushion.destroyIfInFire(serverLevel);
            level.playSound(null, cushion.getX(), cushion.getY(), cushion.getZ(), SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 0.75F, 0.8F);
            cushion.gameEvent(GameEvent.ENTITY_PLACE, player);
        }
        stack.consume(1, placeContext.getPlayer());
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    /**
     * Cauldrons, hoppers and composters have a hollow top. Their selection box is the full block,
     * so clicks land on top of the rim. 26.3 re-aims the click at the real collision shape,
     * so the cushion drops inside onto the bottom instead.
     */
    private static UseOnContext useCollisionShapeIfNeeded(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null) {
            return context;
        }
        BlockPos clicked = context.getClickedPos();
        BlockState state = context.getLevel().getBlockState(clicked);
        if (!state.is(ModTags.Blocks.CUSHION_USES_COLLISION_SHAPE)) {
            return context;
        }
        Vec3 eye = player.getEyePosition();
        Vec3 hit = context.getClickLocation();
        Vec3 end = hit.add(hit.subtract(eye).normalize().scale(0.001));
        BlockHitResult result = state.getCollisionShape(context.getLevel(), clicked).clip(eye, end, clicked);
        if (result == null) {
            return context;
        }
        return new UseOnContext(player, context.getHand(), result);
    }
}

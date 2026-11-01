package com.buffsteve.autumnbackport.block;

import java.util.EnumMap;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

/**
 * Shelf mushroom, ported from 26.3's ShelfMushroomBlock.
 *
 * Sticks out sideways from the side of any block with a solid face (FACING points away from it).
 * Two sizes: AGE 0 = small, AGE 1 = large; bone meal grows small into large.
 * Bouncy to land on, like a bed, and halves fall damage.
 */
public class ShelfMushroomBlock extends HorizontalDirectionalBlock implements BonemealableBlock {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_1;

    /** 26.3 values: keeps 75% of the landing speed as bounce, and halves fall distance. */
    private static final double BOUNCE_RESTITUTION = 0.75;
    private static final float FALL_DISTANCE_REDUCTION = 0.5F;

    // Hitboxes per size and direction. Same boxes as 26.3, written out as pixel coordinates
    // for a mushroom facing north (attached to the block south of it), then rotated.
    private static final Map<Direction, VoxelShape> SMALL = rotations(
            new double[] {3, 9, 9, 13, 11, 16},    // cap
            new double[] {5, 8, 12, 11, 9, 16});   // underside
    private static final Map<Direction, VoxelShape> LARGE = rotations(
            new double[] {1, 8, 6, 15, 11, 16},    // cap
            new double[] {4, 6, 10, 12, 8, 16});   // underside

    public ShelfMushroomBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(AGE, 0));
    }


    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, AGE);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return (state.getValue(AGE) == 0 ? SMALL : LARGE).get(state.getValue(FACING));
    }

    // ---- Placement: needs a solid face behind it ----

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        BlockPos support = pos.relative(facing.getOpposite());
        return level.getBlockState(support).isFaceSturdy(level, support, facing);
    }

    /** Attaches to the first wall you're looking toward that can hold it. */
    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = defaultBlockState();
        for (Direction direction : context.getNearestLookingDirections()) {
            if (direction.getAxis().isHorizontal()) {
                state = state.setValue(FACING, direction.getOpposite());
                if (state.canSurvive(context.getLevel(), context.getClickedPos())) {
                    return state;
                }
            }
        }
        return null;
    }

    /** Pops off if the block it's attached to is removed. */
    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighbor, LevelAccessor level,
                                     BlockPos pos, BlockPos neighborPos) {
        if (direction == state.getValue(FACING).getOpposite() && !state.canSurvive(level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, direction, neighbor, level, pos, neighborPos);
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return false;
    }

    // ---- Bouncing (same approach as a bed, with 26.3's numbers) ----

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        super.fallOn(level, state, pos, entity, fallDistance * FALL_DISTANCE_REDUCTION);
    }

    @Override
    public void updateEntityAfterFallOn(BlockGetter level, Entity entity) {
        if (entity.isSuppressingBounce()) { // sneaking
            super.updateEntityAfterFallOn(level, entity);
            return;
        }
        Vec3 motion = entity.getDeltaMovement();
        if (motion.y < 0.0) {
            double factor = entity instanceof LivingEntity ? 1.0 : 0.8;
            entity.setDeltaMovement(motion.x, -motion.y * BOUNCE_RESTITUTION * factor, motion.z);
        }
    }

    // ---- Bone meal: small -> large ----

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
        return state.getValue(AGE) < 1;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        level.setBlock(pos, state.setValue(AGE, state.getValue(AGE) + 1), Block.UPDATE_CLIENTS);
    }

    // ---- Hitbox rotation helper ----

    /** Takes boxes for a north-facing mushroom and builds the east, south and west versions. */
    private static Map<Direction, VoxelShape> rotations(double[]... northBoxes) {
        Map<Direction, VoxelShape> shapes = new EnumMap<>(Direction.class);
        for (Direction facing : Direction.Plane.HORIZONTAL) {
            VoxelShape shape = Shapes.empty();
            for (double[] b : northBoxes) {
                shape = Shapes.or(shape, rotated(facing, b[0], b[1], b[2], b[3], b[4], b[5]));
            }
            shapes.put(facing, shape.optimize());
        }
        return shapes;
    }

    private static VoxelShape rotated(Direction facing, double x1, double y1, double z1, double x2, double y2, double z2) {
        return switch (facing) {
            case NORTH -> Block.box(x1, y1, z1, x2, y2, z2);
            case SOUTH -> Block.box(16 - x2, y1, 16 - z2, 16 - x1, y2, 16 - z1);
            case EAST -> Block.box(16 - z2, y1, x1, 16 - z1, y2, x2);
            case WEST -> Block.box(z1, y1, 16 - x2, z2, y2, 16 - x1);
            default -> throw new IllegalArgumentException("Not horizontal: " + facing);
        };
    }
}

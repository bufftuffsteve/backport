package com.buffsteve.autumnbackport.block;

import com.mojang.serialization.MapCodec;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Leaf litter, from 1.21.5 (26.3 scatters it under Dappled Forest poplars).
 * A flat layer of fallen leaves made of 1 to 4 quarter-block pieces, like pink petals:
 * use more leaf litter on it to add a piece. Colored by the biome's "dry foliage" tint.
 */
public class LeafLitterBlock extends BushBlock {
    public static final MapCodec<LeafLitterBlock> CODEC = simpleCodec(LeafLitterBlock::new);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    /** Same property name as 1.21.5+ ("segment_amount"), so placed litter survives a world upgrade. */
    public static final IntegerProperty AMOUNT = IntegerProperty.create("segment_amount", 1, 4);

    // One 8x1x8 quarter per piece; pieces fill in counter-clockwise from the facing direction.
    private static final Map<Direction, VoxelShape> QUARTERS = new EnumMap<>(Direction.class);
    static {
        QUARTERS.put(Direction.NORTH, Block.box(0, 0, 0, 8, 1, 8));
        QUARTERS.put(Direction.EAST, Block.box(8, 0, 0, 16, 1, 8));
        QUARTERS.put(Direction.SOUTH, Block.box(8, 0, 8, 16, 1, 16));
        QUARTERS.put(Direction.WEST, Block.box(0, 0, 8, 8, 1, 16));
    }
    private final Map<BlockState, VoxelShape> shapes = new HashMap<>();

    public LeafLitterBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(AMOUNT, 1));
    }

    @Override
    protected MapCodec<? extends BushBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, AMOUNT);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return shapes.computeIfAbsent(state, s -> {
            VoxelShape shape = Shapes.empty();
            Direction dir = s.getValue(FACING);
            for (int i = 0; i < s.getValue(AMOUNT); i++) {
                shape = Shapes.or(shape, QUARTERS.get(dir));
                dir = dir.getCounterClockWise();
            }
            return shape.singleEncompassing();
        });
    }

    /** Needs a solid top face below it (so it can sit on stone, logs, slabs... not only dirt). */
    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos below = pos.below();
        return level.getBlockState(below).isFaceSturdy(level, below, Direction.UP);
    }

    // ---- Adding pieces ----

    @Override
    protected boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        if (!context.isSecondaryUseActive() && context.getItemInHand().is(asItem()) && state.getValue(AMOUNT) < 4) {
            return true;
        }
        return super.canBeReplaced(state, context);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState existing = context.getLevel().getBlockState(context.getClickedPos());
        if (existing.is(this)) {
            return existing.setValue(AMOUNT, Math.min(4, existing.getValue(AMOUNT) + 1));
        }
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return rotate(state, mirror.getRotation(state.getValue(FACING)));
    }
}

package com.buffsteve.autumnbackport.mixin;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/** TreeDecoratorType's constructor is private in vanilla. */
@Mixin(TreeDecoratorType.class)
public interface TreeDecoratorTypeInvoker {
    @Invoker("<init>")
    static <P extends TreeDecorator> TreeDecoratorType<P> autumnbackport$create(MapCodec<P> codec) {
        throw new AssertionError();
    }
}

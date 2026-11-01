package com.buffsteve.autumnbackport.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.CherryParticle;
import net.minecraft.client.particle.SpriteSet;

/**
 * A falling poplar leaf. Reuses 1.21.1's cherry petal motion (a slow, swaying fall),
 * the closest match to 26.3's falling-leaf particle. The color comes from the sprite.
 */
public class PoplarLeafParticle extends CherryParticle {
    public PoplarLeafParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites) {
        super(level, x, y, z, sprites);
    }
}

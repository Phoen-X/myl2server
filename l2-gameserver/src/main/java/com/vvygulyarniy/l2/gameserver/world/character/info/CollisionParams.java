package com.vvygulyarniy.l2.gameserver.world.character.info;

import lombok.Data;

/**
 * Created by Phoen-X on 03.03.2017.
 */
@Data
public final class CollisionParams {
    private final double height;
    private final double radius;

    private CollisionParams(double height, double radius) {
        this.height = height;
        this.radius = radius;
    }

    public static CollisionParams collisionParams(double height, double radius) {
        return new CollisionParams(height, radius);
    }
}

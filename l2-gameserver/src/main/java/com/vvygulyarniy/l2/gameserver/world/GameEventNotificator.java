package com.vvygulyarniy.l2.gameserver.world;

import com.vvygulyarniy.l2.gameserver.world.character.L2Character;

/**
 * Phoen-X on 12.03.2017.
 */
public interface GameEventNotificator {
    void notifyMoveFinished(L2Character character);
}

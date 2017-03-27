package com.vvygulyarniy.l2.gameserver.world.management.stats;

import com.vvygulyarniy.l2.gameserver.world.character.L2Player;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Phoen-X on 19.03.2017.
 */
public class PatkLvlDependencyTableTest {
    @Test
    public void calculatesPatkBonusBasedOnCurrentLevel() throws Exception {
        PatkLvlDependencyTable lvlDependencyTable = new PatkLvlDependencyTable();
        L2Player playerMock = mock(L2Player.class);
        when(playerMock.getLevel()).thenReturn(1);
        double result = lvlDependencyTable.calculate(playerMock);

        assertThat(result).isEqualTo(0.9);
    }
}
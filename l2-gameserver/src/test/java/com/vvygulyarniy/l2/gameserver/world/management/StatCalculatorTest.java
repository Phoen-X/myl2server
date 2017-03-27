package com.vvygulyarniy.l2.gameserver.world.management;

import com.vvygulyarniy.l2.gameserver.world.character.L2Player;
import com.vvygulyarniy.l2.gameserver.world.character.info.CharacterAppearance;
import com.vvygulyarniy.l2.gameserver.world.character.info.ClassId;
import com.vvygulyarniy.l2.gameserver.world.management.stats.PatkCalculationFormula;
import com.vvygulyarniy.l2.gameserver.world.management.stats.StatCalculator;
import org.testng.annotations.Test;

import static com.vvygulyarniy.l2.gameserver.world.character.info.stat.BasicStat.STR;
import static com.vvygulyarniy.l2.gameserver.world.character.info.stat.CombatStat.Type.PATK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Phoen-X on 19.03.2017.
 */
public class StatCalculatorTest {

    @Test
    public void shouldUsePatkFormulaForPatkCalculation() throws Exception {
        L2Player player = new L2Player(1,
                                       "acc",
                                       ClassId.abyssWalker,
                                       new CharacterAppearance(CharacterAppearance.Sex.MALE,
                                                               (byte) 1,
                                                               (byte) 1,
                                                               (byte) 1),
                                       "name",
                                       1);

        player.getBasicStats().set(STR, 0);
        PatkCalculationFormula formulaMock = mock(PatkCalculationFormula.class);
        when(formulaMock.calculate(any())).thenReturn(152.3);
        StatCalculator calculator = new StatCalculator(formulaMock);

        double statValue = calculator.calculateCombatStat(player, PATK);
        assertThat(statValue).isEqualTo(152.3, offset(0.0001));
    }
}
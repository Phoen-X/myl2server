package com.vvygulyarniy.l2.gameserver.world.management.stats;

import com.vvygulyarniy.l2.gameserver.world.character.L2Player;
import com.vvygulyarniy.l2.gameserver.world.character.info.CharacterAppearance;
import com.vvygulyarniy.l2.gameserver.world.character.info.ClassId;
import com.vvygulyarniy.l2.gameserver.world.item.L2Weapon;
import org.testng.annotations.Test;

import static com.vvygulyarniy.l2.gameserver.world.character.info.CharacterAppearance.Sex.MALE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Phoen-X on 19.03.2017.
 */
public class PatkCalculationFormulaTest {

    @Test
    public void calculatesPatkBasedOnStrWeaponAndLvl() throws Exception {
        L2Player player = new L2Player(1,
                                       "account",
                                       ClassId.abyssWalker,
                                       new CharacterAppearance(MALE, (byte) 1, (byte) 1, (byte) 1),
                                       "char_name", 1);

        PatkStrDependencyTable strDependencyMock = mock(PatkStrDependencyTable.class);
        PatkLvlDependencyTable lvlDependencyMock = mock(PatkLvlDependencyTable.class);
        when(strDependencyMock.get(anyInt())).thenReturn(13.0);
        when(lvlDependencyMock.calculate(any())).thenReturn(13.0);
        player.getPaperDoll().wearRightHand(new L2Weapon(1, 1, "some_weap", 300, 0));

        PatkCalculationFormula formula = new PatkCalculationFormula(strDependencyMock, lvlDependencyMock);

        assertThat(formula.calculate(player)).isEqualTo(50700);
    }
}
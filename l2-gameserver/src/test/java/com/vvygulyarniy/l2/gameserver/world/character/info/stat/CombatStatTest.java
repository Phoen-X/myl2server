package com.vvygulyarniy.l2.gameserver.world.character.info.stat;

import org.testng.annotations.Test;

import static com.vvygulyarniy.l2.gameserver.world.character.info.stat.CombatStat.Type.PATK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Offset.offset;

/**
 * Phoen-X on 19.03.2017.
 */
public class CombatStatTest {
    @Test
    public void combatStatHasType() throws Exception {
        CombatStat stat = new CombatStat(PATK, 100);
        assertThat(stat.getType()).isEqualTo(PATK);
    }

    @Test
    public void combatStatHasItsBasicValue() throws Exception {
        CombatStat stat = new CombatStat(PATK, 100);
        assertThat(stat.getBaseValue()).isEqualTo(100);
    }

    @Test
    public void multipliersCanBeAddedToCombatStat() throws Exception {
        CombatStat stat = new CombatStat(PATK, 1);
        stat.addMultiplier(1.2);

        assertThat(stat.getMultiplier()).isEqualTo(1.2, offset(0.001));
    }

    @Test
    public void multipleMultipliersCanBeAdded() throws Exception {
        CombatStat stat = new CombatStat(PATK, 1);
        stat.addMultiplier(1.08);
        stat.addMultiplier(1.1);

        assertThat(stat.getMultiplier()).isEqualTo(1.188, offset(0.001));
    }

    @Test
    public void statCanHaveAdders() throws Exception {
        CombatStat stat = new CombatStat(PATK, 100);
        stat.applyAdder(200);

        assertThat(stat.getAdder()).isEqualTo(200);
    }

    @Test
    public void statCanHaveMultipleAdders() throws Exception {
        CombatStat stat = new CombatStat(PATK, 100);
        stat.applyAdder(200);
        stat.applyAdder(120);

        assertThat(stat.getAdder()).isEqualTo(320);
    }
}
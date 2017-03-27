package com.vvygulyarniy.l2.gameserver.world.character.info.stat;

import org.testng.annotations.Test;

import static com.vvygulyarniy.l2.gameserver.world.character.info.stat.BasicStat.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Phoen-X on 17.03.2017.
 */
public class BasicStatSetTest {

    @Test
    public void shouldContainStrStat() throws Exception {
        BasicStatSet statSet = BasicStatSet.of(1, 2, 3, 4, 5, 6);
        assertThat(statSet.get(STR)).isEqualTo(1);
    }

    @Test
    public void shouldContainDexStat() throws Exception {
        BasicStatSet statSet = BasicStatSet.of(1, 2, 3, 4, 5, 6);
        assertThat(statSet.get(DEX)).isEqualTo(2);
    }

    @Test
    public void shouldContainConStat() throws Exception {
        BasicStatSet statSet = BasicStatSet.of(1, 2, 3, 4, 5, 6);
        assertThat(statSet.get(CON)).isEqualTo(3);
    }

    @Test
    public void shouldContainIntStat() throws Exception {
        BasicStatSet statSet = BasicStatSet.of(1, 2, 3, 4, 5, 6);
        assertThat(statSet.get(INT)).isEqualTo(4);
    }

    @Test
    public void shouldContainWitStat() throws Exception {
        BasicStatSet statSet = BasicStatSet.of(1, 2, 3, 4, 5, 6);
        assertThat(statSet.get(WIT)).isEqualTo(5);
    }

    @Test
    public void shouldContainMenStat() throws Exception {
        BasicStatSet statSet = BasicStatSet.of(1, 2, 3, 4, 5, 6);
        assertThat(statSet.get(MEN)).isEqualTo(6);
    }

    @Test
    public void basicStatCanBeChanged() {
        BasicStatSet statSet = BasicStatSet.of(1, 2, 3, 4, 5, 6);
        statSet.set(STR, 13);
        assertThat(statSet.get(STR)).isEqualTo(13);
    }
}
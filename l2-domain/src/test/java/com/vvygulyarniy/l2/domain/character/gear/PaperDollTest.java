package com.vvygulyarniy.l2.domain.character.gear;

import com.vvygulyarniy.l2.domain.item.L2GearItem;
import com.vvygulyarniy.l2.domain.item.L2GenericGearItem;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Phoen-X on 03.03.2017.
 */
public class PaperDollTest {
    private final PaperDoll doll = new PaperDoll();

    @Test
    public void shouldReturnCurrentlyWornHat() throws Exception {
        assertThat(doll.getHead()).isNull();
    }

    @Test
    public void shouldWearHat() throws Exception {
        L2GearItem item = new L2GenericGearItem(1, 1, "some_hat", 0);
        doll.wearHead(item);

        assertThat(doll.getHead()).isEqualTo(item);
    }

    @Test
    public void shouldReturnCurrentlyWornNecklace() throws Exception {
        PaperDoll doll = new PaperDoll();
        assertThat(doll.getNecklace()).isNull();
    }

    @Test
    public void shouldWearNecklace() throws Exception {
        L2GearItem item = new L2GenericGearItem(1, 1, "some_necklace", 0);
        PaperDoll doll = new PaperDoll();
        doll.wearNecklace(item);

        assertThat(doll.getNecklace()).isEqualTo(item);
    }
}
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

    @Test
    public void shouldWearLeftEarring() throws Exception {
        L2GearItem earring = new L2GenericGearItem(1, 1, "some_earring", 0);
        doll.wearLeftEarring(earring);
        assertThat(doll.getLeftEarring()).isEqualTo(earring);
    }

    @Test
    public void shouldWearRightEarring() throws Exception {
        L2GearItem rightEarring = new L2GenericGearItem(1, 1, "some_earring", 0);

        doll.wearRightEarring(rightEarring);

        assertThat(doll.getRightEar()).isEqualTo(rightEarring);
    }

    @Test
    public void shouldWearLeftRing() throws Exception {
        L2GearItem leftRing = new L2GenericGearItem(1, 1, "left_ring", 0);
        doll.wearLeftRing(leftRing);

        assertThat(doll.getLeftRing()).isEqualTo(leftRing);
    }

    @Test
    public void shouldWearRightRing() throws Exception {
        L2GearItem rightRing = new L2GenericGearItem(1, 1, "right_ring", 0);
        doll.wearRightRing(rightRing);

        assertThat(doll.getRightRing()).isEqualTo(rightRing);
    }

    @Test
    public void shouldWearChest() throws Exception {
        L2GearItem chest = new L2GenericGearItem(1, 1, "chest", 0);
        doll.wearChest(chest);

        assertThat(doll.getChest()).isEqualTo(chest);
    }

    @Test
    public void shouldWearUnderwear() throws Exception {
        L2GearItem underwear = new L2GenericGearItem(1, 1, "underwear", 1);
        doll.wearUnderwear(underwear);

        assertThat(doll.getUnderwear()).isEqualTo(underwear);
    }

    @Test
    public void shouldWearRightHand() throws Exception {
        L2GearItem rightHand = new L2GenericGearItem(1, 1, "rhand", 0);
        doll.wearRightHand(rightHand);

        assertThat(doll.getRightHand()).isEqualTo(rightHand);
    }

    @Test
    public void shouldWearLeftHand() throws Exception {
        L2GearItem leftHand = new L2GenericGearItem(1, 1, "lhand", 0);
        doll.wearLeftHand(leftHand);

        assertThat(doll.getLeftHand()).isEqualTo(leftHand);
    }

    @Test
    public void shouldWearGloves() throws Exception {
        L2GearItem gloves = new L2GenericGearItem(1, 1, "gloves", 0);
        doll.wearGloves(gloves);

        assertThat(doll.getGloves()).isEqualTo(gloves);
    }

    @Test
    public void shouldWearCloak() throws Exception {
        L2GearItem cloak = new L2GenericGearItem(1, 1, "cloak", 0);
        doll.wearCloak(cloak);

        assertThat(doll.getCloak()).isEqualTo(cloak);
    }

    @Test
    public void shouldWearLegs() throws Exception {
        L2GearItem legs = new L2GenericGearItem(1, 1, "legs", 0);
        doll.wearLegs(legs);

        assertThat(doll.getLegs()).isEqualTo(legs);
    }

    @Test
    public void shouldWearBoots() throws Exception {
        L2GearItem boots = new L2GenericGearItem(1, 1, "boots", 0);
        doll.wearBoots(boots);

        assertThat(doll.getBoots()).isEqualTo(boots);
    }
}
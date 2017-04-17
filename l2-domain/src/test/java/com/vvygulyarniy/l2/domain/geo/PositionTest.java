package com.vvygulyarniy.l2.domain.geo;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Phoen-X on 09.04.2017.
 */
public class PositionTest {

    @Test
    public void distanceBetweenPositionsIsDistanceBetweenItsPoints() throws Exception {
        Position first = new Position(0, 0, 0);
        Position second = new Position(100, 0, 0);

        assertThat(first.distanceTo(second)).isEqualTo(100);
    }
}
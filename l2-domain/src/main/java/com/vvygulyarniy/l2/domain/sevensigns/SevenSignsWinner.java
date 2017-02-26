package com.vvygulyarniy.l2.domain.sevensigns;

/**
 * Created by Phoen-X on 25.02.2017.
 */
public enum SevenSignsWinner {
    NONE, DUSK, DAWN;

    public int getId() {
        return this.ordinal();
    }
}

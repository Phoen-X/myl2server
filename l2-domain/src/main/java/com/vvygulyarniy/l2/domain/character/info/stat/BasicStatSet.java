package com.vvygulyarniy.l2.domain.character.info.stat;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

import static com.vvygulyarniy.l2.domain.character.info.stat.BasicStat.*;

/**
 * Created by Phoen-X on 26.02.2017.
 */
@EqualsAndHashCode
@ToString
public class BasicStatSet {
    private final Map<BasicStat, Integer> statValues = new HashMap<>();

    private BasicStatSet(int str, int dex, int con, int intValue, int wit, int men) {
        statValues.put(CON, con);
        statValues.put(STR, str);
        statValues.put(DEX, dex);
        statValues.put(INT, intValue);
        statValues.put(WIT, wit);
        statValues.put(MEN, men);
    }

    public static BasicStatSet of(int str, int dex, int con, int intStat, int wit, int men) {
        return new BasicStatSet(str, dex, con, intStat, wit, men);
    }

    public int get(BasicStat stat) {
        return statValues.get(stat);
    }
}

package com.vvygulyarniy.l2.gameserver.world.config.npc;

import java.util.List;

/**
 * Phoen-X on 12.03.2017.
 */
public class XmlNpcInfoRepository implements NpcInfoRepository {
    private final List<Npc> npcInfo;

    public XmlNpcInfoRepository(XmlNpcSpawnInfoParser parser) {
        this.npcInfo = parser.getParsedData();
    }

    @Override
    public List<Npc> findAll() {
        return npcInfo;
    }
}

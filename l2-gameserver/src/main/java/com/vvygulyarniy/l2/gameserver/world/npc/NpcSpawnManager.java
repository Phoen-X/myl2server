package com.vvygulyarniy.l2.gameserver.world.npc;

import com.vvygulyarniy.l2.gameserver.world.L2World;
import com.vvygulyarniy.l2.gameserver.world.character.L2Character;
import com.vvygulyarniy.l2.gameserver.world.character.L2Npc;
import com.vvygulyarniy.l2.gameserver.world.character.info.CollisionParams;
import com.vvygulyarniy.l2.gameserver.world.config.npc.Npc;
import com.vvygulyarniy.l2.gameserver.world.config.npc.NpcInfoRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * Phoen-X on 12.03.2017.
 */
@Slf4j
public class NpcSpawnManager {
    private final L2World world;
    private final NpcInfoRepository repo;
    private final Map<Npc, NpcSpawnInfo> npcMap = new HashMap<>();

    public NpcSpawnManager(L2World world, NpcInfoRepository repo) {
        this.world = world;
        this.repo = repo;
    }

    public void spawnNpcs() {
        repo.findAll().forEach(npc -> {
            if (!npcMap.containsKey(npc)) {
                spawnNewNpc(npc);
            }
        });
    }

    private void spawnNewNpc(Npc npc) {
        log.info("NPC spawned: {}", npc);
        L2Npc npcInstance = new L2Npc(L2Npc.ID_SEQUENCE.incrementAndGet(),
                                      npc.getNpcId(),
                                      npc.getName(),
                                      npc.getLevel(),
                                      CollisionParams.collisionParams(24.5, 9));
        npcInstance.setPosition(npc.getSpawnPosition());
        npcMap.put(npc, new NpcSpawnInfo(npc, npcInstance));
        world.spawnNpc(npcInstance);
    }


    private static class NpcSpawnInfo {
        private Npc template;
        private L2Character npcInstance;

        public NpcSpawnInfo(Npc template, L2Character npcInstance) {
            this.template = template;
            this.npcInstance = npcInstance;
        }
    }
}

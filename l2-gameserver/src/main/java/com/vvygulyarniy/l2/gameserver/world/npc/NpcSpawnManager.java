package com.vvygulyarniy.l2.gameserver.world.npc;

import com.google.common.eventbus.EventBus;
import com.vvygulyarniy.l2.gameserver.world.character.L2Character;
import com.vvygulyarniy.l2.gameserver.world.character.L2Npc;
import com.vvygulyarniy.l2.gameserver.world.character.info.CollisionParams;
import com.vvygulyarniy.l2.gameserver.world.config.npc.Npc;
import com.vvygulyarniy.l2.gameserver.world.config.npc.NpcInfoRepository;
import com.vvygulyarniy.l2.gameserver.world.event.npc.NpcSpawned;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Phoen-X on 12.03.2017.
 */
@Slf4j
public class NpcSpawnManager {
    private final EventBus eventBus;
    private final NpcInfoRepository repo;
    private final Map<Npc, NpcSpawnInfo> npcMap = new HashMap<>();

    public NpcSpawnManager(ScheduledExecutorService scheduler,
                           EventBus eventBus,
                           NpcInfoRepository repo,
                           int tickDelay,
                           TimeUnit timeUnit) {
        this.eventBus = eventBus;
        this.repo = repo;
        scheduler.scheduleAtFixedRate(this::spawnNpcs, 30000, tickDelay, timeUnit);
    }

    private void spawnNpcs() {
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
        eventBus.post(new NpcSpawned(npcInstance));
    }


    private static class NpcSpawnInfo {
        private Npc template;
        private L2Character npcInstance;

        NpcSpawnInfo(Npc template, L2Character npcInstance) {
            this.template = template;
            this.npcInstance = npcInstance;
        }
    }
}

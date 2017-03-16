package com.vvygulyarniy.l2.gameserver.world.config.npc;

import com.vvygulyarniy.l2.domain.geo.Position;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Duration;

/**
 * Phoen-X on 12.03.2017.
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Npc {
    private int npcId;
    private String name;
    private int level;
    private Position spawnPosition;
    private Duration respawnTime;

    public Npc() {
    }

    public Npc(int npcId, String name, int level, Position spawnPosition, Duration respawnTime) {
        this.npcId = npcId;
        this.name = name;
        this.level = level;
        this.spawnPosition = spawnPosition;
        this.respawnTime = respawnTime;
    }
}

package com.vvygulyarniy.l2.gameserver.world.character;

import com.vvygulyarniy.l2.gameserver.network.GameClientConnection;
import com.vvygulyarniy.l2.gameserver.network.packet.server.L2GameServerPacket;
import com.vvygulyarniy.l2.gameserver.world.character.gear.PaperDoll;
import com.vvygulyarniy.l2.gameserver.world.character.info.CharacterAppearance;
import com.vvygulyarniy.l2.gameserver.world.character.info.ClassId;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString
public class L2Player extends L2Character {
    private final PaperDoll paperDoll = new PaperDoll();
    @Setter
    private GameClientConnection connection;
    private String accountName;
    private CharacterAppearance appearance;
    private ClassId classId;
    @Getter
    private int exp;
    @Getter
    private int sp;
    @Setter
    @Getter
    private int currLoad;
    @Setter
    @Getter
    private int maxLoad;
    @Getter
    private int clanId;
    @Getter
    private int karma;
    @Getter
    private int pkKills;


    public L2Player(int id,
                    String accountName,
                    ClassId classId,
                    CharacterAppearance appearance,
                    String name,
                    int level) {
        super(id, name, level, classId.getCollisionParams(appearance.getSex()), 200, 100, 50);
        this.accountName = accountName;
        this.appearance = appearance;
        this.classId = classId;
    }

    public void send(L2GameServerPacket packet) {
        connection.sendPacket(packet);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || !Objects.equals(this.getClass(), obj.getClass())) return false;

        L2Player other = (L2Player) obj;

        return this.getId() == other.getId();
    }

    @Override
    public int hashCode() {
        return id;
    }
}

package com.vvygulyarniy.l2.gameserver.world.event;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.vvygulyarniy.l2.gameserver.network.packet.server.StatusUpdate;
import com.vvygulyarniy.l2.gameserver.world.L2World;
import com.vvygulyarniy.l2.gameserver.world.character.L2Character;

import static com.vvygulyarniy.l2.gameserver.network.packet.server.StatusUpdate.*;

/**
 * Phoen-X on 18.03.2017.
 */
public class CharRegeneratedNotificator {
    private final EventBus bus;
    private final L2World world;

    public CharRegeneratedNotificator(L2World world, EventBus bus) {
        this.bus = bus;
        this.world = world;
        bus.register(this);
    }

    @Subscribe
    public void charRegenerated(CharRegenerated event) {
        L2Character regeneratedChar = event.getL2Character();
        world.getOnlinePlayers().forEach(player -> {

            StatusUpdate packet = new StatusUpdate(regeneratedChar);
            packet.addAttribute(CUR_CP, (int) regeneratedChar.getCp().getCurrValue());
            packet.addAttribute(CUR_HP, (int) regeneratedChar.getHp().getCurrValue());
            packet.addAttribute(CUR_MP, (int) regeneratedChar.getMp().getCurrValue());

            player.send(packet);
        });
    }
}

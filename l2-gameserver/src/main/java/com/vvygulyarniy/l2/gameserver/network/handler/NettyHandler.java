package com.vvygulyarniy.l2.gameserver.network.handler;

import com.l2server.network.communication.SessionId;
import com.vvygulyarniy.l2.gameserver.crypt.CryptService;
import com.vvygulyarniy.l2.gameserver.events.UserEventBus;
import com.vvygulyarniy.l2.gameserver.network.packet.client.L2GameClientPacket;
import com.vvygulyarniy.l2.gameserver.session.GameSession;
import com.vvygulyarniy.l2.gameserver.session.SessionManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyHandler extends ChannelInboundHandlerAdapter {
    private static final AttributeKey<SessionId> gameSessionKey = AttributeKey.valueOf("gameSession");
    private final SessionManager sessionManager;
    private final PacketToEventMapper mapper;
    private final CryptService cryptService;
    private final UserEventBus eventBus;

    public NettyHandler(SessionManager sessionManager,
                        PacketToEventMapper mapper,
                        CryptService cryptService,
                        UserEventBus eventBus) {
        this.sessionManager = sessionManager;
        this.mapper = mapper;
        this.cryptService = cryptService;
        this.eventBus = eventBus;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        log.info("New connection acquired: {}", ctx);
        SessionId sessionId = sessionManager.startSession();
        cryptService.initSession(sessionId);
        ctx.channel().attr(gameSessionKey).set(sessionId);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        log.info("Got msg: {}", msg);
        GameSession session = sessionManager.getSession(ctx.channel().attr(gameSessionKey).get());
        if (session == null) {
            ctx.disconnect();
        } else {
            L2GameClientPacket packet = (L2GameClientPacket) msg;
            eventBus.post(mapper.convert(session, packet));
            //packet.process(packetProcessor, l2GameClient);
        }
    }
}

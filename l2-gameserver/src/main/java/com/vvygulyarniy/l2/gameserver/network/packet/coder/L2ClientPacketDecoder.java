package com.vvygulyarniy.l2.gameserver.network.packet.coder;

import com.vvygulyarniy.l2.gameserver.network.L2GameClient;
import com.vvygulyarniy.l2.gameserver.network.L2GameClient.GameClientState;
import com.vvygulyarniy.l2.gameserver.network.packet.client.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Phoen-X on 23.02.2017.
 */
@Slf4j
public class L2ClientPacketDecoder extends ByteToMessageDecoder {
    private static final AttributeKey<L2GameClient> gameClientKey = AttributeKey.valueOf("l2GameClient");

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        L2GameClient client = ctx.channel().attr(gameClientKey).get();
        in.markReaderIndex();
        int dataSize = in.readShortLE() & 0xFFFF - 2;
        ByteBuffer byteBuffer = ByteBuffer.allocate(in.readableBytes() + 256).order(ByteOrder.LITTLE_ENDIAN);
        byte[] data = new byte[in.readableBytes()];
        in.readBytes(data);
        byteBuffer.put(data);
        byteBuffer.position(0);
        log.debug("Decoding packet: {}", Arrays.toString(data));
        final boolean ret = client.decrypt(byteBuffer, data.length);
        log.debug("Decoded packet: {}", Arrays.toString(byteBuffer.array()));
        if (ret && byteBuffer.hasRemaining()) {
            // apply limit
            final int limit = byteBuffer.limit();
            byteBuffer.limit(byteBuffer.position() + dataSize);
            final L2GameClientPacket cp = createPacket(byteBuffer, client);
            if (cp != null) {
                if (cp.read()) {
                    out.add(cp);
                } else {
                    in.resetReaderIndex();
                }
            } else {
                log.warn("Unknown packet, continuing");
            }
        }
    }

    public L2GameClientPacket createPacket(ByteBuffer buf, L2GameClient client) {
        int opCode = buf.get() & 0xFF;

        GameClientState state = client.getState();

        switch (state) {
            case CONNECTED:
                switch (opCode) {
                    case 0x0e:
                        return new ProtocolVersion(buf);

                    case 0x2b:
                        return new AuthLogin(buf);
                    default:
                        break;
                }
                break;
            case AUTHED:
                switch (opCode) {
                    case 0x00:
                        return new Logout(buf);
                    case 0x0c:
                        return new CharacterCreate(buf);
                    case 0x0d:
                        return new CharacterDelete(buf);
                    case 0x12:
                        return new CharacterSelect(buf);
                    case 0x13:
                        return new NewCharacter(buf);
                    case 0x7b:
                        return new CharacterRestore(buf);
                    case 0xd0:
                        int id2 = -1;
                        if (buf.remaining() >= 2) {
                            id2 = buf.getShort() & 0xffff;
                        } else {
                            log.warn("Client: {} sent a 0xd0 without the second opcode.", client);
                            break;
                        }

                        switch (id2) {
                            case 0x36:
                                return new RequestGotoLobby(buf);
                            case 0x93:
                                return new RequestEx2ndPasswordCheck(buf);
                            case 0x94:
                                return new RequestEx2ndPasswordVerify(buf);
                            case 0x95:
                                return new RequestEx2ndPasswordReq(buf);
                            default:
                                break;
                        }
                        break;
                    default:
                        break;
                }
                break;
            case IN_GAME:
                switch (opCode) {
                    case 0x00:
                        return new Logout(buf);
                    case 0x01:
                        return new Attack(buf);
                    case 0x03:
                        return new RequestStartPledgeWar(buf);
                    case 0x04:
                        return new RequestReplyStartPledgeWar(buf);
                    case 0x05:
                        return new RequestStopPledgeWar(buf);
                    case 0x06: // RequestSCCheck
                        return new RequestReplyStopPledgeWar(buf);
                    case 0x07:
                        return new RequestSurrenderPledgeWar(buf);
                    case 0x08:
                        return new RequestReplySurrenderPledgeWar(buf);
                    case 0x09:
                        return new RequestSetPledgeCrest(buf);
                    case 0x0b:
                        return new RequestGiveNickName(buf);
                    case 0x0f:
                        return new MoveBackwardToLocation(buf);
                    case 0x10:
                        // Say
                        break;
                    case 0x11:
                        return new EnterWorld(buf);
                    case 0x12:
                        // CharacterSelect, in case of player spam clicks on loginscreen
                        log.warn("Player spam clicks on login screen");
                        break;
                    case 0x14:
                        return new RequestItemList(buf);
                    case 0x15:
                        // RequestEquipItem
                        log.warn("Used obsolete RequestEquipItem packet!");
                        break;
                    case 0x16:
                        return new RequestUnEquipItem(buf);
                    case 0x17:
                        return new RequestDropItem(buf);
                    case 0x19:
                        return new UseItem(buf);
                    case 0x1a:
                        return new TradeRequest(buf);
                    case 0x1b:
                        return new AddTradeItem(buf);
                    case 0x1c:
                        return new TradeDone(buf);
                    case 0x1f:
                        return new Action(buf);
                    case 0x22:
                        return new RequestLinkHtml(buf);
                    case 0x23:
                        return new RequestBypassToServer(buf);
                    case 0x24:
                        return new RequestBBSwrite(buf);
                    case 0x25:
                        // RequestCreatePledge
                        break;
                    case 0x26:
                        return new RequestJoinPledge(buf);
                    case 0x27:
                        return new RequestAnswerJoinPledge(buf);
                    case 0x28:
                        return new RequestWithdrawalPledge(buf);
                    case 0x29:
                        return new RequestOustPledgeMember(buf);
                    case 0x2c:
                        return new RequestGetItemFromPet(buf);
                    case 0x2e:
                        return new RequestAllyInfo(buf);
                    case 0x2f:
                        return new RequestCrystallizeItem(buf);
                    case 0x30:
                        return new RequestPrivateStoreManageSell(buf);
                    case 0x31:
                        return new SetPrivateStoreListSell(buf);
                    case 0x32:
                        return new AttackRequest(buf);
                    case 0x33:
                        // RequestTeleportPacket
                        break;
                    case 0x34:
                        // return new RequestSocialAction();
                        log.warn("Used obsolete RequestSocialAction packet");
                        break;
                    case 0x35:
                        // return new ChangeMoveType2();
                        log.warn("Used obsolete ChangeMoveType packet");
                        break;
                    case 0x36:
                        // return new ChangeWaitType2();
                        log.warn("Used obsolete ChangeWaitType packet");
                        break;
                    case 0x37:
                        return new RequestSellItem(buf);
                    case 0x38:
                        // RequestMagicSkillList
                        break;
                    case 0x39:
                        return new RequestMagicSkillUse(buf);
                    case 0x3a: // SendApperingPacket
                        return new Appearing(buf);
                    case 0x3b:
                        return new SendWareHouseDepositList(buf);
                    case 0x3c:
                        return new SendWareHouseWithDrawList(buf);
                    case 0x3d:
                        return new RequestShortCutReg(buf);
                    case 0x3f:
                        return new RequestShortCutDel(buf);
                    case 0x40:
                        return new RequestBuyItem(buf);
                    case 0x41:
                        // RequestDismissPledge
                        break;
                    case 0x42:
                        return new RequestJoinParty(buf);
                    case 0x43:
                        return new RequestAnswerJoinParty(buf);
                    case 0x44:
                        return new RequestWithDrawalParty(buf);
                    case 0x45:
                        return new RequestOustPartyMember(buf);
                    case 0x46:
                        // RequestDismissParty
                        break;
                    case 0x47:
                        return new CannotMoveAnymore(buf);
                    case 0x48:
                        return new RequestTargetCanceld(buf);
                    case 0x49:
                        return new Say2(buf);
                    case 0x4a:
                        int id_2 = -1;
                        if (buf.remaining() >= 2) {
                            id_2 = buf.getShort() & 0xffff;
                        } else {
                            log.warn("Client: {} sent a 0x4a without the second opcode.", client);

                            break;
                        }
                        switch (id_2) {
                            case 0x00:
                                // SuperCmdCharacterInfo
                                break;
                            case 0x01:
                                // SuperCmdSummonCmd
                                break;
                            case 0x02:
                                // SuperCmdServerStatus
                                break;
                            case 0x03:
                                // SendL2ParamSetting
                                break;
                            default:
                                break;
                        }
                        break;
                    case 0x4d:
                        return new RequestPledgeMemberList(buf);
                    case 0x4f:
                        // RequestMagicList
                        break;
                    case 0x50:
                        return new RequestSkillList(buf);
                    case 0x52:
                        return new MoveWithDelta(buf);
                    case 0x53:
                        return new RequestGetOnVehicle(buf);
                    case 0x54:
                        return new RequestGetOffVehicle(buf);
                    case 0x55:
                        return new AnswerTradeRequest(buf);
                    case 0x56:
                        return new RequestActionUse(buf);
                    case 0x57:
                        return new RequestRestart(buf);
                    case 0x58:
                        return new RequestSiegeInfo(buf);
                    case 0x59:
                        return new ValidatePosition(buf);
                    case 0x5a:
                        // RequestSEKCustom
                        break;
                    case 0x5b:
                        return new StartRotating(buf);
                    case 0x5c:
                        return new FinishRotating(buf);
                    case 0x5e:
                        return new RequestShowBoard(buf);
                    case 0x5f:
                        return new RequestEnchantItem(buf);
                    case 0x60:
                        return new RequestDestroyItem(buf);
                    case 0x62:
                        return new RequestQuestList(buf);
                    case 0x63: // RequestDestroyQuest
                        return new RequestQuestAbort(buf);
                    case 0x65:
                        return new RequestPledgeInfo(buf);
                    case 0x66:
                        return new RequestPledgeExtendedInfo(buf);
                    case 0x67:
                        return new RequestPledgeCrest(buf);
                    case 0x6b: // RequestSendL2FriendSay
                        return new RequestSendFriendMsg(buf);
                    case 0x6c:
                        return new RequestShowMiniMap(buf);
                    case 0x6d:
                        // RequestSendMsnChatLog
                        break;
                    case 0x6e: // RequestReload
                        return new RequestRecordInfo(buf);
                    case 0x6f:
                        return new RequestHennaEquip(buf);
                    case 0x70:
                        return new RequestHennaRemoveList(buf);
                    case 0x71:
                        return new RequestHennaItemRemoveInfo(buf);
                    case 0x72:
                        return new RequestHennaRemove(buf);
                    case 0x73:
                        return new RequestAcquireSkillInfo(buf);
                    case 0x74:
                        return new SendBypassBuildCmd(buf);
                    case 0x75:
                        return new RequestMoveToLocationInVehicle(buf);
                    case 0x76:
                        return new CannotMoveAnymoreInVehicle(buf);
                    case 0x77:
                        return new RequestFriendInvite(buf);
                    case 0x78: // RequestFriendAddReply
                        return new RequestAnswerFriendInvite(buf);
                    case 0x79:
                        return new RequestFriendList(buf);
                    case 0x7a:
                        return new RequestFriendDel(buf);
                    case 0x7c:
                        return new RequestAcquireSkill(buf);
                    case 0x7d:
                        return new RequestRestartPoint(buf);
                    case 0x7e:
                        return new RequestGMCommand(buf);
                    case 0x7f:
                        return new RequestPartyMatchConfig(buf);
                    case 0x80:
                        return new RequestPartyMatchList(buf);
                    case 0x81:
                        return new RequestPartyMatchDetail(buf);
                    case 0x83: // SendPrivateStoreBuyList
                        return new RequestPrivateStoreBuy(buf);
                    case 0x85:
                        return new RequestTutorialLinkHtml(buf);
                    case 0x86:
                        return new RequestTutorialPassCmdToServer(buf);
                    case 0x87:
                        return new RequestTutorialQuestionMark(buf);
                    case 0x88:
                        return new RequestTutorialClientEvent(buf);
                    case 0x89:
                        return new RequestPetition(buf);
                    case 0x8a:
                        return new RequestPetitionCancel(buf);
                    case 0x8b:
                        return new RequestGmList(buf);
                    case 0x8c:
                        return new RequestJoinAlly(buf);
                    case 0x8d:
                        return new RequestAnswerJoinAlly(buf);
                    case 0x8e: // RequestWithdrawAlly
                        return new AllyLeave(buf);
                    case 0x8f: // RequestOustAlly
                        return new AllyDismiss(buf);
                    case 0x90:
                        return new RequestDismissAlly(buf);
                    case 0x91:
                        return new RequestSetAllyCrest(buf);
                    case 0x92:
                        return new RequestAllyCrest(buf);
                    case 0x93:
                        return new RequestChangePetName(buf);
                    case 0x94:
                        return new RequestPetUseItem(buf);
                    case 0x95:
                        return new RequestGiveItemToPet(buf);
                    case 0x96:
                        return new RequestPrivateStoreQuitSell(buf);
                    case 0x97:
                        return new SetPrivateStoreMsgSell(buf);
                    case 0x98:
                        return new RequestPetGetItem(buf);
                    case 0x99:
                        return new RequestPrivateStoreManageBuy(buf);
                    case 0x9a: // SetPrivateStoreList
                        return new SetPrivateStoreListBuy(buf);
                    case 0x9c:
                        return new RequestPrivateStoreQuitBuy(buf);
                    case 0x9d:
                        return new SetPrivateStoreMsgBuy(buf);
                    case 0x9f: // SendPrivateStoreBuyList
                        return new RequestPrivateStoreSell(buf);
                    case 0xa0:
                        // SendTimeCheckPacket
                        break;
                    case 0xa6:
                        // RequestSkillCoolTime
                        break;
                    case 0xa7:
                        return new RequestPackageSendableItemList(buf);
                    case 0xa8:
                        return new RequestPackageSend(buf);
                    case 0xa9:
                        return new RequestBlock(buf);
                    case 0xaa:
                        return new RequestSiegeInfo(buf);
                    case 0xab: // RequestCastleSiegeAttackerList
                        return new RequestSiegeAttackerList(buf);
                    case 0xac:
                        return new RequestSiegeDefenderList(buf);
                    case 0xad: // RequestJoinCastleSiege
                        return new RequestJoinSiege(buf);
                    case 0xae: // RequestConfirmCastleSiegeWaitingList
                        //return new RequestConfirmSiegeWaitingList();
                        break;
                    case 0xAF:
                        return new RequestSetCastleSiegeTime(buf);
                    case 0xb0:
                        return new MultiSellChoose(buf);
                    case 0xb1:
                        // NetPing
                        break;
                    case 0xb2:
                        // RequestRemainTime
                        break;
                    case 0xb3:
                        return new BypassUserCmd(buf);
                    case 0xb4:
                        return new SnoopQuit(buf);
                    case 0xb5:
                        return new RequestRecipeBookOpen(buf);
                    case 0xb6: // RequestRecipeItemDelete
                        return new RequestRecipeBookDestroy(buf);
                    case 0xb7:
                        return new RequestRecipeItemMakeInfo(buf);
                    case 0xb8:
                        return new RequestRecipeItemMakeSelf(buf);
                    case 0xb9:
                        // RequestRecipeShopManageList
                        break;
                    case 0xba:
                        return new RequestRecipeShopMessageSet(buf);
                    case 0xbb:
                        return new RequestRecipeShopListSet(buf);
                    case 0xbc:
                        return new RequestRecipeShopManageQuit(buf);
                    case 0xbd:
                        // RequestRecipeShopManageCancel
                        break;
                    case 0xbe:
                        return new RequestRecipeShopMakeInfo(buf);
                    case 0xbf: // RequestRecipeShopMakeDo
                        return new RequestRecipeShopMakeItem(buf);
                    case 0xc0: // RequestRecipeShopSellList
                        return new RequestRecipeShopManagePrev(buf);
                    case 0xc1: // RequestObserverEndPacket
                        return new ObserverReturn(buf);
                    case 0xc2:
                        // Unused (RequestEvaluate/VoteSociality)
                        break;
                    case 0xc3:
                        return new RequestHennaItemList(buf);
                    case 0xc4:
                        return new RequestHennaItemInfo(buf);
                    case 0xc5:
                        return new RequestBuySeed(buf);
                    case 0xc6: // ConfirmDlg
                        return new DlgAnswer(buf);
                    case 0xc7: // RequestPreviewItem
                        return new RequestPreviewItem(buf);
                    case 0xc8:
                        return new RequestSSQStatus(buf);
                    case 0xc9:
                        return new RequestPetitionFeedback(buf);
                    case 0xcb:
                        return new GameGuardReply(buf);
                    case 0xcc:
                        return new RequestPledgePower(buf);
                    case 0xcd:
                        return new RequestMakeMacro(buf);
                    case 0xce:
                        return new RequestDeleteMacro(buf);
                    case 0xcf: // RequestProcureCrop
                        // return new RequestBuyProcure();
                        break;

                    case 0xd0:
                        int id2 = -1;
                        if (buf.remaining() >= 2) {
                            id2 = buf.getShort() & 0xffff;
                        } else {
                            log.warn("Client: {} sent a 0xd0 without the second opcode.", client);
                            break;
                        }

                        switch (id2) {
                            case 0x01:
                                return new RequestManorList(buf);
                            case 0x02:
                                return new RequestProcureCropList(buf);
                            case 0x03:
                                return new RequestSetSeed(buf);
                            case 0x04:
                                return new RequestSetCrop(buf);
                            case 0x05:
                                return new RequestWriteHeroWords(buf);
                            case 0x5F:
                                /**
                                 * Server Packets: ExMpccRoomInfo FE:9B ExListMpccWaiting FE:9C ExDissmissMpccRoom FE:9D ExManageMpccRoomMember FE:9E ExMpccRoomMember FE:9F
                                 */
                                // TODO: RequestJoinMpccRoom chdd
                                break;
                            case 0x5D:
                                // TODO: RequestListMpccWaiting chddd
                                break;
                            case 0x5E:
                                // TODO: RequestManageMpccRoom chdddddS
                                break;
                            case 0x06:
                                return new RequestExAskJoinMPCC(buf);
                            case 0x07:
                                return new RequestExAcceptJoinMPCC(buf);
                            case 0x08:
                                return new RequestExOustFromMPCC(buf);
                            case 0x09:
                                return new RequestOustFromPartyRoom(buf);
                            case 0x0a:
                                return new RequestDismissPartyRoom(buf);
                            case 0x0b:
                                return new RequestWithdrawPartyRoom(buf);
                            case 0x0c:
                                return new RequestChangePartyLeader(buf);
                            case 0x0d:
                                return new RequestAutoSoulShot(buf);
                            case 0x0e:
                                return new RequestExEnchantSkillInfo(buf);
                            case 0x0f:
                                return new RequestExEnchantSkill(buf);
                            case 0x10:
                                return new RequestExPledgeCrestLarge(buf);
                            case 0x11:
                                return new RequestExSetPledgeCrestLarge(buf);
                            case 0x12:
                                return new RequestPledgeSetAcademyMaster(buf);
                            case 0x13:
                                return new RequestPledgePowerGradeList(buf);
                            case 0x14:
                                return new RequestPledgeMemberPowerInfo(buf);
                            case 0x15:
                                return new RequestPledgeSetMemberPowerGrade(buf);
                            case 0x16:
                                return new RequestPledgeMemberInfo(buf);
                            case 0x17:
                                return new RequestPledgeWarList(buf);
                            case 0x18:
                                return new RequestExFishRanking(buf);
                            case 0x19:
                                return new RequestPCCafeCouponUse(buf);
                            case 0x1b:
                                return new RequestDuelStart(buf);
                            case 0x1c:
                                return new RequestDuelAnswerStart(buf);
                            case 0x1d:
                                // RequestExSetTutorial
                                break;
                            case 0x1e:
                                return new RequestExRqItemLink(buf);
                            case 0x1f:
                                // CanNotMoveAnymoreAirShip
                                break;
                            case 0x20:
                                return new MoveToLocationInAirShip(buf);
                            case 0x21:
                                return new RequestKeyMapping(buf);
                            case 0x22:
                                return new RequestSaveKeyMapping(buf);
                            case 0x23:
                                return new RequestExRemoveItemAttribute(buf);
                            case 0x24:
                                return new RequestSaveInventoryOrder(buf);
                            case 0x25:
                                return new RequestExitPartyMatchingWaitingRoom(buf);
                            case 0x26:
                                //return new RequestConfirmTargetItem();
                                break;
                            case 0x27:
                                //return new RequestConfirmRefinerItem();
                                break;
                            case 0x28:
                                //return new RequestConfirmGemStone();
                                break;
                            case 0x29:
                                return new RequestOlympiadObserverEnd(buf);
                            case 0x2a:
                                return new RequestCursedWeaponList(buf);
                            case 0x2b:
                                return new RequestCursedWeaponLocation(buf);
                            case 0x2c:
                                return new RequestPledgeReorganizeMember(buf);
                            case 0x2d:
                                return new RequestExMPCCShowPartyMembersInfo(buf);
                            case 0x2e:
                                return new RequestOlympiadMatchList(buf);
                            case 0x2f:
                                return new RequestAskJoinPartyRoom(buf);
                            case 0x30:
                                return new AnswerJoinPartyRoom(buf);
                            case 0x31:
                                return new RequestListPartyMatchingWaitingRoom(buf);
                            case 0x32:
                                return new RequestExEnchantSkillSafe(buf);
                            case 0x33:
                                return new RequestExEnchantSkillUntrain(buf);
                            case 0x34:
                                return new RequestExEnchantSkillRouteChange(buf);
                            case 0x35:
                                return new RequestExEnchantItemAttribute(buf);
                            case 0x36:
                                return new ExGetOnAirShip(buf);
                            case 0x38:
                                return new MoveToLocationAirShip(buf);
                            case 0x39:
                                return new RequestBidItemAuction(buf);
                            case 0x3a:
                                return new RequestInfoItemAuction(buf);
                            case 0x3b:
                                return new RequestExChangeName(buf);
                            case 0x3c:
                                return new RequestAllCastleInfo(buf);
                            case 0x3d:
                                return new RequestAllFortressInfo(buf);
                            case 0x3e:
                                return new RequestAllAgitInfo(buf);
                            case 0x3f:
                                return new RequestFortressSiegeInfo(buf);
                            case 0x40:
                                return new RequestGetBossRecord(buf);
                            case 0x41:
                                //return new RequestRefine();
                                break;
                            case 0x42:
                                return new RequestConfirmCancelItem(buf);
                            case 0x43:
                                //return new RequestRefineCancel();
                                break;
                            case 0x44:
                                return new RequestExMagicSkillUseGround(buf);
                            case 0x45:
                                return new RequestDuelSurrender(buf);
                            case 0x46:
                                return new RequestExEnchantSkillInfoDetail(buf);
                            case 0x48:
                                return new RequestFortressMapInfo(buf);
                            case 0x49:
                                // RequestPVPMatchRecord
                                break;
                            case 0x4a:
                                return new SetPrivateStoreWholeMsg(buf);
                            case 0x4b:
                                return new RequestDispel(buf);
                            case 0x4c:
                                return new RequestExTryToPutEnchantTargetItem(buf);
                            case 0x4d:
                                return new RequestExTryToPutEnchantSupportItem(buf);
                            case 0x4e:
                                return new RequestExCancelEnchantItem(buf);
                            case 0x4f:
                                return new RequestChangeNicknameColor(buf);
                            case 0x50:
                                return new RequestResetNickname(buf);
                            case 0x51:
                                int id3 = 0;
                                if (buf.remaining() >= 4) {
                                    id3 = buf.getInt();
                                } else {
                                    log.warn("Client: {} sent a 0xd0:0x51 without the third opcode.", client);
                                    break;
                                }
                                switch (id3) {
                                    case 0x00:
                                        return new RequestBookMarkSlotInfo(buf);
                                    case 0x01:
                                        return new RequestSaveBookMarkSlot(buf);
                                    case 0x02:
                                        return new RequestModifyBookMarkSlot(buf);
                                    case 0x03:
                                        return new RequestDeleteBookMarkSlot(buf);
                                    case 0x04:
                                        return new RequestTeleportBookMark(buf);
                                    case 0x05:
                                        // RequestChangeBookMarkSlot
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            case 0x52:
                                return new RequestWithDrawPremiumItem(buf);
                            case 0x53:
                                // RequestJump
                                break;
                            case 0x54:
                                // RequestStartShowCrataeCubeRank
                                break;
                            case 0x55:
                                // RequestStopShowCrataeCubeRank
                                break;
                            case 0x56:
                                // NotifyStartMiniGame
                                break;
                            case 0x57:
                                return new RequestJoinDominionWar(buf);
                            case 0x58:
                                return new RequestDominionInfo(buf);
                            case 0x59:
                                // RequestExCleftEnter
                                break;
                            case 0x5a:
                                return new RequestExCubeGameChangeTeam(buf);
                            case 0x5b:
                                return new EndScenePlayer(buf);
                            case 0x5c:
                                return new RequestExCubeGameReadyAnswer(buf);
                            case 0x63:
                                return new RequestSeedPhase(buf);
                            case 0x65:
                                return new RequestPostItemList(buf);
                            case 0x66:
                                return new RequestSendPost(buf);
                            case 0x67:
                                return new RequestReceivedPostList(buf);
                            case 0x68:
                                return new RequestDeleteReceivedPost(buf);
                            case 0x69:
                                return new RequestReceivedPost(buf);
                            case 0x6a:
                                return new RequestPostAttachment(buf);
                            case 0x6b:
                                return new RequestRejectPostAttachment(buf);
                            case 0x6c:
                                return new RequestSentPostList(buf);
                            case 0x6d:
                                return new RequestDeleteSentPost(buf);
                            case 0x6e:
                                return new RequestSentPost(buf);
                            case 0x6f:
                                return new RequestCancelPostAttachment(buf);
                            case 0x70:
                                // RequestShowNewUserPetition
                                break;
                            case 0x71:
                                // RequestShowStepThree
                                break;
                            case 0x72:
                                // RequestShowStepTwo
                                break;
                            case 0x73:
                                // ExRaidReserveResult
                                break;
                            case 0x75:
                                return new RequestRefundItem(buf);
                            case 0x76:
                                return new RequestBuySellUIClose(buf);
                            case 0x77:
                                // RequestEventMatchObserverEnd
                                break;
                            case 0x78:
                                return new RequestPartyLootModification(buf);
                            case 0x79:
                                return new AnswerPartyLootModification(buf);
                            case 0x7a:
                                return new AnswerCoupleAction(buf);
                            case 0x7b:
                                return new BrEventRankerList(buf);
                            case 0x7c:
                                // AskMembership
                                break;
                            case 0x7d:
                                // RequestAddExpandQuestAlarm
                                break;
                            case 0x7e:
                                return new RequestVoteNew(buf);
                            case 0x84:
                                return new RequestExAddContactToContactList(buf);
                            case 0x85:
                                return new RequestExDeleteContactFromContactList(buf);
                            case 0x86:
                                return new RequestExShowContactList(buf);
                            case 0x87:
                                return new RequestExFriendListExtended(buf);
                            case 0x88:
                                return new RequestExOlympiadMatchListRefresh(buf);
                            case 0x89:
                                // RequestBRGamePoint
                                break;
                            case 0x8A:
                                // RequestBRProductList
                                break;
                            case 0x8B:
                                // RequestBRProductInfo
                                break;
                            case 0x8C:
                                // RequestBRBuyProduct
                                break;
                            case 0x8D:
                                // RequestBRRecentProductList
                                break;
                            case 0x8E:
                                // BrMinigameLoadScores
                                break;
                            case 0x8F:
                                // BrMinigameInsertScore
                                break;
                            case 0x90:
                                // BrLectureMark
                                break;
                            case 0x91:
                                // RequestGoodsInventoryInfo
                                break;
                            case 0x92:
                                // RequestUseGoodsInventoryItem
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        break;
                }
                break;
        }
        /*switch (opCode) {
            case 0x0e:
                return new ProtocolVersion();
            case 0x2b:
                return new AuthLogin();
            case 0x13:
                return new NewCharacter();
            case 0x0c:
                return new CharacterCreate();
            case 0x12:
                return new RequestPledgeSetAcademyMaster();
            case 0xd0:
                int subOpCode = -1;
                if (buf.remaining() >= 2) {
                    subOpCode = buf.getShort() & 0xffff;
                } else {
                    log.warn("Client sent a 0xd0 without the second opCode.");
                    return null;
                }

                switch (subOpCode) {
                    case 0x36:
                        return new RequestGotoLobby();
                    *//*case 0x93:
                        return new RequestEx2ndPasswordCheck();
                        break;
                    case 0x94:
                        return new RequestEx2ndPasswordVerify();
                        break;
                    case 0x95:
                        return new RequestEx2ndPasswordReq();
                        break;*//*
                    default:
                        log.warn("Unknown second opCode {} for opCode {}", Integer.toHexString(subOpCode), Integer.toHexString(opCode));
                        return null;
                }
            default:
                log.warn("Unknown packet opCode: {}", Integer.toHexString(opCode));
                return null;
        }*/
        return null;
    }
}

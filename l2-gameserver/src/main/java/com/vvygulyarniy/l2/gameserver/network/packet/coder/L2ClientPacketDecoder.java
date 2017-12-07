package com.vvygulyarniy.l2.gameserver.network.packet.coder;

import com.l2server.network.NioNetStringBuffer;
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
                cp.setBuffer(byteBuffer);
                cp.set_sbuf(new NioNetStringBuffer(64 * 1024));
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
                        return new ProtocolVersion();

                    case 0x2b:
                        return new AuthLogin();
                    default:
                        break;
                }
                break;
            case AUTHED:
                switch (opCode) {
                    case 0x00:
                        return new Logout();
                    case 0x0c:
                        return new CharacterCreate();
                    case 0x0d:
                        return new CharacterDelete();
                    case 0x12:
                        return new CharacterSelect();
                    case 0x13:
                        return new NewCharacter();
                    case 0x7b:
                        return new CharacterRestore();
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
                                return new RequestGotoLobby();
                            case 0x93:
                                return new RequestEx2ndPasswordCheck();
                            case 0x94:
                                return new RequestEx2ndPasswordVerify();
                            case 0x95:
                                return new RequestEx2ndPasswordReq();
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
                        return new Logout();
                    case 0x01:
                        return new Attack();
                    case 0x03:
                        return new RequestStartPledgeWar();
                    case 0x04:
                        return new RequestReplyStartPledgeWar();
                    case 0x05:
                        return new RequestStopPledgeWar();
                    case 0x06: // RequestSCCheck
                        return new RequestReplyStopPledgeWar();
                    case 0x07:
                        return new RequestSurrenderPledgeWar();
                    case 0x08:
                        return new RequestReplySurrenderPledgeWar();
                    case 0x09:
                        return new RequestSetPledgeCrest();
                    case 0x0b:
                        return new RequestGiveNickName();
                    case 0x0f:
                        return new MoveBackwardToLocation();
                    case 0x10:
                        // Say
                        break;
                    case 0x11:
                        return new EnterWorld();
                    case 0x12:
                        // CharacterSelect, in case of player spam clicks on loginscreen
                        log.warn("Player spam clicks on login screen");
                        break;
                    case 0x14:
                        return new RequestItemList();
                    case 0x15:
                        // RequestEquipItem
                        log.warn("Used obsolete RequestEquipItem packet!");
                        break;
                    case 0x16:
                        return new RequestUnEquipItem();
                    case 0x17:
                        return new RequestDropItem();
                    case 0x19:
                        return new UseItem();
                    case 0x1a:
                        return new TradeRequest();
                    case 0x1b:
                        return new AddTradeItem();
                    case 0x1c:
                        return new TradeDone();
                    case 0x1f:
                        return new Action();
                    case 0x22:
                        return new RequestLinkHtml();
                    case 0x23:
                        return new RequestBypassToServer();
                    case 0x24:
                        return new RequestBBSwrite();
                    case 0x25:
                        // RequestCreatePledge
                        break;
                    case 0x26:
                        return new RequestJoinPledge();
                    case 0x27:
                        return new RequestAnswerJoinPledge();
                    case 0x28:
                        return new RequestWithdrawalPledge();
                    case 0x29:
                        return new RequestOustPledgeMember();
                    case 0x2c:
                        return new RequestGetItemFromPet();
                    case 0x2e:
                        return new RequestAllyInfo();
                    case 0x2f:
                        return new RequestCrystallizeItem();
                    case 0x30:
                        return new RequestPrivateStoreManageSell();
                    case 0x31:
                        return new SetPrivateStoreListSell();
                    case 0x32:
                        return new AttackRequest();
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
                        return new RequestSellItem();
                    case 0x38:
                        // RequestMagicSkillList
                        break;
                    case 0x39:
                        return new RequestMagicSkillUse();
                    case 0x3a: // SendApperingPacket
                        return new Appearing();
                    case 0x3b:
                        return new SendWareHouseDepositList();
                    case 0x3c:
                        return new SendWareHouseWithDrawList();
                    case 0x3d:
                        return new RequestShortCutReg();
                    case 0x3f:
                        return new RequestShortCutDel();
                    case 0x40:
                        return new RequestBuyItem();
                    case 0x41:
                        // RequestDismissPledge
                        break;
                    case 0x42:
                        return new RequestJoinParty();
                    case 0x43:
                        return new RequestAnswerJoinParty();
                    case 0x44:
                        return new RequestWithDrawalParty();
                    case 0x45:
                        return new RequestOustPartyMember();
                    case 0x46:
                        // RequestDismissParty
                        break;
                    case 0x47:
                        return new CannotMoveAnymore();
                    case 0x48:
                        return new RequestTargetCanceld();
                    case 0x49:
                        return new Say2();
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
                        return new RequestPledgeMemberList();
                    case 0x4f:
                        // RequestMagicList
                        break;
                    case 0x50:
                        return new RequestSkillList();
                    case 0x52:
                        return new MoveWithDelta();
                    case 0x53:
                        return new RequestGetOnVehicle();
                    case 0x54:
                        return new RequestGetOffVehicle();
                    case 0x55:
                        return new AnswerTradeRequest();
                    case 0x56:
                        return new RequestActionUse();
                    case 0x57:
                        return new RequestRestart();
                    case 0x58:
                        return new RequestSiegeInfo();
                    case 0x59:
                        return new ValidatePosition();
                    case 0x5a:
                        // RequestSEKCustom
                        break;
                    case 0x5b:
                        return new StartRotating();
                    case 0x5c:
                        return new FinishRotating();
                    case 0x5e:
                        return new RequestShowBoard();
                    case 0x5f:
                        return new RequestEnchantItem();
                    case 0x60:
                        return new RequestDestroyItem();
                    case 0x62:
                        return new RequestQuestList();
                    case 0x63: // RequestDestroyQuest
                        return new RequestQuestAbort();
                    case 0x65:
                        return new RequestPledgeInfo();
                    case 0x66:
                        return new RequestPledgeExtendedInfo();
                    case 0x67:
                        return new RequestPledgeCrest();
                    case 0x6b: // RequestSendL2FriendSay
                        return new RequestSendFriendMsg();
                    case 0x6c:
                        return new RequestShowMiniMap();
                    case 0x6d:
                        // RequestSendMsnChatLog
                        break;
                    case 0x6e: // RequestReload
                        return new RequestRecordInfo();
                    case 0x6f:
                        return new RequestHennaEquip();
                    case 0x70:
                        return new RequestHennaRemoveList();
                    case 0x71:
                        return new RequestHennaItemRemoveInfo();
                    case 0x72:
                        return new RequestHennaRemove();
                    case 0x73:
                        return new RequestAcquireSkillInfo();
                    case 0x74:
                        return new SendBypassBuildCmd();
                    case 0x75:
                        return new RequestMoveToLocationInVehicle();
                    case 0x76:
                        return new CannotMoveAnymoreInVehicle();
                    case 0x77:
                        return new RequestFriendInvite();
                    case 0x78: // RequestFriendAddReply
                        return new RequestAnswerFriendInvite();
                    case 0x79:
                        return new RequestFriendList();
                    case 0x7a:
                        return new RequestFriendDel();
                    case 0x7c:
                        return new RequestAcquireSkill();
                    case 0x7d:
                        return new RequestRestartPoint();
                    case 0x7e:
                        return new RequestGMCommand();
                    case 0x7f:
                        return new RequestPartyMatchConfig();
                    case 0x80:
                        return new RequestPartyMatchList();
                    case 0x81:
                        return new RequestPartyMatchDetail();
                    case 0x83: // SendPrivateStoreBuyList
                        return new RequestPrivateStoreBuy();
                    case 0x85:
                        return new RequestTutorialLinkHtml();
                    case 0x86:
                        return new RequestTutorialPassCmdToServer();
                    case 0x87:
                        return new RequestTutorialQuestionMark();
                    case 0x88:
                        return new RequestTutorialClientEvent();
                    case 0x89:
                        return new RequestPetition();
                    case 0x8a:
                        return new RequestPetitionCancel();
                    case 0x8b:
                        return new RequestGmList();
                    case 0x8c:
                        return new RequestJoinAlly();
                    case 0x8d:
                        return new RequestAnswerJoinAlly();
                    case 0x8e: // RequestWithdrawAlly
                        return new AllyLeave();
                    case 0x8f: // RequestOustAlly
                        return new AllyDismiss();
                    case 0x90:
                        return new RequestDismissAlly();
                    case 0x91:
                        return new RequestSetAllyCrest();
                    case 0x92:
                        return new RequestAllyCrest();
                    case 0x93:
                        return new RequestChangePetName();
                    case 0x94:
                        return new RequestPetUseItem();
                    case 0x95:
                        return new RequestGiveItemToPet();
                    case 0x96:
                        return new RequestPrivateStoreQuitSell();
                    case 0x97:
                        return new SetPrivateStoreMsgSell();
                    case 0x98:
                        return new RequestPetGetItem();
                    case 0x99:
                        return new RequestPrivateStoreManageBuy();
                    case 0x9a: // SetPrivateStoreList
                        return new SetPrivateStoreListBuy();
                    case 0x9c:
                        return new RequestPrivateStoreQuitBuy();
                    case 0x9d:
                        return new SetPrivateStoreMsgBuy();
                    case 0x9f: // SendPrivateStoreBuyList
                        return new RequestPrivateStoreSell();
                    case 0xa0:
                        // SendTimeCheckPacket
                        break;
                    case 0xa6:
                        // RequestSkillCoolTime
                        break;
                    case 0xa7:
                        return new RequestPackageSendableItemList();
                    case 0xa8:
                        return new RequestPackageSend();
                    case 0xa9:
                        return new RequestBlock();
                    case 0xaa:
                        return new RequestSiegeInfo();
                    case 0xab: // RequestCastleSiegeAttackerList
                        return new RequestSiegeAttackerList();
                    case 0xac:
                        return new RequestSiegeDefenderList();
                    case 0xad: // RequestJoinCastleSiege
                        return new RequestJoinSiege();
                    case 0xae: // RequestConfirmCastleSiegeWaitingList
                        //return new RequestConfirmSiegeWaitingList();
                        break;
                    case 0xAF:
                        return new RequestSetCastleSiegeTime();
                    case 0xb0:
                        return new MultiSellChoose();
                    case 0xb1:
                        // NetPing
                        break;
                    case 0xb2:
                        // RequestRemainTime
                        break;
                    case 0xb3:
                        return new BypassUserCmd();
                    case 0xb4:
                        return new SnoopQuit();
                    case 0xb5:
                        return new RequestRecipeBookOpen();
                    case 0xb6: // RequestRecipeItemDelete
                        return new RequestRecipeBookDestroy();
                    case 0xb7:
                        return new RequestRecipeItemMakeInfo();
                    case 0xb8:
                        return new RequestRecipeItemMakeSelf();
                    case 0xb9:
                        // RequestRecipeShopManageList
                        break;
                    case 0xba:
                        return new RequestRecipeShopMessageSet();
                    case 0xbb:
                        return new RequestRecipeShopListSet();
                    case 0xbc:
                        return new RequestRecipeShopManageQuit();
                    case 0xbd:
                        // RequestRecipeShopManageCancel
                        break;
                    case 0xbe:
                        return new RequestRecipeShopMakeInfo();
                    case 0xbf: // RequestRecipeShopMakeDo
                        return new RequestRecipeShopMakeItem();
                    case 0xc0: // RequestRecipeShopSellList
                        return new RequestRecipeShopManagePrev();
                    case 0xc1: // RequestObserverEndPacket
                        return new ObserverReturn();
                    case 0xc2:
                        // Unused (RequestEvaluate/VoteSociality)
                        break;
                    case 0xc3:
                        return new RequestHennaItemList();
                    case 0xc4:
                        return new RequestHennaItemInfo();
                    case 0xc5:
                        return new RequestBuySeed();
                    case 0xc6: // ConfirmDlg
                        return new DlgAnswer();
                    case 0xc7: // RequestPreviewItem
                        return new RequestPreviewItem();
                    case 0xc8:
                        return new RequestSSQStatus();
                    case 0xc9:
                        return new RequestPetitionFeedback();
                    case 0xcb:
                        return new GameGuardReply();
                    case 0xcc:
                        return new RequestPledgePower();
                    case 0xcd:
                        return new RequestMakeMacro();
                    case 0xce:
                        return new RequestDeleteMacro();
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
                                return new RequestManorList();
                            case 0x02:
                                return new RequestProcureCropList();
                            case 0x03:
                                return new RequestSetSeed();
                            case 0x04:
                                return new RequestSetCrop();
                            case 0x05:
                                return new RequestWriteHeroWords();
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
                                return new RequestExAskJoinMPCC();
                            case 0x07:
                                return new RequestExAcceptJoinMPCC();
                            case 0x08:
                                return new RequestExOustFromMPCC();
                            case 0x09:
                                return new RequestOustFromPartyRoom();
                            case 0x0a:
                                return new RequestDismissPartyRoom();
                            case 0x0b:
                                return new RequestWithdrawPartyRoom();
                            case 0x0c:
                                return new RequestChangePartyLeader();
                            case 0x0d:
                                return new RequestAutoSoulShot();
                            case 0x0e:
                                return new RequestExEnchantSkillInfo();
                            case 0x0f:
                                return new RequestExEnchantSkill();
                            case 0x10:
                                return new RequestExPledgeCrestLarge();
                            case 0x11:
                                return new RequestExSetPledgeCrestLarge();
                            case 0x12:
                                return new RequestPledgeSetAcademyMaster();
                            case 0x13:
                                return new RequestPledgePowerGradeList();
                            case 0x14:
                                return new RequestPledgeMemberPowerInfo();
                            case 0x15:
                                return new RequestPledgeSetMemberPowerGrade();
                            case 0x16:
                                return new RequestPledgeMemberInfo();
                            case 0x17:
                                return new RequestPledgeWarList();
                            case 0x18:
                                return new RequestExFishRanking();
                            case 0x19:
                                return new RequestPCCafeCouponUse();
                            case 0x1b:
                                return new RequestDuelStart();
                            case 0x1c:
                                return new RequestDuelAnswerStart();
                            case 0x1d:
                                // RequestExSetTutorial
                                break;
                            case 0x1e:
                                return new RequestExRqItemLink();
                            case 0x1f:
                                // CanNotMoveAnymoreAirShip
                                break;
                            case 0x20:
                                return new MoveToLocationInAirShip();
                            case 0x21:
                                return new RequestKeyMapping();
                            case 0x22:
                                return new RequestSaveKeyMapping();
                            case 0x23:
                                return new RequestExRemoveItemAttribute();
                            case 0x24:
                                return new RequestSaveInventoryOrder();
                            case 0x25:
                                return new RequestExitPartyMatchingWaitingRoom();
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
                                return new RequestOlympiadObserverEnd();
                            case 0x2a:
                                return new RequestCursedWeaponList();
                            case 0x2b:
                                return new RequestCursedWeaponLocation();
                            case 0x2c:
                                return new RequestPledgeReorganizeMember();
                            case 0x2d:
                                return new RequestExMPCCShowPartyMembersInfo();
                            case 0x2e:
                                return new RequestOlympiadMatchList();
                            case 0x2f:
                                return new RequestAskJoinPartyRoom();
                            case 0x30:
                                return new AnswerJoinPartyRoom();
                            case 0x31:
                                return new RequestListPartyMatchingWaitingRoom();
                            case 0x32:
                                return new RequestExEnchantSkillSafe();
                            case 0x33:
                                return new RequestExEnchantSkillUntrain();
                            case 0x34:
                                return new RequestExEnchantSkillRouteChange();
                            case 0x35:
                                return new RequestExEnchantItemAttribute();
                            case 0x36:
                                return new ExGetOnAirShip();
                            case 0x38:
                                return new MoveToLocationAirShip();
                            case 0x39:
                                return new RequestBidItemAuction();
                            case 0x3a:
                                return new RequestInfoItemAuction();
                            case 0x3b:
                                return new RequestExChangeName();
                            case 0x3c:
                                return new RequestAllCastleInfo();
                            case 0x3d:
                                return new RequestAllFortressInfo();
                            case 0x3e:
                                return new RequestAllAgitInfo();
                            case 0x3f:
                                return new RequestFortressSiegeInfo();
                            case 0x40:
                                return new RequestGetBossRecord();
                            case 0x41:
                                //return new RequestRefine();
                                break;
                            case 0x42:
                                return new RequestConfirmCancelItem();
                            case 0x43:
                                //return new RequestRefineCancel();
                                break;
                            case 0x44:
                                return new RequestExMagicSkillUseGround();
                            case 0x45:
                                return new RequestDuelSurrender();
                            case 0x46:
                                return new RequestExEnchantSkillInfoDetail();
                            case 0x48:
                                return new RequestFortressMapInfo();
                            case 0x49:
                                // RequestPVPMatchRecord
                                break;
                            case 0x4a:
                                return new SetPrivateStoreWholeMsg();
                            case 0x4b:
                                return new RequestDispel();
                            case 0x4c:
                                return new RequestExTryToPutEnchantTargetItem();
                            case 0x4d:
                                return new RequestExTryToPutEnchantSupportItem();
                            case 0x4e:
                                return new RequestExCancelEnchantItem();
                            case 0x4f:
                                return new RequestChangeNicknameColor();
                            case 0x50:
                                return new RequestResetNickname();
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
                                        return new RequestBookMarkSlotInfo();
                                    case 0x01:
                                        return new RequestSaveBookMarkSlot();
                                    case 0x02:
                                        return new RequestModifyBookMarkSlot();
                                    case 0x03:
                                        return new RequestDeleteBookMarkSlot();
                                    case 0x04:
                                        return new RequestTeleportBookMark();
                                    case 0x05:
                                        // RequestChangeBookMarkSlot
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            case 0x52:
                                return new RequestWithDrawPremiumItem();
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
                                return new RequestJoinDominionWar();
                            case 0x58:
                                return new RequestDominionInfo();
                            case 0x59:
                                // RequestExCleftEnter
                                break;
                            case 0x5a:
                                return new RequestExCubeGameChangeTeam();
                            case 0x5b:
                                return new EndScenePlayer();
                            case 0x5c:
                                return new RequestExCubeGameReadyAnswer();
                            case 0x63:
                                return new RequestSeedPhase();
                            case 0x65:
                                return new RequestPostItemList();
                            case 0x66:
                                return new RequestSendPost();
                            case 0x67:
                                return new RequestReceivedPostList();
                            case 0x68:
                                return new RequestDeleteReceivedPost();
                            case 0x69:
                                return new RequestReceivedPost();
                            case 0x6a:
                                return new RequestPostAttachment();
                            case 0x6b:
                                return new RequestRejectPostAttachment();
                            case 0x6c:
                                return new RequestSentPostList();
                            case 0x6d:
                                return new RequestDeleteSentPost();
                            case 0x6e:
                                return new RequestSentPost();
                            case 0x6f:
                                return new RequestCancelPostAttachment();
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
                                return new RequestRefundItem();
                            case 0x76:
                                return new RequestBuySellUIClose();
                            case 0x77:
                                // RequestEventMatchObserverEnd
                                break;
                            case 0x78:
                                return new RequestPartyLootModification();
                            case 0x79:
                                return new AnswerPartyLootModification();
                            case 0x7a:
                                return new AnswerCoupleAction();
                            case 0x7b:
                                return new BrEventRankerList();
                            case 0x7c:
                                // AskMembership
                                break;
                            case 0x7d:
                                // RequestAddExpandQuestAlarm
                                break;
                            case 0x7e:
                                return new RequestVoteNew();
                            case 0x84:
                                return new RequestExAddContactToContactList();
                            case 0x85:
                                return new RequestExDeleteContactFromContactList();
                            case 0x86:
                                return new RequestExShowContactList();
                            case 0x87:
                                return new RequestExFriendListExtended();
                            case 0x88:
                                return new RequestExOlympiadMatchListRefresh();
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

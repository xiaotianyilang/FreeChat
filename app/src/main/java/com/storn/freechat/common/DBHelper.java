package com.storn.freechat.common;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.orm.androrm.DatabaseAdapter;
import com.orm.androrm.Model;
import com.orm.androrm.Where;
import com.orm.androrm.statement.SelectStatement;
import com.orm.androrm.tovo.CreateVoBySqlite;
import com.storn.freechat.model.ChatMessageEntity;
import com.storn.freechat.model.FriendsEntity;
import com.storn.freechat.model.FriendsGroup;
import com.storn.freechat.model.GroupEntity;
import com.storn.freechat.model.MessageEntity;
import com.storn.freechat.model.User;
import com.storn.freechat.vo.ChatMessageEntityVo;
import com.storn.freechat.vo.FriendsEntityVo;
import com.storn.freechat.vo.FriendsGroupVo;
import com.storn.freechat.vo.GroupEntityVo;
import com.storn.freechat.vo.MessageEntityVo;
import com.storn.freechat.vo.UserVo;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库
 *
 * @author tianshutong
 *         Created by tianshutong on 2017/1/10.
 * @version 1.0
 */

public class DBHelper {

    /**
     * 数据库版本
     */
    public static final int DATABASE_VERSION = 1;

    private static volatile DBHelper dbHelper;

    /**
     * 获取数据库对象
     *
     * @return
     */
    public static DBHelper getInstance() {
        if (dbHelper == null) {
            synchronized (DBHelper.class) {
                if (dbHelper == null) {
                    dbHelper = new DBHelper();
                }
            }
        }
        return dbHelper;
    }

    /**
     * 初始化数据库
     *
     * @param context
     * @param dbName
     */
    public static void initDataBase(Context context, String dbName) {
        List<Class<? extends Model>> models = new ArrayList<>();
        models.add(MessageEntity.class);
        models.add(ChatMessageEntity.class);
        models.add(FriendsEntity.class);
        models.add(GroupEntity.class);
        models.add(FriendsGroup.class);
        models.add(FriendsEntity.class);
        models.add(User.class);
        DatabaseAdapter.setDatabaseName(dbName);
        DatabaseAdapter dba = DatabaseAdapter.getInstance(context, DATABASE_VERSION);
        dba.setModels(models);
    }

    /**
     * 插入或更新登录用户
     *
     * @param context
     * @param userVo
     */
    public void insertOrUpdateAccount(Context context, UserVo userVo) {
        DatabaseAdapter dba = DatabaseAdapter.getInstance(context);
        ContentValues contentValues = DBUtils.UserVo2Cv(userVo);
        Where where = new Where();
        where.and("jid", userVo.jid);
        dba.doInsertOrUpdate("user", contentValues, where);
    }

    /**
     * 插入或更新消息表
     *
     * @param context
     * @param msgVo
     */
    public void insertOrUpdateMessage(Context context, MessageEntityVo msgVo) {
        DatabaseAdapter dba = DatabaseAdapter.getInstance(context);
        ContentValues contentValues = DBUtils.Message2Cv(msgVo);
        Where where = new Where();
        where.and("fromJid", msgVo.fromJid);
        dba.doInsertOrUpdate("messageentity", contentValues, where);
    }

    /**
     * 插入或更新聊天消息表
     *
     * @param context
     * @param chatMsgVo
     */
    public void insertOrUpdateChatMessage(Context context, ChatMessageEntityVo chatMsgVo) {
        DatabaseAdapter dba = DatabaseAdapter.getInstance(context);
        ContentValues contentValues = DBUtils.ChatMessage2Cv(chatMsgVo);
        Where where = new Where();
        where.and("fromJid", chatMsgVo.fromJid);
        dba.doInsertOrUpdate("chatmessageentity", contentValues, where);
    }

    /**
     * 插入或更新群表
     *
     * @param context
     * @param groupEntityVo
     */
    public void insertOrUpdateGroup(Context context, GroupEntityVo groupEntityVo) {
        DatabaseAdapter dba = DatabaseAdapter.getInstance(context);
        ContentValues contentValues = DBUtils.Group2Cv(groupEntityVo);
        Where where = new Where();
        where.and("roomJid", groupEntityVo.roomJid);
        dba.doInsertOrUpdate("groupentity", contentValues, where);
    }

    /**
     * 插入或更新好友分组
     *
     * @param context
     * @param friendsGroupVo
     */
    public void insertOrUpdateFriendsGroup(Context context, FriendsGroupVo friendsGroupVo) {
        DatabaseAdapter dba = DatabaseAdapter.getInstance(context);
        ContentValues contentValues = DBUtils.FriendsGroup2Cv(friendsGroupVo);
        Where where = new Where();
        where.and("name", friendsGroupVo.name);
        dba.doInsertOrUpdate("friendsgroup", contentValues, where);
    }

    /**
     * 插入或更新好友
     *
     * @param context
     * @param friendsEntityVo
     */
    public void insertOrUpdateFriends(Context context, FriendsEntityVo friendsEntityVo) {
        DatabaseAdapter dba = DatabaseAdapter.getInstance(context);
        ContentValues contentValues = DBUtils.Friends2Cv(friendsEntityVo);
        Where where = new Where();
        where.and("jid", friendsEntityVo.jid);
        dba.doInsertOrUpdate("friendsentity", contentValues, where);
    }

    /**
     * 通过jid查询消息列表
     *
     * @param context
     * @param jid
     */
    public List<MessageEntityVo> queryMessageByJid(Context context, String jid) {
        DatabaseAdapter dba = DatabaseAdapter.getInstance(context);
        SelectStatement selectStatement = new SelectStatement();
        selectStatement.from("messageentity");
        selectStatement.where(new Where().and("myJid", jid));
        selectStatement.orderBy("time");
        dba.open();
        Cursor cursor = dba.query(selectStatement);
        List<MessageEntityVo> messageEntityVoList = null;
        try {
            if (cursor != null) {
                messageEntityVoList = CreateVoBySqlite.cursor2VOList(cursor, MessageEntityVo.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dba.close();
        }
        return messageEntityVoList;
    }

    /**
     * 根据登录用户查询聊天消息
     *
     * @param context
     * @param myJid
     * @param fromJid
     * @return
     */
    public List<ChatMessageEntityVo> queryChatMessageByJid(Context context, String myJid, String fromJid) {
        DatabaseAdapter dba = DatabaseAdapter.getInstance(context);
        Where where = new Where();
        where.and("myJid", myJid);
        where.and("fromJid", fromJid);
        SelectStatement selectStatement = new SelectStatement();
        selectStatement.from("chatmessageentity");
        selectStatement.where(where);
        selectStatement.orderBy("time");
        dba.open();
        Cursor cursor = dba.query(selectStatement);
        List<ChatMessageEntityVo> chatMessageEntityVoList = null;
        try {
            if (cursor != null) {
                chatMessageEntityVoList = CreateVoBySqlite.cursor2VOList(cursor, ChatMessageEntityVo.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dba.close();
        }
        return chatMessageEntityVoList;
    }

}

package com.example.bilibililivedanmu.Bean;

import org.json.JSONArray;
import org.json.JSONObject;

public class DmData {
    private static volatile DmData instance;
    public static  DmData getInstance(){
        if(instance==null){
            synchronized (DmData.class){
                if(instance==null){
                    instance = new DmData();
                }
            }
        }
        return instance;
    }
    public  enum DmType{ MESSAGE,GIFT,OTHER}
    public class DmMessage {
        private String message;
        private userData user;
        private long messageColor;
        private long timeData;
        class userData {
            private String userName;
            private long userID;
            private int isAdmin;
            private int isVIP;
            private int isxxx;
            private int what1;
            private int what2;
            private int guard;

            userData(long id, String name, int admin, int vip, int xxx, int w1, int w2,int gd) {
                userID = id;
                userName = name;
                isAdmin = admin;
                isVIP = vip;
                isxxx = xxx;
                what1 = w1;
                what2 = w2;
                guard = gd;
            }

            public String getUserName() {
                return userName;
            }

            public long getUserID() {
                return userID;
            }

            public boolean getIsAdmin() {
                return isAdmin == 1;
            }

            public boolean getIsVIP() {
                return isVIP == 1;
            }

            public boolean getIsxxx() {
                return isxxx == 1;
            }
            public int getGuard(){return guard;}
        }

        public DmMessage(long messageColor, long timeData,String message, long id, String uName, int admin, int vip, int xxx, int w1, int w2,int gd){
            this.timeData = timeData;
            this.messageColor = messageColor;
            this.message = message;
            this.user = new userData(id,uName,admin,vip,xxx,w1,w2,gd);
        }
        public String getMessage() {
            return message;
        }

        public long getMessageColor() {
            return messageColor;
        }

        public userData getUser() {
            return user;
        }

    }
    public  class DmGift {
        private String giftName;
        private String userName;
        private int uid;
        private int num;
        private int price;
        public DmGift(String giftName,String userName,int uid,int num,int price){
            this.giftName = giftName;
            this.userName = userName;
            this.num = num;
            this.price =price;
            this.uid = uid;
        }
        public String getGiftName(){return giftName;}
        public String getUserName(){return userName;}
        public int getUid(){return uid;}
        public int getNum(){return num;}
        public int getPrice(){return price;}
    }
    public   DmStruct addJson(byte[] data, int len) {
        String str = new String(data, 0, len);
        JSONObject jsonData;
        Object res=null;
        DmStruct dmStruct =new DmStruct();
        try {
            jsonData = new JSONObject(str);
            String cmdData = jsonData.getString("cmd");
            //为了避免小电视导致的bug
            //{"cmd":"DANMU_MSG","info":[[0,1,25,16777215,1500035957,1500035957,0,"fe1118f1",0],"- ( \u309c- \u309c)\u3064\u30ed \u4e7e\u676f~ - bilibili",[97668354,"bilib103",0,1,0],[3,"\u7403\u8ff7","Dean\u7403","74596"],[11,">50000"],[]],"roomid":"26057"}
            Object test = jsonData.get("roomid");
            if(test!=null)
                cmdData = "Other";
            /*
            cmd
            SEND_GIFT 发送礼物
            DANMU_MSG 弹幕信息
            WELCOME 进入信息
            WELCOME_GUARD 舰长进入信息
            ROOM_BLOCK_MSG 房间订阅信息
            SYS_MSG 系统信息
            PREPARING 直播间状态：准备中
            ROOM_SILENT_ON 开启房间禁言
            {"cmd":"TV_START","data":{"id":"22999","dtime":180,"msg":{"cmd":"SYS_MSG","msg":"【萝卜蹲了我不蹲】:?在直播间:?【246】:?赠送 小电视一个，请前往抽奖","rep":1,"styleType":2,"url":"http:\/\/live.bilibili.com\/246","roomid":246,"real_roomid":26057,"rnd":1500035921,"tv_id":"22999"}}}
             */
            switch (cmdData) {
                case "SEND_GIFT":
                    JSONObject giftJson = jsonData.getJSONObject("data");
                    res = resolveGift(giftJson);
                    dmStruct.setType(DmType.GIFT);
                    break;
                case "DANMU_MSG":
                    JSONArray danmuJson = jsonData.getJSONArray("info");
                    res =  resolveDanmuJson(danmuJson);
                    dmStruct.setType(DmType.MESSAGE);
                    break;
                default:
                    dmStruct.setType(DmType.OTHER);
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
        dmStruct.setObj(res);
        return dmStruct;
    }
    private  Object resolveDanmuJson(JSONArray jsonArray){
        DmMessage message=null;
        try {
            JSONArray danmuData = jsonArray.getJSONArray(0);
            /*[0,1,25,16777215,1494147479,"1494140542",0,"c560f131",0]
            1：弹幕类型
            25：弹幕字体大小
            16777215：弹幕颜色十进制
            1494147479：发送时间
             */
            long messageColor = danmuData.getLong(3);
            long timeData = danmuData.getLong(4);
            /*
            弹幕内容
             */
            String messageData = jsonArray.getString(1);
            /*
            [20083719,"宗戊炎执",0,0,0,10000,1]
            20083719：用户id
            宗戊炎执：用户名称
            0：admin
            0: vip
            0: xxx
            10000: w1
            1:w2
             */
            JSONArray userData = jsonArray.getJSONArray(2);
            long id = userData.getLong(0);
            String userName = userData.getString(1);
            int admin = userData.getInt(2);
            int vip = userData.getInt(3);
            int xxx = userData.getInt(4);
            int w1 = userData.getInt(5);
            int w2 = userData.getInt(6);

            /*
            获取舰长
             */
            int gurad = jsonArray.getInt(jsonArray.length()-1);
            message = new DmMessage(messageColor,timeData,messageData,id,userName,admin,vip,xxx,w1,w2,gurad);

        }catch (Exception e){
            e.printStackTrace();
        }
        return message;
    }
    private  Object resolveGift(JSONObject jsonObject){
        DmGift dmGift = null;
        try {
            String giftName = jsonObject.getString("giftName");
            String userName = jsonObject.getString("uname");
            int num = jsonObject.getInt("num");
            int uid = jsonObject.getInt("uid");
            int price = jsonObject.getInt("price");
            dmGift = new DmGift(giftName,userName,uid,num,price);
        }catch (Exception e){
            e.printStackTrace();
        }
        return dmGift;
    }

}

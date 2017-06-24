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
    public  enum DmType{ MESSAGE,GIFT}

    public class DmMessage {
        private String message;
        private userData user;
        private long messageColor;
        private medalData medal;
        private long timeData;
        class medalData {
            private String name;
            private String up;
            private int rank;

            medalData(String name, String up, int rank) {
                this.name = name;
                this.up = up;
                this.rank = rank;
            }

            public String getName() {
                return name;
            }

            public String getUp() {
                return up;
            }

            public int getRank() {
                return rank;
            }
        }

        class userData {
            private String userName;
            private long userID;
            private int isAdmin;
            private int isVIP;
            private int isxxx;
            private int what1;
            private int what2;

            userData(long id, String name, int admin, int vip, int xxx, int w1, int w2) {
                userID = id;
                userName = name;
                isAdmin = admin;
                isVIP = vip;
                isxxx = xxx;
                what1 = w1;
                what2 = w2;
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
        }

        public DmMessage(long messageColor, long timeData,String message, long id, String uName, int admin, int vip, int xxx, int w1, int w2, String rName, String up, int rank) {
            this.timeData = timeData;
            this.messageColor = messageColor;
            this.message = message;
            this.user = new userData(id,uName,admin,vip,xxx,w1,w2);
            this.medal = new medalData(rName,up,rank);
        }
        public DmMessage(long messageColor, long timeData,String message, long id, String uName, int admin, int vip, int xxx, int w1, int w2){
            this.timeData = timeData;
            this.messageColor = messageColor;
            this.message = message;
            this.user = new userData(id,uName,admin,vip,xxx,w1,w2);
            this.medal = null;
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

        public medalData getMedal() {
            return medal;
        }

    }
    public  class DmGift {

    }
    public   Object addJson(byte[] data, int len) {
        String str = new String(data, 0, len);
        JSONObject jsonData;
        Object res=null;
        try {
            jsonData = new JSONObject(str);
            String cmdData = jsonData.getString("cmd");

            switch (cmdData) {
                case "SEND_GIFT":
                    JSONObject giftJson = jsonData.getJSONObject("data");
                    res = resolveGift(giftJson);
                    break;
                case "DANMU_MSG":
                    JSONArray danmuJson = jsonData.getJSONArray("info");
                    res =  resolveDanmuJson(danmuJson);
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
        return res;
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
            [12,"白泽","yuki琥珀",1067,9982427]
            12：等级
            白泽：勋章名称
            yuki琥珀：up
             */

            JSONArray medalData = jsonArray.getJSONArray(3);
            if(medalData==null||medalData.length()==0){
                message = new DmMessage(messageColor,timeData,messageData,id,userName,admin,vip,xxx,w1,w2);
            }
            else{
                int rank = medalData.getInt(0);
                String rname = medalData.getString(1);
                String up = medalData.getString(2);
                message = new DmMessage(messageColor,timeData,messageData,id,userName,admin,vip,xxx,w1,w2,rname,up,rank);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return message;
    }
    private  Object resolveGift(JSONObject jsonObject){
        return null;
    }

}
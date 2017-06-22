package com.example.bilibililivedanmu;

import android.os.Looper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by chazz on 2017/4/24.
 */

public class BilibiliDM   {
    private final String TAG = getClass().toString();
    private BiliDMSocket.GetBiliDMThread socketTread;
    private Object synWorkThread;

    public BilibiliDM(int roomId,String url){
        synWorkThread = new Object();
        socketTread = new BiliDMSocket.GetBiliDMThread(roomId,url);
    }
    public void start(){
        synchronized (synWorkThread){
            socketTread.start();
        }
    }
    public void stop(){
        synchronized (synWorkThread){
            socketTread.close();
        }
    }

}

public class BiliDMSocket {
    private  final  String TAG = getClass().toString();
    public static class GetBiliDMThread extends  Thread{
        public interface OnSocketReceiveCallBack{
            public void OnReceiveFromServerMsg(int type,Object msg);
        }
        public   OnSocketReceiveCallBack mOnSocketReceiveCallBack;
        public OnSocketReceiveCallBack getOnSocketReceiveCallBack(){
            return mOnSocketReceiveCallBack;
        }
        public void setOnSocketReceiveCallBack(OnSocketReceiveCallBack callBack){
            mOnSocketReceiveCallBack = callBack;
        }
        private final  String TAG = getClass().toString();
        int roomID;
        boolean isAlive;
        String socketUrl;
        int socketPort = 788;
        Socket client = null;
        DataInputStream inputStream = null;
        DataOutputStream outputStream = null;
        GetBiliDMThread(int id,final String url){
            roomID = id;
            socketUrl = url;
            isAlive = true;
        }

        @Override
        public void run() {
            byte[] buf = new byte[1024*4];
            BiliDMdata dMdata = BiliDMdata.getInstance();
           long lastKeepTime = System.currentTimeMillis();
            Looper.prepare();
            while (isAlive){
                if(client == null){
                    if(!openSocket()){
                        throw new RuntimeException("连接弹幕服务器失败");
                    }
                }
                try {
                    if(inputStream.available()<=0){

                        if (System.currentTimeMillis() - lastKeepTime >= 30000) {
                            try {
                                outputStream.write(new byte[]{0x00,0x00,0x00,0x10,0x00,0x10,0x00,0x01,0x00,0x00,0x00,0x02,0x00,0x00,0x00,0x01});
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            lastKeepTime = System.currentTimeMillis();
                        }
                        sleep(300);
                        continue;
                    }
                    int packLen = inputStream.readInt();
                    int magic = inputStream.readInt();
                    int packType = inputStream.readInt();
                    int param = inputStream.readInt();
                    packLen = packLen-16;
                    /*
                    剩下数据包大于0时，进行读取判断
                    数据包类型：1，2，3均为在线人数数据包
                    4：不详
                    5：弹幕数据包
                    */
                    if(packLen>0){
                        switch (packType){
                            case 1:
                                inputStream.read(buf,0,packLen);DebugLog.d(TAG,new String(buf,0,packLen));break;
                            case 2:
                                inputStream.read(buf,0,packLen);DebugLog.d(TAG,new String(buf,0,packLen));break;
                            case 3:
                                int number = inputStream.readInt();break;
                            case 4:
                                inputStream.read(buf,0,packLen);DebugLog.d(TAG,new String(buf,0,packLen));break;
                            case 5:
                                inputStream.read(buf,0,packLen);
                                dMdata.addJson(buf,packLen);
                                if()
                                break;
                            default:
                                inputStream.read(buf,0,packLen);DebugLog.d(TAG,new String(buf,0,packLen));;
                        }
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        private boolean openSocket(){
            try {
                try {
                    client = new Socket(socketUrl,socketPort);
                }catch (UnknownHostException e){
                    e.printStackTrace();
                    client = null;
                    outputStream = null;
                    inputStream = null;
                    isAlive = false;
                    return  false;
                }
                outputStream = new DataOutputStream(client.getOutputStream());
                outputStream.write(requestData(roomID));
                inputStream = new DataInputStream(client.getInputStream());
            }catch (IOException e){
                e.printStackTrace();
                client = null;
                outputStream = null;
                inputStream = null;
                isAlive  = false;
                return  false;
            }
            return  true;
        }
        public  void close(){
            isAlive  = false;
            closeSocket();
        }
        private byte[] requestData(int id){
            StringBuffer sb = new StringBuffer();
            int uid = (int)(100000000000000.0+200000000000000.0*Math.random());
            sb.append("{\"roomid\":");
            sb.append(id);
            sb.append(",\"uid\":");
            sb.append(uid);
            sb.append("}");
            DebugLog.d(TAG,sb.toString());
            try{
                String str = new String(sb.toString().getBytes(),"UTF-8");
                int len = str.length()+16;
                byte [] bytes = requestData(len,(short) 16,(short) 1,7,1);
                byte [] bytes1 = new byte[bytes.length+str.length()];
                System.arraycopy(bytes,0,bytes1,0,bytes.length);
                System.arraycopy(str.getBytes(),0,bytes1,bytes.length,len-16);
                return bytes1;
            }catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }
            return  null;
        }
        private byte[] requestData(int len,short magic,short ver,int action,int param)
        {
            return  new byte[]{(byte)(len>>24),(byte)(len>>16),(byte)(len>>8),(byte)(len),(byte)(magic>>8),(byte)(magic),(byte)(ver>>8),(byte)(ver),(byte)(action>>24),(byte)(action>>16),(byte)(action>>8),(byte)(action),(byte)(param>>24),(byte)(param>>16),(byte)(param>>8),(byte)(param)};
        }
        private void closeSocket(){
            if(client != null){
                try {
                    client.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            if(client != null){
                try {
                    client.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            if(client != null){
                try {
                    client.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }


    }
}


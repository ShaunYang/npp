package com.zhuoyi.fauction.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.zhuoyi.fauction.model.Category;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.List;
import java.util.Set;

//配置用户管理
public class ConfigUserManager {

	private static final String TN = "user";

	public static String getSearchPhone(Context cxt) {
		return cxt.getSharedPreferences(TN, 0).getString("searchphone", "");
	}

	public static void setSearchPhone(Context cxt, String phone) {
		cxt.getSharedPreferences(TN, 0).edit().putString("searchphone", phone)
				.commit();
	}

	public static String getUnicodeIEME(Context cxt) {
		return cxt.getSharedPreferences(TN, 0).getString("IEME", "");
	}

	public static void setUnicodeIEME(Context cxt, String IEME) {
		cxt.getSharedPreferences(TN, 0).edit().putString("IEME", IEME)
				.commit();
	}

	public static String getToken(Context cxt) {
		return cxt.getSharedPreferences(TN, 0).getString("token", "");
	}

	public static void setToken(Context cxt, String token) {
		cxt.getSharedPreferences(TN, 0).edit().putString("token", token)
				.commit();
	}

	public static String getUserId(Context cxt) {
		return cxt.getSharedPreferences(TN, 0).getString("user_id", "");
	}

	public static void setUserId(Context cxt, String user_id) {
		cxt.getSharedPreferences(TN, 0).edit().putString("user_id", user_id)
				.commit();
	}

	//密钥
	public static String getSecretKey(Context cxt) {
		return cxt.getSharedPreferences(TN, 0).getString("secretKey", "");
	}

	public static void setSecretKey(Context cxt, String secretKey) {
		cxt.getSharedPreferences(TN, 0).edit().putString("secretKey", secretKey)
				.commit();
	}
	//密码加密字符串
	public static String getDataAuth(Context cxt) {
		return cxt.getSharedPreferences(TN, 0).getString("dataAuth", "");
	}

	public static void setDataAuth(Context cxt, String dataAuth) {
		cxt.getSharedPreferences(TN, 0).edit().putString("dataAuth", dataAuth)
				.commit();
	}
	
	public static String getRegistPhone(Context cxt) {
		return cxt.getSharedPreferences(TN, 0).getString("phone", "");
	}

	public static void setRegistPhone(Context cxt, String phone) {
		cxt.getSharedPreferences(TN, 0).edit().putString("phone", phone)
				.commit();
	}
	

	public static int getDBVersion(Context cxt) {
		return cxt.getSharedPreferences(TN, 0).getInt("DBVersion", 0);
	}

	public static void setDBVersion(Context cxt, int id) {
		cxt.getSharedPreferences(TN, 0).edit().putInt("DBVersion", id).commit();
	}

	public static void setDoctorId(Context cxt,String id){
		cxt.getSharedPreferences(TN, 0).edit().putString("doctorId", id).commit();
	}
	
	public static String getDoctorId(Context cxt) {
		return cxt.getSharedPreferences(TN, 0).getString("doctorId", null);
	}

	public static String getSessionDoctorID(Context cxt) {
		return cxt.getSharedPreferences(TN, 0).getString("sessionDoctorID", "");
	}

	public static String getSessionId(Context cxt) {
		return cxt.getSharedPreferences(TN, 0).getString("sessionID", null);
	}

	public static void setSessionDoctorID(Context cxt, String id) {
		cxt.getSharedPreferences(TN, 0).edit().putString("sessionDoctorID", id)
				.commit();
	}

	public static void setSessionId(Context cxt, String id) {
		cxt.getSharedPreferences(TN, 0).edit().putString("sessionID", id)
				.commit();
	}

	public static boolean isLoginStatus(Context cxt) {
		return cxt.getSharedPreferences(TN, 0).getBoolean("login", false);
	}

	public static void setLoginStatus(Context cxt, boolean b) {
		cxt.getSharedPreferences(TN, 0).edit().putBoolean("login", b).commit();
	}

	public static void setLoginName(Context cxt, String name) {
		cxt.getSharedPreferences(TN, 0).edit().putString("login_name", name)
				.commit();
	}

	public static void setLoginPwd(Context cxt, String pwd) {
		cxt.getSharedPreferences(TN, 0).edit().putString("login_pwd", pwd)
				.commit();
	}

	public static String getLoginName(Context cxt) {
		return cxt.getSharedPreferences(TN, 0).getString("login_name", null);
	}

	public static String getLoginPwd(Context cxt) {
		return cxt.getSharedPreferences(TN, 0).getString("login_pwd", null);
	}
	
	/**
	 * 用于重新登陆时的判断
	 * @param cxt
	 * @param isAlready
	 */
	public static void setAlreadyLogin(Context cxt,boolean isAlready){
		cxt.getSharedPreferences(TN, 0).edit().putBoolean("already_login", isAlready).commit();
	}
	
	public static boolean isAlreadyLogin(Context cxt){
		return cxt.getSharedPreferences(TN, 0).getBoolean("already_login", false);
	}

	public static boolean isAutoLogin(Context cxt) {
		return cxt.getSharedPreferences(TN, 0).getBoolean("auto_login", false);
	}

	public static void setAoutoLogin(Context cxt, boolean b) {
		cxt.getSharedPreferences(TN, 0).edit().putBoolean("auto_login", b)
				.commit();
	}

	public static boolean isFirstOpen(Context cxt){
		return cxt.getSharedPreferences(TN, 0).getBoolean("first_open", true);
	}

	public static void setFirstOpen(Context cxt,boolean flag){
		cxt.getSharedPreferences(TN, 0).edit().putBoolean("first_open",flag).commit();
	}


	

	/**
	 * 获取根据用户id保存的图文咨询获取的保存的字符
	 * 
	 * @param cxt
	 * @param memberId
	 *            用户id
	 * @return 获取编辑框临时存入的字符
	 */
	public static String getUserTempEditStr(Context cxt, String memberId) {
		return cxt.getSharedPreferences(TN, 0).getString(memberId, "");
	}

	public static void clear(Context cxt) {
		cxt.getSharedPreferences(TN, 0).edit().clear().commit();
	}

	/**
	 * 保存头像链接
	 * 
	 * @param cxt
	 * @param avatarUrl
	 */
	public static void setAvatar(Context cxt, String avatarUrl) {
		cxt.getSharedPreferences(TN, 0).edit()
				.putString("personAvatar", avatarUrl).commit();
	}

	/**
	 * 获取保存的头像链接
	 * 
	 * @param cxt
	 * @return
	 */
	public static String getAvatar(Context cxt) {
		return cxt.getSharedPreferences(TN, 0).getString("personAvatar", "");
	}



	/**
	 * desc:保存对象

	 * @param context
	 * @param key
	 * @param obj 要保存的对象，只能保存实现了serializable的对象
	 * modified:
	 */
	public static void saveObject(Context context,String key ,Object obj){
		try {
			// 保存对象
			SharedPreferences.Editor sharedata = context.getSharedPreferences(TN, 0).edit();
			//先将序列化结果写到byte缓存中，其实就分配一个内存空间
			ByteArrayOutputStream bos=new ByteArrayOutputStream();
			ObjectOutputStream os=new ObjectOutputStream(bos);
			//将对象序列化写入byte缓存
			os.writeObject(obj);
			//将序列化的数据转为16进制保存
			String bytesToHexString = bytesToHexString(bos.toByteArray());
			//保存该16进制数组
			sharedata.putString(key, bytesToHexString);
			sharedata.commit();
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("", "保存obj失败");
		}
	}
	/**
	 * desc:将数组转为16进制
	 * @param bArray
	 * @return
	 * modified:
	 */
	public static String bytesToHexString(byte[] bArray) {
		if(bArray == null){
			return null;
		}
		if(bArray.length == 0){
			return "";
		}
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;
		for (int i = 0; i < bArray.length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}
	/**
	 * desc:获取保存的Object对象
	 * @param context
	 * @param key
	 * @return
	 * modified:
	 */
	public static Object readObject(Context context,String key ){
		try {
			SharedPreferences sharedata = context.getSharedPreferences(TN, 0);
			if (sharedata.contains(key)) {
				String string = sharedata.getString(key, "");
				if(TextUtils.isEmpty(string)){
					return null;
				}else{
					//将16进制的数据转为数组，准备反序列化
					byte[] stringToBytes = StringToBytes(string);
					ByteArrayInputStream bis=new ByteArrayInputStream(stringToBytes);
					ObjectInputStream is=new ObjectInputStream(bis);
					//返回反序列化得到的对象
					Object readObject = is.readObject();
					return readObject;
				}
			}
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//所有异常返回null
		return null;

	}
	/**
	 * desc:将16进制的数据转为数组
	 * <p>创建人：聂旭阳 , 2014-5-25 上午11:08:33</p>
	 * @param data
	 * @return
	 * modified:
	 */
	public static byte[] StringToBytes(String data){
		String hexString=data.toUpperCase().trim();
		if (hexString.length()%2!=0) {
			return null;
		}
		byte[] retData=new byte[hexString.length()/2];
		for(int i=0;i<hexString.length();i++)
		{
			int int_ch;  // 两位16进制数转化后的10进制数
			char hex_char1 = hexString.charAt(i); ////两位16进制数中的第一位(高位*16)
			int int_ch1;
			if(hex_char1 >= '0' && hex_char1 <='9')
				int_ch1 = (hex_char1-48)*16;   //// 0 的Ascll - 48
			else if(hex_char1 >= 'A' && hex_char1 <='F')
				int_ch1 = (hex_char1-55)*16; //// A 的Ascll - 65
			else
				return null;
			i++;
			char hex_char2 = hexString.charAt(i); ///两位16进制数中的第二位(低位)
			int int_ch2;
			if(hex_char2 >= '0' && hex_char2 <='9')
				int_ch2 = (hex_char2-48); //// 0 的Ascll - 48
			else if(hex_char2 >= 'A' && hex_char2 <='F')
				int_ch2 = hex_char2-55; //// A 的Ascll - 65
			else
				return null;
			int_ch = int_ch1+int_ch2;
			retData[i/2]=(byte) int_ch;//将转化后的数放入Byte里
		}
		return retData;
	}
	


	
}

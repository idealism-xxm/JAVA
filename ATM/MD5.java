package ATM;

import java.security.MessageDigest;

public class MD5 {
	public static String getMD5(String s) {//生成MD5，用十六进制表示
		try {
			StringBuilder sBuilder = new StringBuilder();
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(s.getBytes());
			byte[] result = md5.digest();
			for(int i = 0; i < result.length; ++i) {
				int tmp = result[i];
				if(tmp < 0) {
					tmp += 256;
				}
				if(tmp < 16) {//如果这一字节的数小于16，为了使十六进制数恒能用2位表示，高位补0
					sBuilder.append("0");
				}
				sBuilder.append(Integer.toHexString(tmp));
			}
			return sBuilder.toString().toUpperCase();
		}
		catch(Exception e) {
			return null;
		}
	}
}

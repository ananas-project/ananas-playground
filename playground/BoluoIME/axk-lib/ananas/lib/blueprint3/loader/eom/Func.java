package ananas.lib.blueprint3.loader.eom;

public class Func {

	public static String toJavaName(String str) {
		StringBuilder sb = new StringBuilder();
		for (char ch : str.toCharArray()) {
			if ('0' <= ch && ch <= '9') {
				sb.append(ch);
			} else if ('a' <= ch && ch <= 'z') {
				sb.append(ch);
			} else if ('A' <= ch && ch <= 'Z') {
				sb.append(ch);
			} else {
				sb.append('_');
			}
		}
		return sb.toString();
	}

}

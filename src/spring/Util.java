package spring;

public class Util {
	public static int toInt(Object o) {
		if(o == null || "null".equals(o)) 
			return 0;
		if(o instanceof Integer) 
			return (Integer)o;
		if(o instanceof String) 
			return Integer.parseInt((String)o);
		return 0;
	}
}

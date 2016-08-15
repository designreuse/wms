package com.snapdeal.library;

import java.util.Collection;

public class Util {

	public static boolean contains(Collection<?> coll, Object o) {
		return coll.contains(o);
	}

	public static String specialTrim(String str){
		if(str !=null)
		{
			return str.replace(String.valueOf((char) 160), " ").trim();
		}
		else
			return null;
	}
}
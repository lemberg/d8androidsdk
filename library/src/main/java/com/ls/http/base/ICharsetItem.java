package com.ls.http.base;



public interface ICharsetItem {
	
	public final static String UTF_8 = "utf-8";
	
	/**	
	 * @return Charset or null if default charset gas to be used.
	 */
	public String getCharset();
}

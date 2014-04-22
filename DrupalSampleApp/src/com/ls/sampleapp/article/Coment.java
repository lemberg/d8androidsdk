package com.ls.sampleapp.article;

public class Coment
{
	public String status;
	public String cid;
	public long last_comment_timestamp;
	public String last_comment_name;
	public String last_comment_uid;
	public int comment_count;
	
	@Override
	public String toString()
	{
		return "Coment [status=" + status + ", cid=" + cid + ", last_comment_timestamp=" + last_comment_timestamp + ", last_comment_name=" + last_comment_name
				+ ", last_comment_uid=" + last_comment_uid + ", comment_count=" + comment_count + "]";
	}
	
	
}

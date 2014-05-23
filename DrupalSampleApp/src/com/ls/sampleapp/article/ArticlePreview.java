package com.ls.sampleapp.article;


public class ArticlePreview
{
	private String nid;	
	private String title;
	private String field_blog_date;
	private String field_file;
	private String field_blog_author;
	private String field_blog_category;
	private String body;
	public String getNid()
	{
		return nid;
	}
	public String getTitle()
	{
		return title;
	}
	public String getDate()
	{
		return field_blog_date;
	}
	public String getAuthor()
	{
		return field_blog_author;
	}
	public String getBody()
	{
		return body;
	}
	public String getImage()
	{
		return this.field_file;
	}
	public String getCategory()
	{
		return field_blog_category;
	}
}

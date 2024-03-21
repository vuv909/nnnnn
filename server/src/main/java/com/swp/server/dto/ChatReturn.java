package com.swp.server.dto;

public class ChatReturn {

	private long id;
	private String name;

	public ChatReturn() {
		// TODO Auto-generated constructor stub
	}

	public ChatReturn(long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}

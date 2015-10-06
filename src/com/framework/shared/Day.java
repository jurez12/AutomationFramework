package com.framework.shared;



public enum Day {
	one("01"), two("02"), three("03"), four("04"), five("05"), six("06"), seven("07"), eight("08"), nine("09"), ten("10"),
	eleven("11"), twelve("12"), thirteen("13"), fourteen("14"), fifteen("15"), sixteen("16"), 
	seventeen("17"), eighteen("18"), nineteen("19"), twenty("20"),
	twentyone("21"), twentytwo("22"), twentythree("23"), twentyfour("24"), twentyfive("25"), twentysix("26"),
	twentyseven("27"), twentyeight("28"), twentynine("29"), thirty("30"),
	thirtyone("31");
	
	String day;
	
	private Day(String day) {
		this.day = day;
	}
	
	public String getDay() {
		return day;
	}
}

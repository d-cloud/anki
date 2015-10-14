package com.anki.desk.service;

public enum PropertiesMenu {
	
	BCAST(0x01),
	READ(0x02),
	WRITE_NO_RSP(0x04),
	WRITE(0x08),
	NOTIFY(0x10)
	
	
	
	
	;
	int p;
	
	
	
	public int getP() {
		return p;
	}

	public void setP(int p) {
		this.p = p;
	}



	PropertiesMenu(int p){
		this.p = p;
	}

}

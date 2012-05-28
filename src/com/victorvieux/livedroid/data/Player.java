package com.victorvieux.livedroid.data;

import java.io.Serializable;
import java.util.List;

public class Player implements Serializable {
	private static final long serialVersionUID = 5351782297710379770L;

	public String Gamertag;
	
	public String Status_Tier;
	public String Status_Online_Status;
	public String Avatar_Gamertile;
	public String Avatar_Gamerpic;
	public String Avatar_Body;
	public String Gamerscore;
	public String GameCount;
	
	public List<Game> games;
}

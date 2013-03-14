package com.victorvieux.livedroid.api.endpoints;

import java.io.Serializable;
import java.util.List;

import com.victorvieux.livedroid.api.data.Game;
import com.victorvieux.livedroid.api.data.Player;

@SuppressWarnings("serial")
public class Games extends Header implements Serializable {
	public Player Player;
	public List<Game> Games;
}

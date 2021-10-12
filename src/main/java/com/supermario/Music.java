package com.supermario;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 *  背景音乐类
 */

public class Music {
    public Music() throws FileNotFoundException, JavaLayerException {
        Player player;
        String str = "src/main/resources/music/music.wav";
        BufferedInputStream name = new BufferedInputStream(new FileInputStream(str));
        player = new Player(name);
        player.play();
    }
}

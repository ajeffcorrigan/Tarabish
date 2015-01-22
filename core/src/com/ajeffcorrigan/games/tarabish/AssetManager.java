package com.ajeffcorrigan.games.tarabish;

import java.util.Collection;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetManager {
	private static float sfxVolume = 1.0f;
	private static float musicVolume = 1.0f;
	
	private static HashMap<String,Sound> sounds = new HashMap<String, Sound>();
	private static HashMap<String,Texture> textures = new HashMap<String, Texture>();
	private static HashMap<String,Music> songs = new HashMap<String, Music>();
	private static HashMap<String,TextureRegion> textureregions = new HashMap<String, TextureRegion>();

	/**
	 * Load a music file into the AssetManager. 
	 * Currently supported formats are WAV, MP3 and OGG.
	 * @param key
	 * @param file
	 */
	public static void loadMusicAs(String key, String file) {
		try {
			if (songs.get(key)!= null) { throw new Exception("Music for key " + key + " already existing!"); }
		} catch (Exception e) { e.printStackTrace(); }
		Music song = Gdx.audio.newMusic(new FileHandle(file));
		songs.put(key, song);
	}
	/**
	 * Returns the music object for use in application.
	 * @param key
	 * @return
	 */
	public static Music getMusic(String key) {
		Music music = songs.get(key);
		try {
			if (music == null) { throw new Exception("No music for key" + key + " found!"); }
		} catch (Exception e) { e.printStackTrace(); }
		return music;
	}
	
	/**
	 * Load a sound into the AssetManager.
	 * Currently supported formats are WAV, MP3 and OGG. 
	 * @param key
	 * @param file
	 */
	public static void loadSoundAs(String key, String file) {
		try { if (sounds.get(key)!= null) { throw new Exception("Sound for key " + key + " already existing!"); } } catch (Exception e) { e.printStackTrace(); }
		Sound sound = Gdx.audio.newSound(new FileHandle(file));
		sounds.put(key, sound);
	}
	/**
	 * Returns a sound object for use in the application.
	 * @param key
	 * @return
	 */
	public static Sound getSound(String key) {
		Sound sound = sounds.get(key);
		try { if (sound == null) { throw new Exception("No sound for key" + key + " found!"); } } catch (Exception e) { e.printStackTrace(); }
		return sound;
	}
	/**
	 * Load a texture into the AssetManager.
	 * @param key
	 * @param file
	 */
	public static void loadTextureAs(String key, String file){
		try { if (textures.get(key)!= null) { throw new Exception("Texture for key " + key + " already exists!"); } } catch (Exception e) { e.printStackTrace(); }
		Texture texture = new Texture(new FileHandle(file));
		textures.put(key, texture);
	}
	/**
	 * Returns the texture object for use in the application.
	 * @param key
	 * @return
	 */
	public static Texture getTexture(String key) {
		Texture text = textures.get(key);
		try { if (textures == null) { throw new Exception("No image for key" + key + " found!"); } } catch (Exception e) { e.printStackTrace(); }
		return text;
	}
	/**
	 * Creates a new TextureRegion asset for use.
	 * @param key
	 * @param sourcekey
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public static void createTextureRegion(String key, String sourcekey, int x, int y, int width, int height) {
		try { if(!textures.containsKey(sourcekey)) { throw new Exception("Source key: " + sourcekey + " does not exist in textures."); } } catch (Exception e) { e.printStackTrace(); }
		Texture text = textures.get(sourcekey);
		try { if (textureregions.get(key)!= null) { throw new Exception("TextureRegion for key " + key + " already exists!"); } } catch (Exception e) { e.printStackTrace(); }
		TextureRegion tr = new TextureRegion(text,x,y,width,height);
		textureregions.put(key, tr);
	}
	/**
	 * Returns the texture region called from key value.
	 * @param key
	 * @return
	 */
	public static TextureRegion getTextureRegion(String key) {
		TextureRegion tr = textureregions.get(key);
		try { if (textureregions == null) { throw new Exception("No image for key" + key + " found!"); } } catch (Exception e) { e.printStackTrace(); }
		return tr;
	}
}

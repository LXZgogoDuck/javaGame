package kernel.audio;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

public class AudioPlayer {
        private Clip[] audios;
        public static int menu = 0;
        public static int l1 = 1;
        public static int l2 = 2;
        private int currentSong;
        private float volume = 0.5f;
        private boolean songMute;

        public AudioPlayer() {
           //load songs for the game
            String[] audioName = { "menu", "level1", "level2" };
            audios = new Clip[audioName.length];
            for (int i = 0; i < audios.length; i++)
                audios[i] = getClip(audioName[i]);
            getSong(menu);
        }
//load audios for the game
        private Clip getClip(String name) {
            URL url = getClass().getResource("/audio/" + name + ".wav");
            AudioInputStream audio;
            try {
                audio = AudioSystem.getAudioInputStream(url);
                Clip clip = AudioSystem.getClip();
                clip.open(audio);
                return clip;
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
            return null;
        }

        public void setVolume(float vol) {
            this.volume = vol;
            setVol();
        }
        
        public void stopSong() {
            if (audios[currentSong].isActive())  audios[currentSong].stop();
        }
        public void setLevelSong(int lvlIndex) {
            if (lvlIndex % 2 == 0)  getSong(l1);
            else  getSong(l2);
        }
        public void lvlCompleted() {
            stopSong();
        }

        public void getSong(int song) {
            stopSong(); //stop current song
            currentSong = song;
            setVol();
            audios[currentSong].setMicrosecondPosition(0);
            audios[currentSong].loop(Clip.LOOP_CONTINUOUSLY);
        }

        public void Mute() {
            this.songMute = !songMute;
            for (Clip c : audios) {
                BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
                booleanControl.setValue(songMute);
            }
        }

        private void setVol() {
            FloatControl contl = (FloatControl) audios[currentSong].getControl(FloatControl.Type.MASTER_GAIN);
            float ran = contl.getMaximum() - contl.getMinimum();
            float obtain = (ran * volume) + contl.getMinimum();
            contl.setValue(obtain);
        }


    }

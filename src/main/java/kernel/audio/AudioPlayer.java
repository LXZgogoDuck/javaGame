package kernel.audio;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

public class AudioPlayer {
        public static int MENU_1 = 0;
        public static int l1 = 1;
        public static int l2 = 2;
        public static int dead = 0;
        public static int jump = 1;
        public static int ko = 2;
        public static int lcomplete = 3;
        private Clip[] songs, effects;
        private int songPlayingNow;
        private float volume = 0.5f;
        private boolean songMute, effectMute;
        private Random rand = new Random();

        public AudioPlayer() {
           //load songs for the game
            String[] names = { "menu", "level1", "level2" };
            songs = new Clip[names.length];
            for (int i = 0; i < songs.length; i++)
                songs[i] = getClip(names[i]);
           // load effects for the game
            String[] effectNames = { "die", "jump", "gameover", "lvlcompleted", "attack1", "attack2", "attack3" };
            effects = new Clip[effectNames.length];
            for (int i = 0; i < effects.length; i++)
                effects[i] = getClip(effectNames[i]);
            seteffectVol();
            getSong(MENU_1);
        }

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
            seteffectVol();
            setSongVol();
        }

        public void stopSong() {
            if (songs[songPlayingNow].isActive())  songs[songPlayingNow].stop();
        }
        public void setLevelSong(int lvlIndex) {
            if (lvlIndex % 2 == 0)  getSong(l1);
            else  getSong(l2);
        }
        public void lvlCompleted() {
            stopSong();
            playEffect(lcomplete);
        }

        public void attacking() {
            int start = 4;
            start += rand.nextInt(3);
            playEffect(start);
        }

        public void playEffect(int effect) {
            if (effects[effect].getMicrosecondPosition() > 0)
                effects[effect].setMicrosecondPosition(0);
            effects[effect].start();
        }

        public void getSong(int song) {
            stopSong();
            songPlayingNow = song;
            setSongVol();
            songs[songPlayingNow].setMicrosecondPosition(0);
            songs[songPlayingNow].loop(Clip.LOOP_CONTINUOUSLY);
        }

        public void Mute() {
            this.songMute = !songMute;
            for (Clip c : songs) {
                BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
                booleanControl.setValue(songMute);
            }
        }
        public void togEffectMute() {
            this.effectMute = !effectMute;
            for (Clip c : effects) {
                BooleanControl bcontrol = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
                bcontrol.setValue(effectMute);
            }
            if (!effectMute)
                playEffect(jump);
        }
        private void setSongVol() {
            FloatControl contl = (FloatControl) songs[songPlayingNow].getControl(FloatControl.Type.MASTER_GAIN);
            float ran = contl.getMaximum() - contl.getMinimum();
            float obtain = (ran * volume) + contl.getMinimum();
            contl.setValue(obtain);
        }

        private void seteffectVol() {
            for (Clip c : effects) {
                FloatControl contl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
                float ran = contl.getMaximum() - contl.getMinimum();
                float obtain = (ran * volume) + contl.getMinimum();
                contl.setValue(obtain);
            }
        }

    }

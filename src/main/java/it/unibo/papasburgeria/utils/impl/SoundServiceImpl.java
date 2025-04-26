package it.unibo.papasburgeria.utils.impl;

import it.unibo.papasburgeria.utils.api.SoundService;

import javax.sound.sampled.Clip;
import java.util.HashMap;
import java.util.Map;

public class SoundServiceImpl implements SoundService {
    private final Map<String, Clip> soundEffects = new HashMap<>();

    public SoundServiceImpl(final String path) {
        loadFromPath(path); // initializing sfx for cached use
    }

    private void loadFromPath(final String path) {

    }

    @Override
    public void play(final String name, final float volumeMultiplier, final float speedMultiplier, final boolean asLooped) {

    }

    @Override
    public void stop(final String name) {

    }
}

package it.unibo.papasburgeria.utils.api;

public interface SoundService {
    void play(String name, float volumeMultiplier, float speedMultiplier, boolean asLooped);
    void stop(String name);
}

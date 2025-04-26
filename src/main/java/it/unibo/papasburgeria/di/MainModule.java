package it.unibo.papasburgeria.di;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import it.unibo.papasburgeria.utils.api.SoundService;
import it.unibo.papasburgeria.utils.impl.SoundServiceImpl;

public class MainModule extends AbstractModule {
    @Override
    protected void configure() {
        //
    }

    @Provides
    @Singleton
    SoundService provideSoundService() {
        return new SoundServiceImpl("");
    }
}

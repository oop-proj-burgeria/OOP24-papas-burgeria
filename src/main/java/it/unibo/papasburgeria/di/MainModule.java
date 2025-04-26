package it.unibo.papasburgeria.di;

import com.google.inject.AbstractModule;

/**
 * Main Guice module that installs the rest submodules.
 */
public class MainModule extends AbstractModule {

    /**
     * @inheritDoc
     */
    @Override
    protected void configure() {
        // as per documentation, lighter modules are better, hence the subdivision
        install(new ModelModule());
        install(new ViewModule());
        install(new ControllerModule());
        install(new UtilsModule());
    }
}

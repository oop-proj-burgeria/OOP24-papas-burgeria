package it.unibo.papasburgeria.controller.impl;

import com.google.inject.Inject;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.papasburgeria.controller.api.MenuController;
import it.unibo.papasburgeria.model.api.GameModel;
import it.unibo.papasburgeria.utils.api.SaveService;
import it.unibo.papasburgeria.utils.impl.saving.SaveInfo;
import it.unibo.papasburgeria.utils.impl.saving.SaveState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of MenuController.
 *
 * <p>
 * See {@link MenuController} for interface details.
 */

public class MenuControllerImpl implements MenuController {
    private final SaveService saveService;
    @SuppressFBWarnings(
            value = "EI_EXPOSE_REP2",
            justification = "Controllers intentionally hold references to mutable models."
    )
    private final GameModel gameModel;

    /**
     * Initializes the menu controller.
     *
     * @param saveService service used to save data
     * @param gameModel   game data model
     */
    @Inject
    public MenuControllerImpl(
            final SaveService saveService,
            final GameModel gameModel
    ) {
        this.saveService = saveService;
        this.gameModel = gameModel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCurrentlyUsedSaveIndex() {
        return this.gameModel.getCurrentSaveSlot();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SaveInfo> getSaves() {
        try {
            final List<SaveState> states = this.saveService.loadAllSlots();
            if (states != null && !states.isEmpty()) {
                final List<SaveInfo> transformed = new ArrayList<>();
                for (int i = 0; i < states.size(); i++) {
                    transformed.add(new SaveInfo(i, states.get(i))); // SaveInfo(i, a, b) otherwise SaveInfo(i, -1, -1)
                }

                return transformed;
            }
        } catch (final IOException e) {
            return Collections.emptyList();
        }

        return Collections.emptyList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "MenuControllerImpl{"
                +
                "saveService="
                + saveService
                +
                ", gameModel="
                + gameModel
                +
                '}';
    }
}

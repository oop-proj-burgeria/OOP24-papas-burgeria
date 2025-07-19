package it.unibo.papasburgeria.view.impl;

import com.google.inject.Inject;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.papasburgeria.controller.api.GrillController;
import it.unibo.papasburgeria.model.api.PattyModel;
import it.unibo.papasburgeria.model.impl.PattyModelImpl;
import it.unibo.papasburgeria.utils.api.ResourceService;
import it.unibo.papasburgeria.utils.api.SfxService;
import it.unibo.papasburgeria.view.api.components.Sprite;
import it.unibo.papasburgeria.view.api.components.SpriteDropListener;
import it.unibo.papasburgeria.view.impl.components.DrawingManagerImpl;
import it.unibo.papasburgeria.view.impl.components.SpriteDragManagerImpl;
import it.unibo.papasburgeria.view.impl.components.SpriteImpl;

import java.awt.Graphics;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

import static it.unibo.papasburgeria.model.IngredientEnum.PATTY;
import static it.unibo.papasburgeria.model.impl.GameModelImpl.GRILL_COLUMNS;
import static it.unibo.papasburgeria.model.impl.GameModelImpl.GRILL_ROWS;
import static it.unibo.papasburgeria.model.impl.PattyModelImpl.MAX_COOK_LEVEL;
import static it.unibo.papasburgeria.model.impl.PattyModelImpl.MIN_COOK_LEVEL;
import static it.unibo.papasburgeria.view.impl.components.DrawingManagerImpl.EXTENSION;
import static it.unibo.papasburgeria.view.impl.components.DrawingManagerImpl.INGREDIENTS_X_SIZE_SCALE;
import static it.unibo.papasburgeria.view.impl.components.DrawingManagerImpl.INGREDIENTS_Y_SIZE_SCALE;
import static it.unibo.papasburgeria.view.impl.components.DrawingManagerImpl.PATTY_SPACING;

/**
 * Manages the GUI for the grill scene in the game.
 */
@SuppressFBWarnings(
        value = {"EI_EXPOSE_REP2", "SE_TRANSIENT_FIELD_NOT_RESTORED"},
        justification = "The controller is injected and shared intentionally; The views are not serialized at runtime"
)
public class GrillViewImpl extends AbstractBaseView implements SpriteDropListener {
    /**
     * Defines the minimum x position to drop a patty on the grill.
     */
    public static final double MIN_X_POS_SCALE_TO_DROP_ON_GRILL = 0.232;
    /**
     * Defines the maximum x position to drop a patty on the grill.
     */
    public static final double MAX_X_POS_SCALE_TO_DROP_ON_GRILL = 0.742;
    /**
     * Defines the minimum y position to drop a patty on the grill.
     */
    public static final double MIN_Y_POS_SCALE_TO_DROP_ON_GRILL = 0.203;
    /**
     * Defines the maximum y position to drop a patty on the grill.
     */
    public static final double MAX_Y_POS_SCALE_TO_DROP_ON_GRILL = 0.981;
    /**
     * Defines the x position of a patty on the grill.
     */
    public static final double PATTY_ON_GRILL_X_POS = MIN_X_POS_SCALE_TO_DROP_ON_GRILL + 0.02;
    /**
     * Defines the y position of a patty on the grill.
     */
    public static final double PATTY_ON_GRILL_Y_POS = MIN_Y_POS_SCALE_TO_DROP_ON_GRILL + 0.02;
    /**
     * Defines the x spacing between patties on the grill.
     */
    public static final double GRILL_X_SPACING = (MAX_X_POS_SCALE_TO_DROP_ON_GRILL - MIN_X_POS_SCALE_TO_DROP_ON_GRILL
            - INGREDIENTS_X_SIZE_SCALE * GRILL_COLUMNS) / (GRILL_COLUMNS + 2);
    /**
     * Defines the y spacing between patties on the grill.
     */
    public static final double GRILL_Y_SPACING = (MAX_Y_POS_SCALE_TO_DROP_ON_GRILL - MIN_Y_POS_SCALE_TO_DROP_ON_GRILL
            - INGREDIENTS_Y_SIZE_SCALE * GRILL_ROWS) / (GRILL_ROWS + 2);
    /**
     * Defines the number of seconds needed to fully cook a patty that is on the grill.
     */
    public static final double SECONDS_TO_FULLY_COOK_PATTY = 15.0;
    /**
     * Defines the increment per second of the cook level of a patty that is on the grill.
     */
    public static final double COOK_LEVEL_INCREMENT_PER_SECOND = MAX_COOK_LEVEL / SECONDS_TO_FULLY_COOK_PATTY;

    private static final double RAW_PATTIES_X_POS_SCALE = 0.028;
    private static final double RAW_PATTIES_Y_POS_SCALE = 0.78;
    private static final double MIN_X_POS_SCALE_TO_DELETE = 0.025;
    private static final double MAX_X_POS_SCALE_TO_DELETE = 0.192;
    private static final double MIN_Y_POS_SCALE_TO_DELETE = 0.203;
    private static final double MAX_Y_POS_SCALE_TO_DELETE = 0.333;
    private static final double COOKED_PATTIES_X_POS_SCALE = 0.807;
    private static final double COOKED_PATTIES_Y_POS_SCALE = 0.795;
    private static final int NUMBER_OF_RAW_PATTIES = 4;

    @Serial
    private static final long serialVersionUID = 1L;
    private final transient GrillController controller;
    private final transient SfxService sfxService;
    private final transient DrawingManagerImpl drawingManager;
    private final transient List<Sprite> draggableRawPatties;
    private final transient List<Sprite> draggableCookedPatties;
    private final transient List<Sprite> draggablePattiesOnGrill;
    private transient boolean stopCooking;

    /**
     * Default constructor, creates and initializes the GUI elements.
     *
     * @param resourceService the service that handles resource obtainment
     * @param sfxService      the service that handles sfx playing
     * @param controller      the grill controller
     * @param drawingManager  the manager for drawing various things
     */
    @Inject
    public GrillViewImpl(
            final ResourceService resourceService,
            final SfxService sfxService,
            final GrillController controller,
            final DrawingManagerImpl drawingManager
    ) {
        this.controller = controller;
        this.sfxService = sfxService;
        this.drawingManager = drawingManager;
        draggableRawPatties = new ArrayList<>();
        draggableCookedPatties = new ArrayList<>();
        draggablePattiesOnGrill = new ArrayList<>();
        stopCooking = false;

        super.setStaticBackgroundImage(resourceService.getImage("grill_background.png"));

        double pbPositionYScale = RAW_PATTIES_Y_POS_SCALE;
        final Sprite pattySprite = new SpriteImpl(resourceService.getImage(
                PATTY.getName() + EXTENSION), new PattyModelImpl(),
                RAW_PATTIES_X_POS_SCALE, pbPositionYScale,
                INGREDIENTS_X_SIZE_SCALE, INGREDIENTS_Y_SIZE_SCALE
        );
        pattySprite.setDraggable(false);
        for (int index = 0; index < NUMBER_OF_RAW_PATTIES; index++) {
            pattySprite.setPbPositionYScale(pbPositionYScale);
            draggableRawPatties.add(new SpriteImpl(pattySprite));
            pbPositionYScale = pbPositionYScale - PATTY_SPACING;
        }

        new SpriteDragManagerImpl(this, draggableRawPatties, this);
        new SpriteDragManagerImpl(this, draggableCookedPatties, this);
        new SpriteDragManagerImpl(this, draggablePattiesOnGrill, this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void update(final double delta) {
        if (!stopCooking) {
            controller.cookPattiesOnGrill();
            draggablePattiesOnGrill.clear();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void paintComponentDelegate(final Graphics graphics) {
        final List<PattyModel> cookedPatties = controller.getCookedPatties();
        drawingManager.generateCookedPatties(
                cookedPatties,
                COOKED_PATTIES_X_POS_SCALE,
                COOKED_PATTIES_Y_POS_SCALE,
                draggableCookedPatties
        );

        final PattyModel[][] pattiesOnGrill = controller.getPattiesOnGrill();
        drawingManager.generatePattiesOnGrill(pattiesOnGrill, draggablePattiesOnGrill);

        for (final Sprite sprite : draggableCookedPatties) {
            sprite.setRemovable(true);
            sprite.draw(getSize(), graphics);
        }

        for (final Sprite sprite : draggablePattiesOnGrill) {
            sprite.draw(getSize(), graphics);
        }

        for (final Sprite sprite : draggableRawPatties) {
            sprite.draw(getSize(), graphics);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showScene() {
        draggableCookedPatties.clear();
        this.sfxService.playSoundLooped("burger_ost.wav", DEFAULT_SOUND_VOLUME);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void hideScene() {
        this.sfxService.stopSound("burger_ost.wav");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void spriteDropped(final Sprite sprite) {
        final double pbPositionXScale =
                sprite.getPbPositionXScale() + INGREDIENTS_X_SIZE_SCALE / 2;
        final double pbPositionYScale =
                sprite.getPbPositionYScale() + INGREDIENTS_Y_SIZE_SCALE / 2;

        final PattyModel patty = (PattyModel) sprite.getIngredient();
        if (pbPositionXScale > MIN_X_POS_SCALE_TO_DROP_ON_GRILL
                && pbPositionXScale < MAX_X_POS_SCALE_TO_DROP_ON_GRILL
                && pbPositionYScale > MIN_Y_POS_SCALE_TO_DROP_ON_GRILL
                && pbPositionYScale < MAX_Y_POS_SCALE_TO_DROP_ON_GRILL) {
            if (controller.addPattyOnGrill(patty, pbPositionXScale, pbPositionYScale)) {
                if (sprite.isRemovable()) {
                    if (controller.getCookedPatties().contains(patty)) {
                        controller.removeCookedPatty(patty);
                        draggableCookedPatties.clear();
                    } else {
                        controller.removePattyFromGrill(patty);
                        controller.addPattyOnGrill(patty, pbPositionXScale, pbPositionYScale);
                    }
                }
                draggablePattiesOnGrill.clear();
            }
        } else if (patty.getBottomCookLevel() + patty.getTopCookLevel() > MIN_COOK_LEVEL
                && pbPositionXScale > MAX_X_POS_SCALE_TO_DROP_ON_GRILL) {
            if (!controller.getCookedPatties().contains(patty)
                    && controller.addCookedPatty(patty)) {
                draggableCookedPatties.clear();
                controller.removePattyFromGrill(patty);
                draggablePattiesOnGrill.clear();
            }
        } else if (pbPositionXScale >= MIN_X_POS_SCALE_TO_DELETE
                && pbPositionXScale <= MAX_X_POS_SCALE_TO_DELETE
                && pbPositionYScale >= MIN_Y_POS_SCALE_TO_DELETE
                && pbPositionYScale <= MAX_Y_POS_SCALE_TO_DELETE
                && patty.getBottomCookLevel() + patty.getTopCookLevel() > MIN_COOK_LEVEL) {
            if (controller.getCookedPatties().contains(patty)) {
                controller.removeCookedPatty(patty);
                draggableCookedPatties.clear();
            } else {
                controller.removePattyFromGrill(patty);
                draggablePattiesOnGrill.clear();
            }
        }

        draggableRawPatties.remove(sprite);
        draggablePattiesOnGrill.remove(sprite);
        draggableCookedPatties.remove(sprite);

        stopCooking = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void spriteClicked(final Sprite sprite) {
        // sprite is null if you're not clicking on a copy, this needs better handling
        if (sprite != null) {
            final double pbPositionXScale = sprite.getPbPositionXScale();
            final double pbPositionYScale = sprite.getPbPositionYScale();
            final PattyModel patty = (PattyModel) sprite.getIngredient();

            if (pbPositionXScale > MIN_X_POS_SCALE_TO_DROP_ON_GRILL
                    && pbPositionXScale < MAX_X_POS_SCALE_TO_DROP_ON_GRILL
                    && pbPositionYScale > MIN_Y_POS_SCALE_TO_DROP_ON_GRILL
                    && pbPositionYScale < MAX_Y_POS_SCALE_TO_DROP_ON_GRILL) {
                controller.flipPatty(patty);
                draggablePattiesOnGrill.clear();
            }

            draggableRawPatties.remove(sprite);
            draggablePattiesOnGrill.remove(sprite);
            draggableCookedPatties.remove(sprite);

            stopCooking = false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void spritePressed(final Sprite sprite) {
        stopCooking = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "GrillViewImpl{"
                + "controller=" + controller
                + ", drawingManager=" + drawingManager
                + ", draggableRawPatties=" + draggableRawPatties
                + ", draggableCookedPatties=" + draggableCookedPatties
                + ", draggablePattiesOnGrill=" + draggablePattiesOnGrill
                + ", stopCooking=" + stopCooking
                + '}';
    }
}

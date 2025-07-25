package it.unibo.papasburgeria.view.impl;

import com.google.inject.Inject;
import it.unibo.papasburgeria.controller.api.DayChangeController;
import it.unibo.papasburgeria.controller.api.GameController;
import it.unibo.papasburgeria.model.IngredientEnum;
import it.unibo.papasburgeria.utils.api.ResourceService;
import it.unibo.papasburgeria.utils.api.scene.SceneType;
import it.unibo.papasburgeria.view.impl.components.ScalableLayoutImpl;
import it.unibo.papasburgeria.view.impl.components.ScaleConstraintImpl;
import it.unibo.papasburgeria.view.impl.components.ScaleImpl;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Font;
import java.awt.Graphics;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

import static it.unibo.papasburgeria.view.impl.components.DrawingManagerImpl.EXTENSION;
import static it.unibo.papasburgeria.view.impl.components.DrawingManagerImpl.INGREDIENTS_X_SIZE_SCALE;
import static it.unibo.papasburgeria.view.impl.components.DrawingManagerImpl.INGREDIENTS_Y_SIZE_SCALE;

/**
 * Manages the GUI for the day changing scene in the game.
 */
public class DayChangeViewImpl extends AbstractBaseView {
    private static final double DAY_LABEL_X_POS = 0.5;
    private static final double DAY_LABEL_Y_POS = 0.15;
    private static final double DAY_LABEL_X_SIZE = 0.2;
    private static final double DAY_LABEL_Y_SIZE = 0.1;
    private static final double DAY_LABEL_ORIGIN = 0.5;

    private static final double UNLOCKED_INGREDIENTS_LABEL_X_POS = 0.5;
    private static final double UNLOCKED_INGREDIENTS_LABEL_Y_POS = 0.25;
    private static final double UNLOCKED_INGREDIENTS_LABEL_X_SIZE = 0.5;
    private static final double UNLOCKED_INGREDIENTS_LABEL_Y_SIZE = 0.1;
    private static final double UNLOCKED_INGREDIENTS_LABEL_ORIGIN = 0.5;

    private static final double UNLOCKED_INGREDIENTS_IMAGE_X_POS = 0.2;
    private static final double UNLOCKED_INGREDIENTS_IMAGE_Y_POS = 0.1 + UNLOCKED_INGREDIENTS_LABEL_Y_POS;
    private static final double UNLOCKED_INGREDIENTS_IMAGE_X_SIZE = INGREDIENTS_X_SIZE_SCALE;
    private static final double UNLOCKED_INGREDIENTS_IMAGE_Y_SIZE = INGREDIENTS_Y_SIZE_SCALE;
    private static final double UNLOCKED_INGREDIENTS_IMAGE_ORIGIN = 0.0;
    private static final double UNLOCKED_INGREDIENTS_IMAGE_X_SPACING = 0.05;
    private static final double UNLOCKED_INGREDIENTS_IMAGE_Y_SPACING = 0.05;

    private static final double NEW_DAY_BUTTON_X_POS = 0.5;
    private static final double NEW_DAY_BUTTON_Y_POS = 0.9;
    private static final double NEW_DAY_BUTTON_X_SIZE = 0.15;
    private static final double NEW_DAY_BUTTON_Y_SIZE = 0.075;
    private static final double NEW_DAY_BUTTON_ORIGIN = 0.5;

    private static final String DEFAULT_FONT_NAME = "Comic Sans MS";
    private static final int DEFAULT_FONT_SIZE = 40;
    private static final Font DEFAULT_FONT = new Font(DEFAULT_FONT_NAME, Font.BOLD, DEFAULT_FONT_SIZE);

    @Serial
    private static final long serialVersionUID = 1L;
    private final transient DayChangeController controller;
    private final transient ResourceService resourceService;
    /**
     * The JLabel for the day.
     */
    private final JLabel dayLabel;
    /**
     * The JLabel for the unlocked ingredients.
     */
    private final JLabel unlockedIngredientsLabel;
    /**
     * The list of JLabel for the unlocked ingredients.
     */
    private final List<JLabel> unlockedIngredientsLabels;

    /**
     * Default constructor, creates and initializes the GUI elements.
     *
     * @param resourceService the service that handles resource obtainment
     * @param gameController  the controller for the game
     * @param controller      the controller for the day change
     */
    @Inject
    public DayChangeViewImpl(
            final ResourceService resourceService,
            final GameController gameController,
            final DayChangeController controller
    ) {
        this.controller = controller;
        this.resourceService = resourceService;
        unlockedIngredientsLabels = new ArrayList<>();

        super.setStaticBackgroundImage(resourceService.getImage("day_change_background.png"));

        final JPanel interfacePanel = super.getInterfacePanel();
        interfacePanel.setLayout(new ScalableLayoutImpl());

        dayLabel = new JLabel();
        dayLabel.setFont(DEFAULT_FONT);
        interfacePanel.add(
                dayLabel,
                new ScaleConstraintImpl(
                        new ScaleImpl(DAY_LABEL_X_SIZE, DAY_LABEL_Y_SIZE),
                        new ScaleImpl(DAY_LABEL_X_POS, DAY_LABEL_Y_POS),
                        new ScaleImpl(DAY_LABEL_ORIGIN)
                )
        );

        unlockedIngredientsLabel = new JLabel();
        unlockedIngredientsLabel.setFont(DEFAULT_FONT);
        interfacePanel.add(
                unlockedIngredientsLabel,
                new ScaleConstraintImpl(
                        new ScaleImpl(UNLOCKED_INGREDIENTS_LABEL_X_SIZE, UNLOCKED_INGREDIENTS_LABEL_Y_SIZE),
                        new ScaleImpl(UNLOCKED_INGREDIENTS_LABEL_X_POS, UNLOCKED_INGREDIENTS_LABEL_Y_POS),
                        new ScaleImpl(UNLOCKED_INGREDIENTS_LABEL_ORIGIN)
                )
        );

        final JButton newDayButton = new JButton("New Day");
        newDayButton.setFont(DEFAULT_FONT);
        newDayButton.setBackground(DEFAULT_BUTTON_BACKGROUND_COLOR);
        newDayButton.setForeground(DEFAULT_BUTTON_TEXT_COLOR);
        newDayButton.setFocusPainted(false);
        newDayButton.addActionListener(e -> gameController.switchToScene(SceneType.REGISTER));
        interfacePanel.add(
                newDayButton,
                new ScaleConstraintImpl(
                        new ScaleImpl(NEW_DAY_BUTTON_X_SIZE, NEW_DAY_BUTTON_Y_SIZE),
                        new ScaleImpl(NEW_DAY_BUTTON_X_POS, NEW_DAY_BUTTON_Y_POS),
                        new ScaleImpl(NEW_DAY_BUTTON_ORIGIN)
                )
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void update(final double delta) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    void paintComponentDelegate(final Graphics g) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showScene() {
        dayLabel.setText("Current Day: " + controller.getCurrentDayNumber());
        unlockedIngredientsLabel.setText("Ingredients unlocked today:");
        loadUnlocks();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void hideScene() {
    }

    /**
     * Loads the sprites for the unlocked ingredients.
     */
    private void loadUnlocks() {
        final JPanel interfacePanel = super.getInterfacePanel();
        for (final JLabel label : unlockedIngredientsLabels) {
            interfacePanel.remove(label);
        }
        unlockedIngredientsLabels.clear();

        final List<IngredientEnum> unlockedIngredients = controller.getIngredientsUnlockedToday();
        if (unlockedIngredients.isEmpty()) {
            unlockedIngredientsLabel.setText("There are no ingredients unlocked today");
        }

        double pbPositionXScale = UNLOCKED_INGREDIENTS_IMAGE_X_POS;
        double pbPositionYScale = UNLOCKED_INGREDIENTS_IMAGE_Y_POS;
        for (final IngredientEnum ingredient : unlockedIngredients) {
            final ImageIcon iconImage =
                    new ImageIcon(resourceService.getImage(ingredient.getName() + EXTENSION));
            final JLabel imageLabel = new JLabel(iconImage);
            interfacePanel.add(
                    imageLabel,
                    new ScaleConstraintImpl(
                            new ScaleImpl(UNLOCKED_INGREDIENTS_IMAGE_X_SIZE, UNLOCKED_INGREDIENTS_IMAGE_Y_SIZE),
                            new ScaleImpl(pbPositionXScale, pbPositionYScale),
                            new ScaleImpl(UNLOCKED_INGREDIENTS_IMAGE_ORIGIN)
                    )
            );
            pbPositionXScale =
                    pbPositionXScale + UNLOCKED_INGREDIENTS_IMAGE_X_SIZE + UNLOCKED_INGREDIENTS_IMAGE_X_SPACING;
            if (pbPositionXScale + UNLOCKED_INGREDIENTS_IMAGE_X_SIZE > 1.0 - UNLOCKED_INGREDIENTS_IMAGE_X_POS) {
                pbPositionYScale =
                        pbPositionYScale + UNLOCKED_INGREDIENTS_IMAGE_Y_SIZE + UNLOCKED_INGREDIENTS_IMAGE_Y_SPACING;
                pbPositionXScale = UNLOCKED_INGREDIENTS_IMAGE_X_POS;
            }
            unlockedIngredientsLabels.add(imageLabel);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "DayChangeViewImpl{"
                + "controller=" + controller
                + ", resourceService=" + resourceService
                + ", dayLabel=" + dayLabel
                + ", unlockedIngredientsLabel=" + unlockedIngredientsLabel
                + ", unlockedIngredientsLabels=" + unlockedIngredientsLabels
                + '}';
    }
}

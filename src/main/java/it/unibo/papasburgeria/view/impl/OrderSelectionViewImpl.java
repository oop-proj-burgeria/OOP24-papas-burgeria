package it.unibo.papasburgeria.view.impl;

import com.google.inject.Inject;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.papasburgeria.controller.api.GameController;
import it.unibo.papasburgeria.controller.api.OrderSelectionController;
import it.unibo.papasburgeria.model.api.OrderModel;
import it.unibo.papasburgeria.utils.api.ResourceService;
import it.unibo.papasburgeria.utils.api.scene.SceneType;
import it.unibo.papasburgeria.view.api.components.Sprite;
import it.unibo.papasburgeria.view.api.components.SpriteDropListener;
import it.unibo.papasburgeria.view.impl.components.DrawingManagerImpl;
import it.unibo.papasburgeria.view.impl.components.ScalableLayoutImpl;
import it.unibo.papasburgeria.view.impl.components.ScaleConstraintImpl;
import it.unibo.papasburgeria.view.impl.components.ScaleImpl;
import it.unibo.papasburgeria.view.impl.components.SpriteDragManagerImpl;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.io.Serial;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import static it.unibo.papasburgeria.view.impl.BurgerAssemblyViewImpl.HALF_RANGE;
import static it.unibo.papasburgeria.view.impl.BurgerAssemblyViewImpl.HAMBURGER_Y_POS_SCALE;
import static it.unibo.papasburgeria.view.impl.components.DrawingManagerImpl.ORDER_X_SIZE_SCALE;
import static it.unibo.papasburgeria.view.impl.components.DrawingManagerImpl.ORDER_Y_SIZE_SCALE;

/**
 * Manages the GUI for the order selection scene in the game.
 */
@SuppressFBWarnings(
        value = {"EI_EXPOSE_REP2", "SE_TRANSIENT_FIELD_NOT_RESTORED"},
        justification = "The controller is injected and shared intentionally; "
                + "The views are not serialized at runtime"
)
public class OrderSelectionViewImpl extends AbstractBaseView implements SpriteDropListener {
    /**
     * Defines the x position of the selected order.
     */
    public static final double ORDER_SELECTED_X_POSITION = 0.1144;
    /**
     * Defines the y position of the selected order.
     */
    public static final double ORDER_SELECTED_Y_POSITION = 0.3487;

    @Serial
    private static final long serialVersionUID = 1L;
    private final transient OrderSelectionController controller;
    private final transient DrawingManagerImpl drawingManager;
    private final transient GameController gameController;
    private final transient List<Sprite> draggableOrderSprites;
    private final transient Map<Sprite, OrderModel> spriteOrders;

    /**
     * Default constructor, creates and initializes the GUI elements.
     *
     * @param resourceService the service that handles resource obtainment
     * @param controller      the order selection controller
     * @param drawingManager  the manager for drawing various things
     * @param gameController  the game controller
     */
    @Inject
    public OrderSelectionViewImpl(
            final ResourceService resourceService,
            final OrderSelectionController controller,
            final DrawingManagerImpl drawingManager,
            final GameController gameController
    ) {
        this.controller = controller;
        this.drawingManager = drawingManager;
        this.gameController = gameController;
        draggableOrderSprites = new ArrayList<>();
        spriteOrders = new IdentityHashMap<>();
        super.setStaticBackgroundImage(resourceService.getImage("order_selection_background.png"));

        final JButton backButton = new JButton(new ImageIcon(resourceService.getImage("back_arrow.png")));
        backButton.setBackground(DEFAULT_BACKGROUND_COLOR);
        backButton.setBorder(BorderFactory.createEmptyBorder());
        backButton.setContentAreaFilled(false);
        backButton.setOpaque(false);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> {
            controller.removeTopBun();
            gameController.switchToScene(SceneType.BURGER_ASSEMBLY);
        });

        final JPanel interfacePanel = super.getInterfacePanel();
        interfacePanel.setLayout(new ScalableLayoutImpl());
        super.getInterfacePanel().add(
                backButton,
                new ScaleConstraintImpl(
                        new ScaleImpl(ScaleConstraintImpl.EIGHTH, ScaleConstraintImpl.EIGHTH),
                        new ScaleImpl(ScaleConstraintImpl.SIXTEENTH, ScaleConstraintImpl.SIXTEENTH),
                        ScaleConstraintImpl.ORIGIN_CENTER
                )
        );

        readOrders();

        new SpriteDragManagerImpl(this, draggableOrderSprites, this);
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
    void paintComponentDelegate(final Graphics graphics) {
        drawingManager.drawHamburger(controller.getHamburger(), getSize(),
                HALF_RANGE, HAMBURGER_Y_POS_SCALE,
                new ArrayList<>(), graphics);

        for (final Sprite sprite : draggableOrderSprites) {
            final OrderModel order = spriteOrders.get(sprite);
            drawingManager.drawOrder(sprite, order, getSize(), graphics);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showScene() {
        readOrders();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void hideScene() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void spriteDropped(final Sprite sprite) {
        final double pbPositionXScale = sprite.getPbPositionXScale() + ORDER_X_SIZE_SCALE / 2;
        final double pbPositionYScale = sprite.getPbPositionYScale() + ORDER_Y_SIZE_SCALE / 2;

        if (pbPositionXScale < ORDER_SELECTED_X_POSITION + ORDER_X_SIZE_SCALE
                && pbPositionXScale > ORDER_SELECTED_X_POSITION
                && pbPositionYScale < ORDER_SELECTED_Y_POSITION + ORDER_Y_SIZE_SCALE
                && pbPositionYScale > ORDER_SELECTED_Y_POSITION
        ) {
            sprite.setPbPositionXScale(ORDER_SELECTED_X_POSITION);
            sprite.setPbPositionYScale(ORDER_SELECTED_Y_POSITION);
            controller.setSelectedOrder(spriteOrders.get(sprite));
            gameController.switchToScene(SceneType.EVALUATE_BURGER);
        } else {
            readOrders();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void spriteClicked(final Sprite sprite) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void spritePressed(final Sprite sprite) {

    }

    /**
     * Creates the sprites for the orders stored in the model.
     */
    private void readOrders() {
        final List<OrderModel> orders = controller.getOrders();
        draggableOrderSprites.clear();
        spriteOrders.clear();
        drawingManager.generateOrderSprites(orders, draggableOrderSprites, spriteOrders);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "OrderSelectionViewImpl{"
                + "controller=" + controller
                + ", drawingManager=" + drawingManager
                + ", gameController=" + gameController
                + ", draggableOrderSprites=" + draggableOrderSprites
                + ", spriteOrders=" + spriteOrders
                + '}';
    }
}

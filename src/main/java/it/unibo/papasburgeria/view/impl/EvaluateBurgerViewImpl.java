package it.unibo.papasburgeria.view.impl;

import com.google.inject.Inject;
import it.unibo.papasburgeria.controller.api.CustomerController;
import it.unibo.papasburgeria.controller.api.EvaluateBurgerController;
import it.unibo.papasburgeria.controller.api.GameController;
import it.unibo.papasburgeria.model.IngredientEnum;
import it.unibo.papasburgeria.model.api.CustomerModel;
import it.unibo.papasburgeria.model.api.HamburgerModel;
import it.unibo.papasburgeria.model.api.OrderModel;
import it.unibo.papasburgeria.model.impl.IngredientModelImpl;
import it.unibo.papasburgeria.utils.api.ResourceService;
import it.unibo.papasburgeria.utils.api.scene.SceneType;
import it.unibo.papasburgeria.view.api.components.DrawingManager;
import it.unibo.papasburgeria.view.api.components.Sprite;
import it.unibo.papasburgeria.view.impl.components.DrawingManagerImpl;
import it.unibo.papasburgeria.view.impl.components.ScalableLayoutImpl;
import it.unibo.papasburgeria.view.impl.components.ScaleConstraintImpl;
import it.unibo.papasburgeria.view.impl.components.ScaleImpl;
import it.unibo.papasburgeria.view.impl.components.SpriteImpl;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serial;
import java.util.ArrayList;

/**
 * the interface which contains the hamburger evaluation.
 */
public class EvaluateBurgerViewImpl extends AbstractBaseView {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Balance label width.
     */
    private static final double BALANCE_WIDTH = 0.33;
    /**
     * Balance label height.
     */
    private static final double BALANCE_HEIGHT = 0.25;
    /**
     * Balance label x coordinate.
     */
    private static final double BALANCE_X_POS = 0.0;
    /**
     * Balance label y coordinate.
     */
    private static final double BALANCE_Y_POS = 0.80;

    /**
     * Percentage label width.
     */
    private static final double PERCENTAGE_WIDTH = 0.15;
    /**
     * Percentage label height.
     */
    private static final double PERCENTAGE_HEIGHT = 0.1;
    /**
     * Percentage label x coordinate.
     */
    private static final double PERCENTAGE_X_POS = 0.77;
    /**
     * Percentage label y coordinate.
     */
    private static final double PERCENTAGE_Y_POS = 0.35;

    /**
     * Payment label width.
     */
    private static final double PAYMENT_WIDTH = 0.15;
    /**
     * Payment label height.
     */
    private static final double PAYMENT_HEIGHT = 0.1;
    /**
     * Payment label x coordinate.
     */
    private static final double PAYMENT_X_POS = 0.77;
    /**
     * Payment label y coordinate.
     */
    private static final double PAYMENT_Y_POS = 0.45;

    /**
     * Tip label width.
     */
    private static final double TIP_WIDTH = 0.15;
    /**
     * Tip label height.
     */
    private static final double TIP_HEIGHT = 0.1;
    /**
     * Tip label x coordinate.
     */
    private static final double TIP_X_POS = 0.77;
    /**
     * Tip label y coordinate.
     */
    private static final double TIP_Y_POS = 0.55;

    /**
     * Continue label width.
     */
    private static final double CONTINUE_WIDTH = 0.1;
    /**
     * Continue label height.
     */
    private static final double CONTINUE_HEIGHT = 0.1;
    /**
     * Continue label x coordinate.
     */
    private static final double CONTINUE_X_POS = 0.77;
    /**
     * Continue label y coordinate.
     */
    private static final double CONTINUE_Y_POS = 0.65;

    /**
     * Origin coordinate.
     */
    private static final double ORIGIN = 0.0;

    /**
     * String indicating currency.
     */
    private static final String MONEY = "$";

    /**
     * The font mainly used in this view.
     */
    private static final Font FONT = new Font("Comic Sans MS", Font.BOLD, 20);

    /**
     * Controller that manages the Hamburger Evaluation phase.
     */
    private final transient EvaluateBurgerController controller;
    /**
     * Manages the drawing of components.
     */
    private final transient DrawingManager drawingManager;
    /**
     * Gets resources such as images.
     */
    private final transient ResourceService resourceService;
    /**
     * Controller that manages customers.
     */
    private final transient CustomerController customerController;

    /**
     * Label used for displaying the player's balance.
     */
    private final JLabel showMoneyLabel;
    /**
     * Label used for displaying percentageLabel.
     */
    private final JLabel percentageLabel;
    /**
     * Label used for displaying payment.
     */
    private final JLabel paymentLabel;
    /**
     * Label used for displaying tips.
     */
    private final JLabel tipLabel;

    /**
     * Main panel used in this view.
     */
    private final JPanel interfacePanel;
    /**
     * Hamburger read and displayed in this view.
     */
    private transient HamburgerModel burger;
    /**
     * Order read and displayed in this view.
     */
    private transient OrderModel order;

    @Inject
    EvaluateBurgerViewImpl(final EvaluateBurgerController controller,
                           final DrawingManager drawingManager,
                           final ResourceService resourceService,
                           final GameController gameController,
                           final CustomerController customerController) {

        this.controller = controller;
        this.drawingManager = drawingManager;
        this.resourceService = resourceService;
        this.customerController = customerController;

        this.interfacePanel = super.getInterfacePanel();
        interfacePanel.setLayout(new ScalableLayoutImpl());
        super.setStaticBackgroundImage(resourceService.getImage("order_evaluation_background.png"));

        final JButton continueButton = new JButton("NEXT");
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                controller.emptyHamburgerOnAssembly();
                if (customerController.isCustomerThreadStatus()
                        || !customerController.getWaitLine().isEmpty()
                        || !customerController.getRegisterLine().isEmpty()) {
                    gameController.switchToScene(SceneType.REGISTER);
                } else {
                    gameController.switchToScene(SceneType.SHOP);
                }
            }
        });

        interfacePanel.add(continueButton,
                new ScaleConstraintImpl(
                        new ScaleImpl(CONTINUE_WIDTH, CONTINUE_HEIGHT),
                        new ScaleImpl(CONTINUE_X_POS, CONTINUE_Y_POS),
                        new ScaleImpl(ORIGIN)
                )
        );
        continueButton.setFont(FONT);

        showMoneyLabel = new JLabel("BAL: 0" + MONEY);
        interfacePanel.add(showMoneyLabel,
                new ScaleConstraintImpl(
                        new ScaleImpl(BALANCE_WIDTH, BALANCE_HEIGHT),
                        new ScaleImpl(BALANCE_X_POS, BALANCE_Y_POS),
                        new ScaleImpl(ORIGIN)
                )
        );
        showMoneyLabel.setFont(FONT);

        percentageLabel = new JLabel("SCORE: 0%");
        interfacePanel.add(percentageLabel,
                new ScaleConstraintImpl(
                        new ScaleImpl(PERCENTAGE_WIDTH, PERCENTAGE_HEIGHT),
                        new ScaleImpl(PERCENTAGE_X_POS, PERCENTAGE_Y_POS),
                        new ScaleImpl(ORIGIN)
                )
        );
        percentageLabel.setFont(FONT);

        paymentLabel = new JLabel("PAY: 0" + MONEY);
        interfacePanel.add(paymentLabel,
                new ScaleConstraintImpl(
                        new ScaleImpl(PAYMENT_WIDTH, PAYMENT_HEIGHT),
                        new ScaleImpl(PAYMENT_X_POS, PAYMENT_Y_POS),
                        new ScaleImpl(ORIGIN)
                )
        );
        paymentLabel.setFont(FONT);

        tipLabel = new JLabel("TIP: 0" + MONEY);
        interfacePanel.add(tipLabel,
                new ScaleConstraintImpl(
                        new ScaleImpl(TIP_WIDTH, TIP_HEIGHT),
                        new ScaleImpl(TIP_X_POS, TIP_Y_POS),
                        new ScaleImpl(ORIGIN)
                )
        );
        tipLabel.setFont(FONT);

        interfacePanel.validate();
    }

    /**
     * Reads the burger and the order.
     */
    private void read() {
        burger = controller.getHamburgerOnAssembly();
        order = controller.getSelectedOrder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showScene() {
        showMoneyLabel.setText("BAL: " + customerController.getBalance() + MONEY);

        read();
        final double satisfaction = customerController.calculateSatisfactionPercentage(
                this.order.getHamburger().copyOf(), this.burger);

        final int payment = customerController.calculatePayment(satisfaction);
        final int tip = customerController.calculateTips(payment);

        customerController.addBalance(payment + tip);

        for (final CustomerModel currentCustomer : customerController.getWaitLine()) {
            if (currentCustomer.getOrder().getOrderNumber() == this.order.getOrderNumber()) {
                customerController.serveCustomer(currentCustomer);
            }
        }
        percentageLabel.setText("score: " + (int) (satisfaction * 100) + "%");
        paymentLabel.setText("pay: " + payment + MONEY);
        tipLabel.setText(" +tip " + tip + MONEY);
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
    void update(final double delta) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    void paintComponentDelegate(final Graphics g) {
        drawingManager.drawHamburger(burger,
                getSize(), BurgerAssemblyViewImpl.HALF_RANGE,
                BurgerAssemblyViewImpl.HAMBURGER_Y_POS_SCALE, new ArrayList<>(), g);

        final Sprite orderSprite = new SpriteImpl(resourceService.getImage("order.png"),
                new IngredientModelImpl(IngredientEnum.CHEESE),
                OrderSelectionViewImpl.ORDER_SELECTED_X_POSITION,
                OrderSelectionViewImpl.ORDER_SELECTED_Y_POSITION,
                DrawingManagerImpl.ORDER_X_SIZE_SCALE,
                DrawingManagerImpl.ORDER_Y_SIZE_SCALE);
        drawingManager.drawOrder(orderSprite, order, getSize(), g);
        interfacePanel.revalidate();
    }
}

package it.unibo.papasburgeria.model.impl;

import it.unibo.papasburgeria.model.UpgradeEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for {@link ShopModelImpl}.
 */
class ShopModelImplTest {

    private ShopModelImpl shop;

    /**
     * Sets up a new ShopModelImpl instance before each test.
     */
    @BeforeEach
    void setUp() {
        shop = new ShopModelImpl();
    }

    /**
     * Tests if initial upgrades are locked.
     */
    @Test
    void testInitialUpgradesAreLocked() {
        for (final UpgradeEnum upgrade : UpgradeEnum.values()) {
            assertFalse(shop.isUpgradeUnlocked(upgrade), "Upgrade should initially be locked: " + upgrade);
        }
    }

    /**
     * Tests {@link ShopModelImpl#unlockUpgrade(UpgradeEnum)}.
     */
    @Test
    void testUnlockUpgrade() {
        final UpgradeEnum upgrade = UpgradeEnum.CUSTOMER_MORE_TIP;
        shop.unlockUpgrade(upgrade);
        assertTrue(shop.isUpgradeUnlocked(upgrade),
                "Upgrade should be unlocked after calling unlockUpgrade.");
    }

    /**
     * Tests {@link ShopModelImpl#lockUpgrade(UpgradeEnum)}.
     */
    @Test
    void testLockUpgrade() {
        final UpgradeEnum upgrade = UpgradeEnum.CUSTOMER_MORE_TIP;
        shop.unlockUpgrade(upgrade);
        shop.lockUpgrade(upgrade);
        assertFalse(shop.isUpgradeUnlocked(upgrade),
                "Upgrade should be locked after calling lockUpgrade.");
    }

    /**
     * Tests {@link ShopModelImpl#unlockAllUpgrades()}.
     */
    @Test
    void testUnlockAllUpgrades() {
        shop.unlockAllUpgrades();
        for (final UpgradeEnum upgrade : UpgradeEnum.values()) {
            assertTrue(shop.isUpgradeUnlocked(upgrade), "All upgrades should be unlocked.");
        }
    }

    /**
     * Tests {@link ShopModelImpl#lockAllUpgrades()}.
     */
    @Test
    void testLockAllUpgrades() {
        shop.unlockAllUpgrades();
        shop.lockAllUpgrades();
        for (final UpgradeEnum upgrade : UpgradeEnum.values()) {
            assertFalse(shop.isUpgradeUnlocked(upgrade), "All upgrades should be locked.");
        }
    }

    /**
     * Tests {@link ShopModelImpl#getUpgradeModifier(UpgradeEnum)}.
     */
    @Test
    void testGetUpgradeModifier() {
        final UpgradeEnum upgrade = UpgradeEnum.CUSTOMER_TIP;
        final double differentValue = 0.0001;
        assertEquals(0, shop.getUpgradeModifier(upgrade), "Modifier should be 0 when upgrade is locked.");
        shop.unlockUpgrade(upgrade);
        assertEquals(upgrade.getModifier(), shop.getUpgradeModifier(upgrade), differentValue,
                "Modifier should match upgrade's modifier after unlocking.");
    }

    /**
     * Tests {@link ShopModelImpl#getUpgrades()}.
     */
    @Test
    void testGetUpgradesReturnsCopy() {
        final Map<UpgradeEnum, Boolean> upgradesCopy = shop.getUpgrades();
        assertNotSame(upgradesCopy, shop.getUpgrades(),
                "getUpgrades() should return a copy.");
        upgradesCopy.put(UpgradeEnum.PLACEMENT_TOLERANCE, true); // This should not affect the internal state
        assertFalse(shop.isUpgradeUnlocked(UpgradeEnum.PLACEMENT_TOLERANCE),
                "Internal state should not be changed by modifying the returned map.");
    }
}

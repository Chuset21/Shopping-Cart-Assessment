import com.chuset.Pricer;
import com.chuset.ShoppingCart;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ShoppingCartTest {

    @Test
    public void testDefaultConstructor() {
        new ShoppingCart();
    }

    @Test
    public void testParameterizedConstructor() {
        new ShoppingCart(new Pricer());

        try {
            new ShoppingCart(null);
            fail("IllegalArgumentException was not thrown with pricer = null in parameterized constructor, unexpected behavior.");
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void canAddAnItem() {
        ShoppingCart sc = new ShoppingCart(new Pricer());

        sc.addItem("apple", 1);

        final ByteArrayOutputStream myOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(myOut));

        sc.printReceipt();
        assertEquals(String.format("apple - 1 - €1.00%ntotal - 1 - €1.00%n"), myOut.toString());
    }

    @Test
    public void canAddMoreThanOneItem() {
        ShoppingCart sc = new ShoppingCart(new Pricer());

        sc.addItem("apple", 2);

        final ByteArrayOutputStream myOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(myOut));

        sc.printReceipt();
        assertEquals(String.format("apple - 2 - €2.00%ntotal - 2 - €2.00%n"), myOut.toString());
    }

    @Test
    public void canAddDifferentItems() {
        final ShoppingCart sc = new ShoppingCart(new Pricer());

        sc.addItem("apple", 2);
        sc.addItem("banana", 1);

        final ByteArrayOutputStream myOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(myOut));

        sc.printReceipt();

        final String result = myOut.toString();

        if (result.startsWith("apple")) {
            assertEquals(String.format("apple - 2 - €2.00%nbanana - 1 - €2.00%ntotal - 3 - €4.00%n"), result);
        } else {
            assertEquals(String.format("banana - 1 - €2.00%napple - 2 - €2.00%ntotal - 3 - €4.00%n"), result);
        }
    }

    @Test
    public void doesntExplodeOnMysteryItem() {
        ShoppingCart sc = new ShoppingCart(new Pricer());

        sc.addItem("crisps", 2);

        final ByteArrayOutputStream myOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(myOut));

        sc.printReceipt();
        assertEquals(String.format("crisps - 2 - €0.00%ntotal - 2 - €0.00%n"), myOut.toString());
    }

    @Test
    public void outputsCorrectOrder() {
        final ShoppingCart sc = new ShoppingCart(new Pricer());

        sc.addItem("apple", 2);
        sc.addItem("banana", 1);

        final ByteArrayOutputStream myOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(myOut));

        sc.printReceipt();
        assertEquals(String.format("apple - 2 - €2.00%nbanana - 1 - €2.00%ntotal - 3 - €4.00%n"), myOut.toString());
    }

    @Test
    public void testNullProductName() {
        final ShoppingCart sc = new ShoppingCart(new Pricer());

        try {
            sc.addItem(null, 2);
            fail("IllegalArgumentException was not thrown with itemType = null in #addItem, unexpected behavior.");
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testNumberOfItemsLessThanOne() {
        final ShoppingCart sc = new ShoppingCart(new Pricer());

        try {
            sc.addItem("apple", 0);
            fail("UnsupportedOperationException was not thrown with number = 0 in #addItem, unexpected behavior.");
        } catch (UnsupportedOperationException ignored) {}

        try {
            sc.addItem("apple", -1);
            fail("UnsupportedOperationException was not thrown with number = -1 in #addItem, unexpected behavior.");
        } catch (UnsupportedOperationException ignored) {}
    }

    @Test
    public void testFormatting() {
        final ShoppingCart sc = new ShoppingCart();

        sc.addItem("apple", 2);
        sc.addItem("banana", 1);

        ByteArrayOutputStream myOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(myOut));

        sc.printReceipt(ShoppingCart.Formatting.NAME_AMOUNT_PRICE);
        assertEquals(String.format("apple - 2 - €2.00%nbanana - 1 - €2.00%ntotal - 3 - €4.00%n"), myOut.toString());

        myOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(myOut));

        sc.printReceipt(ShoppingCart.Formatting.PRICE_AMOUNT_NAME);
        assertEquals(String.format("€2.00 - 2 - apple%n€2.00 - 1 - banana%n€4.00 - 3 - total%n"), myOut.toString());

        myOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(myOut));

        sc.printReceipt(ShoppingCart.Formatting.PRICE_NAME_AMOUNT);
        assertEquals(String.format("€2.00 - apple - 2%n€2.00 - banana - 1%n€4.00 - total - 3%n"), myOut.toString());
    }
}
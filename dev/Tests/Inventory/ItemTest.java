package Tests.Inventory;

import BusinessLayer.Inventory.Discount;
import BusinessLayer.Inventory.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemTest {
    Item item;

    @BeforeEach
    void before(){
        item = new Item(1, "testItem", 20, 10, 1, "tester", 7, 15, 20);
    }

    @Test
    void getPrice() {
        assertEquals(item.getPrice(), 20);
    }

    @Test
    void getCost() {
        assertEquals(item.getCost(), 10);
    }

    @Test
    void priceDisc() {
        Discount d = new Discount(LocalDate.now().minusDays(1), LocalDate.now().plusDays(1),20);
        Discount d2 = new Discount(LocalDate.now().minusDays(10), LocalDate.now().minusDays(5), 50);
        item.addPriceDiscount(d2);
        assertEquals(item.getPrice(), 20);
        item.addPriceDiscount(d);
        assertEquals(item.getPrice(), 16);
    }

    @Test
    void costDisc() {
        Discount d = new Discount(LocalDate.now().minusDays(1), LocalDate.now().plusDays(1),20);
        Discount d2 = new Discount(LocalDate.now().minusDays(10), LocalDate.now().minusDays(5), 50);
        item.addCostDiscount(d2);
        assertEquals(item.getCost(), 10);
        item.addCostDiscount(d);
        assertEquals(item.getCost(), 8);
    }

}
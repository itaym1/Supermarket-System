package Tests.Inventory;

import BusinessLayer.Inventory.Category;
import BusinessLayer.Inventory.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CategoryTest {
    Category cat;

    @BeforeEach
    void before(){
        cat = new Category("test");
        cat.addItem(1, "testItem", 20, 10, 1, "tester", 7, 15, 20);
    }
    @Test
    void getItem() {
        assertEquals(cat.getItem(1).getId(), 1);
        assertNull(cat.getItem(1000));
    }

    @Test
    void getSubCat() {
        assertNull(cat.getCategory("subCat"));
        Category subCat = new Category("subCat");
        cat.addSubCategory(subCat);
        assertEquals(cat.getCategory("subCat"), subCat);
    }

    @org.junit.jupiter.api.Test
    void addItem() {
        assertNull(cat.getItem(2));
        cat.addItem(2, "testItem2", 20, 10, 1, "tester", 7, 15, 20);
        assertEquals(cat.getItem(2).getId(), 2);
    }

    @org.junit.jupiter.api.Test
    void removeItem() {
        Item i = cat.getItem(1);
        assertEquals(cat.getItem(1), i);
        cat.removeItem(1);
        i = cat.getItem(1);
        assertNull(i);
    }

    @org.junit.jupiter.api.Test
    void addSubCategory() {
        assertNull(cat.getCategory("subTest"));
        Category subCat = new Category("subTest");
        cat.addSubCategory(subCat);
        assertEquals(cat.getCategory("subTest"), subCat);
    }

    @Test
    void addCatDiscount() {
        Item i = cat.getItem(1);
        cat.addItem(2, "testItem2", 10, 10, 1, "tester", 7, 15, 20);
        Item i2 = cat.getItem(2);
        assertEquals(i.getPrice(), 20);
        cat.addCategoryDiscount(LocalDate.now().minusDays(10), LocalDate.now().minusDays(2), 50);
        assertEquals(i.getPrice(), 20);
        cat.addCategoryDiscount(LocalDate.now().minusDays(2), LocalDate.now().plusDays(2), 50);
        assertEquals(i.getPrice(), 10);
        assertEquals(i2.getPrice(), 5);
    }

    @Test
    void addItemDiscount() {
        Item i = cat.getItem(1);
        cat.addItem(2, "testItem2", 10, 10, 1, "tester", 7, 15, 20);
        Item i2 = cat.getItem(2);
        assertEquals(i.getPrice(), 20);
        assertEquals(i2.getPrice(), 10);
        cat.addItemDiscount(LocalDate.now().minusDays(2), LocalDate.now().plusDays(2), 50, 2);
        assertEquals(i.getPrice(), 20);
        assertEquals(i2.getPrice(), 5);
    }

    @Test
    void addManuDiscount(){
        Item i = cat.getItem(1);
        assertEquals(i.getCost(), 10);
        cat.addManuDiscount(LocalDate.now().minusDays(2), LocalDate.now().plusDays(2), 50, 1);
        assertEquals(i.getCost(), 5);
    }
}
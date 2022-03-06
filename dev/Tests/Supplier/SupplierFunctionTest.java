package Tests.Supplier;

import BusinessLayer.Supplier.ProductController;
import BusinessLayer.Supplier.SupplierCard;
import BusinessLayer.Supplier.SupplierController;
import org.junit.Assert;

import java.util.HashMap;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class SupplierFunctionTest {
    SupplierController sc = SupplierController.getInstance();
    ProductController pc = ProductController.getInstance();
    SupplierCard supCard;

    @org.junit.Before
    public void setUp() throws Exception {
        sc.createSupCard("Sahar", 1, "Raanana", "kalifa@gmail.com" , 45802000,
                "credit card","shimon 052-6093400" , "Sunday", false);;
        supCard = sc.getSuppliers().get(1);

        sc.addProductToSupplier(1,800,"Bamba","Snacks",3.5,2);
        sc.addProductToSupplier(1,801,"Bisly","Snacks",4.5,2);
        sc.addProductToSupplier(1,802,"Apropo","Snacks",3,2);

    }

    @org.junit.After
    public void tearDown() throws Exception {
        if(sc.getSuppliers().get(supCard.getSupplierID()) != null)
            sc.deleteSupCard(1);
    }

    @org.junit.Test
    public void addBillOfQuantity() {
        assertNull(pc.getDiscounts().get(supCard.getSupplierID()));
        try{
            HashMap<Integer,Integer> minQuantity = new HashMap<>();
            HashMap<Integer,Integer> discounts = new HashMap<>();
            pc.addBillOfQuantity(supCard.getSupplierID(),minQuantity,discounts);
        }catch (Exception e){
            fail("Exception: " + e.getMessage());
        }
        assertFalse(!pc.getDiscounts().get(supCard.getSupplierID()).getDiscountList().isEmpty());
    }

    @org.junit.Test
    public void deleteSupCard() {
        assertTrue(sc.getSuppliers().get(supCard.getSupplierID()) != null); //show he exist
        try{
            sc.deleteSupCard(supCard.getSupplierID());
        }catch (Exception e){
            fail("Exception: " + e.getMessage());
        }
        assertNull(sc.getSuppliers().get(supCard.getSupplierID())); //Show Does Not Exist
    }

    @org.junit.Test
    public void deleteBillOfQuantity() {
        HashMap<Integer,Integer> minQuantity = new HashMap<>();
        HashMap<Integer,Integer> discounts = new HashMap<>();
        pc.addBillOfQuantity(supCard.getSupplierID(),minQuantity,discounts);
        assertFalse(!pc.getDiscounts().get(supCard.getSupplierID()).getDiscountList().isEmpty());
        try{
            pc.deleteBillOfQuantity(supCard.getSupplierID());
        } catch (Exception e){
            fail("Exception: " + e.getMessage());
        }
        assertNull(pc.getDiscounts().get(supCard.getSupplierID()));
    }

    @org.junit.Test
    public void removeProductToSupplier() {
        assertFalse(pc.getSupplierProd().isEmpty());

        try{
            sc.removeProductToSupplier(1 ,801);
        }catch (Exception e){
            fail("Exception "+ e.getMessage());
        }

        assertFalse(pc.getSupplierProd().containsKey(801));
    }

    @org.junit.Test
    public void editSupplierName() {
        try{
            sc.EditSupplierName(1 , "Batya");
        }catch (Exception e){
            fail("Exception: " + e.getMessage());
        }

        assertEquals(supCard.getSupplierName(),"Batya");
        Assert.assertNotEquals(supCard.getSupplierName(), "Sahar");
    }

    @org.junit.Test
    public void EditPickup() {
        try{
            sc.EditPickup(1 , true);
        }catch (Exception e){
            fail("Exception: " + e.getMessage());
        }

        assertEquals(supCard.isPickUp(),true);
        Assert.assertNotEquals(supCard.isPickUp(), false);
    }
}
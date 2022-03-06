package Tests.Supplier;
import BusinessLayer.Supplier.BillOfQuantities;
import BusinessLayer.Supplier.Order;
import BusinessLayer.Supplier.OrderController;
import BusinessLayer.Supplier.Product;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class OrderFunctionsTest {
    OrderController oc;
    Order order;
    Product product;
    int orderNum;
    HashMap<Integer, Integer> products;
    BillOfQuantities billOfQ;

    @Before
    public void setUp() throws Exception {
        oc = OrderController.getInstance();
        product = new Product(800,1,"Bamba","Snacks", 3.5,2);
        products = new HashMap<>();
        products.put(800,100);
        orderNum = oc.nextOrderID;
        order = new Order(orderNum,1,false,products);
        oc.getOrders().put(orderNum,order);

        HashMap<Integer,Integer> minQuantity = new HashMap<>();
        HashMap<Integer,Integer> discounts = new HashMap<>();
        billOfQ = new BillOfQuantities(minQuantity,discounts);

        oc.getProdController().getDiscounts().put(1,billOfQ);
    }

    @After
    public void tearDown() throws Exception {
        product = null;
        order = null;
        products = null;
        orderNum = -1;
        billOfQ = null;
        oc.getOrders().remove(orderNum);
    }

    @Test
    public void removeOrder() {
        try{
            oc.removeOrder(orderNum);
        }catch (Exception e){
            fail("Exception: "+ e.getMessage());
        }
        assertNull(oc.getOrders().get(orderNum));
    }


    @Test
    public void updateProdQuantity() {
        assertTrue(products.get(800) == 100);
        try{
            oc.updateProdQuantity(orderNum,800,200);
        }catch (Exception e){
            fail("Exception: "+ e.getMessage());
        }
        assertTrue(products.get(800) == 200);
    }

    @Test
    public void addProdToBill() {
        assertTrue(billOfQ.getDiscountList().isEmpty());
        assertTrue(billOfQ.getMinQuantityForDis().isEmpty());
        try{
            oc.addProdToBill(1,800,100,10);
        }catch (Exception e){
            fail("Exception: "+ e.getMessage());
        }
        assertTrue(billOfQ.getDiscountList().get(800) != null);
        assertTrue(billOfQ.getDiscountList().get(800) == 10);
        assertTrue(billOfQ.getMinQuantityForDis().get(800) == 100);
    }

    @Test
    public void removeProdFromBill() {
        oc.addProdToBill(1,800,100,10);
        try{
            oc.removeProdFromBill(1,800);
        }catch (Exception e){
            fail("Exception: "+ e.getMessage());
        }
        assertTrue(billOfQ.getDiscountList().isEmpty());
        assertTrue(billOfQ.getMinQuantityForDis().isEmpty());

    }
}
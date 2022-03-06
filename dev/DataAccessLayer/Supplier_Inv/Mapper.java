package DataAccessLayer.Supplier_Inv;

import BusinessLayer.Supplier.Order;
import BusinessLayer.Inventory.*;
import BusinessLayer.Response;
import BusinessLayer.ResponseT;
import BusinessLayer.Supplier.*;
import DataAccessLayer.Supplier_Inv.DAO.*;
import DataAccessLayer.Supplier_Inv.DTO.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Mapper {
    /**
     * This class holds the identity maps of Employees and Shifts.
     * Any access to the data base goes through here.
     */
    private BillOfQuantityDAO billOfQuantityDAO;
    private CategoryDAO categoryDAO;
    private OrderDAO orderDAO;
    private PeriodicOrderDAO periodicOrderDAO;
    private ProductsInOrderDAO productsInOrderDAO;
    private ProductsOfSupplierDAO productsOfSupplierDAO;
    private SupplierDAO supplierDAO;
    private ItemDAO itemDAO;
    private DiscountDAO priceDisDAO;
    private DiscountDAO costDisDAO;
    private SaleDAO saleDAO;
    private FaultyItemDAO faultyItemDAO;
    private ProductsInPeriodocDAO productsInPeriodicDAO;

    public void editQuantityInPeriodic(int orderID, int productID, int quant) {
        productsInPeriodicDAO.editQuantityInPeriodic(orderID, productID, quant);
    }

    public void deleteProductFromOrder(int productID, int orderID) {
        productsInOrderDAO.delete(orderID, productID);
    }

    public void updateProductQuantInOrder(int orderID, int productID, int quantity) {
        productsInOrderDAO.updateQuantity(orderID, productID, quantity);
    }


    private static class MapperHolder {
        private static Mapper instance = new Mapper();
    }

    private Mapper() {
        billOfQuantityDAO = new BillOfQuantityDAO();
        categoryDAO = new CategoryDAO();
        orderDAO = new OrderDAO();
        periodicOrderDAO = new PeriodicOrderDAO();
        productsInOrderDAO = new ProductsInOrderDAO();
        productsOfSupplierDAO = new ProductsOfSupplierDAO();
        supplierDAO = new SupplierDAO();
        itemDAO = new ItemDAO();
        priceDisDAO = new DiscountDAO("itemPriceDiscount");
        costDisDAO = new DiscountDAO("itemCostDiscount");
        saleDAO = new SaleDAO();
        faultyItemDAO = new FaultyItemDAO();
        productsInPeriodicDAO = new ProductsInPeriodocDAO();
    }

    public static Mapper getInstance() {
        return MapperHolder.instance;
    }


    public ResponseT<CategoryDTO> addSubCategory(Category cat, String superName) {
        ResponseT<CategoryDTO> catDto = categoryDAO.create(cat);
        if(catDto.ErrorOccured() || categoryDAO.createSubCategory(cat, superName).ErrorOccured()) {
            return new ResponseT<>("could not add sub category");
        }
        return new ResponseT<>(catDto.value);
    }

    public ResponseT<CategoryDTO> addCategory(Category cat) {
        ResponseT<CategoryDTO> catDto = categoryDAO.create(cat);
        if(catDto.ErrorOccured()) {
            return new ResponseT<>("could not add sub category");
        }
        return new ResponseT<>(catDto.value);
    }

    public ResponseT<ItemDTO> addItem(Item i, Category c) {
        ResponseT<ItemDTO> itemRes = itemDAO.create(i);
        if(itemRes.ErrorOccured() || categoryDAO.createCategoryItems(c, i.getId()).ErrorOccured()) {
            return new ResponseT<>("Could not add Item");
        }
        return new ResponseT<>(itemRes.value);
    }

    public Response deleteItem(Item item, Category cat) {
        if(itemDAO.delete(item).ErrorOccured() || categoryDAO.deleteCategoryItems(cat, item.getId()).ErrorOccured()){
            return new Response("error deleting item");
        }
        return new Response();
    }

    public Response deleteOrder(int orderID) {
        if(orderDAO.delete(orderID).ErrorOccured() || productsInOrderDAO.deleteOrder(orderID).ErrorOccured()){
            return new Response("error deleting item");
        }
        return new Response();

    }
    public ResponseT<ItemDTO> updateItem(Item item) {
        ResponseT<ItemDTO> resItem = itemDAO.update(item);
        if(resItem.ErrorOccured()) {
            return new ResponseT<>("cannot update item");
        }
        return new ResponseT<>(resItem.value);
    }

    public ResponseT<DiscountDTO> addPriceDiscount(Discount d, int id) {
        ResponseT<DiscountDTO> resDis = priceDisDAO.create(d,id);
        if (resDis.ErrorOccured()) {
            return new ResponseT<>("cannot add discount");
        }
        return new ResponseT<>(resDis.value);
    }

    public ResponseT<DiscountDTO> addCostDiscount(Discount d, int id) {
        ResponseT<DiscountDTO> resDis = costDisDAO.create(d,id);
        if (resDis.ErrorOccured()) {
            return new ResponseT<>("cannot add discount");
        }
        return new ResponseT<>(resDis.value);
    }

    public ResponseT<SaleDTO> addSale(Sale newSale) {
        return saleDAO.create(newSale);
    }

    public ResponseT<FaultyItemDTO> addFaulty(FaultyItem newFI) {
        return faultyItemDAO.create(newFI);
    }


    //    public ResponseT<SupplierDTO> getSupplier(Integer supplierID) {
//        if (suppliers.containsKey(supplierID))
//            return new ResponseT<>(suppliers.get(supplierID));
//        ResponseT<SupplierDTO> sup = supplierDAO.get(supplierID);
//        if (!sup.ErrorOccured())
//            suppliers.put(supplierID, sup.value);
//        return sup;
//    }
    public ResponseT<HashMap<Integer, SupplierCard>> loadSupplierCard() {
        ResponseT<List<SupplierDTO>> supplierRes = supplierDAO.read();
        HashMap<Integer, SupplierCard> res = new HashMap<>();
        if (!supplierRes.ErrorOccured()) {
            for (SupplierDTO dbSupplier : supplierRes.value) {
                res.put(dbSupplier.getID(), new SupplierCard(dbSupplier.getName(), dbSupplier.getID(), dbSupplier.getAddress(),
                        dbSupplier.getEmail(), dbSupplier.getBankAcc(), dbSupplier.getPaymentMethod(),
                        dbSupplier.getContacts(), dbSupplier.getInfoSupDay(), dbSupplier.isPickUp()));
            }
        } else {
            return new ResponseT<>("Could not load Supplier cards");
        }
        return new ResponseT<>(res);
    }

    public ResponseT<HashMap<Integer,BillOfQuantities>> loadBillsOfQuantity() {
        ResponseT<List<BillOfQuantityDTO>> billRes = billOfQuantityDAO.read();
        HashMap<Integer,BillOfQuantities> res = new HashMap<>();
        if (!billRes.ErrorOccured()) {
            for (BillOfQuantityDTO dbBill : billRes.value) {
                int supplierID = dbBill.getSupplierID();
                if (res.containsKey(supplierID)) {
                    res.get(supplierID).getMinQuantityForDis().put(dbBill.getProductID(), dbBill.getMinQuantity());
                    res.get(supplierID).getDiscountList().put(dbBill.getProductID(), dbBill.getPercentDiscount());
                } else {
                    HashMap<Integer, Integer> min = new HashMap<>();
                    min.put(dbBill.getProductID(), dbBill.getMinQuantity());
                    HashMap<Integer, Integer> dis = new HashMap<>();
                    dis.put(dbBill.getProductID(), dbBill.getPercentDiscount());
                    res.put(supplierID, new BillOfQuantities(min, dis));
                }
            }
        } else {
            return new ResponseT<>("Could not load Bills Of Quantity");
        }
        return new ResponseT<>(res);
    }

    public ResponseT<HashMap<Integer, Order>> loadOrders() {
        ResponseT<HashMap<Integer, OrderDTO>> orderRes = orderDAO.read();
        HashMap<Integer,Order> res = new HashMap<>();
        if (!orderRes.ErrorOccured()) {
            for (OrderDTO dbOrder : orderRes.value.values()) {
                HashMap<Integer,Integer> productsInOrder = productsInOrderDAO.getProductsFromOrder(dbOrder.getOrderID()).value; //all products from specific order
                res.put(dbOrder.getOrderID(), new Order(dbOrder.getOrderID(), dbOrder.getSupplierID(), dbOrder.isDelivered(), productsInOrder));
            }
        } else {
            return new ResponseT<>("Could not load orders");
        }
        return new ResponseT<>(res);
    }

    public ResponseT<HashMap<Integer, PeriodicOrder>> loadPeriodic() {
        ResponseT<HashMap<Integer, PeriodicOrderDTO>> perOrderRes = periodicOrderDAO.read();
        HashMap<Integer, PeriodicOrder> res = new HashMap<>();
        if (!perOrderRes.ErrorOccured()) {
            for (PeriodicOrderDTO dbPeriodic : perOrderRes.value.values()) {
                HashMap<Integer,Integer> productsInOrder = periodicOrderDAO.getProductsFromPeriod(dbPeriodic.getOrderID()).value; //all products from specific order
                if (productsInOrder == null) productsInOrder = new HashMap<>();
                res.put(dbPeriodic.getOrderID(),new PeriodicOrder(dbPeriodic.getOrderID(), dbPeriodic.getIntervals(), dbPeriodic.getSupplyDate(), productsInOrder));
            }
        } else {
            return new ResponseT<>("Could not load periodic orders");
        }
        return new ResponseT<>(res);
    }

    public ResponseT<HashMap<Integer,HashMap<Integer,Product>>> loadProducts() {
        ResponseT<List<ProductsOfSupplierDTO>> productRes = productsOfSupplierDAO.read();
        HashMap<Integer,HashMap<Integer, Product>> res = new HashMap<>();
        if (!productRes.ErrorOccured()) {
            for (ProductsOfSupplierDTO dbProdOfSupp : productRes.value) {
                if (!res.containsKey(dbProdOfSupp.getSupplierID())) {
                    res.put(dbProdOfSupp.getSupplierID(), new HashMap<>());
                }
            }
            for (ProductsOfSupplierDTO dbProdOfSupp : productRes.value) {
                res.get(dbProdOfSupp.getSupplierID()).put(dbProdOfSupp.getProductID(), new Product(dbProdOfSupp.getProductID(), dbProdOfSupp.getSupplierID(), dbProdOfSupp.getName(),
                        dbProdOfSupp.getCategory(),dbProdOfSupp.getPrice(),dbProdOfSupp.getPidSuperLee()));
            }
        } else {
            return new ResponseT<>("Could not load orders");
        }
        return new ResponseT<>(res);
    }


//    public ResponseT<BillOfQuantityDTO> getBillOfQuantity(Integer supplierID) {
//        if (billsOfQuantity.containsKey(supplierID))
//            return new ResponseT<>(billsOfQuantity.get(supplierID));
//        ResponseT<BillOfQuantityDTO> bill = billOfQuantityDAO.get(supplierID);
//        if (!bill.ErrorOccured())
//            billsOfQuantity.put(supplierID, bill.value);
//        return bill;
//    }

//    public ResponseT<OrderDTO> getOrder(Integer orderID) {
//        if (orders.containsKey(orderID))
//            return new ResponseT<>(orders.get(orderID));
//        ResponseT<OrderDTO> order = orderDAO.get(orderID);
//        if (!order.ErrorOccured())
//            orders.put(orderID, order.value);
//        return order;
//    }

    public ResponseT<List<Category>> loadCategory() {
        ResponseT<List<Item>> itemsRes = loadItems();
        ResponseT<List<CategoryDTO>> catRes = categoryDAO.read();
        ResponseT<List<CategoryItemsDTO>> catItemRes = categoryDAO.readCategoryItems();
        ResponseT<List<subCategoriesDTO>> subCatRes = categoryDAO.readSubCategory();
        List<Category> allCats = new LinkedList<>();
        List<Category> firstCats = new LinkedList<>();
        if (!itemsRes.ErrorOccured() && !catRes.ErrorOccured() && !catItemRes.ErrorOccured() && !subCatRes.ErrorOccured()) {

            ResponseT<List<Discount>> disRes = loadDiscounts(itemsRes.value);
            if (disRes.ErrorOccured()) {
                return new ResponseT<>("something went wrong with loading discounts");
            }
            //loading categories
            for (CategoryDTO dbCat : catRes.value) {
                Category cat = new Category(dbCat.getName());
                allCats.add(cat);
                firstCats.add(cat);
            }
            //adding items to categories
            for (CategoryItemsDTO catItem : catItemRes.value) {
                ResponseT<Item> iRes = getItem(catItem.getItemID(), itemsRes.value);
                ResponseT<Category> cRes = getCat(catItem.getCatName(), allCats);
                if (!iRes.ErrorOccured() && !cRes.ErrorOccured()) {
                    cRes.value.addItem(iRes.value);
                }
            }
            //setting SubCategories
            for (subCategoriesDTO subCat : subCatRes.value) {
                ResponseT<Category> father = getCat(subCat.getFatherCategory(), allCats);
                ResponseT<Category> child = getCat(subCat.getChildCategory(), allCats);
                if (!father.ErrorOccured() && !child.ErrorOccured()) {
                    father.value.addSubCategory(child.value);
                    firstCats.remove(getCat(subCat.getChildCategory(), allCats).value);
                } else {
                    return new ResponseT<>("Could not load Categories");
                }
            }
        } else {
            return new ResponseT<>("Could not load Categories");
        }
        return new ResponseT<>(firstCats);
    }

    public ResponseT<List<FaultyItem>> loadFaulty() {
        ResponseT<List<FaultyItemDTO>> faultyRes = faultyItemDAO.read();
        List<FaultyItem> res = new LinkedList<>();
        if (!faultyRes.ErrorOccured()) {
            for (FaultyItemDTO dbFaulty : faultyRes.value) {
                res.add(new FaultyItem(dbFaulty.getItemId(), dbFaulty.getExpDate(), dbFaulty.getAmount()));
            }
        } else {
            return new ResponseT<>("Could not load Faulty items");
        }
        return new ResponseT<>(res);
    }

    public ResponseT<List<Sale>> loadSales() {
        ResponseT<List<SaleDTO>> saleRes = saleDAO.read();
        List<Sale> res = new LinkedList<>();
        if (!saleRes.ErrorOccured()) {
            for (SaleDTO dbSale : saleRes.value) {
                res.add(new Sale(dbSale.getItemID(), dbSale.getCost(), dbSale.getPrice(), dbSale.getDate(), dbSale.getAmount()));
            }
        } else {
            return new ResponseT<>("Could not load Sales");
        }
        return new ResponseT<>(res);
    }

    private ResponseT<List<Item>> loadItems() {
        ResponseT<List<ItemDTO>> itemsRes = itemDAO.read();
        List<Item> res = new LinkedList<>();
        if (!itemsRes.ErrorOccured()) {
            for (ItemDTO dbItem : itemsRes.value) {
                res.add(new Item(dbItem.getId(), dbItem.getName(), dbItem.getPrice(), dbItem.getCost(), dbItem.getShelfNum(), dbItem.getManufacturer(), dbItem.getShelfQuantity(), dbItem.getStorageQuantity(), dbItem.getMinAlert()));
            }
        } else {
            return new ResponseT<>("Could not load items");
        }
        return new ResponseT<>(res);
    }

    private ResponseT<List<Discount>> loadDiscounts(List<Item> items) {
        ResponseT<List<DiscountDTO>> priceDisRes = priceDisDAO.read();
        ResponseT<List<DiscountDTO>> costDisRes = costDisDAO.read();
        List<Discount> result = new LinkedList<>();
        if (!priceDisRes.ErrorOccured() && !costDisRes.ErrorOccured()) {
            for (DiscountDTO priceDis : priceDisRes.value) {
                Discount toAdd = new Discount(priceDis.getStart(), priceDis.getEnd(), priceDis.getDiscountPr());
                result.add(toAdd);
                ResponseT<Item> iRes = getItem(priceDis.getItemId(), items);
                if (!iRes.ErrorOccured()) {
                    iRes.value.addPriceDiscount(toAdd);
                } else {
                    return new ResponseT<>("Error loading Discounts");
                }
            }
            for (DiscountDTO costDis : costDisRes.value) {
                Discount toAdd = new Discount(costDis.getStart(), costDis.getEnd(), costDis.getDiscountPr());
                result.add(toAdd);
                ResponseT<Item> iRes = getItem(costDis.getItemId(), items);
                if (!iRes.ErrorOccured()) {
                    iRes.value.addCostDiscount(toAdd);
                } else {
                    return new ResponseT<>("Error loading Discounts");
                }
            }
        } else {
            return new ResponseT<>("Error loading Discounts");
        }
        return new ResponseT<>(result);
    }

    private ResponseT<Item> getItem(int id, List<Item> items) {
        for (Item i : items) {
            if (i.getId() == id) {
                return new ResponseT<>(i);
            }
        }
        return new ResponseT<>("Error");
    }

    private ResponseT<Category> getCat(String name, List<Category> cats) {
        if (name == null) {
            return null;
        }
        for (Category c : cats) {
            if (c.getName().equals(name)) {
                return new ResponseT<>(c);
            }
        }
        return new ResponseT<>("Error");
    }

    public void addSupplier(SupplierCard supCard) {
        supplierDAO.insert(supCard);
    }

    public void deleteSupCard(int supplierID) {
        supplierDAO.delete(supplierID);
    }

    public void updateSupplierStringColumn(String colName, int supplierID, String newVal) {
        supplierDAO.update(colName, supplierID, newVal);
    }

    public void updateSupplierIntColumn(String colName, int supplierID, int newVal) {
        supplierDAO.update(colName, supplierID, newVal);
    }

    public void updateSupplierBoolColumn(String colName, int supplierID, boolean newVal) {
        supplierDAO.update(colName, supplierID, newVal);
    }


    public ResponseT<OrderDTO> addOrder(Order order) {
        ResponseT<OrderDTO> orderRes = orderDAO.insert(order);
        if (orderRes.ErrorOccured()) {
            return new ResponseT<>("Could not add Product");
        }
        return orderRes;
    }

    public void setOrderPrice(int orderID, double price){
        orderDAO.updatePrice(orderID, price);
    }

    public void addProductInOrder(int suppID, int orderID, int productID, int quantity) {
        productsInOrderDAO.insert(orderID,productID,quantity,suppID);
    }



    public void addBillOfQuantity(int supplierID, int pid, int minQ, int discount) {
        billOfQuantityDAO.insert(supplierID, pid, minQ, discount);
    }

    public void updateMinQuantity(int supplierID, int pid, int newMinQ) {
        billOfQuantityDAO.updateMinQuantity(supplierID, pid, newMinQ);
    }

    public void updateDiscount(int supplierID, int pid, int newDis) {
        billOfQuantityDAO.updatePercentDiscount(supplierID, pid, newDis);
    }

    public void deleteBillOfQ(int supplierID) {
        billOfQuantityDAO.delete(supplierID);
    }

    public void deleteProductFromBill(int supplierID, int prodID){
        billOfQuantityDAO.deleteProduct(supplierID,prodID);
    }

    public Response addProductToSupplier(int productID, int supplierID, String name, String category, double price, int pidSuperLee) {
        return productsOfSupplierDAO.insert(productID,supplierID,name,category,price,pidSuperLee);
    }

    public void deleteProductFromSupplier(int supplierID, int productID) {
        productsOfSupplierDAO.delete(supplierID,productID);
    }

    public void addPeriodicOrder(int orderID, LocalDate supplyDate, int interval) {
        periodicOrderDAO.insert(orderID, supplyDate, interval);
    }

    public ResponseT<Integer> getNextOrderID(){
        ResponseT<Integer> res = orderDAO.getNextOrderID();
        if(res.value == null){
            return new ResponseT<Integer>(0);
        }
        return res;
    }

    public void addProductToPeriodic(int orderID, int productID, int quantity) {
        productsInPeriodicDAO.insert(orderID, productID, quantity);
    }

    public void deletePeriodicOrder(int orderID) {
        periodicOrderDAO.delete(orderID);
    }

    public void deleteProductFromPeriodic(int orderID, int productID) {
        productsInPeriodicDAO.deleteProductFromPeriodic(orderID, productID);
    }

    public void editInterval(int orderID, int interval) {
        periodicOrderDAO.updateInterval(orderID, interval);
    }

}
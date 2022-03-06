package BusinessLayer.Supplier;

import BusinessLayer.Response;
import DataAccessLayer.Supplier_Inv.Mapper;

import java.util.HashMap;
import java.util.Set;

public class ProductController {

    private static ProductController productController = null;

    private HashMap<Integer, HashMap<Integer, BusinessLayer.Supplier.Product>> supplierProd; // <supID: Integer, HashMap<Integer:productID,Product>>
    private HashMap<Integer, BusinessLayer.Supplier.BillOfQuantities> discounts; // <supID: Integer, List<Product>>
    private Mapper mapper;

    private ProductController()
    {
        mapper = Mapper.getInstance();
        supplierProd = mapper.loadProducts().value;
        discounts = mapper.loadBillsOfQuantity().value;
    }

    public static ProductController getInstance()
    {
        if (productController == null)
            productController = new ProductController();
        return productController;
    }


    public void addBillOfQuantity(int supplierID, HashMap<Integer, Integer> minQuantityForDis,  HashMap<Integer, Integer> discountList){
        if (discounts.containsKey(supplierID)){
            throw new IllegalArgumentException("This Supplier Already Have Bill Of Quantities");
        }
        else{
            BusinessLayer.Supplier.BillOfQuantities billOfQ = new BusinessLayer.Supplier.BillOfQuantities(minQuantityForDis,discountList );
            discounts.put(supplierID , billOfQ);
        }
    }

    public void addBilltoDB(int supplierID, int prodID, int minDis, int percentage) {
        if (discounts.containsKey(supplierID)){
            throw new IllegalArgumentException("This Supplier Already Have Bill Of Quantities");
        }
        else{
            mapper.addBillOfQuantity(supplierID, prodID, minDis, percentage);
        }
    }

    public void deleteSupCard(int supplierID){
        supplierProd.remove(supplierID);
        discounts.remove(supplierID);
    }

    public void deleteBillOfQuantity(int supplierID) {
        mapper.deleteBillOfQ(supplierID);
        BusinessLayer.Supplier.BillOfQuantities tmp = discounts.remove(supplierID);
        if (tmp == null){
            throw new IllegalArgumentException("This Supplier Does Not Have Bill Of Quantity To Delete");
        }
    }

    public void addProductToSupplier(int supplierID, int productID, String name, String category, double price, int pidSuperLee) {
        if (supplierProd.containsKey(supplierID)) {
            if (supplierProd.get(supplierID).containsKey(productID)) {
                throw new IllegalArgumentException("This Item Already Exists In The Supplier Products List");
            } else {
                Response res = mapper.addProductToSupplier(productID, supplierID, name, category, price,pidSuperLee);
                BusinessLayer.Supplier.Product prod = new BusinessLayer.Supplier.Product(productID, supplierID, name, category, price,pidSuperLee);
                supplierProd.get(supplierID).put(productID, prod);
            }
        }
    }

    public void removeProductToSupplier(int supplierID, int productID) {
        if (!supplierProd.containsKey(supplierID)){
            throw new IllegalArgumentException("This Supplier Does Not Exists In The System");
        }
        if (!supplierProd.get(supplierID).containsKey(productID)){
            throw new IllegalArgumentException("This Item Does Not Exists In The Supplier Products List");
        }
        else{
            mapper.deleteProductFromSupplier(supplierID, productID);
            supplierProd.get(supplierID).remove(productID);
        }
    }

    public String showSupplierProducts(int supplierID){
        if (!supplierProd.containsKey(supplierID)){
            throw new IllegalArgumentException("This Supplier Does Not Exists In The System");
        }
        if (supplierProd.get(supplierID).values().isEmpty())
            throw new IllegalArgumentException("This Supplier Does Not Have Any Products");

//        HashMap<Integer, BussinessLayer.Supplier.Product> supItems = supplierProd.get(supplierID);
//        if (supItems.isEmpty()){
//            throw new IllegalArgumentException("This Supplier Does Not Have Any Products");
//        }

        String productsList = '\n' + "products list: " + '\n';
        for(BusinessLayer.Supplier.Product p : supplierProd.get(supplierID).values()){
            productsList += p.toString() + '\n';
        }
        return productsList;
    }

    public void checkBillExist(int suppID){
        if(discounts.containsKey(suppID)){
            throw new IllegalArgumentException("Bill Of Quantity Already Exist");
        }
    }

    public void checkBillNotExist(int suppID){
        if(!discounts.containsKey(suppID)){
            throw new IllegalArgumentException("Bill Of Quantity Does Not Exist");
        }
    }

    public double calculateDiscount(int productId, int quantity, int suppID){
        double priceBeforeDiscount = supplierProd.get(suppID).get(productId).getPrice() * quantity;
        BusinessLayer.Supplier.BillOfQuantities bill = discounts.get(suppID);
        if(bill == null ) //no bill for this supplier
            return priceBeforeDiscount;
        Integer minQ = bill.getMinQuantityForDis().get(productId);
        if(minQ == null || (minQ > quantity)) //no discount for this product
            return priceBeforeDiscount;
        int percentDis = bill.getDiscountList().get(productId);
        double substract =  (priceBeforeDiscount * percentDis)/100;
        return priceBeforeDiscount - substract;
    }

    public void newSupplier(int supplierID){
        HashMap<Integer, BusinessLayer.Supplier.Product> products = new HashMap<>();
        supplierProd.put(supplierID, products);
    }

    public void checkProductExist(int supID, int prodID){
        if(!supplierProd.get(supID).containsKey(prodID)){
            throw new IllegalArgumentException("This Supplier Does Not Have This Product ID");
        }
    }

    public void checkProductInBillOfQ(int supID, int prodID){
        if(!discounts.get(supID).getMinQuantityForDis().containsKey(prodID)){
            throw new IllegalArgumentException("This Product Does Not Exist In The Bill Of Quantity");
        }
    }

    public void editMinQuantity(int supplierID, int pid, int newQ) {
        mapper.updateMinQuantity(supplierID, pid, newQ);
        discounts.get(supplierID).getMinQuantityForDis().replace(pid,newQ);
    }

    public void editDiscount(int supplierID, int pid, int discount) {
        mapper.updateDiscount(supplierID, pid, discount);
        discounts.get(supplierID).getDiscountList().replace(pid,discount);
    }

    public void addProdToBill(int supplierID, int pid, int minQ, int discount) {
        discounts.get(supplierID).getDiscountList().put(pid,discount);
        discounts.get(supplierID).getMinQuantityForDis().put(pid,minQ);
    }

    public void removeProdFromBill(int supplierID, int pid) {
        mapper.deleteProductFromBill(supplierID,pid);
        discounts.get(supplierID).getDiscountList().remove(pid);
        discounts.get(supplierID).getMinQuantityForDis().remove(pid);
    }

    public HashMap<Integer, HashMap<Integer, Product>> getSupplierProd() {
        return supplierProd;
    }

    public HashMap<Integer, BillOfQuantities> getDiscounts(){
        return this.discounts;
    }

    public void addSuppliers(Set<Integer> keySet) {
        for(Integer supID : keySet){
            if (supplierProd.get(supID) == null)
                supplierProd.put(supID, new HashMap<>());
        }

    }


}

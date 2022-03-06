//package Tests.DeliveryTest.BusinessLayerTests;
//
//import BuisnessLayer.DeliveryBusinessLayer.*;
//import DataAccessLayer.DeliveryDataAccessLayer.DTO.*;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.InputMismatchException;
//
//import static org.junit.jupiter.api.Assertions.*;
//// in the add fuction we check the gets function - so we dont need to implement those
//// not sure if we can hold fields of other classes to use them
//// cant run it - need to find a way why
//// I think we need to add only CLITest. both CLITest and facadeTest cover every thing we need.
////              __
////             |  |
////    ________ \   \
////  ( __       `    `--------------
////  ( __
////  ( __
////  ( _________,----------------
////
//
//// TODO : everything related to "get data" (lines 151, 172, 197) are in comment now for testing SQL
//class FacadeControllerTest {
//    FacadeController fc;
//    DeliveryController dec;
//    DriverController drc;
//    AreaController arc;
//    TaskController tac;
//    TruckController trc;
//
//    @org.junit.jupiter.api.BeforeEach
//    void setUp() {
//        fc = new FacadeController();
//        dec = new DeliveryController();
//        drc = new DriverController();
//        arc = new AreaController();
//        tac = new TaskController();
//        trc = new TruckController();
//
//        ArrayList<tmpEmployee> drivers = new ArrayList<>();
//        drivers.add(new tmpEmployee("Nitzan the Lary", 20000));
//        drivers.add(new tmpEmployee("Yanay the Sunny", 11000));
//        fc.tempAddDriver(drivers);
//        AreaDTO testArea = new AreaDTO("TestArea");
//        fc.addNewArea(testArea);
//        TruckDTO testTruck = new TruckDTO("12345678","Mercedes", 20000,12000);
//        fc.addTruck(testTruck);
//        TruckDTO truck2 = new TruckDTO("34556123","Renault Fluence", 20000,8500);
//        fc.addTruck(truck2);
//        LocationDTO testKg = new LocationDTO("King-David 4, Kiryat-Gat", "052535606", "Asaf Stern");
//        fc.addLocation(testArea, testKg);
//        LocationDTO orig = new LocationDTO("Ben-Gurion 1, Beer Sheva", "0866445531", "Refael Farjun");
//        fc.addLocation(testArea, orig);
//        HashMap<String, Integer> task1Items = new HashMap<>();
//        task1Items.put("6Cola",800);
//        HashMap<String, Integer> task2Items = new HashMap<>();
//        task2Items.put("eggsXL",500);
//        task2Items.put("watermelon",20);
//        TaskDTO task1 = new TaskDTO(task1Items,"unloading",testKg);
//        TaskDTO task2 = new TaskDTO(task2Items,"loading",testKg);
//        task1 = fc.addTask(task1);
//        task2 = fc.addTask(task2);
//        ArrayList<TaskDTO> destinations1 = new ArrayList<>();
//        ArrayList<TaskDTO> destinations2 = new ArrayList<>();
//        DeliveryDTO testDel1 = new DeliveryDTO("23-3-21","8:00","12345678","Nitzan the Lary",20800,"",testKg ,destinations1);
//        fc.createFullDelivery(testDel1);
//        DeliveryDTO testDel2 = new DeliveryDTO("23-4-22","16:00","34556123","Nitzan the Lary",0,"",testKg ,destinations2);
//        fc.createFullDelivery(testDel2);
//
//    }
//
////    @org.junit.jupiter.api.AfterEach
////    void tearDown() {
////    }
//
//    @org.junit.jupiter.api.Test
//    void addNewArea() {
//        AreaDTO south = new AreaDTO("South");
//        assertFalse(fc.getAreas().contains(south));
//        fc.addNewArea(south);
////        System.out.println(fc.getAreas());
//        assertEquals(fc.getAreas().get(1).getAreaName(), "South");
//
//    }
//
//    @org.junit.jupiter.api.Test
//    void addLocation() {
//        LocationDTO bs = new LocationDTO("Ben-Gurion 1, Beer Sheva", "0866445531", "Refael Farjun");
//        AreaDTO south = new AreaDTO("South");
//        fc.addNewArea(south);
//        assertFalse(fc.getLocationsByAreas().get(south.getAreaName()).contains(bs));
////        assertNotEquals(fc.getLocationByAddress(new Response<String>("Ben-Gurion 1, Beer Sheva") ), bs);
//        fc.addLocation(south, bs);
////        assertEquals(fc.getLocationByAddress(new Response<String>("Ben-Gurion 1, Beer Sheva") ), bs);
//        assertEquals(fc.getLocationsByAreas().get(south.getAreaName()).get(0).getAddress(), "Ben-Gurion 1, Beer Sheva");
//    }
//
////    @org.junit.jupiter.api.Test
////    void getAreas() {
////    }
////
////    @org.junit.jupiter.api.Test
////    void getLocationByAddress() {
////    }
//
//    @org.junit.jupiter.api.Test
//    void addTask() {
//        LocationDTO dest = new LocationDTO("Omer 17, Omer", "086754921", "Nitzan Lary");
//        AreaDTO south = new AreaDTO("South");
//        fc.addNewArea(south);
//        fc.addLocation(south, dest);
//
//        HashMap<String, Integer> taskItems = new HashMap<>();
//        taskItems.put("milk",40);
//        taskItems.put("bread",25);
//        TaskDTO task = new TaskDTO(taskItems,"loading",dest);
//        assertFalse(fc.getTasks().contains(task));
////        assertNull(fc.addTask(task).getId()); // check if there is NO an ID
//        task = fc.addTask(task);
//        assertTrue(fc.getTasks().contains(task));
//        assertNotNull(task.getId()); // check if there is an ID
//    }
//
//
//    @org.junit.jupiter.api.Test
//    void addTruck() {
//        TruckDTO truck1 = new TruckDTO("4755857","Mercedes x", 15000,8000);
//        assertFalse(fc.getTrucks().contains(truck1));
//        fc.addTruck(truck1);
//        assertTrue(fc.getTrucks().contains(truck1));
//    }
//
////    @org.junit.jupiter.api.Test
////    void getTrucks() {
////    }
////
////    @org.junit.jupiter.api.Test
////    void getDeliveryById() {
////    }
////
//    @org.junit.jupiter.api.Test
//    void sendDelivery() {
//        // add delivery and check if it in the data
//        DeliveryDTO deliveryDTO = fc.getDeliveryById("A000");
////        DeliveryDTO deliveryDTO = new DeliveryDTO(delivery);
//        fc.sendDelivery(deliveryDTO, new Response<>(true));
////        assertTrue(fc.getDeliveryData().contains(deliveryDTO));
//    }
//
//    @org.junit.jupiter.api.Test
//    void createFullDelivery() {
//        LocationDTO orig = fc.getLocationByAddress(new Response<>("Ben-Gurion 1, Beer Sheva"));
////
//        LocationDTO dest = fc.getLocationByAddress(new Response<>("King-David 4, Kiryat-Gat"));
//
////        LocationDTO dest = new LocationDTO("Omer 17, Omer", "086754921", "Nitzan Lary");
//        HashMap<String, Integer> taskItems = new HashMap<>();
//        taskItems.put("milk",40);
//        taskItems.put("bread",25);
//        TaskDTO destinations = new TaskDTO(taskItems,"loading",dest);
//        ArrayList<TaskDTO> arr = new ArrayList<>();
////        arr.add(destinations);
//        arr.add(fc.addTask(destinations));
//        DeliveryDTO ddt = new DeliveryDTO("23-3-2021","12:00","12345678","Yanay Sun",15874,"",orig ,arr);
//
//        ddt = fc.createFullDelivery(ddt);
//        assertEquals(fc.getDeliveryById(ddt.getId()), ddt);
////        assertFalse(fc.getDeliveryData().contains(ddt)); // False - because we insert new Delivery to data only when it send
//
//    }
//
////    @org.junit.jupiter.api.Test
////    void getTasks() {
////    }
////
//    @org.junit.jupiter.api.Test
//    void getUpdatableDeliveries() {
//        // add 2 delivery one is updateable and one isnt and check them
//        DeliveryDTO delivery1 = fc.getDeliveryById("A000");
//        DeliveryDTO delivery2 = fc.getDeliveryById("A001");
//        System.out.println(delivery1.getDate());
//        assertFalse(fc.getUpdatableDeliveries().contains(delivery1));
//        assertTrue(fc.getUpdatableDeliveries().contains(delivery2));
//    }
//
//    @org.junit.jupiter.api.Test
//    void updateDelivery() {
//        DeliveryDTO deliveryDTO = fc.getDeliveryById("A000");
//        deliveryDTO.setDate("1-5-21");
//        fc.updateDelivery(deliveryDTO, "A000");
//        assertSame("1-5-21", fc.getDeliveryById("A002").getDate());
//        Assertions.assertThrows(InputMismatchException.class, () -> fc.getDeliveryById("A000"));
////        assertEquals(fc.getDeliveryData().get(0).getId(), "A000");
//    }
//
////    @org.junit.jupiter.api.Test(expected = InputMismatchException.class)
////    void testAfterUpdate(){
////        fc.getDeliveryById("A000");
////    }
//
//
//    @org.junit.jupiter.api.Test
//    void isLegalDepartureWeight() {
//        DeliveryDTO deliveryDTO = fc.getDeliveryById("A000");
////        DeliveryDTO deliveryDTO = new DeliveryDTO(delivery);
////        System.out.println(fc.isLegalDepartureWeight("1000000",deliveryDTO).getData().contains("Exceeded"));
//        assertTrue(fc.isLegalDepartureWeight("1000000",deliveryDTO).getData().contains("Exceeded"));
//
//    }
//
//    @Test
//    void getDriversToTruck() {
//        DeliveryDTO deliveryDTO1 = fc.getDeliveryById("A000");
//        DeliveryDTO deliveryDTO2 = fc.getDeliveryById("A001");
//        TruckDTO truckDTO1 = fc.getTruckByDelivery(deliveryDTO1);
//        TruckDTO truckDTO2 = fc.getTruckByDelivery(deliveryDTO2);
//        DriverDTO nitzan = fc.getAllDrivers().get(0);
//        DriverDTO yanay = fc.getAllDrivers().get(1);
//
//        assertFalse(fc.getDriversToTruck(truckDTO1).contains(yanay));
//        assertTrue(fc.getDriversToTruck(truckDTO2).contains(yanay));
//        assertTrue(fc.getDriversToTruck(truckDTO1).contains(nitzan));
//
//    }
//}
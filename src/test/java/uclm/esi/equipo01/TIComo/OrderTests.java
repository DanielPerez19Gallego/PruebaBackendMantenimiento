package uclm.esi.equipo01.TIComo;

import static org.junit.Assert.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.openjson.JSONObject;

import uclm.esi.equipo01.http.Manager;
import uclm.esi.equipo01.http.OrderController;
import uclm.esi.equipo01.model.DatabaseSequence;
import uclm.esi.equipo01.model.Order;
import uclm.esi.equipo01.model.OrderRate;
import uclm.esi.equipo01.model.PlateAndOrder;
import uclm.esi.equipo01.service.OrderService;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:test.properties")
public class OrderTests {
	
	@Autowired
	private OrderService orderService;
	
	private OrderController controller;
	private OrderService service;

    private static MongoOperations mongoOperations;
	
	/*********************************************************************
	*
	* - Method name: setMongoOperations
	* - Description of the Method: initialize the data of the DB
	* - Calling arguments: A list of the calling arguments, their types, and
	* brief explanations of what they do: None
	* - Return value: None
	* - Required Files: None
	* - List of Checked Exceptions and an indication of when each exception
	* is thrown: None.
	*
	*********************************************************************/
    @Autowired
    public void setMongoOperations(MongoOperations mongoOperations) {
    	OrderTests.mongoOperations = mongoOperations;
    }
    
	/*********************************************************************
	*
	* - Method name: initiate
	* - Description of the Method: remove all data DB after execution
	* - Calling arguments: A list of the calling arguments, their types, and
	* brief explanations of what they do: None
	* - Return value: None
	* - Required Files: None
	* - List of Checked Exceptions and an indication of when each exception
	* is thrown: None.
	*
	*********************************************************************/
    @AfterClass
    public static void initiate() {
    	Manager.get().getOrderRepository().deleteAll();
    	mongoOperations.findAndRemove(query(where("_id").is(Order.SEQUENCE_ID)), DatabaseSequence.class);
    }
    
	/*********************************************************************
	*
	* - Method name: setUp
	* - Description of the Method: initialize the data of the service and controller before execution
	* - Calling arguments: A list of the calling arguments, their types, and
	* brief explanations of what they do: None
	* - Return value: None
	* - Required Files: None
	* - List of Checked Exceptions and an indication of when each exception
	* is thrown: None.
	*
	*********************************************************************/
	@Before
    public void setUp() {
        service = Mockito.mock(OrderService.class);
        controller = new OrderController(); 
    }
	
	
	/*********************************************************************
	*
	* - Method name: test01 to test02
	* - Description of the Method: Tests carried out to add order in order controller
	* - Calling arguments: A list of the calling arguments, their types, and
	* brief explanations of what they do: None
	* - Return value: None
	* - Required Files: None
	* - List of Checked Exceptions and an indication of when each exception
	* is thrown: None.
	*
	*********************************************************************/
	@Test
	public void test01() {
	    Map<String, Object> info = new HashMap<String, Object>();
	    
		info.put("restaurantID", "1");
		JSONObject infoPlate = new JSONObject();
		infoPlate.put("price", 10);
		infoPlate.put("quantity", 1);
		JSONObject cart = new JSONObject();
		cart.put("1", infoPlate);	
		info.put("cart", cart);	
		
	    long idClient = 1;
		
		JSONObject jso = new JSONObject(info);	
		
		Mockito.when(service.makeOrder(jso,idClient)).thenReturn(new ResponseEntity<>(HttpStatus.OK));
		ResponseEntity<String> httpResponse = controller.makeOrder(info,idClient);	
		assertEquals(HttpStatus.OK, httpResponse.getStatusCode());
		this.testModificarOrden();

	}
	
	/*********************************************************************
	*
	* - Method name: test02 to test06
	* - Description of the Method: Tests carried out to add order in order service
	* - Calling arguments: A list of the calling arguments, their types, and
	* brief explanations of what they do: None
	* - Return value: None
	* - Required Files: None
	* - List of Checked Exceptions and an indication of when each exception
	* is thrown: None.
	*
	*********************************************************************/
	@Test
	public void test02() {
		JSONObject info = new JSONObject();
	    
		info.put("restaurantID", "1");
		JSONObject infoPlate = new JSONObject();
		infoPlate.put("price", 10);
		infoPlate.put("quantity", 1);
		JSONObject cart = new JSONObject();
		cart.put("2", infoPlate);	
		info.put("cart", cart);	
		
	    long idClient = 1;
		
	    ResponseEntity<String> result = orderService.makeOrder(info, idClient);
	    ResponseEntity<String> valueExpected = new ResponseEntity<>("Pedido añadido correctamente", HttpStatus.OK);
		assertEquals(valueExpected.getStatusCode(), result.getStatusCode());
	}
	
	@Test
	public void test03() {
		JSONObject info = new JSONObject();
		
		info.put("restaurantID", "1");
		JSONObject infoPlate = new JSONObject();
		infoPlate.put("price", 10);
		infoPlate.put("quantity", 2);
		JSONObject cart = new JSONObject();
		cart.put("1", infoPlate);	
		info.put("cart", cart);	
		
	    long idClient = 1;
		
	    ResponseEntity<String> result = orderService.makeOrder(info, idClient);
	    ResponseEntity<String> valueExpected = new ResponseEntity<>("Pedido añadido correctamente", HttpStatus.OK);
		assertEquals(valueExpected.getStatusCode(), result.getStatusCode());
	}
	
	
	@Test
	public void test04() {
		JSONObject info = new JSONObject();
	    
		info.put("restaurantID", "1");
		JSONObject infoPlate1 = new JSONObject();
		infoPlate1.put("price", 10);
		infoPlate1.put("quantity", 1);
		JSONObject infoPlate2 = new JSONObject();
		infoPlate2.put("price", 15);
		infoPlate2.put("quantity", 2);
		JSONObject cart = new JSONObject();
		cart.put("1", infoPlate1);	
		cart.put("2", infoPlate2);	
		info.put("cart", cart);	
		
	    long idClient = 1;

	    ResponseEntity<String> result = orderService.makeOrder(info, idClient);
	    ResponseEntity<String> valueExpected = new ResponseEntity<>("Pedido añadido correctamente", HttpStatus.OK);
		assertEquals(valueExpected.getStatusCode(), result.getStatusCode());
	}
	
	
	@Test
	public void test05() {
		JSONObject info = new JSONObject();
	    
		info.put("restaurantID", "1");
		JSONObject infoPlate = new JSONObject();
		infoPlate.put("price", 10);
		infoPlate.put("quantity", 1);
		JSONObject cart = new JSONObject();
		cart.put("1", infoPlate);	
		cart.put("3", infoPlate);	
		cart.put("4", infoPlate);	
		info.put("cart", cart);	
		
	    long idClient = 1;

	    ResponseEntity<String> result = orderService.makeOrder(info, idClient);
	    ResponseEntity<String> valueExpected = new ResponseEntity<>("Pedido añadido correctamente", HttpStatus.OK);
		assertEquals(valueExpected.getStatusCode(), result.getStatusCode());
	}
	
	@Test
	public void test06() {
		JSONObject info = new JSONObject();
	    
		info.put("restaurantID", "1");
		JSONObject infoPlate = new JSONObject();
		infoPlate.put("price", 10);
		infoPlate.put("quantity", 1);
		JSONObject cart = new JSONObject();
		cart.put("1", infoPlate);	
		info.put("cart", cart);
		
	    long idClient = 1;
		
	    ResponseEntity<String> result = orderService.makeOrder(info, idClient);
	    ResponseEntity<String> valueExpected = new ResponseEntity<>("Pedido añadido correctamente", HttpStatus.OK);
		assertEquals(valueExpected.getStatusCode(), result.getStatusCode());
	}
	
	//////////////////////////////////////////////////////////////////////
	//NUEVOS TESTS HECHOS A PARTIR DE AQUI EN EL SPRINT DE MANTENIMIENTO//
	//////////////////////////////////////////////////////////////////////
	@Test 
	public void test33() {
		List<Order> result = orderService.showAllOrders();
		assertNotNull(result);
	}
	
	@Test 
	public void test34() {
		long restaurantID = 1;
		List<Order> result = orderService.showAllOrdersByRestaurant(restaurantID);
		assertNotNull(result);
	}
	
	@Test 
	public void test35() {
		long clientID = 1;
		List<Order> result = orderService.showAllOrdersByClient(clientID);
		assertNotNull(result);
	}
	
	/*********************************************************************
	*
	* - Method name: test07 to test08
	* - Description of the Method: Tests carried out to change the order´s state in
	* OrderController 
	* - Calling arguments: A list of the calling arguments, their types, and
	* brief explanations of what they do: None
	* - Return value: None
	* - Required Files: None
	* - List of Checked Exceptions and an indication of when each exception
	* is thrown: None.
	*
	*********************************************************************/
	@Test
	public void test07() {
		Map<String, Object> info = new HashMap<String, Object>();
		info.put("clientID", "1");
		info.put("riderID", "1");
		info.put("restaurantID", "1");
		info.put("state", "NEW");
		
		long id = 1;
		
		JSONObject jso = new JSONObject(info);
		
		Mockito.when(service.modifyOrder(jso, id)).thenReturn(new ResponseEntity<>("El pedido se ha modificado correctamente", HttpStatus.OK));
		ResponseEntity<String> httpResponse = controller.modifyOrder(info, id);
		assertEquals(HttpStatus.OK, httpResponse.getStatusCode());
	}
	
	public void testModificarOrden() {
		Map<String, Object> info = new HashMap<String, Object>();
		info.put("restaurantID", "1");
		info.put("clientID", "1");
		
		JSONObject infoPlate = new JSONObject();
		infoPlate.put("price", 10);
		infoPlate.put("quantity", 1);
		JSONObject cart = new JSONObject();
		cart.put("1", infoPlate);	
		info.put("cart", cart);
		
		long id = 1;
		
		JSONObject jso = new JSONObject(info);
		Mockito.when(service.modificaOrden(jso, id)).thenReturn(new ResponseEntity<>("El pedido se ha modificado correctamente", HttpStatus.OK));
		ResponseEntity<String> httpResponse = controller.modificarOrden(info, id);	
		assertEquals(HttpStatus.OK, httpResponse.getStatusCode());
	}
	
	@Test
	public void test08() {
		Map<String, Object> info = new HashMap<String, Object>();
		info.put("clientID", "1");
		info.put("riderID", "1");
		info.put("restaurantID", "1");
		info.put("state", "NEW");
		
		long id = 16;
		
		JSONObject jso = new JSONObject(info);
		
		Mockito.when(service.modifyOrder(jso, id)).thenReturn(new ResponseEntity<>("El pedido se ha modificado incorrectamente", HttpStatus.BAD_REQUEST));
		ResponseEntity<String> httpResponse = controller.modifyOrder(info, id);
		assertEquals(HttpStatus.BAD_REQUEST, httpResponse.getStatusCode());
	}
	
	/*********************************************************************
	*
	* - Method name: test09 to test17
	* - Description of the Method: Tests carried out to change the order´s state in
	* OrderService 
	* - Calling arguments: A list of the calling arguments, their types, and
	* brief explanations of what they do: None
	* - Return value: None
	* - Required Files: None
	* - List of Checked Exceptions and an indication of when each exception
	* is thrown: None.
	*
	*********************************************************************/
	@Test
	public void test09() {
		JSONObject info = new JSONObject();
	    
		info.put("clientID", "1");
		info.put("riderID", "10");
		info.put("restaurantID", "12");
		info.put("state", "NEW");
		
		long id = 2;
		
		ResponseEntity<String> result = orderService.modifyOrder(info, id);
	    ResponseEntity<String> valueExpected = new ResponseEntity<>("El pedido se ha  modificado correctamente", HttpStatus.OK);
		assertEquals(valueExpected.getStatusCode(), result.getStatusCode());
	}
	
	@Test
	public void test10() {
		JSONObject info = new JSONObject();
	    
		info.put("clientID", "2");
		info.put("riderID", "3");
		info.put("restaurantID", "5");
		info.put("state", "ONTHEWAY");
		
		long id = 3;
		
		ResponseEntity<String> result = orderService.modifyOrder(info, id);
	    ResponseEntity<String> valueExpected = new ResponseEntity<>("El pedido se ha  modificado correctamente", HttpStatus.OK);
		assertEquals(valueExpected.getStatusCode(), result.getStatusCode());
	}
	
	@Test
	public void test11() {
		JSONObject info = new JSONObject();
	    
		info.put("clientID", "6");
		info.put("riderID", "30");
		info.put("restaurantID", "2");
		info.put("state", "DELIVERED");
		
		long id = 4;
		
		ResponseEntity<String> result = orderService.modifyOrder(info, id);
	    ResponseEntity<String> valueExpected = new ResponseEntity<>("El pedido se ha  modificado correctamente", HttpStatus.OK);
		assertEquals(valueExpected.getStatusCode(), result.getStatusCode());
	}
		
	//////////////////////////////////////////////////////////////////////
	//NUEVOS TESTS HECHOS A PARTIR DE AQUI EN EL SPRINT DE MANTENIMIENTO//
	//////////////////////////////////////////////////////////////////////
	@Test 
	public void test36() {
		long id = 30;
		List<Order> result = orderService.showAllOrdersByRider(id);
		assertNotNull(result);
	}

	@Test 
	public void test37() {
		long orderID = 1;
		List<PlateAndOrder> result = orderService.showPlatesByOrder(orderID);
		assertNotNull(result);
	}

	@Test 
	public void test38() {
		long orderID = 1;
		OrderRate result = orderService.showOrderRate(orderID);
		assertNotNull(result);
	}
	
	//////////////////////////////////////
	
	@Test
	public void test12() {
		JSONObject info = new JSONObject();
	    
		info.put("clientID", "13");
		info.put("riderID", "10");
		info.put("restaurantID", "14");
		info.put("state", "XXX");
		
		long id = 16;
		
		ResponseEntity<String> result = orderService.modifyOrder(info, id);
	    ResponseEntity<String> valueExpected = new ResponseEntity<>("El pedido se ha  modificado incorrectamente", HttpStatus.BAD_REQUEST);
		assertEquals(valueExpected.getStatusCode(), result.getStatusCode());
	}
	
	@Test
	public void test13() {
		JSONObject info = new JSONObject();
	    
		info.put("clientID", "13");
		info.put("riderID", "10");
		info.put("restaurantID", "14");
		info.put("state", "ONTHEW");
		
		
		long id = 16;
		
		ResponseEntity<String> result = orderService.modifyOrder(info, id);
	    ResponseEntity<String> valueExpected = new ResponseEntity<>("El pedido se ha  modificado incorrectamente", HttpStatus.BAD_REQUEST);
		assertEquals(valueExpected.getStatusCode(), result.getStatusCode());
	}
	
	@Test
	public void test14() {
		JSONObject info = new JSONObject();
	    
		info.put("clientID", "13");
		info.put("riderID", "10");
		info.put("restaurantID", "14");
		info.put("state", "N");
		
		
		long id = 16;
		
		ResponseEntity<String> result = orderService.modifyOrder(info, id);
	    ResponseEntity<String> valueExpected = new ResponseEntity<>("El pedido se ha  modificado incorrectamente", HttpStatus.BAD_REQUEST);
		assertEquals(valueExpected.getStatusCode(), result.getStatusCode());
	}
	
	@Test
	public void test15() {
		JSONObject info = new JSONObject();
	    
		info.put("clientID", "XX");
		info.put("riderID", "10");
		info.put("restaurantID", "14");
		info.put("state", "NEW");
		
		
		long id = 16;
		
		ResponseEntity<String> result = orderService.modifyOrder(info, id);
	    ResponseEntity<String> valueExpected = new ResponseEntity<>("El pedido se ha  modificado incorrectamente", HttpStatus.BAD_REQUEST);
		assertEquals(valueExpected.getStatusCode(), result.getStatusCode());
	}
	
	@Test
	public void test16() {
		JSONObject info = new JSONObject();
	    
		info.put("clientID", "XX");
		info.put("riderID", "10");
		info.put("restaurantID", "14");
		info.put("state", "XX");
		
		
		long id = 16;
		
		ResponseEntity<String> result = orderService.modifyOrder(info, id);
	    ResponseEntity<String> valueExpected = new ResponseEntity<>("El pedido se ha  modificado incorrectamente", HttpStatus.BAD_REQUEST);
		assertEquals(valueExpected.getStatusCode(), result.getStatusCode());
	}
	
	@Test
	public void test17() {
		JSONObject info = new JSONObject();
	    
		info.put("clientID", "13");
		info.put("riderID", "XX");
		info.put("restaurantID", "14");
		info.put("state", "way");
		
		
		long id = 16;
		
		ResponseEntity<String> result = orderService.modifyOrder(info, id);
	    ResponseEntity<String> valueExpected = new ResponseEntity<>("El pedido se ha  modificado incorrectamente", HttpStatus.BAD_REQUEST);
		assertEquals(valueExpected.getStatusCode(), result.getStatusCode());
	}
	
	/*********************************************************************
	*
	* - Method name: test18 to test19
	* - Description of the Method: Tests carried out to delete order in order controller
	* - Calling arguments: A list of the calling arguments, their types, and
	* brief explanations of what they do: None
	* - Return value: None
	* - Required Files: None
	* - List of Checked Exceptions and an indication of when each exception
	* is thrown: None.
	*
	*********************************************************************/
	@Test
	public void test18() {

	    long idOrder = 1;
		
		Mockito.when(service.deleteOrder(idOrder)).thenReturn(new ResponseEntity<>(HttpStatus.OK));
		ResponseEntity<String> httpResponse = controller.deleteOrder(idOrder);	
	
		assertEquals(HttpStatus.OK, httpResponse.getStatusCode());
	}
	
	@Test
	public void test19() {
	    
	    long idOrder = 30;	
		
		Mockito.when(service.deleteOrder(idOrder)).thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
		ResponseEntity<String> httpResponse = controller.deleteOrder(idOrder);	
	
		assertEquals(HttpStatus.BAD_REQUEST, httpResponse.getStatusCode());
	}
	
	/*********************************************************************
	*
	* - Method name: test20 to test25
	* - Description of the Method: Tests carried out to delete order in order service
	* - Calling arguments: A list of the calling arguments, their types, and
	* brief explanations of what they do: None
	* - Return value: None
	* - Required Files: None
	* - List of Checked Exceptions and an indication of when each exception
	* is thrown: None.
	*
	*********************************************************************/
	@Test
	public void test20() {
	    
	    long idOrder = 2;
		
	    ResponseEntity<String> result = orderService.deleteOrder(idOrder);
	    ResponseEntity<String> valueExpected = new ResponseEntity<>("Pedido eliminado correctamente", HttpStatus.OK);
		assertEquals(valueExpected.getStatusCode(), result.getStatusCode());
	}
	
	@Test
	public void test21() {
	    
	    long idOrder = 3;	
		
	    ResponseEntity<String> result = orderService.deleteOrder(idOrder);
	    ResponseEntity<String> valueExpected = new ResponseEntity<>("Pedido eliminado correctamente", HttpStatus.OK);
		assertEquals(valueExpected.getStatusCode(), result.getStatusCode());
	}
	
	@Test
	public void test22() {
	    
	    long idOrder = 4;
		
	    ResponseEntity<String> result = orderService.deleteOrder(idOrder);
	    ResponseEntity<String> valueExpected = new ResponseEntity<>("Pedido eliminado correctamente", HttpStatus.OK);
		assertEquals(valueExpected.getStatusCode(), result.getStatusCode());
	}
	
	@Test
	public void test23() {

	    long idOrder = 10;
		
	    ResponseEntity<String> result = orderService.deleteOrder(idOrder);
	    ResponseEntity<String> valueExpected = new ResponseEntity<>("Pedido eliminado correctamente", HttpStatus.BAD_REQUEST);
		assertEquals(valueExpected.getStatusCode(), result.getStatusCode());
	}
	
	@Test
	public void test24() {

	    long idOrder = 49;
		
	    ResponseEntity<String> result = orderService.deleteOrder(idOrder);
	    ResponseEntity<String> valueExpected = new ResponseEntity<>("Pedido eliminado correctamente", HttpStatus.BAD_REQUEST);
		assertEquals(valueExpected.getStatusCode(), result.getStatusCode());
	}
	
	@Test
	public void test25() {

	    long idOrder = 100;
		
	    ResponseEntity<String> result = orderService.deleteOrder(idOrder);
	    ResponseEntity<String> valueExpected = new ResponseEntity<>("Pedido eliminado correctamente", HttpStatus.BAD_REQUEST);
		assertEquals(valueExpected.getStatusCode(), result.getStatusCode());
	}
	
	//////////////////////////////////////////////////////////////////////
	//NUEVOS TESTS HECHOS A PARTIR DE AQUI EN EL SPRINT DE MANTENIMIENTO//
	//////////////////////////////////////////////////////////////////////
	/*********************************************************************
	*
	* - Method name: test26
	* - Description of the Method: 
	* - Calling arguments: 
	* - Return value: 
	* - Required Files: 
	* - List of Checked Exceptions and an indication of when each exception
	* is thrown: 
	*
	*********************************************************************/
	@Test
	public void test26() {
		Map<String, Object> info = new HashMap<String, Object>();
		JSONObject order = new JSONObject();
		
		order.put("id", "1");
		order.put("clientID", "1");
		order.put("riderID", "1");
		order.put("restaurantID", "1");
		order.put("state", "NEW");
		order.put("price", "40");
		order.put("releaseDate", "2022-12-03T10:15:30");
	   
		info.put("order", order);
		info.put("rateRestaurant", "1");
		info.put("rateRider", "1");
		info.put("description", "Muy bien");
		
		
		JSONObject jso = new JSONObject(info);
		
		Mockito.when(service.rateOrder(jso)).thenReturn(new ResponseEntity<>("Pedido valorado correctamente", HttpStatus.OK));
		ResponseEntity<String> httpResponse = controller.rateOrder(info);
		assertEquals(HttpStatus.OK, httpResponse.getStatusCode());
	}
	
	/*********************************************************************
	*
	* - Method name: test27
	* - Description of the Method: 
	* - Calling arguments: 
	* - Return value: 
	* - Required Files: 
	* - List of Checked Exceptions and an indication of when each exception
	* is thrown: 
	*
	*********************************************************************/
	@Test
	public void test27() {
		
		JSONObject info = new JSONObject();
		JSONObject order = new JSONObject();
		
		order.put("id", "1");
		order.put("clientID", "1");
		order.put("riderID", "1");
		order.put("restaurantID", "1");
		order.put("state", "NEW");
		order.put("price", "40");
		order.put("releaseDate", "2022-12-03T10:15:30");
	   
		info.put("order", order);
		info.put("rateRestaurant", "1");
		info.put("rateRider", "1");
		info.put("description", "Muy bien");
		
	    ResponseEntity<String> result = orderService.rateOrder(info);
	    ResponseEntity<String> valueExpected = new ResponseEntity<>("Pedido valorado correctamente", HttpStatus.OK);
		assertEquals(valueExpected.getStatusCode(), result.getStatusCode());
		
		order.put("state", "ONTHEWAY");
		result = orderService.rateOrder(info);
	    valueExpected = new ResponseEntity<>("Pedido valorado correctamente", HttpStatus.OK);
		assertEquals(valueExpected.getStatusCode(), result.getStatusCode());
		
		order.put("state", "DELIVERED");
		result = orderService.rateOrder(info);
	    valueExpected = new ResponseEntity<>("Pedido valorado correctamente", HttpStatus.OK);
		assertEquals(valueExpected.getStatusCode(), result.getStatusCode());
		
	}
	
	/*********************************************************************
	*
	* - Method name: test28 to test29
	* - Description of the Method: 
	* - Calling arguments: 
	* - Return value: 
	* - Required Files: 
	* - List of Checked Exceptions and an indication of when each exception
	* is thrown: 
	*
	*********************************************************************/
	@Test
	public void test28() {

		List<Integer> rates = new ArrayList<Integer>();
		
		for(int i = 0; i < 6; i++) {
			rates.add(i);
		}
		
	    double result = orderService.calculateAverageRate(rates);
	    double valueExpected = 3;
		assertEquals(valueExpected, result, 0.5);
	}
	
	@Test
	public void test29() {

		List<Integer> rates = new ArrayList<Integer>();
		
		double result = orderService.calculateAverageRate(rates);
		assertEquals(0, result, 0.1);
	}
	
	/*********************************************************************
	*
	* - Method name: test30
	* - Description of the Method: 
	* - Calling arguments: 
	* - Return value: 
	* - Required Files: 
	* - List of Checked Exceptions and an indication of when each exception
	* is thrown: 
	*
	*********************************************************************/
	@Test
	public void test30() {
		Map<String, Object> info = new HashMap<String, Object>();
		
		info.put("simultaneousRiderOrders", "5");
		
		JSONObject jso = new JSONObject(info);
		
		Mockito.when(service.setSimultaneousRiderOrders(jso)).thenReturn(new ResponseEntity<>("Número de pedidos simultaneos actualizado correctamente", HttpStatus.OK));
		ResponseEntity<String> httpResponse = controller.setSimultaneousRiderOrders(info);
		assertEquals(HttpStatus.OK, httpResponse.getStatusCode());
	}
	
	/*********************************************************************
	*
	* - Method name: test31
	* - Description of the Method: 
	* - Calling arguments: 
	* - Return value: 
	* - Required Files: 
	* - List of Checked Exceptions and an indication of when each exception
	* is thrown: 
	*
	*********************************************************************/
	@Test
	public void test31() {
		
		JSONObject info = new JSONObject();
		
		info.put("simultaneousRiderOrders", "5");
		
	    ResponseEntity<String> result = orderService.setSimultaneousRiderOrders(info);
	    ResponseEntity<String> valueExpected = new ResponseEntity<>("Número de pedidos sumultaneos actualizado correctamente", HttpStatus.OK);
		assertEquals(valueExpected.getStatusCode(), result.getStatusCode());
		
		info.put("simultaneousRiderOrders", "-1" );
		
	    result = orderService.setSimultaneousRiderOrders(info);
	    valueExpected = new ResponseEntity<>("Número de pedidos no válido", HttpStatus.BAD_REQUEST);
		assertEquals(valueExpected.getStatusCode(), result.getStatusCode());
	}
	
	/*********************************************************************
	*
	* - Method name: test32
	* - Description of the Method: 
	* - Calling arguments: 
	* - Return value: 
	* - Required Files: 
	* - List of Checked Exceptions and an indication of when each exception
	* is thrown: 
	*
	*********************************************************************/
	@Test
	public void test32() {
	    Map<String, Object> info = new HashMap<String, Object>();
	    
		info.put("restaurantID", "1");
		JSONObject infoPlate = new JSONObject();
		infoPlate.put("price", 10);
		infoPlate.put("quantity", 1);
		JSONObject cart = new JSONObject();
		cart.put("1", infoPlate);	
		info.put("cart", cart);	
		
	    long idClient = 1;
		
		JSONObject jso = new JSONObject(info);	
		
		Mockito.when(service.makeOrder(jso, idClient)).thenReturn(new ResponseEntity<>(HttpStatus.OK));
		ResponseEntity<String> httpResponse = controller.makeOrderByEmail(info, "pepegamo@mail.com");	
		assertEquals(HttpStatus.EXPECTATION_FAILED, httpResponse.getStatusCode());
	}
	
	
}

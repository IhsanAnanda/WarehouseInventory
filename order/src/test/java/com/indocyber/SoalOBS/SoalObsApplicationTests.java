package com.indocyber.SoalOBS;

import com.indocyber.SoalOBS.dto.OrderDTO;
import com.indocyber.SoalOBS.service.OrderService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
class SoalObsApplicationTests {

	@Autowired
	OrderService orderService;

	@Test
	void testOrder1() throws Exception{
		OrderDTO.Request request = new OrderDTO.Request();
		request.setOrderNo("O1");
		OrderDTO.Response response = orderService.getOrderById(request);
		Assert.assertNotNull(response.getResult());
	}

}

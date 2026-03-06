package com.indocyber.SoalOBS;

import com.indocyber.SoalOBS.dto.GetInventoryDTO;
import com.indocyber.SoalOBS.service.InventoryService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
class SoalObsApplicationTests {

	@Autowired
	InventoryService inventoryService;

	@Test
	void testInventory1() throws Exception {
		GetInventoryDTO.Request request = new GetInventoryDTO.Request();
		request.setId(1);
		GetInventoryDTO.Response response = inventoryService.getInventoryById(request);
		Assert.assertNotNull(response.getResult());

	}

}

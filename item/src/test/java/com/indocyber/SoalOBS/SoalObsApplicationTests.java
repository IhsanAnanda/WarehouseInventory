package com.indocyber.SoalOBS;

import com.indocyber.SoalOBS.dto.GetItemDTO;
import com.indocyber.SoalOBS.service.ItemService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
class SoalObsApplicationTests {

	@Autowired
	private ItemService itemService;

	@Test
	void testItem1() throws Exception {
		GetItemDTO.Request request = new GetItemDTO.Request();
		request.setId(1);
		GetItemDTO.Response response = itemService.getItemById(request);
		Assert.assertNotNull(response.getResult());
	}

}

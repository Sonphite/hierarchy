package com.amazingco.hierarchy;

import com.amazingco.hierarchy.rest.dto.Node;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class HierarchyIntTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void findNodeByIdShouldReturnExpectedNode() throws Exception {
		final String parentContentAsString = this.mockMvc.perform(post("/create").param("parentNodeId", "3dcda40b-c245-459a-b001-35a7149ade30"))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andReturn()
				.getResponse()
				.getContentAsString();
		final Node parentNode = objectMapper.readValue(parentContentAsString, Node.class);

		final String nodeAsString = this.mockMvc.perform(get("/"+parentNode.getUuid()))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andReturn()
				.getResponse()
				.getContentAsString();
		Node fetchedNodeById = objectMapper.readValue(nodeAsString, Node.class);
		assertThat("Expected node has the expected uuid", fetchedNodeById.getUuid(), equalTo(parentNode.getUuid()));
	}



}

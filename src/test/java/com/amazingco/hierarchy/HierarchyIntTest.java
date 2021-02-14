package com.amazingco.hierarchy;

import com.amazingco.hierarchy.rest.dto.Node;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityManager;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
	@Autowired
	private EntityManager entityManager;
	private Node rootNode;


	@BeforeEach
	void setUp() throws Exception {
		final String parentContentAsString = this.mockMvc.perform(post("/create").param("parentNodeId", "3dcda40b-c245-459a-b001-35a7149ade30"))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andReturn()
				.getResponse()
				.getContentAsString();
		rootNode = objectMapper.readValue(parentContentAsString, Node.class);

	}

	@Test
	@DirtiesContext
	public void findNodeByIdShouldReturnExpectedNode() throws Exception {
		final String parentContentAsString = this.mockMvc.perform(post("/create").param("parentNodeId", rootNode.getUuid()))
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

	@Test
	@DirtiesContext
	public void updatingParentNodeToAKnownDescendantIsNotSuccessful() throws Exception {
		final String parentContentAsString = this.mockMvc.perform(post("/create").param("parentNodeId", rootNode.getUuid()))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andReturn()
				.getResponse()
				.getContentAsString();
		final Node parentNode = objectMapper.readValue(parentContentAsString, Node.class);

		final String nodeAsString = this.mockMvc.perform(post("/create").param("parentNodeId", parentNode.getUuid()))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andReturn()
				.getResponse()
				.getContentAsString();
		Node fetchedNodeById = objectMapper.readValue(nodeAsString, Node.class);

		this.mockMvc.perform(put("/"+parentNode.getUuid()+"/"+fetchedNodeById.getUuid()))
				.andDo(print())
				.andExpect(status().is4xxClientError());
	}

	@Test
	@DirtiesContext
	public void updatingParentNodeToNotADescendantNodeIsSuccessful() throws Exception {
		final String parentContentAsString = this.mockMvc.perform(post("/create").param("parentNodeId", rootNode.getUuid()))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andReturn()
				.getResponse()
				.getContentAsString();
		final Node rootNode = objectMapper.readValue(parentContentAsString, Node.class);

		final String firstChild = this.mockMvc.perform(post("/create").param("parentNodeId", rootNode.getUuid()))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andReturn()
				.getResponse()
				.getContentAsString();
		Node firstChildNode = objectMapper.readValue(firstChild, Node.class);

		final String secondChild = this.mockMvc.perform(post("/create").param("parentNodeId", firstChildNode.getUuid()))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andReturn()
				.getResponse()
				.getContentAsString();
		Node secondChildNode = objectMapper.readValue(secondChild, Node.class);

		this.mockMvc.perform(put("/"+secondChildNode.getUuid()+"/"+rootNode.getUuid()))
				.andDo(print())
				.andExpect(status().isOk());
	}


}

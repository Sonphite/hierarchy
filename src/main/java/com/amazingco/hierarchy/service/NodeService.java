package com.amazingco.hierarchy.service;

import com.amazingco.hierarchy.jpa.NodeRepository;
import com.amazingco.hierarchy.model.NodeEntity;
import com.amazingco.hierarchy.rest.dto.Node;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.EntityManagerFactoryAccessor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NodeService {
    @Autowired
    NodeRepository nodeRepository;
    @Autowired
    EntityManager entityManager;

    @Transactional(Transactional.TxType.REQUIRED)
    public Node createNodeEntityWithParent(String parentNodeId) {
        Optional<NodeEntity> parentNode = nodeRepository.findById(UUID.fromString(parentNodeId));
        final NodeEntity nodeEntity = new NodeEntity();
        if(parentNode.isEmpty()) {
            if(nodeRepository.findAll().size() != 0) {
                throw new IllegalArgumentException("We need a known parentNodeId");
            } else {
                log.info("Since no nodes are present we make this the root node and ignore supplied id: "+parentNodeId);
                return Node.fromEntity(nodeRepository.save(nodeEntity));
            }
        } else {
            nodeEntity.setParentNode(parentNode.get());
            return Node.fromEntity(nodeRepository.save(nodeEntity));
        }
    }

    public Node findNodeById(String uuid) {
        return nodeRepository.findById(UUID.fromString(uuid))
                .map(Node::fromEntity)
                .orElse(null);
    }

    public Collection<NodeEntity> allNodes() {
        return nodeRepository.findAll();
    }

    @Transactional
    public Collection<Node> childNodesOfNode(String nodeId) {
        Collection<NodeEntity> nodeEntityCollection = nodeRepository.findAllByParentNodeId(UUID.fromString(nodeId));
        return nodeEntityCollection.stream()
                .map(Node::fromEntity)
                .collect(Collectors.toSet());
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public boolean updateParentNode(String nodeId, String targetParentNodeId) {
        Optional<NodeEntity> targetParentEntity = nodeRepository.findById(UUID.fromString(targetParentNodeId));
        Optional<NodeEntity> nodeEntity = nodeRepository.findById(UUID.fromString(nodeId));
        if(targetParentEntity.isPresent() && nodeEntity.isPresent()) {
            if(isNodeDescendantOfNode(nodeEntity.get(), targetParentEntity.get())) {
                nodeEntity.get().setParentNode(targetParentEntity.get());
                return true;
            } else {
                log.warn("Conditions for changing parent node not met.");
                return false;
            }
        } else {
            log.warn("One of the supplied ids is not a nodeId. nodeIsPresent: {}, targetNodeIsPresent: {}", nodeEntity.isPresent(), targetParentEntity.isPresent() );
            return false;
        }
    }

    boolean isNodeDescendantOfNode(NodeEntity givenNode, NodeEntity targetParentNode) {
        if(givenNode.getUuid().equals(targetParentNode.getUuid())) {
            log.error("Tried to make a node its own parent");
            return false;
        }
        if(givenNode.getParentNode() == null) {
            log.error("Tried to make the root node a child of one of its descendants");
            return false;
        }
        NodeEntity nodeEntity = targetParentNode;
        while (nodeEntity.getParentNode() != null) {
            nodeEntity = nodeEntity.getParentNode();
            if(nodeEntity.getUuid().equals(givenNode.getUuid())) {
                log.warn("Trying to make one of the given nodes descendants its parent will break or split the treebase.");
                return false;
            }
        }
        return true;
    }


}

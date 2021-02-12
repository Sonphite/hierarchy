package com.amazingco.hierarchy.service;

import com.amazingco.hierarchy.jpa.NodeRepository;
import com.amazingco.hierarchy.model.NodeEntity;
import com.amazingco.hierarchy.rest.dto.Node;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class NodeService {
    @Autowired
    NodeRepository nodeRepository;

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

}

package com.amazingco.hierarchy.rest;

import com.amazingco.hierarchy.rest.dto.Node;
import com.amazingco.hierarchy.service.NodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class NodeRestResource {
    @Autowired
    NodeService nodeService;

    @PostMapping("/create")
    public Node createNode(@RequestParam("parentNodeId") String parentNodeId) {
        return nodeService.createNodeEntityWithParent(parentNodeId);
    }

    @GetMapping("/{nodeId}")
    public Node nodeById(@PathVariable("nodeId") String nodeId) {
        return nodeService.findNodeById(nodeId);
    }

    @GetMapping("/{nodeId}/children")
    public Collection<Node> childNodes(@PathVariable("nodeId") String nodeId) {
        return nodeService.childNodesOfNode(nodeId);
    }

    @PutMapping("/{nodeId}/{parentNodeId}")
    public ResponseEntity<Void> updateParentNode(@PathVariable("nodeId") String nodeId, @PathVariable("parentNodeId") String parentNodeId) {
        if(nodeService.updateParentNode(nodeId, parentNodeId)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/all")
    public Collection<Node> allNodes() {
        return nodeService.allNodes().stream()
                .map(Node::fromEntity)
                .collect(Collectors.toSet());
    }


}

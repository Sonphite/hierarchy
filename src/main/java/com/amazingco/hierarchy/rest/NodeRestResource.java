package com.amazingco.hierarchy.rest;

import com.amazingco.hierarchy.rest.dto.Node;
import com.amazingco.hierarchy.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
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


    @GetMapping("/all")
    public Collection<Node> allNodes() {
        return nodeService.allNodes().stream()
                .map(Node::fromEntity)
                .collect(Collectors.toSet());
    }


}

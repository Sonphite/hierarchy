package com.amazingco.hierarchy.rest.dto;

import com.amazingco.hierarchy.model.NodeEntity;
import lombok.Builder;
import lombok.Value;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Value
@Builder
public class Node {
    String uuid;
    int height;
    Node parentNode;
    Collection<String> childNodeIds;

    public static Node fromEntity(NodeEntity nodeEntity) {
        return Node.builder()
                .uuid(nodeEntity.getUuid().toString())
                .parentNode(Optional.ofNullable(nodeEntity.getParentNode())
                        .map(Node::fromEntity)
                        .orElse(null))
                .height(nodeHeight(nodeEntity))
                .childNodeIds(nodeEntity.getChildNodes().stream().map(NodeEntity::getUuidAsString).collect(Collectors.toSet()))
                .build();

    }


    static int nodeHeight(NodeEntity nodeEntity) {
        return nodeEntity.getParentNode() != null ? (1 + nodeHeight(nodeEntity.getParentNode())) : 0;
    }
}

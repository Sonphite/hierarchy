package com.amazingco.hierarchy.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@NamedQueries({
        @NamedQuery(name = NodeEntity.FIND_BY_PARENT_NODE_ID, query = " select ne from NodeEntity ne where ne.parentNode.uuid = :uuid"),
        @NamedQuery(name = NodeEntity.UPDATE_PARENT_NODE_ID, query = " update NodeEntity set parentNode = :parentNode where uuid = :uuid")
})
public class NodeEntity {
    final static String FIND_BY_PARENT_NODE_ID = "NodeEntity.findAllByParentNodeId";
    final static String UPDATE_PARENT_NODE_ID = "NodeEntity.updateParentNode";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID uuid;
    @ManyToOne(fetch = FetchType.EAGER)
    NodeEntity parentNode;
    @OneToMany(mappedBy = "parentNode", fetch = FetchType.LAZY)
    Collection<NodeEntity> childNodes = Collections.emptySet();

    public String getUuidAsString() {
        return uuid.toString();
    }

    public final NodeEntity rootNode() {
        if(this.parentNode != null) {
            return this.getParentNode().rootNode();
        } else {
            return this;
        }
    }
}

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
@Table(name = "NODE_ENTITY")
public class NodeEntity {
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

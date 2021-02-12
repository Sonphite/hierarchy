package com.amazingco.hierarchy.jpa;

import com.amazingco.hierarchy.model.NodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NodeRepository extends JpaRepository<NodeEntity, UUID> {


}

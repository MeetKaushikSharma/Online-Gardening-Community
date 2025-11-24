package com.gardening.repository;

import com.gardening.entity.PendingContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PendingContentRepository extends JpaRepository<PendingContent, Integer> {
}

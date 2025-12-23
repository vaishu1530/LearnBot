package com.example.learnbot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.learnbot.model.ChatHistory;

public interface ChatRepository extends JpaRepository<ChatHistory, Long> {
	    // This looks at the 'user' field you just added and filters by its ID
	    List<ChatHistory> findByUserIdOrderByIdDesc(Long userId);
	}


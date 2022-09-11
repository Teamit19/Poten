package com.example.poten.repository;

import com.example.poten.domain.Follow;
import com.example.poten.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    List<Follow> findAllByFollower(User user);

    @Modifying
    @Transactional
    void deleteByFollowingIdAndFollowerId(Long clubId, Long userId);




}

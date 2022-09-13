package com.example.poten.repository;

import com.example.poten.domain.Interest;
import com.example.poten.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterestRepository extends JpaRepository<Interest, Long> {

    List<Interest> findAllByUser(User user);

}

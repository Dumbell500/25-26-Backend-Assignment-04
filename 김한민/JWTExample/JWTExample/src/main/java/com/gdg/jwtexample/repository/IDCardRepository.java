package com.gdg.jwtexample.repository;

import com.gdg.jwtexample.domain.IDCard;
import com.gdg.jwtexample.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDCardRepository extends JpaRepository<IDCard, Long> {
    List<IDCard> findAllByUser(User user);
    Optional<IDCard> findByIdAndUser(Long id, User user);
}

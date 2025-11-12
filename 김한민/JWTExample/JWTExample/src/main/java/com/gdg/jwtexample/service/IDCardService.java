package com.gdg.jwtexample.service;

import com.gdg.jwtexample.domain.IDCard;
import com.gdg.jwtexample.domain.User;
import com.gdg.jwtexample.dto.idcard.IDCardResponse;
import com.gdg.jwtexample.dto.idcard.IDCardSaveRequest;
import com.gdg.jwtexample.dto.idcard.IDCardUpdateRequest;
import com.gdg.jwtexample.repository.IDCardRepository;
import com.gdg.jwtexample.repository.UserRepository;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class IDCardService {

    private final IDCardRepository idCardRepository;
    private final UserRepository userRepository;

    public IDCardService(IDCardRepository idCardRepository, UserRepository userRepository) {
        this.idCardRepository = idCardRepository;
        this.userRepository = userRepository;
    }

    private User currentUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));
    }

    /**  읽기 전용 (성능 최적화 가능) */
    @Transactional(readOnly = true)
    public List<IDCardResponse> list(Principal principal) {
        User user = currentUser(principal.getName());
        return idCardRepository.findAllByUser(user).stream()
                .map(IDCardResponse::from)
                .collect(Collectors.toList());
    }

    /** 저장(쓰기 트랜잭션 필요) */
    @Transactional
    public IDCardResponse save(Principal principal, IDCardSaveRequest req) {
        User user = currentUser(principal.getName());
        IDCard card = new IDCard(
                user,
                req.realName(),
                req.nationalId(),
                req.birth(),
                LocalDate.now()
        );
        idCardRepository.save(card);
        return IDCardResponse.from(card);
    }

    /** 수정(쓰기 트랜잭션 필요) */
    @Transactional
    public IDCardResponse update(Principal principal, Long id, IDCardUpdateRequest req) {
        User user = currentUser(principal.getName());
        IDCard card = idCardRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new NoSuchElementException("신분증을 찾을 수 없습니다."));

        card.update(req.realName(), req.nationalId(), req.birth());
        return IDCardResponse.from(card);
    }

    /** 삭제(쓰기 트랜잭션 필요) */
    @Transactional
    public void delete(Principal principal, Long id) {
        User user = currentUser(principal.getName());
        IDCard card = idCardRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new NoSuchElementException("신분증을 찾을 수 없습니다."));

        idCardRepository.delete(card);
    }
}

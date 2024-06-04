package com.savemyreceipt.smr.infrastructure;

import com.savemyreceipt.smr.domain.Group;
import com.savemyreceipt.smr.exception.ErrorStatus;
import com.savemyreceipt.smr.exception.model.CustomException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    Optional<Group> findById(Long id);

    List<Group> findByNameContaining(String keyword);

    Page<Group> findByNameContaining(String keyword, Pageable pageable);

    default Group getGroupById(Long id) {
        return findById(id).orElseThrow(
            () -> new CustomException(ErrorStatus.GROUP_NOT_FOUND, ErrorStatus.GROUP_NOT_FOUND.getMessage())
        );
    }




}

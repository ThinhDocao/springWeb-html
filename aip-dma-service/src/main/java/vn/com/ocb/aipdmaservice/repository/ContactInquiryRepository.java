package vn.com.ocb.aipdmaservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.ocb.aipdmaservice.entity.ContactInquiryEntity;

import java.util.List;

@Repository
public interface ContactInquiryRepository extends JpaRepository<ContactInquiryEntity, Long> {
    List<ContactInquiryEntity> findAllByOrderByCreatedAtDesc();
    List<ContactInquiryEntity> findTop10ByOrderByCreatedAtDesc();
}

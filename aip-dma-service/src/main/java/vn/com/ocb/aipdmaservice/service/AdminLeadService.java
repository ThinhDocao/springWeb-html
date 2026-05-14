package vn.com.ocb.aipdmaservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.ocb.aipdmaservice.entity.ContactInquiryEntity;
import vn.com.ocb.aipdmaservice.entity.OrderEntity;
import vn.com.ocb.aipdmaservice.entity.OrderItemEntity;
import vn.com.ocb.aipdmaservice.model.admin.AdminLead;
import vn.com.ocb.aipdmaservice.repository.ContactInquiryRepository;
import vn.com.ocb.aipdmaservice.repository.OrderRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminLeadService {

    private final OrderRepository orderRepository;
    private final ContactInquiryRepository contactInquiryRepository;

    public List<AdminLead> getAllLeads() {
        List<AdminLead> leads = new ArrayList<>();
        for (OrderEntity order : orderRepository.findAllByOrderByCreatedAtDesc()) {
            leads.add(mapOrder(order));
        }
        for (ContactInquiryEntity inquiry : contactInquiryRepository.findAllByOrderByCreatedAtDesc()) {
            leads.add(mapInquiry(inquiry));
        }
        leads.sort(Comparator.comparing(AdminLead::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder())));
        return leads;
    }

    public List<AdminLead> getRecentLeads(int limit) {
        return getAllLeads().stream().limit(limit).collect(Collectors.toList());
    }

    public long countNewLeads() {
        return getAllLeads().stream()
                .filter(lead -> "NEW".equals(normalizeStatus(lead.getStatus()))
                        || "PENDING".equals(normalizeStatus(lead.getStatus())))
                .count();
    }

    public AdminLead findLead(String type, Long id) {
        if ("ORDER".equalsIgnoreCase(type)) {
            return orderRepository.findById(id).map(this::mapOrder).orElse(null);
        }
        return contactInquiryRepository.findById(id).map(this::mapInquiry).orElse(null);
    }

    @Transactional
    public void updateStatus(String type, Long id, String status) {
        String normalized = normalizeStatus(status);
        if ("ORDER".equalsIgnoreCase(type)) {
            orderRepository.findById(id).ifPresent(order -> {
                order.setStatus(normalized);
                orderRepository.save(order);
            });
        } else {
            contactInquiryRepository.findById(id).ifPresent(inquiry -> {
                inquiry.setStatus(normalized);
                inquiry.setRead(!"NEW".equals(normalized));
                contactInquiryRepository.save(inquiry);
            });
        }
    }

    public OrderEntity findOrder(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public ContactInquiryEntity findInquiry(Long id) {
        return contactInquiryRepository.findById(id).orElse(null);
    }

    private AdminLead mapOrder(OrderEntity order) {
        String source = "Giỏ hàng / Xác nhận đơn";
        if (order.getItems() != null && !order.getItems().isEmpty()) {
            source = order.getItems().stream()
                    .map(OrderItemEntity::getProductName)
                    .collect(Collectors.joining(", "));
        }
        return AdminLead.builder()
                .id(order.getId())
                .type("ORDER")
                .code(order.getOrderCode())
                .fullName(order.getFullName())
                .phone(order.getPhone())
                .email(order.getEmail())
                .subject(order.getConfirmationMethod())
                .message(order.getNote())
                .source(source)
                .status(normalizeStatus(order.getStatus()))
                .totalAmount(order.getTotalAmount())
                .createdAt(order.getCreatedAt())
                .build();
    }

    private AdminLead mapInquiry(ContactInquiryEntity inquiry) {
        String source = inquiry.getSourcePage();
        if (inquiry.getProductName() != null && !inquiry.getProductName().trim().isEmpty()) {
            source = inquiry.getProductName();
        }
        if (source == null || source.trim().isEmpty()) {
            source = "Form liên hệ";
        }
        return AdminLead.builder()
                .id(inquiry.getId())
                .type("CONTACT")
                .code("LH-" + inquiry.getId())
                .fullName(inquiry.getFullName())
                .phone(inquiry.getPhone())
                .email(inquiry.getEmail())
                .subject(inquiry.getSubject())
                .message(inquiry.getMessage())
                .source(source)
                .status(normalizeStatus(inquiry.getStatus()))
                .createdAt(inquiry.getCreatedAt())
                .build();
    }

    private String normalizeStatus(String status) {
        if (status == null || status.trim().isEmpty() || "PENDING".equalsIgnoreCase(status)) {
            return "NEW";
        }
        return status.trim().toUpperCase();
    }
}

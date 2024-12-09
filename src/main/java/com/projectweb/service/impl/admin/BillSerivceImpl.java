package com.projectweb.service.impl.admin;

import com.projectweb.model.OgnBillingDetail;
import com.projectweb.model.OgnOrder;
import com.projectweb.model.dto.RevenueDto;
import com.projectweb.reponsitory.admin.BillReponsitory;
import com.projectweb.service.admin.BillService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BillSerivceImpl implements BillService {

    @Autowired
    private BillReponsitory billReponsitory ;

    @Override
    public Page<OgnBillingDetail> findAll(int pageNumber, String search) {
        try {
            Pageable pageable = PageRequest.of(pageNumber - 1, 10);
            Specification<OgnBillingDetail> specification = Specification.where(null);

            if (Objects.nonNull(search)) {
                specification = specification
                        .or((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("userid").get("username"), "%" + search + "%"))
                        .or((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("ward"), "%" + search + "%"))
                        .or((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("district"), "%" + search + "%"))
                        .or((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("city"), "%" + search + "%"))
                        .or((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("streetaddress"), "%" + search + "%"))
                        .or((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + search + "%"));


                // Thêm điều kiện tìm kiếm theo id
                try {
                    Long id = Long.parseLong(search);
                    BigDecimal price = new BigDecimal(search); // Chuyển search thành Long
                    specification = specification
                            .or( (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("shippingFee"), price))
                            .or( (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id));
                } catch (NumberFormatException e) {
                    // Nếu không thể chuyển đổi search thành Long, thì bỏ qua phần này
                    System.out.println("search không phải kiểu Long, bỏ qua tìm kiếm theo id");
                }
            }

            Page<OgnBillingDetail> page = billReponsitory.findAll(specification,pageable);
            return page != null ? page : Page.empty(pageable); // Trả về Page trống nếu không có dữ liệu
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Page.empty(); // Trả về Page trống nếu có lỗi
    }

    @Override
    public List<OgnBillingDetail> findAll() {
        try {
            return billReponsitory.findAll();
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public OgnBillingDetail findByOrderId(OgnOrder order) {
        try {
            return billReponsitory.findByOrderid(order);
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public OgnBillingDetail findById(long id) throws UsernameNotFoundException {
        Optional<OgnBillingDetail> optionalOgnBillingDetail = billReponsitory.findFirstById(id);
        return optionalOgnBillingDetail.orElse(null);
    }


    @Override
    @Transactional
    public OgnBillingDetail insertBill(OgnBillingDetail ognBillingDetail) {
        try {
            ognBillingDetail.setCity(ognBillingDetail.getCity());
            ognBillingDetail.setDistrict(ognBillingDetail.getDistrict());
            ognBillingDetail.setName(ognBillingDetail.getName());
            ognBillingDetail.setUserid(ognBillingDetail.getUserid());
            ognBillingDetail.setWard(ognBillingDetail.getWard());
            ognBillingDetail.setPhone(ognBillingDetail.getPhone());
            ognBillingDetail.setStreetaddress(ognBillingDetail.getStreetaddress());
            ognBillingDetail.setPaymentMethod(ognBillingDetail.getPaymentMethod());
            ognBillingDetail.setShippingFee(ognBillingDetail.getShippingFee());
            return billReponsitory.save(ognBillingDetail);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public OgnBillingDetail updateBill(OgnBillingDetail billingDetail) {
        try {
            // Chuyển đổi String thành Long
            Long userIdString = billingDetail.getId();

            // Tìm kiếm người dùng theo userid dưới dạng Long
            OgnBillingDetail ognBillingDetail = billReponsitory.findFirstById(userIdString)
                    .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

            // Cập nhật thông tin người dùng
            ognBillingDetail.setCity(billingDetail.getCity());
            ognBillingDetail.setDistrict(billingDetail.getDistrict());
            ognBillingDetail.setName(billingDetail.getName());
            ognBillingDetail.setUserid(billingDetail.getUserid());
            ognBillingDetail.setWard(billingDetail.getWard());
            ognBillingDetail.setPhone(billingDetail.getPhone());
            ognBillingDetail.setStreetaddress(billingDetail.getStreetaddress());
            ognBillingDetail.setPaymentMethod(billingDetail.getPaymentMethod());
            ognBillingDetail.setShippingFee(billingDetail.getShippingFee());

            // Lưu người dùng đã cập nhật
            return billReponsitory.save(ognBillingDetail);
        } catch (RuntimeException ex) {
            // Xử lý riêng cho RuntimeException
            System.err.println(ex.getMessage()); // In ra thông báo lỗi
            throw ex; // Ném lại lỗi để xử lý tiếp
        } catch (Exception ex) {
            // Xử lý cho các trường hợp ngoại lệ khác
            ex.printStackTrace();
        }
        return null;
    }


    @Override
    public boolean deleteBill(long id) {
        try {
            OgnBillingDetail billingDetail = billReponsitory.findFirstById(id).orElse(null);
            billReponsitory.delete(billingDetail);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public List<RevenueDto> getMonthlyRevenue() {
        List<Object[]> results = billReponsitory.findMonthlyRevenue();

        // Chuyển đổi kết quả từ Object[] sang DTO
        return results.stream()
                .map(result -> new RevenueDto(
                        ((Number) result[0]).intValue(), // Month
                        ((Number) result[1]).intValue(), // Year
                        ((Number) result[2]).longValue()  // TotalRevenue
                ))
                .collect(Collectors.toList());
    }


}

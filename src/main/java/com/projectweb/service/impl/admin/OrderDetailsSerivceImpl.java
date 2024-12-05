package com.projectweb.service.impl.admin;

import com.projectweb.model.OgnOrder;
import com.projectweb.model.OgnOrderdetail;
import com.projectweb.model.dto.TotalOrderItemDTO;
import com.projectweb.reponsitory.admin.OrderDetailsReponsitory;
import com.projectweb.service.admin.OrderDetailsService;
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
import java.util.*;

@Service
@AllArgsConstructor
public class OrderDetailsSerivceImpl implements OrderDetailsService {

    @Autowired
    private OrderDetailsReponsitory orderDetailsReponsitory ;

    @Override
    public Page<OgnOrderdetail> findAll(int pageNumber, String search) {
        try {
            Pageable pageable = PageRequest.of(pageNumber - 1, 10);
            Specification<OgnOrderdetail> specification = Specification.where(null);

            if (Objects.nonNull(search)) {
                specification = specification
                        .or((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("productid").get("productname"), "%" + search + "%"));


                // Thêm điều kiện tìm kiếm theo id
                try {
                    Long id = Long.parseLong(search);
                    BigDecimal price = new BigDecimal(search); // Chuyển search thành Long
                    specification = specification
                            .or( (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("price"), price))
                            .or( (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("quantity"), id))
                            .or( (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("orderid").get("id"), id))
                            .or( (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id));
                } catch (NumberFormatException e) {
                    // Nếu không thể chuyển đổi search thành Long, thì bỏ qua phần này
                    System.out.println("search không phải kiểu Long, bỏ qua tìm kiếm theo id");
                }
            }

            Page<OgnOrderdetail> page = orderDetailsReponsitory.findAll(specification,pageable);
            return page != null ? page : Page.empty(pageable); // Trả về Page trống nếu không có dữ liệu
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Page.empty(); // Trả về Page trống nếu có lỗi
    }

    @Override
    public List<OgnOrderdetail> findAll() {
        try {
            return orderDetailsReponsitory.findAll();
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public List<TotalOrderItemDTO> findTotal() {
        try {
            // Tạo danh sách để lưu kết quả
            List<TotalOrderItemDTO> listTotal = new ArrayList<>();

            // Map để nhóm dữ liệu theo ID và tính tổng số lượng
            Map<Long, Long> quantityMap = new HashMap<>();

            // Duyệt qua danh sách orderDetails
            List<OgnOrderdetail> list = orderDetailsReponsitory.findAll();
            for (OgnOrderdetail ognOrderdetail : list) {
                Long id = ognOrderdetail.getOrderid().getId();
                Long quantity = ognOrderdetail.getQuantity();

                // Tính tổng số lượng cho mỗi ID
                quantityMap.put(id, quantityMap.getOrDefault(id, 0L) + quantity);
            }

            // Chuyển đổi từ Map sang danh sách TotalOrderItemDTO
            for (Map.Entry<Long, Long> entry : quantityMap.entrySet()) {
                TotalOrderItemDTO totalItem = new TotalOrderItemDTO();
                totalItem.setId(entry.getKey());
                totalItem.setQuantity(entry.getValue());
                listTotal.add(totalItem);
            }
            return listTotal;
            // Kết quả: listTotal chứa danh sách các ID và tổng quantity
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public List<OgnOrderdetail>  findById(OgnOrder order) throws UsernameNotFoundException {
        List<OgnOrderdetail> optionalOgnOrderdetail = orderDetailsReponsitory.findByOrderid(order);
        return optionalOgnOrderdetail;
    }


    @Override
    @Transactional
    public OgnOrderdetail insertOrderdetail(OgnOrderdetail orderdetail) {
        try {
            orderdetail.setPrice(orderdetail.getPrice());
            orderdetail.setQuantity(orderdetail.getQuantity());
            orderdetail.setProductid(orderdetail.getProductid());
            orderdetail.setOrderid(orderdetail.getOrderid());
            return orderDetailsReponsitory.save(orderdetail);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public OgnOrderdetail updateOrderdetail(OgnOrderdetail orderdetail) {
        try {
            // Chuyển đổi String thành Long
            Long userIdString = orderdetail.getId();

            // Tìm kiếm người dùng theo userid dưới dạng Long
            OgnOrderdetail existingOrder = orderDetailsReponsitory.findFirstById(userIdString)
                    .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

            // Cập nhật thông tin người dùng
            existingOrder.setPrice(orderdetail.getPrice());
            existingOrder.setQuantity(orderdetail.getQuantity());
            existingOrder.setProductid(orderdetail.getProductid());
            existingOrder.setOrderid(orderdetail.getOrderid());

            // Lưu người dùng đã cập nhật
            return orderDetailsReponsitory.save(existingOrder);
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
    public boolean deleteOrderdetail(long id) {
        try {
            OgnOrderdetail ognOrderdetail = orderDetailsReponsitory.findFirstById(id).orElse(null);
            orderDetailsReponsitory.delete(ognOrderdetail);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }


}

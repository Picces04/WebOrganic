package com.projectweb.service.impl.admin;

import com.projectweb.model.OgnOrder;
import com.projectweb.reponsitory.admin.OrderReponsitory;
import com.projectweb.service.admin.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderSerivceImpl implements OrderService {

    @Autowired
    private OrderReponsitory orderReponsitory ;

    @Override
    public Page<OgnOrder> findAll(int pageNumber, String search) {
        try {
            // Thêm sắp xếp theo orderdate (mới nhất trước)
            Pageable pageable = PageRequest.of(pageNumber - 1, 10, Sort.by(Sort.Direction.DESC, "orderdate"));
            Specification<OgnOrder> specification = Specification.where(null);

            if (Objects.nonNull(search) && !search.isEmpty()) {
                specification = specification
                        .or((root, query, criteriaBuilder) ->
                                criteriaBuilder.like(root.get("status"), "%" + search + "%"))
                        .or((root, query, criteriaBuilder) ->
                                criteriaBuilder.like(root.get("userid").get("userid"), "%" + search + "%"));

                // Thêm điều kiện tìm kiếm theo id
                try {
                    Long id = Long.parseLong(search); // Chuyển search thành Long
                    specification = specification
                            .or((root, query, criteriaBuilder) ->
                                    criteriaBuilder.equal(root.get("id"), id));
                } catch (NumberFormatException e) {
                    // Nếu không thể chuyển đổi search thành Long, thì bỏ qua phần này
                    System.out.println("search không phải kiểu Long, bỏ qua tìm kiếm theo id");
                }
            }

            // Thực hiện tìm kiếm và phân trang
            Page<OgnOrder> page = orderReponsitory.findAll(specification, pageable);
            return page != null ? page : Page.empty(pageable); // Trả về Page trống nếu không có dữ liệu
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Page.empty(); // Trả về Page trống nếu có lỗi
    }


    @Override
    public List<OgnOrder> findAll() {
        try {
            return orderReponsitory.findAll();
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public OgnOrder findById(long id) throws UsernameNotFoundException {
        Optional<OgnOrder> optionalOgnOrder = orderReponsitory.findFirstById(id);
        return optionalOgnOrder.orElse(null);
    }

    @Override
    public List SumYear() {
        return List.of();
    }


    @Override
    @Transactional
    public OgnOrder insertOrder(OgnOrder order) {
        try {
            order.setUserid(order.getUserid());
            order.setStatus(order.getStatus());
            order.setOrderdate(order.getOrderdate());
            return orderReponsitory.save(order);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public OgnOrder updateOrder(OgnOrder order) {
        try {
            // Chuyển đổi String thành Long
            Long userIdString = order.getId();

            // Tìm kiếm người dùng theo userid dưới dạng Long
            OgnOrder existingOrder = orderReponsitory.findFirstById(userIdString)
                    .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

            // Cập nhật thông tin người dùng
            if (order.getUserid() != null && order.getOrderdate()!= null) {
                existingOrder.setUserid(order.getUserid());
                existingOrder.setOrderdate(order.getOrderdate());
            }
            existingOrder.setStatus(order.getStatus());
            // Lưu người dùng đã cập nhật
            return orderReponsitory.save(existingOrder);
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
    public boolean deleteOrder(long id) {
        try {
            OgnOrder ognOrder = orderReponsitory.findFirstById(id).orElse(null);
            orderReponsitory.delete(ognOrder);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }


}

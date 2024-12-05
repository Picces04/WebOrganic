package com.projectweb.service.impl.admin;

import com.projectweb.model.OgnSaleprice;
import com.projectweb.reponsitory.admin.SaleReponsitory;
import com.projectweb.service.admin.SaleService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;


@Service
@AllArgsConstructor
public class SaleServiceImpl implements SaleService {
    @Autowired
    SaleReponsitory saleReponsitory;

    @Override
    public Page<OgnSaleprice> findAll(String search, int pageNumber) {
        try {
            // Khởi tạo Pageable cho phân trang
            Pageable pageable = PageRequest.of(pageNumber - 1, 10);

            // Khởi tạo Specification ban đầu
            Specification<OgnSaleprice> specification = Specification.where(null);

            if (Objects.nonNull(search) && !search.trim().isEmpty()) {
                // Thêm điều kiện tìm kiếm
                try {
                    // Kiểm tra xem search có phải là Long, BigDecimal, hay LocalDate không và áp dụng các điều kiện tìm kiếm tương ứng
                    Long id = null;
                    BigDecimal sale = null;
                    LocalDate date = null;

                    try {
                        id = Long.parseLong(search);
                    } catch (NumberFormatException e) {
                        // Nếu không thể chuyển đổi search thành Long, bỏ qua
                    }

                    try {
                        sale = new BigDecimal(search);
                    } catch (NumberFormatException e) {
                        // Nếu không thể chuyển đổi search thành BigDecimal, bỏ qua
                    }

                    try {
                        date = LocalDate.parse(search);
                    } catch (Exception e) {
                        // Nếu không thể chuyển đổi search thành LocalDate, bỏ qua
                    }

                    // Thêm điều kiện tìm kiếm vào Specification
                    if (id != null) {
                        Long finalId = id;
                        specification = specification.or((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("productid").get("id"), finalId))
                                .or((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), finalId));
                    }
                    if (sale != null) {
                        BigDecimal finalSale = sale;
                        specification = specification.or((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("salepercentage"), finalSale));
                    }
                    if (date != null) {
                        LocalDate finalDate = date;
                        specification = specification.or((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("startdate"), finalDate))
                                .or((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("enddate"), finalDate));
                    }

                } catch (Exception e) {
                    System.out.println("Lỗi khi tìm kiếm: " + e.getMessage());
                }
            }

            // Trả về kết quả phân trang
            Page<OgnSaleprice> page = saleReponsitory.findAll(specification, pageable);
            return page != null ? page : Page.empty(pageable); // Trả về Page trống nếu không có dữ liệu

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Page.empty(); // Trả về Page trống nếu có lỗi
    }


    @Override
    public List<OgnSaleprice> findAll() {
        try {
            return saleReponsitory.findAll();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public List<OgnSaleprice> findSaleTheDay() {
        try {
            LocalDate tomorrow = LocalDate.now().plusDays(1); // Ngày mai
            return saleReponsitory.findSaleTheDay(tomorrow); // Truy vấn bằng ngày mai
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public OgnSaleprice insertSale(OgnSaleprice Saleprice) {
        try {
            Saleprice.setId(Saleprice.getId());
            Saleprice.setProductid(Saleprice.getProductid());
            Saleprice.setCategoryid(Saleprice.getCategoryid());
            Saleprice.setSalepercentage(Saleprice.getSalepercentage());
            Saleprice.setStartdate(Saleprice.getStartdate());
            Saleprice.setEnddate(Saleprice.getEnddate());
            return saleReponsitory.save(Saleprice);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public OgnSaleprice updateSale(OgnSaleprice saleprice) {
        try {
            // Chuyển đổi String thành Long
            Long ID = saleprice.getId();

            // Tìm kiếm người dùng theo id dưới dạng Long
            OgnSaleprice existingSale = saleReponsitory.findFirstById(ID)
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

            // Cập nhật thông tin người dùng
            existingSale.setSalepercentage(saleprice.getSalepercentage());
            existingSale.setStartdate(saleprice.getStartdate());
            existingSale.setEnddate(saleprice.getEnddate());

            // Lưu người dùng đã cập nhật
            return saleReponsitory.save(existingSale);
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
    public boolean deleteSale(long id) {
        try {
            OgnSaleprice saleprice = saleReponsitory.findFirstById(id).orElse(null);
            saleReponsitory.delete(saleprice);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}

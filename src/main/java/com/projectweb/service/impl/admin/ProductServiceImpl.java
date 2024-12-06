package com.projectweb.service.impl.admin;

import com.projectweb.model.OgnProduct;
import com.projectweb.reponsitory.admin.ProductReponsitory;
import com.projectweb.service.admin.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductReponsitory productReponsitory;

    @Override
    public Page<OgnProduct> findAll(String search, int pageNumber, Sort sort) {
        try {
            // Truyền sort vào PageRequest để đảm bảo sắp xếp
            Pageable pageable = PageRequest.of(pageNumber - 1, 20, sort);

            Specification<OgnProduct> specification = Specification.where(null);

            if (Objects.nonNull(search)) {
                specification = specification
                        .or((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("productname"), "%" + search + "%"))
                        .or((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("shortdesc"), "%" + search + "%"))
                        .or((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("description"), "%" + search + "%"))
                        .or((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("unit"), "%" + search + "%"))
                        .or((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("image"), "%" + search + "%"))
                        .or((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("listimage"), "%" + search + "%"))
                        .or((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("categoryid").get("categoryname")), "%" + search.toLowerCase() + "%"));
                try {
                    Long id = Long.parseLong(search);
                    BigDecimal price = new BigDecimal(search);
                    specification = specification
                            .or((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id))
                            .or((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("price"), price))
                            .or((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("stock"), id));

                } catch (NumberFormatException e) {
                    System.out.println("search không phải kiểu Long, bỏ qua tìm kiếm theo id");
                }
            }

            // Thực hiện truy vấn và trả về kết quả
            Page<OgnProduct> page = productReponsitory.findAll(specification, pageable);
            return page != null ? page : Page.empty(pageable);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Page.empty(); // Trả về Page trống nếu có lỗi
    }


    @Override
    public List<OgnProduct> findAll() {
        try {
            return productReponsitory.findAll();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public OgnProduct findOne(Long id) {
        Optional<OgnProduct> product = productReponsitory.findById(id);
        return product.orElse(null); // Trả về null nếu không tìm thấy sản phẩm
    }

    @Override
    public Integer Totalproduct() {
        List<OgnProduct> products = productReponsitory.findAll();
        return products.size();
    }

    @Override
    public OgnProduct insertProduct(OgnProduct product) {
        try {
            product.setId(product.getId());
            product.setCategoryid(product.getCategoryid());
            product.setShortdesc(product.getShortdesc());
            product.setDescription(product.getDescription());
            product.setProductname(product.getProductname());
            product.setImage(product.getImage());
            product.setListimage(product.getListimage());
            product.setPrice(product.getPrice());
            product.setStock(product.getStock());
            product.setUnit(product.getUnit());
            return productReponsitory.save(product);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public OgnProduct updateProduct(OgnProduct product) {

        try {
            // Chuyển đổi String thành Long
            Long productID = product.getId();

            // Tìm kiếm người dùng theo userid dưới dạng Long
            OgnProduct existingProduct = productReponsitory.findFirstById(productID)
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

            // Cập nhật thông tin người
            existingProduct.setImage(product.getImage());
            existingProduct.setListimage(product.getListimage());
            existingProduct.setCategoryid(product.getCategoryid());
            existingProduct.setProductname(product.getProductname());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setStock(product.getStock());
            existingProduct.setUnit(product.getUnit());
            existingProduct.setShortdesc(product.getShortdesc());

            // Lưu người dùng đã cập nhật
            return productReponsitory.save(existingProduct);
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
    public boolean deleteProduct(long id) {
        try {
            OgnProduct ognProduct = productReponsitory.findFirstById(id).orElse(null);
            productReponsitory.delete(ognProduct);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}

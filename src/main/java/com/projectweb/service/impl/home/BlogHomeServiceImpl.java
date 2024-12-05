package com.projectweb.service.impl.home;


import com.projectweb.model.OgnBlog;
import com.projectweb.model.OgnProduct;
import com.projectweb.model.OgnSaleprice;
import com.projectweb.reponsitory.home.BlogHomeReponsitory;
import com.projectweb.reponsitory.home.ShopCategoryReponsitory;
import com.projectweb.reponsitory.home.ShopReponsitory;
import com.projectweb.service.home.BlogHomeService;
import com.projectweb.service.home.ShopCategoryService;
import jakarta.persistence.criteria.Join;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Service
@AllArgsConstructor
public class BlogHomeServiceImpl implements BlogHomeService {
    BlogHomeReponsitory blogHomeReponsitory;

    @Override
    public Page<OgnBlog> findAll(int pageNumber, String search, String tag) {
        try {
            int pageSize = 16;
            // Tạo Pageable với sắp xếp theo blogdate giảm dần (mới nhất trước)
            Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(Sort.Order.desc("blogdate")));

            // Tạo Specification
            Specification<OgnBlog> specification = Specification.where(null);
            if (Objects.nonNull(search)) {
                specification = specification
                        .or((root, query, criteriaBuilder) ->criteriaBuilder.like(root.get("tag"), '%' + search + '%' ))
                        .or((root, query, criteriaBuilder) ->criteriaBuilder.like(root.get("blogshortdesc"), '%' + search + '%' ))
                        .or((root, query, criteriaBuilder) ->criteriaBuilder.like(root.get("blogname"), '%' + search + '%' ));
            }
            if (Objects.nonNull(tag)) {
                specification = specification
                        .or((root, query, criteriaBuilder) ->criteriaBuilder.like(root.get("tag"), '%' + tag+ '%' ));
            }
            // Áp dụng Specification và Pageable
            return blogHomeReponsitory.findAll(specification, pageable);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Page.empty();
    }

    @Override
    public OgnBlog findOne(Long id) {
        try {
            return blogHomeReponsitory.findById(id);
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public List<OgnBlog> findAll() {
        try {
            return blogHomeReponsitory.findAll();
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}

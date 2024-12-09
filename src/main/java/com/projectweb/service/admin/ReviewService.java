package com.projectweb.service.admin;

import com.projectweb.model.OgnProduct;
import com.projectweb.model.OgnStarrating;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface ReviewService {
    Page<OgnStarrating> findAll(String search , int pageNumber);
    List<OgnStarrating> findAll();

    List<OgnStarrating> findStatus();
    Map<OgnProduct, Integer> getAverageStarRatings();
    Integer Totalreview();

    //CRUD Admin
    OgnStarrating insertReview(OgnStarrating review);
    OgnStarrating updateReview(OgnStarrating review);
    boolean deleteReview(long id);
    //CRUD Admin
}

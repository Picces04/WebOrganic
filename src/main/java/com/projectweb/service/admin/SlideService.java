package com.projectweb.service.admin;

import com.projectweb.model.OgnSlide;

import java.util.List;

public interface SlideService {
    List<OgnSlide> findAll();
    List<OgnSlide> findStatusTop();
    List<OgnSlide> findStatusMid();
    List<OgnSlide> findStatusBottom();
    //CRUD Admin
    OgnSlide insertSlide(OgnSlide slide);
    OgnSlide updateSlide(OgnSlide slide);
    boolean deleteSlide(long id);
    //CRUD Admin
}

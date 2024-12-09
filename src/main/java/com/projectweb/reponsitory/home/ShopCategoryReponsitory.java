package com.projectweb.reponsitory.home;


import com.projectweb.model.OgnProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ShopCategoryReponsitory extends JpaRepository<OgnProduct, String> , JpaSpecificationExecutor<OgnProduct> {

}

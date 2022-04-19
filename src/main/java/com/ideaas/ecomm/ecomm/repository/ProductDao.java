package com.ideaas.ecomm.ecomm.repository;

import com.ideaas.ecomm.ecomm.domain.Brand;
import com.ideaas.ecomm.ecomm.domain.Category;
import com.ideaas.ecomm.ecomm.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductDao extends JpaRepository<Product, Long> {

    List<Product> findAllByCategory(final Category category);

    List<Product> findAllByNameContainingIgnoreCase(final String value);

    List<Product> searchAllByBrandIn(Collection<Brand> brandId);

    List<Product> searchAllByCategoryIn(Collection<Category> categories);
}

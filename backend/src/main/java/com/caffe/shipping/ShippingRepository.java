package com.caffe.shipping;

import com.caffe.domain.shipping.entity.Shipping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface ShippingRepository extends JpaRepository<Shipping, Integer> {

}

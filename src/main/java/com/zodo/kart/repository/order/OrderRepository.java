package com.zodo.kart.repository.order;

import com.zodo.kart.entity.order.Order;
import com.zodo.kart.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Author : Bhanu prasad
 */

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByPayment_MerchantTransactionId(String merchantTransactionId);
    List<Order> findAllOrdersByUserId(Long customerId);
    List<Order> findAllOrdersBySellerId(String sellerId);
    List<Order> findBySellerIdAndOrderStatusIn(String sellerId,List<OrderStatus> statuses);
    List<Order> findByOrderStatusIn(List<OrderStatus> statuses);
}

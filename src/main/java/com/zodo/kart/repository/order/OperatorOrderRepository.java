package com.zodo.kart.repository.order;

import com.zodo.kart.entity.order.OperatorOrder;
import com.zodo.kart.entity.order.Order;
import com.zodo.kart.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OperatorOrderRepository extends JpaRepository<OperatorOrder,Long> {

    Optional<OperatorOrder> findByPayment_MerchantTransactionId(String merchantTransactionId);
    List<OperatorOrder> findAllOrdersByOperatorId(String operatorId);
    List<OperatorOrder> findAllOrdersBySellerId(String sellerId);
    List<OperatorOrder> findBySellerIdAndOrderStatusIn(String sellerId,List<OrderStatus> statuses);
    List<OperatorOrder> findByOrderStatusIn(List<OrderStatus> statuses);
}

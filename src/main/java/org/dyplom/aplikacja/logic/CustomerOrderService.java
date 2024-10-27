package org.dyplom.aplikacja.logic;

import java.util.List;
import org.dyplom.aplikacja.model.CustomerOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerOrderService {

  @Autowired
  private CustomerOrderRepository customerOrderRepository;

  public CustomerOrder createOrder(CustomerOrder order) {
    return customerOrderRepository.save(order);
  }

  public CustomerOrder updateOrder(Long id, CustomerOrder orderDetails) {
    CustomerOrder order = customerOrderRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Order not found"));
    order.setTitle(orderDetails.getTitle());
    order.setDescription(orderDetails.getDescription());
    order.setStatus(orderDetails.getStatus());
    return customerOrderRepository.save(order);
  }

  public List<CustomerOrder> getAllOrders() {
    return customerOrderRepository.findAll();
  }

  public void deleteOrder(Long id) {
    customerOrderRepository.deleteById(id);
  }
}

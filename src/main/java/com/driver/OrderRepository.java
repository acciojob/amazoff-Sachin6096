package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class OrderRepository {

   HashMap<String,Order> orderHashMap = new HashMap<>();

   HashMap<String,DeliveryPartner> partnerHashMap = new HashMap<>();

   HashMap<String,String> orderDeliveryPartnerHashMap = new HashMap<>();

   HashMap<String,List<String>> partnerOrderMap = new HashMap<>();
    public void addOrder(Order order) {
        orderHashMap.put(order.getId(),order);
    }

    public void addPartner(String partnerId) {

        DeliveryPartner partner = new DeliveryPartner(partnerId);
        partnerHashMap.put(partnerId,partner);
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        if(orderHashMap.containsKey(orderId) && partnerHashMap.containsKey(partnerId))
        {
            orderDeliveryPartnerHashMap.put(orderId,partnerId);
            List<String> list = new ArrayList<>();
            if(partnerOrderMap.containsKey(partnerId))
            {
                list = partnerOrderMap.get(partnerId);
            }
            if(!list.contains(orderId))
            {
                list.add(orderId);
            }

            partnerOrderMap.put(partnerId,list);

            partnerHashMap.get(partnerId).setNumberOfOrders(list.size());
        }



    }

    public Order getOrderById(String orderId) {
        return orderHashMap.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        return partnerHashMap.get(partnerId);
    }

    public Integer getOrdercountByPartnerId(String partnerId) {


        return partnerHashMap.get(partnerId).getNumberOfOrders();
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        return partnerOrderMap.get(partnerId);
    }

    public List<String> getAllorders() {
        return new ArrayList<>(orderHashMap.keySet());
    }

    public Integer getCountOfUnassignedOrders() {
        return orderHashMap.size() - orderDeliveryPartnerHashMap.size();

    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(int time, String partnerId) {
        int count = 0;
        List<String> list = partnerOrderMap.get(partnerId);
        for(String s : list)
        {
            Order order = orderHashMap.get(s);
            if(order.getDeliveryTime() > time) count++;
        }
        return count;
    }

    public int getLastDeliveryTimeByPartnerId(String partnerId) {
       int max = 0;
       List<String> list = partnerOrderMap.get(partnerId);
       for(String s : list)
       {
           Order order = orderHashMap.get(s);
           if(order.getDeliveryTime() > max) max = order.getDeliveryTime();
       }
       return max;

    }

    public void deletePartnerById(String partnerId) {
        partnerHashMap.remove(partnerId);

        if(partnerOrderMap.containsKey(partnerId))
        {
            List<String> list = partnerOrderMap.get(partnerId);
            for(String s : list)
            {
                orderDeliveryPartnerHashMap.remove(s);
            }
            partnerOrderMap.remove(partnerId);
        }

    }

    public void deleteOrderById(String orderId) {
        orderHashMap.remove(orderId);
        if(orderDeliveryPartnerHashMap.containsKey(orderId))
        {
            String partner = orderDeliveryPartnerHashMap.get(orderId);
            List<String> list = partnerOrderMap.get(partner);
            list.remove(orderId);
            partnerOrderMap.put(partner,list);
            orderDeliveryPartnerHashMap.remove(orderId);
        }
    }
}

package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class OrderRepository {

    HashMap<String, Order> ordermap = new HashMap<>();

    HashMap<String, DeliveryPartner> partnermap = new HashMap<>();

    HashMap<String, String> orderpartnerdb = new HashMap<>();

    HashMap<String, List<String>> partnerorderdb = new HashMap<>();
    public void addOrder(Order order) {
        ordermap.put(order.getId(),order);
    }

    public void addPartner(String partnerId) {

        DeliveryPartner dp = new DeliveryPartner(partnerId);
        partnermap.put(partnerId,dp);
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        if(ordermap.containsKey(orderId) && partnermap.containsKey(partnerId))
        {
            orderpartnerdb.put(orderId,partnerId);
        }

        List<String> currOrder = new ArrayList<>();
        if(partnerorderdb.containsKey(partnerId))
        {
            currOrder = partnerorderdb.get(partnerId);
        }

        currOrder.add(orderId);
        partnerorderdb.put(partnerId,currOrder);
    }

    public Order getOrderById(String orderId) {
        return ordermap.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        return partnermap.get(partnerId);
    }

    public Integer getOrdercountByPartnerId(String partnerId) {
         return partnermap.get(partnerId).getNumberOfOrders();
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        List<String> ordersBypartner = new ArrayList<>();
        return ordersBypartner = partnerorderdb.get(partnerId);
    }

    public List<String> getAllorders() {
        List<String> allOrders = new ArrayList<>();

        for(String o : ordermap.keySet())
        {
            allOrders.add(o);
        }
        return allOrders;
    }

    public Integer getCountOfUnassignedOrders() {
        int count = ordermap.size()- orderpartnerdb.size();
        return count;
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(int time, String partnerId) {

        int count = 0;
        for(String s : partnerorderdb.get(partnerId))
        {
            int curr = ordermap.get(s).getDeliveryTime();
            if(curr > time) count++;
        }

        return count;
    }

    public int getLastDeliveryTimeByPartnerId(String partnerId) {

        int max = 0;
        for(String s : partnerorderdb.get(partnerId))
        {
            max = Math.max(max,ordermap.get(s).getDeliveryTime());
        }
        return max;
    }

    public void deletePartnerById(String partnerId) {
        partnermap.remove(partnerId);


        for(String s : orderpartnerdb.keySet())
        {
            if(orderpartnerdb.get(s) == partnerId) orderpartnerdb.remove(s);
        }

        if(partnerorderdb.containsKey(partnerId))
        {
            partnerorderdb.remove(partnerId);
        }
    }

    public void deleteOrderById(String orderId) {
        ordermap.remove(orderId);

        String s = orderpartnerdb.get(orderId);
        partnerorderdb.get(s).remove(orderId);

        orderpartnerdb.remove(orderId);
    }
}

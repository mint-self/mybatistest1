package com.xm.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xm.mall.dao.OrderItemMapper;
import com.xm.mall.dao.OrderMapper;
import com.xm.mall.dao.ProductMapper;
import com.xm.mall.dao.ShippingMapper;
import com.xm.mall.enums.OrderStatusEnum;
import com.xm.mall.enums.PaymentTypeEnum;
import com.xm.mall.enums.ProductStatusEnum;
import com.xm.mall.enums.ResponseEnum;
import com.xm.mall.pojo.*;
import com.xm.mall.service.ICartService;
import com.xm.mall.service.IOrderService;
import com.xm.mall.vo.OrderItemVo;
import com.xm.mall.vo.OrderVo;
import com.xm.mall.vo.ResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author mintFM
 * @create 2022-01-31 7:05
 */
@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private ShippingMapper shippingMapper;

    @Autowired
    private ICartService cartService;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    /**
     * 创建订单
     * @param uid  用户的ID
     * @param shippingId  收货地址的ID
     * @return
     */
    @Override
    @Transactional
    public ResponseVo<OrderVo> create(Integer uid, Integer shippingId) {
        /*
        对收货地址进行校验
         */
        //获取收获地址对象
        Shipping shipping = shippingMapper.selectByUidAndShippingId(uid,shippingId);
        if (shipping == null) {
            //如果没有获取到则返回地址不存在的错误信息
            return ResponseVo.error(ResponseEnum.SHIPPING_NOT_EXIST);
        }

        /*
        获取购物车，也要进行校验：检查出是否有商品，有库存等
         */
        //获取购物车，对购物车进行检查
        List<Cart> cartList = cartService.listForCart(uid).stream()
                .filter(Cart::getProductSelected)
                .collect(Collectors.toList());
        //检查购物车是否为空
        if (CollectionUtils.isEmpty(cartList)) {
            //购物车为空，让用户选择商品
            return ResponseVo.error(ResponseEnum.CART_SELECTED_IS_EMPTY);
        }
        //获取购物车cartList中的所有商品IDproductIds
        Set<Integer> productIdSet = cartList.stream()
                .map(Cart::getProductId)
                .collect(Collectors.toSet());
        List<Product> productList = productMapper.selectByProductIdSet(productIdSet);
        Map<Integer, Product> map = productList.stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        //获取订单项，对订单项进行检查
        List<OrderItem> orderItemList = new ArrayList<>();
        //设置订单编号
        Long orderNo = generateOrderNo();
        //对购物车项进行检查
        for (Cart cart: cartList) {
            //根据商品ID productId来查询数据库
            Product product = map.get(cart.getProductId());
            //检查是否有商品
            if (product == null) {
                return ResponseVo.error(ResponseEnum.PRODUCT_OFF_SALE_OR_DELETE, "商品不存在，productId = " + cart.getProductId());
            }

            //检查商品的上架和下架状态
            if (!ProductStatusEnum.ON_SALE.getCode().equals(product.getStatus())) {
                //商品不再售
                return ResponseVo.error(ResponseEnum.PRODUCT_OFF_SALE_OR_DELETE, "商品不是在售状态，" + product.getName());
            }

            //检查库存是否充足
            if (product.getStock() < cart.getQuantity()) {
                //库存不足
                return ResponseVo.error(ResponseEnum.PROODUCT_STOCK_ERROR, "库存不正确，" + product.getName());
            }

            //获取订单项
            OrderItem orderItem = buildOrderItem(uid, orderNo, cart.getQuantity(), product);
            orderItemList.add(orderItem);

            //减少库存
            product.setStock(product.getStock() - cart.getQuantity());
            int row = productMapper.updateByPrimaryKeySelective(product);
            if (row <= 0) {
                //操作不正确
                return ResponseVo.error(ResponseEnum.ERROR);
            }
        }

        /*
        计算总价格：只计算其中选中的商品
        并生成订单做入库操作
         */
        //创建订单
        Order order = buildOrder(uid, orderNo, shippingId,orderItemList);
        int rowForOrder = orderMapper.insertSelective(order);
        if (rowForOrder <= 0) {
            //没有插入
            return ResponseVo.error(ResponseEnum.ERROR);
        }

        //批量插入
        int rowForOrderItem = orderItemMapper.batchInsert(orderItemList);
        if (rowForOrderItem <= 0) {
            //没有插入
            return ResponseVo.error(ResponseEnum.ERROR);
        }

        //更新购物车：只对其中选中的商品进行操作
        for (Cart cart: cartList) {
            //对已经生成订单的商品从购物车中删除
            cartService.delete(uid,cart.getProductId());
        }

        //构造返回前端的订单项orderVo
        OrderVo orderVo = buildOrderVo(order,orderItemList,shipping);
        return ResponseVo.success(orderVo);
    }

    @Override
    public ResponseVo<PageInfo> list(Integer uid, Integer pageNum, Integer pageSize) {
        //订单详情页需要分页
        PageHelper.startPage(pageNum,pageSize);
        List<Order> orderList = orderMapper.selectByUid(uid);
        Set<Long> orderNoSet = orderList.stream()
                .map(Order::getOrderNo)
                .collect(Collectors.toSet());
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNoSet(orderNoSet);
        //list集合转化成map集合
        Map<Long, List<OrderItem>> orderItemMap = orderItemList.stream()
                .collect(Collectors.groupingBy(OrderItem::getOrderNo));

        //收货地址id
        Set<Integer> shippingIdSet = orderList.stream()
                .map(Order::getShippingId)
                .collect(Collectors.toSet());
        //收货地址
        List<Shipping> shippingList = shippingMapper.selectByIdSet(shippingIdSet);
        Map<Integer, Shipping> shippingMap = shippingList.stream()
                .collect(Collectors.toMap(Shipping::getId, shipping -> shipping));

        List<OrderVo> orderVoList = new ArrayList<>();
        for (Order order: orderList) {
            //创建返回前端的订单
            OrderVo orderVo = buildOrderVo(order,
                    orderItemMap.get(order.getOrderNo()),
                    shippingMap.get(order.getShippingId()));
            orderVoList.add(orderVo);
        }

        PageInfo pageInfo = new PageInfo<>(orderList);
        pageInfo.setList(orderVoList);
        return ResponseVo.success(pageInfo);
    }

    @Override
    public ResponseVo<OrderVo> detail(Integer uid, Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null || !order.getUserId().equals(uid)) {
            //如果订单为空或者订单号和数据库中的不匹配，则报错
            return ResponseVo.error(ResponseEnum.ORDER_NOT_EXIST);
        }
        //订单编号
        Set<Long> orderNoSet = new HashSet<>();
        orderNoSet.add(order.getOrderNo());
        //订单项
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNoSet(orderNoSet);
        //收货地址
        Shipping shipping = shippingMapper.selectByPrimaryKey(order.getShippingId());
        //创建返回前端的订单对象
        OrderVo orderVo = buildOrderVo(order,orderItemList,shipping);
        return ResponseVo.success(orderVo);
    }

    @Override
    public ResponseVo cancel(Integer uid, Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null || !order.getUserId().equals(uid)) {
            //如果订单不存在或者订单号和数据库中的不匹配
            return ResponseVo.error(ResponseEnum.ORDER_NOT_EXIST);
        }
        //本项目只设置未付款的订单可以取消，企业具体的取消条件看公司的业务需求
        if (!order.getStatus().equals(OrderStatusEnum.NO_PAY.getCode())) {
            //订单状态不是未付款的要取消订单，则订单状态有误
            return ResponseVo.error(ResponseEnum.ORDER_STATUS_ERROR);
        }
        //其他的未付款的订单可以取消订单
        order.setStatus(OrderStatusEnum.CANCELED.getCode());
        //设置关闭订单的时间为当前的时间
        order.setCloseTime(new Date());
        //更新数据库
        int row = orderMapper.updateByPrimaryKeySelective(order);
        if (row <= 0) {
            //没有更新成功则报错
            return ResponseVo.error(ResponseEnum.ERROR);
        }
        return ResponseVo.success();
    }

    @Override
    public void paid(Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null) {
            //如果订单不存在则抛出异常
            throw new RuntimeException(ResponseEnum.ORDER_NOT_EXIST.getDesc() + "订单Id:" + orderNo);
        }
        //本项目设置只有【未付款】的订单才可以修改为【已付款】
        if (!order.getStatus().equals(OrderStatusEnum.NO_PAY.getCode())) {
            //不是未付款的其他状态就不可以
            throw new RuntimeException(ResponseEnum.ORDER_STATUS_ERROR.getDesc() + "订单id:" + orderNo);
        }

        //设置付款好的订单状态和订单时间为当前时间
        order.setStatus(OrderStatusEnum.PAID.getCode());
        order.setPaymentTime(new Date());
        int row = orderMapper.updateByPrimaryKeySelective(order);
        if (row <= 0) {
            //更新失败
            throw new RuntimeException("将订单状态更新为已支付状态的操作失败，订单id:" + orderNo);
        }

    }


    /**
     * 生成订单编号 如果是企业级的则用分布式id
     * @return
     */
    private Long generateOrderNo() {
        return System.currentTimeMillis() + new Random().nextInt(999);
    }

    /**
     * 创建订单中的某个详情的商品订单项
     * @param uid
     * @param orderNo
     * @param quantity
     * @param product
     * @return
     */
    private OrderItem buildOrderItem(Integer uid, Long orderNo, Integer quantity, Product product) {
        OrderItem item = new OrderItem();
        item.setUserId(uid);
        item.setOrderNo(orderNo);
        item.setProductId(product.getId());
        item.setProductName(product.getName());
        item.setProductImage(product.getMainImage());
        item.setCurrentUnitPrice(product.getPrice());
        item.setQuantity(quantity);
        item.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        return item;
    }

    /**
     * 创建订单
     * @param uid
     * @param orderNo
     * @param shippingId
     * @param orderItemList
     * @return
     */
    private Order buildOrder(Integer uid, Long orderNo, Integer shippingId, List<OrderItem> orderItemList) {
        BigDecimal payment = orderItemList.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(uid);
        order.setShippingId(shippingId);
        order.setPayment(payment);
        order.setPaymentType(PaymentTypeEnum.PAY_ONLINE.getCode());
        //本项目不设置邮编号
        order.setPostage(0);
        order.setStatus(OrderStatusEnum.NO_PAY.getCode());
       return order;
    }


    /**
     * 创建orderVo:返回给前端页面的order对象
     * @param order
     * @param orderItemList
     * @param shipping
     * @return
     */
    private OrderVo buildOrderVo(Order order, List<OrderItem> orderItemList, Shipping shipping) {
        OrderVo orderVo = new OrderVo();
        BeanUtils.copyProperties(order,orderVo);
        List<OrderItemVo> OrderItemVoList = orderItemList.stream().map(e -> {
            OrderItemVo orderItemVo = new OrderItemVo();
            BeanUtils.copyProperties(e,orderItemVo);
            return orderItemVo;
        }).collect(Collectors.toList());
        orderVo.setOrderItemVoList(OrderItemVoList);
        if (shipping != null) {
            orderVo.setShippingId(shipping.getId());
            orderVo.setShippingVo(shipping);
        }
        return orderVo;
    }
}

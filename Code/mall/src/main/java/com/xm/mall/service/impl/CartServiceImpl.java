package com.xm.mall.service.impl;

import com.google.gson.Gson;
import com.xm.mall.dao.ProductMapper;
import com.xm.mall.enums.ProductStatusEnum;
import com.xm.mall.enums.ResponseEnum;
import com.xm.mall.form.CartAddForm;
import com.xm.mall.form.CartUpdateForm;
import com.xm.mall.pojo.Cart;
import com.xm.mall.pojo.Product;
import com.xm.mall.service.ICartService;
import com.xm.mall.vo.CartProductVo;
import com.xm.mall.vo.CartVo;
import com.xm.mall.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author mintFM
 * @create 2022-01-27 17:42
 */
@Service
public class CartServiceImpl implements ICartService {
    //对常量名字做格式化
    private final static String CART_REDIS_KEY_TEMPLATE = "cart_%d";

    @Autowired
    private ProductMapper productMapper;

    //这个变量是Spring自己封装好的
    @Autowired
    private StringRedisTemplate redisTemplate;

    //引入JSon
    private Gson gson = new Gson();

    //添加商品
    @Override
    public ResponseVo<CartVo> add(Integer uid, CartAddForm form) {
        //设定每次添加到购物车中的商品数量都是加1
        Integer quantity = 1;

        Product product = productMapper.selectByPrimaryKey(form.getProductId());
        /*
        要把商品加到购物车，得判断商品是否存在，商品是否正常在售，商品库存是否充足
         */

        //判断商品是否存在
        if (product == null) {
            //不存在
            return ResponseVo.error(ResponseEnum.PRODUCT_NOT_EXIST);
        }

        //判断商品是否正常在售
        if (!product.getStatus().equals(ProductStatusEnum.ON_SALE.getCode())) {
            //不在售
            return ResponseVo.error(ResponseEnum.PRODUCT_OFF_SALE_OR_DELETE);
        }

        //判断商品库存是否充足
        if (product.getStock() <= 0) {
            //不充足
            return ResponseVo.error(ResponseEnum.PROODUCT_STOCK_ERROR);
        }

        /*
        通过上面对商品信息判断好后，就可以将商品信息加入到redis中
         */
        //写入redis；key部分是采用cart_uid 例如：cart_1; 该hash的结构，因为这样可以更快的遍历购物车，提高速度和性能
        //遍历hash表
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        //从redis中读取数据
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE,uid);
        Cart cart;
        //然后在从hash中get出数据
        String value = opsForHash.get(redisKey,String.valueOf(product.getId()));
        if (StringUtils.isEmpty(value)) {
            //如果是空的，即表示没有商品，没有商品则要新增加商品
            cart = new Cart(product.getId(),quantity,form.getSelected());

        } else {
            //如果已经有商品了，则每次增加商品是往购物车中使得数量加1
            //转换为对象
            cart = gson.fromJson(value,Cart.class);
            //数量进行加1
            cart.setQuantity(cart.getQuantity() + quantity);
        }

        opsForHash.put(redisKey,
                String.valueOf(product.getId()),
                gson.toJson(cart));

      return list(uid);
    }

    //返回购物车列表
    @Override
    public ResponseVo<CartVo> list(Integer uid) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE,uid);
        Map<String, String> entries = opsForHash.entries(redisKey);
        //全选
        boolean selectAll = true;
        //购物车总量
        Integer cartTotalQuantity = 0;
        //购物车商品总价格
        BigDecimal cartTotalPrice = BigDecimal.ZERO;
        //前端页面要返回的购物车对象
        CartVo cartVo = new CartVo();
        List<CartProductVo> cartProductVoList = new ArrayList<>();
        //TODO 下面的for循环是在MySQL中查找数据，查找的速度会比较慢，建议使用MySQL中的in
        for (Map.Entry<String, String> entry: entries.entrySet()) {
            Integer productId = Integer.valueOf(entry.getKey());
            //装换成java对象
            Cart cart = gson.fromJson(entry.getValue(),Cart.class);
            //TODO 这里就是需要优化的地方
            Product product = productMapper.selectByPrimaryKey(productId);
            //显示出我们想呈现的
            if (product != null) {
                CartProductVo cartProductVo = new CartProductVo(productId,
                        cart.getQuantity(),
                        product.getName(),
                        product.getSubtitle(),
                        product.getMainImage(),
                        product.getPrice(),
                        product.getStatus(),
                        product.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())),
                        product.getStock(),
                        cart.getProductSelected()
                );
                //添加到list列表中
                cartProductVoList.add(cartProductVo);
                //如果不是全选
                if (!cart.getProductSelected()) {
                    selectAll = false;
                }

                //计算总价，只计算购物车中选中的商品
                if (cart.getProductSelected()) {
                    //如果是选中的，就计算
                    cartTotalPrice = cartTotalPrice.add(cartProductVo.getProductTotalPrice());
                }
            }

            //购物车中的商品总数量
            cartTotalQuantity += cart.getQuantity();
        }
        //全选：有一个没选中的就不是全选
        cartVo.setSelectAll(selectAll);
        //计算购物车里的总数
        cartVo.setCartTotalQuantity(cartTotalQuantity);
        //购物车里的总价，就是购物车里所有东西的和
        cartVo.setCartTotalPrice(cartTotalPrice);
        //商品列表
        cartVo.setCartProductVoList(cartProductVoList);
        return ResponseVo.success(cartVo);
    }

    //更新购物车中的商品信息
    @Override
    public ResponseVo<CartVo> update(Integer uid, Integer productId, CartUpdateForm form) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        //String.format：制定字符串的显示格式，让后面的uid以前面的格式显示
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE,uid);
        //String.valueOf：将别的类型转换为String类型
        String value = opsForHash.get(redisKey,String.valueOf(productId));
        if (StringUtils.isEmpty(value)){
            //没有商品则无法更新，所以报错
            return ResponseVo.error(ResponseEnum.CART_PRODUCT_NOT_EXIST);
        }

        //如果已经有商品了，则可以修改购物车中商品的内容
        //转换成cart对象
        Cart cart = gson.fromJson(value,Cart.class);
        if (form.getQuantity() != null && form.getQuantity() >= 0) {
            //则是有商品
            cart.setQuantity(form.getQuantity());
        }
        if (form.getSelected() != null) {
            //如果不是全选，则设置为全选
            cart.setProductSelected(form.getSelected());
        }
        opsForHash.put(redisKey,String.valueOf(productId),gson.toJson(cart));

        return list(uid);
    }

    //删除
    @Override
    public ResponseVo<CartVo> delete(Integer uid, Integer productId) {
        HashOperations<String, String, String> opsForHas = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE,uid);
        String value = opsForHas.get(redisKey,String.valueOf(productId));
        if (StringUtils.isEmpty(value)) {
            //没有该商品, 报错
            return ResponseVo.error(ResponseEnum.CART_PRODUCT_NOT_EXIST);
        }
        //如果有该商品直接删除
        opsForHas.delete(redisKey,String.valueOf(productId));
        return list(uid);
    }

    //全选
    @Override
    public ResponseVo<CartVo> selectAll(Integer uid) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey  = String.format(CART_REDIS_KEY_TEMPLATE, uid);

        //对购物车进行遍历，设置为全选
        for (Cart cart: listForCart(uid)) {
            cart.setProductSelected(true);
            opsForHash.put(redisKey,
                    String.valueOf(cart.getProductId()),
                    gson.toJson(cart));
        }
        return list(uid);
    }

    //全不选
    @Override
    public ResponseVo<CartVo> unSelectAll(Integer uid) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey  = String.format(CART_REDIS_KEY_TEMPLATE, uid);

        //遍历购物车，设置为全不选
        for (Cart cart: listForCart(uid)) {
            cart.setProductSelected(false);
            opsForHash.put(redisKey,
                    String.valueOf(cart.getProductId()),
                    gson.toJson(cart));
        }

        return list(uid);
    }

    //购物车商品数量总和
    @Override
    public ResponseVo<Integer> sum(Integer uid) {
        Integer sum = listForCart(uid).stream()
                .map(Cart::getQuantity)
                .reduce(0,Integer::sum);
        return ResponseVo.success(sum);
    }

    //遍历购物车，完成全选、全不选的功能
    @Override
    public List<Cart> listForCart(Integer uid) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey  = String.format(CART_REDIS_KEY_TEMPLATE, uid);
        Map<String, String> entries = opsForHash.entries(redisKey);

        List<Cart> cartList = new ArrayList<>();
        //遍历购物车
        for (Map.Entry<String,String> entry: entries.entrySet()) {
            cartList.add(gson.fromJson(entry.getValue(),Cart.class));
        }
        return cartList;
    }
}

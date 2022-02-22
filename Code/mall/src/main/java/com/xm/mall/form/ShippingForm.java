package com.xm.mall.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author mintFM
 * @create 2022-01-29 8:01
 */
@Data
public class ShippingForm {
    //因为要入参的参数对象和pojo的不是完全一样，所以新建一个form对象来进行入参

    @NotBlank
    private String receiverName;

    @NotBlank
    private String receiverPhone;

    @NotBlank
    private String receiverMobile;

    @NotBlank
    private String receiverProvince;

    @NotBlank
    private String receiverCity;

    @NotBlank
    private String receiverDistrict;

    @NotBlank
    private String receiverAddress;

    @NotBlank
    private String receiverZip;
}

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>支付</title>
</head>
<body>
<div id="myQrcode"></div>
<div id="orderId" hidden>${orderId}</div>
<div id="returnUrl" hidden>${returnUrl}</div>


<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/1.5.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.qrcode/1.0/jquery.qrcode.min.js"></script>
<script>
    jQuery('#myQrcode').qrcode({
        text	: "${codeUrl}"
    });

    $(function () {
        //通过定时器轮询在控制台打印出信息,没2秒打印一次
        setInterval(function () {
            console.log('开始查询支付状态：')
            $.ajax({
                'url': '/pay/queryByOrderId',
                data: {
                    'orderId': $('#orderId').text()
                },
                //编写请求成功或者失败的回调函数
                success: function (result) {
                    console.log(result)
                    if (result.platformStatus != null
                        && result.platformStatus === 'SUCCESS') {
                        location.href = $('#returnUrl').text()
                    }
                },
                error: function (result) {
                    alert(result)
                }
            })
        },2000)
    });
</script>
</body>
</html>
/**
 * Created by flowerpower on 2017. 05. 17..
 */

$(document).ready(function() {
    var paymentSelect = $("#payment");

    paymentSelect.change(function() {
        var nextStepId = paymentSelect.val();

        if (nextStepId == "creditcard") {
            $(".paypal-row").css("display", "none");
            $(".credit-card-row").css("display", "inline");


        } else if (nextStepId == "paypal") {
            $(".credit-card-row").css("display", "none");
            $(".paypal-row").css("display", "inline");

        }
    });
});
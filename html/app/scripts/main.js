'use strict';

$('.tax-service-nav-item').click(function(){
    $('.tax-service-nav-item.active').removeClass("active");
    $(this).addClass("active");
});

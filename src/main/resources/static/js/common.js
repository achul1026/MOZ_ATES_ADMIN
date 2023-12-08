$(function(){

  $('.list-title').click(function(){
	 // alert('asdasd')
	/* $('.sub-list').slideToggle(500);*/
	  if ($(this).hasClass("on")) {
		  //alert('온');
		    $(this).siblings().slideUp();
            $(this).find('.arrow').removeClass('on') 
            $(this).removeClass('on');
        } else {
			//alert('오프');
			 $(this).siblings().slideDown();
			 $(this).find('.arrow').addClass('on')
			 $(this).addClass('on')
			 $('.list-title').not(this).siblings().slideUp();
			 $('.list-title').not(this).find('.arrow').removeClass('on');
			 $('.list-title').not(this).removeClass('on');
        }
  });

})

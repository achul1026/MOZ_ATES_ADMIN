$(function (){

    $(document).on('click','.paginate_button',function(){
        if(!$(this).hasClass('disabled')){
            var startIndex = 0;
            if($(this).hasClass('next')){
                startIndex = parseInt($("#hdnStart").val())+10;
            }else if($(this).hasClass('previous')){
                startIndex = parseInt($("#hdnStart").val())-10;
            }else{
                startIndex = ($(this).attr('data-dt-idx')-1)*10;
            }
            $("#hdnStart").val(startIndex);
            $("#frmSearch").submit();
        }
    });

    $("#btnSearch").click(function () {
        $("#hdnStart").val('0');
        $("#frmSearch").submit();
    });
})
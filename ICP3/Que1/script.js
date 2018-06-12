var colors = [ "22ac5e", "d68236", "71b5c2", "af2655", "b34de7", "e6bd01", "104393", "ca4d94"];
function setPreviewColor(color) {
    $('.preview').css('background-color', color);
    $('.color-code').text($('.preview').css('background-color'));
}
function addBox(color) {
    $('#colors').prepend("<div class='item' style='background-color: " + color + ";'><div>");
}
$(document).ready(function(){


    $.each( colors, function( i, val ) {

        addBox(val);
    });


    setPreviewColor(colors[Math.floor(Math.random()*colors.length)]);
    $(document).on('keydown keyup keypress', '#color', function(){
        color = $(this).val();
        setPreviewColor(color);
    })

    //adding to fav-color
    $( "#add-to-favorite" ).click(function() {
        color = $(this).prev().val();
        addBox(color);
        $(this).prev().val("");
        $(this).prev().focus();
    });

    //on hover
    $(document).on('mouseover', '.item', function(){
        color = $(this).css("background-color");
        setPreviewColor(color);
    })


});
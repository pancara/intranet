$('#cont_album').tabs({fxSlide: true, fxSpeed: 'slow'});

$("#form_upload_picture > form").ajaxForm({
    success: function(data) {
        var html = data + $("#new_picture").html();
        $("#new_picture").html(html);
    },
    resetForm: true
});

$(".btn_remove").click(function() {
    var ok = window.confirm("Hapus data ?");
    if (!ok) return false; 
});

(function($) {
    $.fn.clickNav = function(options) {
        var defaults = { url : "#"};
        var config = $.extend(defaults, options);

        return this.each(function() {
            $(this).click(function() {
               location.href = config.url;
            });
        });
    };
})(jQuery);
var _console = window.console;

(function($) {
    $.fn.pikiwiTree = function(options) {
        var defaults = {};
        var params = $.extend(defaults, options);
 
        return this.each(function() {
            var e = $(this);
            e.addClass("datastore");
            e.find("li > span").addClass("hitarea");
            e.find("li ul").show();

            e.find("li span.text").click(function() {
                $(this).siblings("ul").toggle({animate: true, speed: "slow"});
                return true;
            });
        });
        
    };
})(jQuery);
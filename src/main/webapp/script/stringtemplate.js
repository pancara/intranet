String.prototype.applyTemplate = function(d) {
    try {
        if (d === '') return this;
        return this.replace(/{([^{}]*)}/g,
                function(a, b) {
                    var r;
                    if (b.indexOf('.') !== -1) { // handle dot notation in {}, such as {Thumbnail.Url}
                        var ary = b.split('.');
                        var obj = d;
                        for (var i = 0; i < ary.length; i++)
                            obj = obj[ary[i]];
                        r = obj;
                    }
                    else
                        r = d[b];
                    if (typeof r === 'string' || typeof r === 'number') return r; else throw (a);
                }
                );
    } catch (ex) {
        alert('Invalid JSON property ' + ex + ' found when trying to apply resultTemplate or paging.summaryTemplate.\nPlease check your spelling and try again.');
    }
};
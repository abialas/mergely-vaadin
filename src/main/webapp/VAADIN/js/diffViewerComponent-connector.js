/**
 * Created by adam on 11.10.2017.
 */
com_us_DiffViewerComponent = function() {
    var e = $(this.getElement());

    this.onStateChange = function () {
        e.mergely({
            cmsettings: {readOnly: this.getState().readOnly, lineNumbers: true}
        });
        e.mergely('lhs', this.getState().leftText);
        e.mergely('rhs', this.getState().rightText);
    };
};
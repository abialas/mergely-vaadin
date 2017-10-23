package com.us;

import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.StyleSheet;
import com.vaadin.ui.AbstractJavaScriptComponent;

/**
 * Created by adam.
 */

@JavaScript({
        "http://ajax.googleapis.com/ajax/libs/jquery/1.9.0/jquery.min.js",
        "vaadin://js/codemirror.js",
        "vaadin://js/mergely.js",
        "vaadin://js/diffViewerComponent-connector.js"
})
@StyleSheet({
        "vaadin://css/codemirror.css",
        "vaadin://css/mergely.css",
})
public class DiffViewerComponent extends AbstractJavaScriptComponent {

    public DiffViewerComponent() {
        this(null, null);
    }

    public DiffViewerComponent(String leftText) {
        this(leftText, null);
    }

    public DiffViewerComponent(String leftText, String rightText) {
        getState().leftText = leftText;
        getState().rightText = rightText;
    }

    public void setLeftText(String leftText) {
        getState().leftText = leftText;
    }

    public void setRightText(String rightText) {
        getState().rightText = rightText;
    }

    @Override
    protected DiffViewerComponentState getState() {
        return (DiffViewerComponentState) super.getState();
    }

}

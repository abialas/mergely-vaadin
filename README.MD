# How to integrate JavaScript control in Vaadin

This project shows how to integrate JS control in Vaadin project. The example is based on a recent task I had to do, which was about creating a control which allows to compare two texts from files. I found a nice JS control -  http://www.mergely.com which does exactly what I need, but it is available in JS only.
This forced me to try to integrate JS control in Vaadin project.

## Required steps to integrate JS control:

### 1. Copy JS files and CSS files (if exists) into your project. 

I put files in the webapp/VAADIN/js and webapp/VAADIN/css folders.

### 2. Create a class which represents the state which is shared between server and client.

The state has to extend JavaScriptComponentState (encapulation skipped in the example). To simplify, our state contains just texts which are being compared.

```
public class DiffViewerComponentState extends JavaScriptComponentState {

    public String leftText;
    public String rightText;

}
```

### 3. Create a class which represents control on the server-side.

Control on the server side has to extend AbstractJavaScriptComponent. In this class, with usage of annotations, you should include all required JS and CSS files. JS files are inluced with @JavaScript annotation and CSS files with @StyleSheet annotation. This means that when the control is added to the page which is being rendered, required JS and CSS will be added to the <head></head> element. Server side control has to implement getState() method which should return state object, available to both the server and client. All the others methods are just to update shared state of control. As soon as the state is updated, the client will be notified.

```
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
```

### 4. Create JS file which represents connector (connects server and client).

To initialize JS component and communication between server and client, JS file connector is required. It has to have an initializer connector function defined, with a name which is fully qualified name of the server side component (including package, but '.' (dots) in package name have to be replaced with '_' (underscores)). It means that our initializer connector function is com_us_DiffViewerComponent (because DiffViewerComponent is defined on the server side inside com.us package). The Connector is run on the Vaadin client side framework, which means it adds some additional methods (like: this.getElement(), which returns HTML DOM element for the component). To handle changes on the server side the onStateChange function is required. It reads shared state and uses it to initialize / update JS component. 


```
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
```

## Usage

When the above implementation is done, a DiffViewerComponent can be used on the server side, just like components which are built-in in Vaadin.

```
VerticalLayout layout = new VerticalLayout();
layout.setMargin(true);
setContent(layout);

final DiffViewerComponent diffComponent = new DiffViewerComponent("Sample text in the left editor");

Button button = new Button("Click Button to set sample text");
button.addClickListener(new Button.ClickListener() {
    public void buttonClick(ClickEvent event) {
        diffComponent.setRightText("Sample text in the right editor");
    }
});


layout.addComponents(button, diffComponent);
```

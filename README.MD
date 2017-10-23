# How to integrate JavaScript control in Vaadin

This project shows how to integrate JS control in Vaadin project. Example is based on recent task I had to do which was about creating a control which allows to compare two texts from files. I found a nice JS control -  http://www.mergely.com which does exactly what I need but it is available in JS only.
This forced me to give a first try to integrate JS control in Vaadin project.

## Required steps to integrate JS control

### 1. Copy JS files and CSS files (if exists) into your project. 

I put files folder webapp/VAADIN/js and webapp/VAADIN/css folders.

### 2. Create class which represent state which is shared between server and client.

State has to extends JavaScriptComponentState (encapulation skipped in the example). To simplify our state contains just texts which are being compared.

```
public class DiffViewerComponentState extends JavaScriptComponentState {

    public String leftText;
    public String rightText;

}
```

### 3. Create class which represent control on the server-side

Contro on the server side has to extends AbstractJavaScriptComponent. In this class with usage of annotations there should be all required JS and CSS files included. JS files are inluced with annotation @JavaScript and CSS files with annotation @StyleSheet. This means that when the control is added to the page which is being rendered to the <head></head> element required JS and CSS will be added. Server side control has to implement getState() method which should return state object which is available to the server and client. All the others methods are just to update shared state of a control. As soon as state is updated a client will be notified.

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

### 4. Create JS file which represent connector (connect server and client)

To initialize JS component and communication between server and client there is a connector JS file required. It has to have a initializer connector function defined with a name which is fully qualified name of the server side component (including package but '.' (dots) in package name have to be replaced with '_' (underscores)). It means that our initializer connector function is com_us_DiffViewerComponent (because DiffViewerComponent is defined on the server side inside com.us package). Connector is run on the Vaadin client side framework which means it adds some additional methods e.g. this.getElement() which returns HTML DOM element for the component. To handle changes on the server side the onStateChange function is required. It reads shared state and uses it to initialize / update JS component. 


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

When the above implementation is done a DiffViewerComponent can be used on the server side like components which are built-in in Vaadin.

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
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
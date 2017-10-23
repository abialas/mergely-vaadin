package com.us;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("mytheme")
@SuppressWarnings("serial")
public class ExtVaadinUI extends UI
{

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = ExtVaadinUI.class, widgetset = "com.us.AppWidgetSet")
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {
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
    }

}

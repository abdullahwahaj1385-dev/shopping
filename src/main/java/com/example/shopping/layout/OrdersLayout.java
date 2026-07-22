package com.example.shopping.layout;

import com.example.shopping.model.Order;
import com.example.shopping.service.OrderService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Route("orders")
public class OrdersLayout extends AppLayout {

    private final OrderService orderService;
    private List<Order> orders = new ArrayList<>();
    private Grid<Order> grid = new Grid<>(Order.class, false);

    private TextField idField = new TextField("ID Ordine");
    private TextField customerField = new TextField("Cliente");
    private TextField totalField = new TextField("Totale");
    private TextField statusField = new TextField("Stato");


    private Order currentOrder; // Tiene traccia dell'ordine che stiamo modificando
    @Autowired
    public OrdersLayout(OrderService orderService) {
        this.orderService = orderService;

        this.orders = new ArrayList<>(orderService.findAll());
        DrawerToggle toggle = new DrawerToggle();
        H1 title = new H1("Gestione Ordini");
        title.getStyle()
                .set("font-size", "1.125rem")
                .set("margin", "0");

        SideNav nav = getSideNav();
        nav.getStyle().set("margin", "var(--vaadin-gap-s)");
        Scroller scroller = new Scroller(nav);
        addToDrawer(scroller);
        addToNavbar(toggle, title);

        grid.addColumn(Order::getIdOrdine).setHeader("ID Ordine");
        grid.addColumn(Order::getCliente).setHeader("Cliente");
        grid.addColumn(Order::getTotale).setHeader("Totale");
        grid.addColumn(Order::getStato).setHeader("Stato");

        Dialog dialog = new Dialog();
        FormLayout dialogLayout = createDialogLayout();
        dialog.add(dialogLayout);

        Button saveButton = createSaveButton(dialog);
        Button cancelButton = new Button("Cancel", e -> dialog.close());
        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(saveButton);

        grid.addComponentColumn(order -> {
            Button editButton = new Button(VaadinIcon.EDIT.create());
            editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            editButton.setTooltipText("Modifica ordine");
            editButton.addClickListener(e -> {
                currentOrder = order;
                dialog.setHeaderTitle("Modifica Ordine");
                idField.setValue(order.getIdOrdine());
                customerField.setValue(order.getCliente());
                totalField.setValue(order.getTotale());
                statusField.setValue(order.getStato());
                dialog.open();
            });

            Button deleteButton = new Button(VaadinIcon.TRASH.create());
            deleteButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
            deleteButton.setTooltipText("Elimina ordine");
            deleteButton.addClickListener(e -> {
                orderService.deleteOrder(order.getId());
                orders.remove(order);
                grid.getDataProvider().refreshAll();
                Notification.show("Ordine eliminato: " + order.getIdOrdine());
            });

            HorizontalLayout actions = new HorizontalLayout(editButton, deleteButton);
            actions.setSpacing(true);
            return actions;
        }).setHeader("Azioni");

        grid.setItems(orders);

        Button addOrderButton = new Button("Nuovo Ordine", VaadinIcon.PLUS.create(), e -> {
            currentOrder = null;
            dialog.setHeaderTitle("Nuovo Ordine");
            idField.clear();
            customerField.clear();
            totalField.clear();
            statusField.clear();
            dialog.open();
        });
        addOrderButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        VerticalLayout mainContent = new VerticalLayout(addOrderButton, grid);
        mainContent.setSizeFull();
        grid.setSizeFull();
        setContent(mainContent);
    }

    private FormLayout createDialogLayout() {
        FormLayout layout = new FormLayout();
        layout.add(idField, customerField, totalField, statusField);
        return layout;
    }

    private Button createSaveButton(Dialog dialog) {
        Button saveButton = new Button("Save", e -> {
            if (currentOrder == null) {
                Order nuovoorder = new Order();
                nuovoorder.setIdOrdine(idField.getValue());
                nuovoorder.setCliente(customerField.getValue());
                nuovoorder.setTotale(totalField.getValue());
                nuovoorder.setStato(statusField.getValue());

                orderService.saveOrder(nuovoorder);
                 //Refreshare la griglia
                orders.clear();
                orders.addAll(orderService.findAll());
                grid.setItems(orders);


                // Logica NUOVO ordine
                /*Order newOrder = new Order(
                        idField.getValue(),
                        customerField.getValue(),
                        totalField.getValue(),
                        statusField.getValue()
                );
                orders.add(newOrder);*/
                Notification.show("Ordine aggiunto con successo!");
            } else {
                // Logica MODIFICA ordine
                currentOrder.setIdOrdine(idField.getValue());
                currentOrder.setCliente(customerField.getValue());
                currentOrder.setTotale(totalField.getValue());
                currentOrder.setStato(statusField.getValue());

                //Edite
                orderService.saveOrder(currentOrder);

                //Refreshare la griglia
                orders.clear();
                orders.addAll(orderService.findAll());
                grid.setItems(orders);

                Notification.show("Ordine modificato con successo!");
            }
            grid.getDataProvider().refreshAll();
            dialog.close();
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        return saveButton;
    }



    private SideNav getSideNav() {
        SideNav nav = new SideNav();
        nav.addItem(
                new SideNavItem("Dashboard", "/dashboard", VaadinIcon.DASHBOARD.create()),
                new SideNavItem("Orders", "/orders", VaadinIcon.CART.create()),
                new SideNavItem("Customers", "/customers", VaadinIcon.USER_HEART.create()),
                new SideNavItem("Products", "/products", VaadinIcon.PACKAGE.create())
        );
        return nav;
    }



}
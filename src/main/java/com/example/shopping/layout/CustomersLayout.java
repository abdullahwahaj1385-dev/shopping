package com.example.shopping.layout;

import com.example.shopping.model.Customer;
import com.example.shopping.service.CustomerService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Route("customers")
public class CustomersLayout extends AppLayout {

    private final H1 title = new H1("Wahaj - Clienti");
    private final DrawerToggle toggle = new DrawerToggle();

    private final CustomerService customerService;
    private List<Customer> customerList = new ArrayList<>();
    private Grid<Customer> grid = new Grid<>(Customer.class, false);


    @Autowired
    public CustomersLayout(CustomerService customerService) {
        this.customerService = customerService;


        this.customerList = new ArrayList<>(customerService.findAll());
        title.getStyle().set("font-size", "1.125rem").set("margin", "0");

        SideNav nav = getSideNav();
        nav.getStyle().set("margin", "var(--vaadin-gap-s)");

        addToDrawer(new Scroller(nav));
        addToNavbar(toggle, title);

        Grid<Customer> grid = new Grid<>(Customer.class, false);
        grid.setSizeFull();

        Grid.Column<Customer> nameColumn = grid.addColumn(Customer::getFullName).setHeader("Nome Completo");
        Grid.Column<Customer> emailColumn = grid.addColumn(Customer::getEmail).setHeader("Email");
        Grid.Column<Customer> phoneColumn = grid.addColumn(Customer::getPhone).setHeader("Telefono");

        //andare a leggere da db gli utenti


        GridListDataView<Customer> dataView = grid.setItems(customerList);
        CustomerFilter customerFilter = new CustomerFilter(dataView);

        // Bottone Nuovo Cliente (passa null come customer)
        Button addCustomerButton = new Button("Nuovo Cliente", VaadinIcon.PLUS.create());
        addCustomerButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addCustomerButton.addClickListener(e -> openCustomerDialog(null, dataView));

        HorizontalLayout toolbar = new HorizontalLayout(addCustomerButton);
        toolbar.setWidthFull();

        // Colonna Azioni con Modifica ed Elimina funzionanti
        grid.addComponentColumn(customer -> {
            Button editButton = new Button(VaadinIcon.EDIT.create());
            editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            editButton.setTooltipText("Modifica cliente");
            // Passa il cliente corrente per la modifica
            editButton.addClickListener(e -> openCustomerDialog(customer, dataView));


            Button deleteButton = new Button(VaadinIcon.TRASH.create());
            deleteButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);

            deleteButton.setTooltipText("Elimina cliente");

            deleteButton.addClickListener(e -> {
                // elimina dal database
                customerService.deleteCustomer(customer.getId());

                // elimina dalla lista della Grid
                customerList.remove(customer);

                // aggiorna la tabella
                dataView.refreshAll();


                Notification.show(
                        "Cliente eliminato: " + customer.getFullName()
                );

            });


            HorizontalLayout actions = new HorizontalLayout(editButton, deleteButton);
            actions.setSpacing(true);
            return actions;
        }).setHeader("Azioni").setWidth("120px").setFlexGrow(0);

        grid.getHeaderRows().clear();
        HeaderRow headerRow = grid.appendHeaderRow();

        headerRow.getCell(nameColumn).setComponent(createFilterHeader("Cerca nome...", customerFilter::setFullName));
        headerRow.getCell(emailColumn).setComponent(createFilterHeader("Cerca email...", customerFilter::setEmail));
        headerRow.getCell(phoneColumn).setComponent(createFilterHeader("Cerca telefono...", customerFilter::setPhone));

        VerticalLayout mainLayout = new VerticalLayout(toolbar, grid);
        mainLayout.setSizeFull();
        mainLayout.setPadding(true);
        mainLayout.setSpacing(true);

        setContent(mainLayout);
    }

    // Gestione unificata di Inserimento (se customer è null) e Modifica (se customer esiste)
    private void openCustomerDialog(Customer customer, GridListDataView<Customer> dataView) {
        Dialog dialog = new Dialog();

        TextField nameField = new TextField("Nome Completo");
        TextField emailField = new TextField("Email");
        TextField phoneField = new TextField("Telefono");

        if (customer == null) {
            dialog.setHeaderTitle("Aggiungi Nuovo Cliente");
        } else {
            dialog.setHeaderTitle("Modifica Cliente");
            nameField.setValue(customer.getFullName() != null ? customer.getFullName() : "");
            emailField.setValue(customer.getEmail() != null ? customer.getEmail() : "");
            phoneField.setValue(customer.getPhone() != null ? customer.getPhone() : "");
        }

        HorizontalLayout dialogLayout = new HorizontalLayout(nameField, emailField, phoneField);
        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(true);
        dialog.add(dialogLayout);

        Button saveButton = new Button("Salva", e -> {
            if (!nameField.getValue().isEmpty()) {
                if (customer == null) {
                    // Logica NUOVO
                    Customer nuovocliente = new Customer();
                    nuovocliente.setFullName(nameField.getValue());
                    nuovocliente.setEmail(emailField.getValue());
                    nuovocliente.setPhone(phoneField.getValue());

                    customerService.saveCustomer(nuovocliente);

                    //Refreshare la griglia
                    customerList.clear();
                    customerList.addAll(customerService.findAll());

                    dataView.refreshAll();

                    Notification.show("Cliente aggiunto con successo!");
                } else {
                    // Logica MODIFICA
                    customer.setFullName(nameField.getValue());
                    customer.setEmail(emailField.getValue());
                    customer.setPhone(phoneField.getValue());

                    //edit
                    customerService.saveCustomer(customer);

                    //Refreshare la griglia
                    customerList.clear();
                    customerList.addAll(customerService.findAll());

                    dataView.refreshAll();

                    Notification.show("Cliente modificato con successo!");
                }

                dataView.refreshAll();
                dialog.close();
            } else {
                Notification.show("Il nome completo è obbligatorio!");
            }
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button cancelButton = new Button("Annulla", e -> dialog.close());

        dialog.getFooter().add(cancelButton, saveButton);

        dialog.open();
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

    private static Component createFilterHeader(String placeholder, Consumer<String> filterChangeConsumer) {
        TextField textField = new TextField();
        textField.setPlaceholder(placeholder);
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.setClearButtonVisible(true);
        textField.setWidthFull();
        textField.addValueChangeListener(e -> filterChangeConsumer.accept(e.getValue()));
        return textField;
    }


    private static class CustomerFilter {
        private final GridListDataView<Customer> dataView;
        private String fullName;
        private String email;
        private String phone;

        public CustomerFilter(GridListDataView<Customer> dataView) {
            this.dataView = dataView;
            this.dataView.addFilter(this::test);
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
            this.dataView.refreshAll();
        }

        public void setEmail(String email) {
            this.email = email;
            this.dataView.refreshAll();
        }

        public void setPhone(String phone) {
            this.phone = phone;
            this.dataView.refreshAll();
        }

        public boolean test(Customer customer) {
            return matches(customer.getFullName(), fullName)
                    && matches(customer.getEmail(), email)
                    && matches(customer.getPhone(), phone);
        }

        private boolean matches(String value, String searchTerm) {
            return searchTerm == null || searchTerm.isEmpty()
                    || (value != null && value.toLowerCase().contains(searchTerm.toLowerCase()));
        }
    }


}

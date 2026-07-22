package com.example.shopping.layout;

import com.example.shopping.model.Product;
import com.example.shopping.service.ProductService;
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

@Route("products")
public class ProductsLayout extends AppLayout {

    private List<Product> products = new ArrayList<>();
    private Grid<Product> grid = new Grid<>(Product.class, false);

    private final ProductService productService;

    private TextField nameField = new TextField("Nome Product");
    private TextField codeField = new TextField("Codice");
    private TextField priceField = new TextField("Prezzo");
    private TextField qtyField = new TextField("Quantità");
    private Product currentProduct; // Tiene traccia del prodotto che stiamo modificando

    @Autowired
    public ProductsLayout(ProductService productService) {
        this.productService = productService;

        this.products= new ArrayList<>(productService.findAll());
        DrawerToggle toggle = new DrawerToggle();

        H1 title = new H1("Prodotti Tecnologici");
        title.getStyle()
                .set("font-size", "1.125rem")
                .set("margin", "0");
        SideNav nav = getSideNav();
        nav.getStyle().set("margin", "var(--vaadin-gap-s)");

        Scroller scroller = new Scroller(nav);

        addToDrawer(scroller);
        addToNavbar(toggle, title);


        grid.addColumn(Product::getNomeProduct).setHeader("Nome Product");
        grid.addColumn(Product::getCodice).setHeader("Codice");
        grid.addColumn(Product::getPrezzo).setHeader("Prezzo");
        grid.addColumn(Product::getQuantita).setHeader("Quantità");


        Dialog dialog = new Dialog();
        FormLayout dialogLayout = createDialogLayout();
        dialog.add(dialogLayout);

        Button saveButton = createSaveButton(dialog);
        Button cancelButton = new Button("Cancel", e -> dialog.close());
        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(saveButton);

        grid.addComponentColumn(product -> {
            Button editButton = new Button(VaadinIcon.EDIT.create());
            editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            editButton.setTooltipText("Modifica prodotto");
            editButton.addClickListener(e -> {
                currentProduct = product;
                dialog.setHeaderTitle("Modifica Prodotto");
                nameField.setValue(product.getNomeProduct());
                codeField.setValue(product.getCodice());
                priceField.setValue(product.getPrezzo());
                qtyField.setValue(product.getQuantita());
                dialog.open();
            });

            Button deleteButton = new Button(VaadinIcon.TRASH.create());
            deleteButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
            deleteButton.setTooltipText("Elimina prodotto");
            deleteButton.addClickListener(e -> {
                productService.deleteProduct(product.getId());

                products.remove(product);

                grid.getDataProvider().refreshAll();
                Notification.show("Prodotto eliminato: " + product.getNomeProduct());
            });

            HorizontalLayout actions = new HorizontalLayout(editButton, deleteButton);
            actions.setSpacing(true);
            return actions;
        }).setHeader("Azioni");

        grid.setItems(products);


        Button addProductButton = new Button("Nuovo Prodotto", VaadinIcon.PLUS.create(), e -> {
            currentProduct = null;
            dialog.setHeaderTitle("Nuovo Prodotto");
            nameField.clear();
            codeField.clear();
            priceField.clear();
            qtyField.clear();
            dialog.open();
        });
        addProductButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);


        VerticalLayout mainContent = new VerticalLayout(addProductButton, grid);
        mainContent.setSizeFull();
        grid.setSizeFull();

        setContent(mainContent);
    }

    private FormLayout createDialogLayout() {
        FormLayout layout = new FormLayout();
        layout.add(nameField, codeField, priceField, qtyField);
        return layout;
    }

    private Button createSaveButton(Dialog dialog) {
        Button saveButton = new Button("Save", e -> {
            if (currentProduct == null) {
                //La logica Nuova
                Product nuovoproduct= new Product();
                nuovoproduct.setNomeProduct(nameField.getValue());
                nuovoproduct.setCodice(codeField.getValue());
                nuovoproduct.setPrezzo(priceField.getValue());
                nuovoproduct.setQuantita(qtyField.getValue());

                productService.saveProduct(nuovoproduct);
                // aggiorna la lista
                products.clear();
                products.addAll(productService.findAll());

                 // aggiorna la griglia
                grid.setItems(products);

                /* Logica NUOVO prodotto
                Product newProduct = new Product(
                        nameField.getValue(),
                        codeField.getValue(),
                        priceField.getValue(),
                        qtyField.getValue()
                );
                products.add(newProduct);*/
                Notification.show("Prodotto aggiunto con successo!");
            } else {

                currentProduct.setNomeProduct(nameField.getValue());
                currentProduct.setCodice(codeField.getValue());
                currentProduct.setPrezzo(priceField.getValue());
                currentProduct.setQuantita(qtyField.getValue());

                //Edit
                productService.saveProduct(currentProduct);


                products.clear();
                products.addAll(productService.findAll());

                // aggiorna la griglia
                grid.setItems(products);



                Notification.show("Prodotto modificato con successo!");
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


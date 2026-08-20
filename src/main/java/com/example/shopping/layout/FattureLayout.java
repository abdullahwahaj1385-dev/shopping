package com.example.shopping.layout;

import com.example.shopping.model.Fattura;
import com.example.shopping.service.FatturaService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Route("fatture")
public class FattureLayout extends AppLayout {

    private final FatturaService fatturaService;
    private List<Fattura> fatture = new ArrayList<>();
    private Grid<Fattura> grid = new Grid<>(Fattura.class, false);

    // Campi del modulo CRUD richiesti
    private TextField numeroField = new TextField("Numero Fattura");
    private TextField clienteField = new TextField("Nuovo Cliente");
    private NumberField importoField = new NumberField("Importo (€)");
    private TextField merceField = new TextField("La Merce Comprata");
    private DatePicker dataField = new DatePicker("Data Consegna");

    private Fattura currentFattura;

    @Autowired
    public FattureLayout(FatturaService fatturaService) {
        this.fatturaService = fatturaService;
        this.fatture = new ArrayList<>(fatturaService.findAll());

        DrawerToggle toggle = new DrawerToggle();
        H1 title = new H1("WahajShop");
        title.getStyle().set("font-size", "1.125rem").set("margin", "0");

        SideNav nav = getSideNav();
        nav.getStyle().set("margin", "var(--vaadin-gap-s)");
        Scroller scroller = new Scroller(nav);
        addToDrawer(scroller);
        addToNavbar(toggle, title);

        // Configurazione delle colonne della Griglia Vaadin
        grid.addColumn(Fattura::getId).setHeader("ID");
        grid.addColumn(Fattura::getNumero).setHeader("N. Fattura");
        grid.addColumn(Fattura::getCliente).setHeader("Cliente");
        grid.addColumn(Fattura::getMerceComprata).setHeader("Merce Comprata");
        grid.addColumn(Fattura::getImporto).setHeader("Importo (€)");
        grid.addColumn(Fattura::getData).setHeader("Data");

        // Configurazione della finestra popup (Dialog)
        Dialog dialog = new Dialog();
        FormLayout dialogLayout = new FormLayout();
        dialogLayout.add(numeroField, clienteField, merceField, importoField, dataField);
        dialog.add(dialogLayout);

        Button saveButton = createSaveButton(dialog);
        Button cancelButton = new Button("Annulla", e -> dialog.close());
        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(saveButton);

        // Colonna delle azioni (Modifica, Elimina, Scarica/Registra PDF)
        grid.addComponentColumn(fattura -> {
            Button editButton = new Button(VaadinIcon.EDIT.create(), e -> {
                currentFattura = fattura;
                dialog.setHeaderTitle("Modifica Fattura");
                numeroField.setValue(fattura.getNumero() != null ? fattura.getNumero() : "");
                clienteField.setValue(fattura.getCliente() != null ? fattura.getCliente() : "");
                merceField.setValue(fattura.getMerceComprata() != null ? fattura.getMerceComprata() : "");
                importoField.setValue(fattura.getImporto() != null ? fattura.getImporto() : 0.0);
                dataField.setValue(fattura.getData());
                dialog.open();
            });
            editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

            Button deleteButton = new Button(VaadinIcon.TRASH.create(), e -> {
                fatturaService.deleteFattura(fattura.getId());
                fatture.remove(fattura);
                grid.getDataProvider().refreshAll();
                Notification.show("Fattura cancellata!");
            });
            deleteButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);

            // PULSANTE STAMPANTE CORRETTO: Crea l'icona verde per registrare la fattura
            Button printButton = new Button(VaadinIcon.PRINT.create());
            printButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SUCCESS);
            printButton.setTooltipText("Registra e Scarica Fattura");

            // Generazione sicura del codice HTML della ricevuta per il cliente finale
            StreamResource resource = new StreamResource("Fattura_" + (fattura.getNumero() != null ? fattura.getNumero() : fattura.getId()) + ".html", () -> {
                String htmlContent = "<html><head><title>Fattura_" + fattura.getNumero() + "</title></head>"
                        + "<body style='font-family:sans-serif;padding:40px;'>"
                        + "<h1 style='color:#2b2b2b;'>WAHAJ SHOP - RICEVUTA FATTURA</h1><hr>"
                        + "<p><b>Numero Documento:</b> " + fattura.getNumero() + "</p>"
                        + "<p><b>Cliente Finale:</b> " + fattura.getCliente() + "</p>"
                        + "<p><b>Data Consegna:</b> " + fattura.getData() + "</p><br>"
                        + "<table style='width:100%;border-collapse:collapse;' border='1' cellpadding='10'>"
                        + "<tr style='background:#f4f4f4;'><th>Descrizione Merce Comprata</th><th>Totale Corrisposto</th></tr>"
                        + "<tr><td>" + fattura.getMerceComprata() + "</td><td>" + fattura.getImporto() + " €</td></tr>"
                        + "</table><br><br>"
                        + "<h3>Fattura registrata nel sistema con successo.</h3>"
                        + "<script>window.print();</script></body></html>";

                return new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
            });

            // Anchor di Vaadin: aggira il blocco di sicurezza del browser scaricando il file direttamente
            Anchor downloadLink = new Anchor(resource, "");
            downloadLink.getElement().setAttribute("download", true);
            downloadLink.add(printButton);

            HorizontalLayout actions = new HorizontalLayout(editButton, deleteButton, downloadLink);
            actions.setSpacing(true);
            return actions;
        }).setHeader("Azioni");

        grid.setItems(fatture);

        // Pulsante principale per creare un nuovo record
        Button addFatturaButton = new Button("Nuova Fattura", VaadinIcon.PLUS.create(), e -> {
            currentFattura = null;
            dialog.setHeaderTitle("Nuova Fattura");
            numeroField.clear();
            clienteField.clear();
            merceField.clear();
            importoField.clear();
            dataField.clear();
            dialog.open();
        });
        addFatturaButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        VerticalLayout mainContent = new VerticalLayout(addFatturaButton, grid);
        mainContent.setSizeFull();
        grid.setSizeFull();
        setContent(mainContent);
    }

    private Button createSaveButton(Dialog dialog) {
        Button saveButton = new Button("Salva", e -> {
            if (currentFattura == null) {
                Fattura nuovaFattura = new Fattura();
                nuovaFattura.setNumero(numeroField.getValue());
                nuovaFattura.setCliente(clienteField.getValue());
                nuovaFattura.setMerceComprata(merceField.getValue());
                nuovaFattura.setImporto(importoField.getValue());
                nuovaFattura.setData(dataField.getValue());
                fatturaService.saveFattura(nuovaFattura);
                Notification.show("Fattura registrata con successo!");
            } else {
                currentFattura.setNumero(numeroField.getValue());
                currentFattura.setCliente(clienteField.getValue());
                currentFattura.setMerceComprata(merceField.getValue());
                currentFattura.setImporto(importoField.getValue());
                currentFattura.setData(dataField.getValue());
                fatturaService.saveFattura(currentFattura);
                Notification.show("Fattura modificata con successo!");
            }

            fatture.clear();
            fatture.addAll(fatturaService.findAll());
            grid.setItems(fatture);
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
                new SideNavItem("Products", "/products", VaadinIcon.PACKAGE.create()),
                new SideNavItem("Fatture", "/fatture", VaadinIcon.FILE_TEXT.create())
        );
        return nav;
    }
}

package com.example.shopping.layout;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Route;

@Route("dashboard")
public class dashboardLayout extends AppLayout {

    public dashboardLayout() {
        DrawerToggle toggle = new DrawerToggle();

        H1 title = new H1("WahajShop");
        title.getStyle()
                .set("font-size", "1.125rem")
                .set("margin", "0");

        SideNav nav = getSideNav();
        nav.getStyle().set("margin", "var(--vaadin-gap-s)");

        Scroller scroller = new Scroller(nav);

        addToDrawer(scroller);
        addToNavbar(toggle, title);

    }

    private SideNav getSideNav() {
        SideNav nav = new SideNav();
        nav.addItem(
                new SideNavItem("Dashboard", "/dashboard",
                        VaadinIcon.DASHBOARD.create()),
                new SideNavItem("Orders", "/orders",
                        VaadinIcon.CART.create()),
                new SideNavItem("Customers", "/customers",
                        VaadinIcon.USER_HEART.create()),
                new SideNavItem("Products", "/products",
                        VaadinIcon.PACKAGE.create())
        );
        return nav;
    }
}

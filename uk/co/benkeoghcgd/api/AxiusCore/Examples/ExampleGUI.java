package uk.co.benkeoghcgd.api.AxiusCore.Examples;

import org.bukkit.event.inventory.InventoryClickEvent;
import uk.co.benkeoghcgd.api.AxiusCore.API.AxiusPlugin;
import uk.co.benkeoghcgd.api.AxiusCore.API.GUI;

public class ExampleGUI extends GUI {
    public ExampleGUI(AxiusPlugin instance, int rows, String title) {
        super(instance, rows, title);
    }

    @Override
    protected void Populate() {
    }

    @Override
    protected void onInvClick(InventoryClickEvent e) {

    }
}

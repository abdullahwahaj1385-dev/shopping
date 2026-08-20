package com.example.shopping.pdf;

import com.example.shopping.model.Fattura;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;

@Component
public class PdfGenera {

    // Questo metodo crea il contenuto della fattura per il cliente finale
    public byte[] generaHtmlFattura(Fattura fattura) {
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
                + "<h3>Fattura registrata nell'applicazione con successo.</h3>"
                + "<script>window.print();</script></body></html>";

        return htmlContent.getBytes(StandardCharsets.UTF_8);
    }
}

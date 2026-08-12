package com.example.shopping.Controller;

import com.example.shopping.DTO.ProductDTO;
import com.example.shopping.model.Product;
import com.example.shopping.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Elenco tutti i prodotti
    @GetMapping("/all")
    public List<Product> getAllProducts() {
        return productService.findAll();
    }

    // La creazione
    @PostMapping("/createNewProduct")
    public String createProduct(@RequestBody ProductDTO productDto) {
        try {
            Product nuovoProdotto = new Product();
            nuovoProdotto.setNomeProduct(productDto.getNomeProduct());
            nuovoProdotto.setPrezzo(productDto.getPrezzo());
            nuovoProdotto.setCodice(productDto.getCodice());
            nuovoProdotto.setQuantita(productDto.getQuantita());

            productService.saveProduct(nuovoProdotto);
            return "prodotto inserito";
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Errore di inserimento", e);
        }
    }

    // La Modifica
    @PutMapping("/{id}")
    public String updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDto) {
        try {
            Product existingProduct = productService.findById(id);
            if (existingProduct == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Prodotto non trovato");
            }
            /*

            existingProduct.setName(productDto.getName());
            existingProduct.setDescription(productDto.getDescription());
            existingProduct.setPrice(productDto.getPrice());
            existingProduct.setStock(productDto.getStock());
            */

            productService.saveProduct(existingProduct);
            return "prodotto modificato";
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Errore di modifica", e);
        }
    }

    //  Delete
    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return "prodotto cancellato";
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Errore di cancellazione", e);
        }
    }

    // Restituisce prodotto per ID
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        Product product = productService.findById(id);
        if (product == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Prodotto non trovato");
        }
        return product;
    }
}

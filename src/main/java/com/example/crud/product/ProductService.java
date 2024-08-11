package com.example.crud.product;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    HashMap<String, Object> datos;

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository=productRepository;
    }

    public List<Product> getProducts(){
        //trae una lista de productos
        return this.productRepository.findAll();
    }

    public ResponseEntity<Object> newProduct(Product product) {
        //consultar si el proyecto existe Opcional
        Optional<Product> res = productRepository.findProductByName(product.getName());

        // si encontor o no enecotro el producto a traves del nombre
        datos=new HashMap<>();

        if (res.isPresent()&& product.getId()==null){// si esta presente y id es null entonces
            datos.put("error", true);
            datos.put("message","ya existe un producto con ese nombre");
            return new ResponseEntity<>(
                    datos,
                    HttpStatus.CONFLICT
            );
        }
        datos.put("message","se ha guardado con exito");
        if (product.getId()!=null) {
            datos.put("message","se ha actualizo con exito");
        }
        productRepository.save(product);
        datos.put("Datos", product);

        return new ResponseEntity<>(
                datos,

                HttpStatus.CREATED
        );
    }
    //Eliminar
    public ResponseEntity<Object> deleteproduct(Long id){
        datos=new HashMap<>();
        boolean esiste=this.productRepository.existsById(id);
       if(!esiste){
           datos.put("error", true);
           datos.put("message","no existe un producto con ese id");
           return new ResponseEntity<>(
                   datos,
                   HttpStatus.CONFLICT
           );
       }
       productRepository.deleteById(id);
        datos.put("message","producto eliminado con exito");
        return new ResponseEntity<>(
                datos,
                HttpStatus.ACCEPTED
        );
    }
}

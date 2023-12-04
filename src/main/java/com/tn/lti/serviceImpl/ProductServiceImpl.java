package com.tn.lti.serviceImpl;

import com.tn.lti.JWT.JwtFilter;
import com.tn.lti.constents.CommandConstants;
import com.tn.lti.dao.ProductDao;
import com.tn.lti.POJO.Category;
import com.tn.lti.POJO.Product;
import com.tn.lti.service.ProductService;
import com.tn.lti.utils.CommandeUtils;
import com.tn.lti.wrapper.ProductWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    ProductDao productDao;

    @Override
    public ResponseEntity<String> addNewProduct(Map<String, String> requestmap) {

        try {
            if (jwtFilter.isAdmin()) {
                log.info("is Admin");
                if (validateMap(requestmap, false)) {
                    productDao.save(extractProductFromMap(requestmap, false));
                    return CommandeUtils.getResponseEntity("Product Add Successfully", HttpStatus.OK);
                } else {
                    return CommandeUtils.getResponseEntity(CommandConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
                }

            } else {

                return CommandeUtils.getResponseEntity(CommandConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return CommandeUtils.getResponseEntity(CommandConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getAllProduct() {
        try {
            return new ResponseEntity<>(productDao.getAllProduct(), HttpStatus.OK);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                log.info(String.valueOf(validateMap(requestMap, true)));
                if (validateMap(requestMap, true)) {
                    Optional<Product> optional = productDao.findById(Integer.parseInt(requestMap.get("id")));

                    if (!optional.isEmpty()) {
                        Product product = extractProductFromMap(requestMap, true);
                        product.setStatus(optional.get().getStatus());
                        productDao.save(product);
                        return CommandeUtils.getResponseEntity("Product Updated Successfully ", HttpStatus.OK);

                    } else {
                        return CommandeUtils.getResponseEntity("Product Id does not exist.", HttpStatus.OK);
                    }

                } else {
                    return CommandeUtils.getResponseEntity(CommandConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
                }

            } else {

                return CommandeUtils.getResponseEntity(CommandConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return CommandeUtils.getResponseEntity(CommandConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteProduct(Integer id) {
        try {
            if (jwtFilter.isAdmin()) {

                Optional<Product> optional = productDao.findById(id);
                if (!optional.isEmpty()) {
                    productDao.deleteById(id);

                    return CommandeUtils.getResponseEntity("Product Delete Successfully ", HttpStatus.OK);

                } else {
                    return CommandeUtils.getResponseEntity("Product id Does not exist in the data base ", HttpStatus.OK);
                }

            } else {
                return CommandeUtils.getResponseEntity(CommandConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommandeUtils.getResponseEntity(CommandConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                Optional<Product> optional = productDao.findById(Integer.parseInt(requestMap.get("id")));
                if (!optional.isEmpty()) {
                    productDao.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
                    return CommandeUtils.getResponseEntity("Product Status Updated Successfully ", HttpStatus.OK);
                } else {

                    return CommandeUtils.getResponseEntity("Product id does not exist ", HttpStatus.OK);

                }

            } else {

                return CommandeUtils.getResponseEntity(CommandConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return CommandeUtils.getResponseEntity(CommandConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getProductByCategory(Integer id) {
        try {
            return new ResponseEntity<>(productDao.getProductByCategory(id), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<ProductWrapper> getProductById(Integer id) {
        try{
            return new ResponseEntity<>(productDao.getProductById(id),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(new ProductWrapper(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Product extractProductFromMap(Map<String, String> requestmap, boolean isAdd) {

        Category category = new Category();

        category.setId(Integer.parseInt(requestmap.get("categoryId")));

        Product product = new Product();
        if (isAdd) {
            product.setId(Integer.parseInt(requestmap.get("id")));
        } else {
            product.setStatus("true");
        }
        product.setName(requestmap.get("name"));
        product.setDescription(requestmap.get("description"));
        product.setPrice(Integer.parseInt(requestmap.get("price")));
        product.setCategory(category);
        return product;


    }

    private boolean validateMap(Map<String, String> requestmap, boolean validateid) {
        if (requestmap.containsKey("name") && requestmap.containsKey("description")) { //you can verfied all the champ
            if (requestmap.containsKey("id") && validateid) {
                return true;
            } else if (!validateid) {
                return true;
            }
        }
        return false;
    }
}

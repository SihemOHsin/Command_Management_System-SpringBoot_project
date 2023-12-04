package com.tn.lti.POJO;

import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import java.io.Serializable;

@NamedQuery(name ="Product.getAllProduct" , query = "select new com.tn.lti.wrapper.ProductWrapper(p.id,p.name,p.description,p.price,p.status,p.category.id,p.category.name ) from Product p")

@NamedQuery(name = "Product.updateStatus" , query = "update Product p set p.status=:status where p.id=:id")

@NamedQuery(name = "Product.getProductByCategory" , query = "select new com.tn.lti.wrapper.ProductWrapper(p.id,p.name ) from Product p where p.category.id=:id and p.status='true'")

@NamedQuery(name = "Product.getProductById",query = "select new com.tn.lti.wrapper.ProductWrapper(p.id,p.name,p.description,p.price ) from Product p where p.id=:id")

@Data
@DynamicUpdate
@DynamicInsert
@Entity
@Table(name="product")
public class Product implements Serializable {

    private static final long serialVersionUID =1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;


    @Column(name = "name")
    private String name;


    /****************************** explain fetch=FetchType.LAZY ****************************************
     * For example, consider two entities, Person and Address, where a Person has one Address and an Address
     * can be associated with many Persons. If the relationship between Person and Address is defined with
     * FetchType. LAZY, when you load a Person from the database, its associated Address will not be loaded
     * immediately. Instead,
     * the Address will only be loaded from the database when you first access the Person's Address property.
     *  while FetchType. EAGER would load the related entity immediately when the containing entity is loaded.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_Category" ,nullable = false)
    private  Category category;

    @Column(name = "description")
    private  String description;

    @Column(name = "price")
    private  Integer price;

    @Column(name="status")
    private  String status;

}

package com.cafy.application.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Data;
import org.hibernate.annotations.*;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
@NamedQuery(name = "Bill.getAllBills",query = "select b from Bill b order by b.id desc")
@NamedQuery(name = "Bill.getBillByUserName",query = "select b from Bill b where b.createdBy=:username order by b.id desc")
@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "bill")
@TypeDefs({
        @TypeDef(name = "json", typeClass = JsonStringType.class),
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
public class Bill implements Serializable {
    private  static  final  long serialVersionUID= 1L ;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "name")
    private String name;

    @Column(name = "contactnumber")
    private String contactNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "paymentmethod")
    private String paymentMethod;

    @Column(name = "total")
    private Integer total;

    @Type(type = "jsonb")
    @Column(name = "productdetails",columnDefinition = "json")
    private String productDetail;


    @Column(name = "createdby")
    private String createdBy;



}

package com.example.ip_management_system.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "ippools")
public class IpPool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String startIp;

    @Column(nullable = false)
    private String endIp;

    @Column(nullable = false)
    private String subnetMask;

    @Column(nullable = false)
    private String gateway;

    @OneToMany(mappedBy = "ipPool")
    private List<Service> services;  // One-to-many relationship with services

    @Column(name = "created_at", updatable = false)
    private String createdAt;

    @Column(name = "updated_at")
    private String updatedAt;


    /**
     * @return Long return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return String return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return String return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return String return the startIp
     */
    public String getStartIp() {
        return startIp;
    }

    /**
     * @param startIp the startIp to set
     */
    public void setStartIp(String startIp) {
        this.startIp = startIp;
    }

    /**
     * @return String return the endIp
     */
    public String getEndIp() {
        return endIp;
    }

    /**
     * @param endIp the endIp to set
     */
    public void setEndIp(String endIp) {
        this.endIp = endIp;
    }

    /**
     * @return String return the subnetMask
     */
    public String getSubnetMask() {
        return subnetMask;
    }

    /**
     * @param subnetMask the subnetMask to set
     */
    public void setSubnetMask(String subnetMask) {
        this.subnetMask = subnetMask;
    }

    /**
     * @return String return the gateway
     */
    public String getGateway() {
        return gateway;
    }

    /**
     * @param gateway the gateway to set
     */
    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    /**
     * @return String return the createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt the createdAt to set
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * @return String return the updatedAt
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     * @param updatedAt the updatedAt to set
     */
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}
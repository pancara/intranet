package com.integrasolusi.pusda.intranet.persistence;

import java.io.Serializable;

/**
 * Programmer   : pancara
 * Date         : Jan 5, 2011
 * Time         : 4:15:15 PM
 */
public class Staff implements Serializable {
    private Long id;
    private Integer version;
    private String name;
    private String golongan;
    private String nip;
    private String jabatan;

    public Staff() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGolongan() {
        return golongan;
    }

    public void setGolongan(String golongan) {
        this.golongan = golongan;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getJabatan() {
        return jabatan;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }

}
package com.ildong.scheduler.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class EtcMenuTable implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String deleteFlag;
    private int menuDepth;
    private String menuName;
    private String menuParent;
    private String menuCode;
    private String menuLink;
    private String menuGubun;

}
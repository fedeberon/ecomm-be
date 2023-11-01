package com.ideaas.ecomm.ecomm.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "SCHEDULES")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "SCH_ID")
    private Long id;

    @Column(name = "SCH_MO_OP")
    private String monOpen;

    @Column(name = "SCH_MO_CL")
    private String monClose;

    @Column(name = "SCH_TU_OP")
    private String tueOpen;

    @Column(name = "SCH_TU_CL")
    private String tueClose;

    @Column(name = "SCH_WE_OP")
    private String wedOpen;

    @Column(name = "SCH_WE_CL")
    private String wedClose;

    @Column(name = "SCH_TH_OP")
    private String thuOpen;

    @Column(name = "SCH_TH_CL")
    private String thuClose;

    @Column(name = "SCH_FR_OP")
    private String friOpen;

    @Column(name = "SCH_FR_CL")
    private String friClose;

    @Column(name = "SCH_SA_OP")
    private String satOpen;

    @Column(name = "SCH_SA_CL")
    private String satClose;

    @Column(name = "SCH_SU_OP")
    private String sunOpen;

    @Column(name = "SCH_SU_CL")
    private String sunClose;
}

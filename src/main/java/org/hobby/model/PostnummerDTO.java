package org.hobby.model;

import lombok.Getter;
@Getter
public class PostnummerDTO {
    private String href;
    private String nr;
    private String navn;
    private Object stormodtageradresser;
    private double[] bbox;
    private double[] visueltcenter;
    private Object[] kommuner;
    private String ændret;
    private String geo_ændret;
    private int geo_version;
    private String dagi_id;


    @Override
    public String toString() {
        return "PostnummerDTO{" +
                "nr='" + nr + '\'' +
                ", navn='" + navn + '\'' +
                '}';
    }
}

package pl.edu.pwr.wordnetloom.common.dto;

public class DataMap {

    private Long id;
    private String text;

    public DataMap(Long id, String text){
        this.id = id;
        this.text = text;
    }

    public Long getId(){return id;}
    public void setId(Long id){this.id = id;}

    public String getText(){return text;}
    public void setText(String text) {this.text = text;}
}
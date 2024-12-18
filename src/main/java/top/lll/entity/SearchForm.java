package top.lll.entity;

import lombok.Data;

@Data
public class SearchForm {
    private String input;
    private String categoryValue;
    private String sortValue;
    private String teamValue;
}

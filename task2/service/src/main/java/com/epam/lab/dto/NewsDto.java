package com.epam.lab.dto;


import java.sql.Date;
import java.util.List;

public class NewsDto {
    private long id;
    private String title;
    private String shortText;
    private String fullText;
    private Date creationDate;
    private Date modificationDate;
    private AuthorDto authorDto;
    private List<TagDto> dtoList;
}

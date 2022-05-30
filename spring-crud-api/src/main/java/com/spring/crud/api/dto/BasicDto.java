package com.spring.crud.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The basic Data Transfer Object.
 *
 * @author Mauricio Generoso
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(access =  AccessLevel.PACKAGE)
@Data
public abstract class BasicDto implements Serializable {

    private String id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private String version;
}
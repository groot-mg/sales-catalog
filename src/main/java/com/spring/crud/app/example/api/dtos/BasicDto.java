package com.spring.crud.app.example.api.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * The basic Data Transfer Object.
 *
 * @author Mauricio Generoso
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class BasicDto implements Serializable {

    private String id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private String version;

    BasicDto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    void setVersion(String version) {
        this.version = version;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasicDto basicDto = (BasicDto) o;
        return id.equals(basicDto.id) &&
                createdAt.equals(basicDto.createdAt) &&
                version.equals(basicDto.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdAt, version);
    }
}
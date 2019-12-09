package com.spring.crud.app.example.api.dtos;

/**
 * The basic V1 Data Transfer Object
 *
 * @author Mauricio Generoso
 */
public abstract class BasicV1Dto extends BasicDto {

    BasicV1Dto() {
        setVersion("V1");
    }

}
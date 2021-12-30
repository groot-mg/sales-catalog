package com.spring.crud.lib.service.validators;


import com.spring.crud.lib.exception.DuplicateException;
import com.spring.crud.lib.model.Item;
import com.spring.crud.lib.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Test {@link DuplicatedItemValidator}
 *
 * @author Mauricio Generoso
 */
@ExtendWith(MockitoExtension.class)
class DuplicatedItemValidatorTest {

    @Mock
    private ItemRepository repository;

    private DuplicatedItemValidator validator;

    @BeforeEach
    void setup() {
        this.validator = new DuplicatedItemValidator();
    }

    @Test
    void validate_shouldPassWhenIsNewAndThereIsNotOtherWithTheSameName() {
        // Arrange
        String name = "testName";

        Item item = new Item();
        item.setName(name);

        doReturn(Optional.empty()).when(repository).findByName(name);

        // Act
        validator.validate(repository, item);

        // Assert
        verify(repository, times(1)).findByName(name);
    }

    @Test
    void validate_shouldThrowsExceptionWhenIsNewAndThereIsOtherWithTheSameName() {
        // Arrange
        String name = "testName";

        Item item = new Item();
        item.setName(name);

        doReturn(Optional.of(new Item())).when(repository).findByName(name);

        // Act & Assert
        assertThrows(DuplicateException.class, () -> validator.validate(repository, item));
    }

    @Test
    void validate_shouldPassWhenIsNotNewAndThereIsNotWithTheSameName() {
        // Arrange
        String name = "testName";

        Item item = new Item();
        item.setId(UUID.randomUUID());
        item.setName(name);

        doReturn(Optional.empty()).when(repository).findByName(name);

        // Act
        validator.validate(repository, item);

        // Assert
        verify(repository, times(1)).findByName(name);
    }

    @Test
    void validate_shouldPassWhenIsNotNewAndThereIsOtherItemWithTheSameName() {
        // Arrange
        String name = "testName";

        Item item = new Item();
        item.setId(UUID.randomUUID());
        item.setName(name);

        Item existingItem = new Item();
        existingItem.setId(UUID.randomUUID());
        existingItem.setName(name);

        doReturn(Optional.of(existingItem)).when(repository).findByName(name);

        // Act & Assert
        assertThrows(DuplicateException.class, () -> validator.validate(repository, item));
    }
}

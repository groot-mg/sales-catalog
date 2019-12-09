package com.spring.crud.app.example.services.validators;


import com.spring.crud.app.example.models.Item;
import com.spring.crud.app.example.repositories.ItemRepository;
import com.spring.crud.app.example.services.exceptions.DuplicateException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

/**
 * Test {@link DuplicatedItemValidator}
 *
 * @author Mauricio Generoso
 */
@RunWith(SpringRunner.class)
public class DuplicatedItemValidatorTest {

    @Mock
    private ItemRepository repository;

    private DuplicatedItemValidator validator;

    @Before
    public void setup() {
        this.validator = new DuplicatedItemValidator();
    }

    @Test
    public void validate_shouldPassWhenIsNewAndThereIsNotOtherWithTheSameName() {
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

    @Test(expected = DuplicateException.class)
    public void validate_shouldThrowsExceptionWhenIsNewAndThereIsOtherWithTheSameName() {
        // Arrange
        String name = "testName";

        Item item = new Item();
        item.setName(name);

        doReturn(Optional.of(new Item())).when(repository).findByName(name);

        // Act
        validator.validate(repository, item);
    }

    @Test
    public void validate_shouldPassWhenIsNotNewAndThereIsNotWithTheSameName(){
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

    @Test(expected = DuplicateException.class)
    public void validate_shouldPassWhenIsNotNewAndThereIsOtherItemWithTheSameName(){
        // Arrange
        String name = "testName";

        Item item = new Item();
        item.setId(UUID.randomUUID());
        item.setName(name);

        Item existingItem = new Item();
        existingItem.setId(UUID.randomUUID());
        existingItem.setName(name);

        doReturn(Optional.of(existingItem)).when(repository).findByName(name);

        // Act
        validator.validate(repository, item);
    }
}

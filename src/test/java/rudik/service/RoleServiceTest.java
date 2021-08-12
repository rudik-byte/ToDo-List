package rudik.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import rudik.exception.NullEntityReferenceException;
import rudik.model.Role;
import rudik.repository.RoleRepository;
import rudik.service.impl.RoleServiceImpl;

import javax.persistence.EntityNotFoundException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {

    // @Mock creates a mock.
    // @InjectMocks creates an instance of the
    // class and injects the mocks that are created with
    // the @Mock (or @Spy) annotations into this instance.
    // With JUnit 5, you must use @ExtendWith(MockitoExtension.class).
    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    public Role expectedRole;

    final Long ROLE_ID = 1L;
    final String NAME = "Ivan";

    @BeforeEach
    public void setUp() {
        expectedRole = new Role();
        expectedRole.setName(NAME);
    }

    @Test
    public void shouldCreateRole() throws NullEntityReferenceException {
        Mockito.doReturn(expectedRole).when(roleRepository).save(expectedRole);

        Role actualRole = roleService.create(expectedRole);

        verify(roleRepository).save(any(Role.class));
        Assertions.assertEquals(expectedRole, actualRole);
    }

    @Test
    public void shouldThrowNullAndNotCreateRole() {
        when(roleRepository.save(any(Role.class))).thenThrow(new IllegalArgumentException());
        Exception exception = assertThrows(NullEntityReferenceException.class, () -> roleService.create(expectedRole));

        Assertions.assertEquals("Role cannot be 'null'", exception.getMessage());
        verify(roleRepository).save(any(Role.class));
    }

    @Test
    public void shouldThrowExceptionReadById() {
        when(roleRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> roleService.readById(ROLE_ID));
    }

    @Test
    public void shouldUpdateRole() throws NullEntityReferenceException, EntityNotFoundException {
        when(roleRepository.findById(anyLong())).thenReturn(Optional.of(expectedRole));
        Mockito.doReturn(expectedRole).when(roleRepository).save(expectedRole);

        Role actualRole = roleService.update(expectedRole);

        verify(roleRepository).save(any(Role.class));
        verify(roleRepository).findById(anyLong());
        Assertions.assertEquals(expectedRole, actualRole);
        Assertions.assertEquals(expectedRole.getId(), actualRole.getId());
    }

    @Test
    public void shouldThrowNullExceptionUpdateRole() {
        Exception exception = assertThrows(NullEntityReferenceException.class, () -> roleService.update(null));

        Assertions.assertEquals("Role cannot be 'null'", exception.getMessage());
        verifyNoInteractions(roleRepository);
    }

    @Test
    public void shouldNotUpdateRole() {
        when(roleRepository.findById(anyLong())).thenReturn(Optional.empty());

        when(roleRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(NoSuchElementException.class, () -> roleService.update(expectedRole));

        verify(roleRepository, times(0)).save(any(Role.class));
    }

    @Test
    public void shouldDeleteRole() throws EntityNotFoundException {
        when(roleRepository.findById(anyLong())).thenReturn(Optional.of(expectedRole));

        roleService.delete(ROLE_ID);

        verify(roleRepository).findById(anyLong());
        verify(roleRepository).delete(any(Role.class));
    }

    @Test
    public void shouldThrowExceptionDeleteRole() {
        when(roleRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> roleService.delete(ROLE_ID));

        verify(roleRepository).findById(anyLong());
        verify(roleRepository, times(0)).delete(any(Role.class));
    }

    @Test
    public void shouldReturnListGetAll() {
        when(roleRepository.findAll()).thenReturn(Collections.emptyList());

        List<Role> actual = roleService.getAll();

        Assertions.assertEquals(0, actual.size());
        verify(roleRepository).findAll();
    }

    @Test
    public void shouldReturnListRoleGetAll() {
        List<Role> rolesExpected = Arrays.asList(new Role(), new Role());
        when(roleRepository.findAll()).thenReturn(rolesExpected);

        List<Role> actualRoles = roleService.getAll();

        Assertions.assertEquals(rolesExpected.size(), actualRoles.size());
        verify(roleRepository).findAll();
    }

}

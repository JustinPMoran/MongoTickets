package coms309.Accounts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class AccountControllerTest {

    @InjectMocks
    private AccountController accountController;

    @Mock
    private AccountRepository accountRepository;


    private Account mockAccount;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock Account object
        mockAccount = new Account();
        mockAccount.setId(1);
        mockAccount.setUsername("testUser");
        mockAccount.setPassword("password");
        mockAccount.setEmail("test@example.com");
    }

    @Test
    void testGetAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        accounts.add(mockAccount);

        when(accountRepository.findAll()).thenReturn(accounts);

        List<Account> result = accountController.getAllAccounts();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("testUser", result.get(0).getUsername());
        verify(accountRepository, times(1)).findAll();
    }

    @Test
    void testCreateUsers() {
        when(accountRepository.save(mockAccount)).thenReturn(mockAccount);

        String response = accountController.createUsers(mockAccount);

        assertEquals("{\"message\":\"success\"}", response);
        verify(accountRepository, times(1)).save(mockAccount);
    }

    @Test
    void testDeleteUsers() {
        doNothing().when(accountRepository).deleteAll();

        String response = accountController.deleteUsers();

        assertEquals("{\"message\":\"success\"}", response);
        verify(accountRepository, times(1)).deleteAll();
    }

    @Test
    void testGetUserById() {
        when(accountRepository.findById(1)).thenReturn(mockAccount);

        Account result = accountController.getUserById(1);

        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        verify(accountRepository, times(1)).findById(1);
    }

    @Test
    void testUpdateUser() {
        Account updatedAccount = new Account();
        updatedAccount.setId(1);
        updatedAccount.setUsername("updatedUser");

        when(accountRepository.findById(1)).thenReturn(mockAccount);
        when(accountRepository.save(updatedAccount)).thenReturn(updatedAccount);

        String response = accountController.updateUser(1, updatedAccount);

        assertEquals("{\"message\":\"success\"}", response);
        verify(accountRepository, times(1)).findById(1);
        verify(accountRepository, times(1)).save(updatedAccount);
    }


    @Test
    void testGetAccountByEmail() {
        when(accountRepository.findByEmail("test@example.com")).thenReturn(mockAccount);

        Account result = accountController.getAccountByEmail("test@example.com");

        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        verify(accountRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void testLoginByEmail() {
        when(accountRepository.findByEmail("test@example.com")).thenReturn(mockAccount);

        int response = accountController.loginByEmail("test@example.com", "password");

        assertEquals(1, response);
        verify(accountRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void testForgotPassword() {
        when(accountRepository.findByEmail("test@example.com")).thenReturn(mockAccount);

        accountController.forgotPassword("test@example.com", "newPassword");

        assertEquals("newPassword", mockAccount.getPassword());
        verify(accountRepository, times(1)).findByEmail("test@example.com");
        verify(accountRepository, times(1)).save(mockAccount);
    }

    @Test
    void testChangePassword() {
        when(accountRepository.findByEmail("test@example.com")).thenReturn(mockAccount);

        accountController.changePassword("test@example.com", "password", "newPassword");

        assertEquals("newPassword", mockAccount.getPassword());
        verify(accountRepository, times(1)).findByEmail("test@example.com");
        verify(accountRepository, times(1)).save(mockAccount);
    }

    @Test
    void testChangeUsername() {
        when(accountRepository.findByEmail("test@example.com")).thenReturn(mockAccount);

        accountController.changeUsername("test@example.com", "newUsername");

        assertEquals("newUsername", mockAccount.getUsername());
        verify(accountRepository, times(1)).findByEmail("test@example.com");
        verify(accountRepository, times(1)).save(mockAccount);
    }
}

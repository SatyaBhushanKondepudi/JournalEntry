package com.satyabhushan.journalapp.service;

import com.satyabhushan.journalapp.entity.JournalEntry;
import com.satyabhushan.journalapp.entity.User;
import com.satyabhushan.journalapp.exceptions.JournalEntryNotFoundException;
import com.satyabhushan.journalapp.exceptions.UserNotFoundWithGivenUserName;
import com.satyabhushan.journalapp.repository.JournalEntryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JournalEntryServiceTest {

    @InjectMocks
    private JournalEntryService journalEntryService ;

    @Mock
    private UserService userService ;

    @Mock
    private JournalEntryRepository journalEntryRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void testGetAllJournalEntries() {
        when(journalEntryRepository.findAll()).thenReturn(new ArrayList<JournalEntry>());
        assertEquals(0, journalEntryService.getAll().size());
    }

    @Test
    void testFindById() {
        when(journalEntryRepository.findById(ArgumentMatchers.any()))
                .thenReturn(java.util.Optional.of(new JournalEntry()));
        assertNotNull(journalEntryService.findById(null));
    }

    @Test
    void testDeleteByIdSuccess() throws UserNotFoundWithGivenUserName {
        User user = User.builder()
                .userName("Satya")
                .password("password")
                .journalEntries(Arrays.asList())
                .build();
        when(userService.findByUserName(ArgumentMatchers.any())).thenReturn(user);
        when(journalEntryRepository.existsById(ArgumentMatchers.any())).thenReturn(true);
        when(userService.saveEntry(ArgumentMatchers.any())).thenReturn(user);
        doNothing().when(journalEntryRepository).deleteById(ArgumentMatchers.any());
        assertTrue(journalEntryService.deleteById(null, "Satya"));
    }

    @Test
    void testDeleteByIdFalse_JournalEntryNotFoundException() throws JournalEntryNotFoundException, UserNotFoundWithGivenUserName {
        User user = User.builder()
                .userName("Satya")
                .password("password")
                .journalEntries(Arrays.asList())
                .build();
        when(userService.findByUserName(ArgumentMatchers.any())).thenReturn(user);
        when(journalEntryRepository.existsById(ArgumentMatchers.any())).thenReturn(false);
        when(userService.saveEntry(ArgumentMatchers.any())).thenReturn(user);
        doNothing().when(journalEntryRepository).deleteById(ArgumentMatchers.any());
        assertThrows(JournalEntryNotFoundException.class, () -> journalEntryService.deleteById(null, "Satya"));
    }

    @Test
    void testSaveEntrySuccess() {
        JournalEntry journalEntry = new JournalEntry();
        journalEntryService.saveEntry(journalEntry);
        verify(journalEntryRepository, times(1)).save(journalEntry);
    }

    @Test
    void testSaveEntryNullJournalEntry() {
        assertThrows(RuntimeException.class, () -> journalEntryService.saveEntry(null));
    }

//    @Test
//    void testDeleteByIdFalse() {
//        when(journalEntryRepository.existsById(ArgumentMatchers.any())).thenReturn(false);
//        assertFalse(journalEntryService.deleteById(null, null));
//    }
//
//    @Test
//    void testSaveEntry() throws UserNotFoundWithGivenUserName {
//        when(userService.findByUserName(ArgumentMatchers.any())).thenReturn(User.builder().build());
//        when(journalEntryRepository.save(ArgumentMatchers.any())).thenReturn(new JournalEntry());
//        assertNotNull(journalEntryService.saveEntry(new JournalEntry(), ""));
//    }
//
//    @Test
//    void testSaveEntryUserNotFound() throws UserNotFoundWithGivenUserName {
//        when(userService.findByUserName(ArgumentMatchers.any())).thenReturn(null);
//        assertThrows(UserNotFoundWithGivenUserName.class, () -> journalEntryService.saveEntry(new JournalEntry(), ""));
//    }
//
//    @Test
//    void testSaveEntryException_RTEWhenFindByUserName() throws UserNotFoundWithGivenUserName {
//        when(userService.findByUserName(ArgumentMatchers.any())).thenThrow(new RuntimeException());
//        assertThrows(RuntimeException.class, () -> journalEntryService.saveEntry(new JournalEntry(), ""));
//    }
//
//    @Test
//    void testSaveEntryException_RTEWhenSavingJournalEntry() throws UserNotFoundWithGivenUserName {
//        when(userService.findByUserName(ArgumentMatchers.any())).thenReturn(User.builder().build());
//        when(journalEntryRepository.save(ArgumentMatchers.any())).thenThrow(new RuntimeException());
//        assertThrows(RuntimeException.class, () -> journalEntryService.saveEntry(new JournalEntry(), ""));
//    }
//
//    @Test
//    void testSaveEntryException_WhenSavingUserEntry() throws UserNotFoundWithGivenUserName {
//        when(userService.findByUserName(ArgumentMatchers.any())).thenReturn(User.builder().build());
//        when(journalEntryRepository.save(ArgumentMatchers.any())).thenReturn(new JournalEntry());
//        when(userService.saveEntry(ArgumentMatchers.any())).thenThrow(new RuntimeException());
//        assertThrows(RuntimeException.class, () -> journalEntryService.saveEntry(new JournalEntry(), ""));
//    }
//
//    @Test
//    void testSaveEntryException_RTEInSaveEntryMethodWhenAllmethodsAreSuccess() throws UserNotFoundWithGivenUserName {
//        when(userService.findByUserName(ArgumentMatchers.any())).thenReturn(User.builder().build());
//        when(journalEntryRepository.save(ArgumentMatchers.any())).thenReturn(new JournalEntry());
//        when(userService.saveEntry(ArgumentMatchers.any())).thenReturn(User.builder().build());
//        assertThrows(RuntimeException.class, () -> journalEntryService.saveEntry(new JournalEntry(), ""));
//    }
//
//    @Test
//    void testSaveEntryException_UserNotFoundWithGivenUserNameInSaveEntryMethodWhenAllmethodsAreSuccess() throws UserNotFoundWithGivenUserName {
//        when(userService.findByUserName(ArgumentMatchers.any())).thenReturn(User.builder().build());
//        when(journalEntryRepository.save(ArgumentMatchers.any())).thenReturn(new JournalEntry());
//        when(userService.saveEntry(ArgumentMatchers.any())).thenReturn(User.builder().build());
//        assertThrows(UserNotFoundWithGivenUserName.class, () -> journalEntryService.saveEntry(new JournalEntry(), ""));
//    }
//
//    @Test
//    void testSaveEntryException_UserNotFoundWithGivenUserNameInSaveEntryMethodWhenFindByUserNameThrowsException() throws UserNotFoundWithGivenUserName {
//        when(userService.findByUserName(ArgumentMatchers.any())).thenThrow(new UserNotFoundWithGivenUserName(""));
//        assertThrows(UserNotFoundWithGivenUserName.class, () -> journalEntryService.saveEntry(new JournalEntry(), ""));
//    }
//
//
//    @Test
//    void testDeleteByIdException() throws UserNotFoundWithGivenUserName {
//        when(userService.findByUserName(ArgumentMatchers.any())).thenReturn(User.builder().build());
//        when(journalEntryRepository.existsById(ArgumentMatchers.any())).thenReturn(true);
//        doThrow(new RuntimeException()).when(journalEntryRepository).deleteById(ArgumentMatchers.any());
//        assertThrows(RuntimeException.class, () -> journalEntryService.deleteById(null, ""));
//    }
//
//    @Test
//    void testDeleteByIdException_UserNotFoundWithGivenUserName() throws UserNotFoundWithGivenUserName {
//        when(userService.findByUserName(ArgumentMatchers.any())).thenReturn(User.builder().build());
//        when(journalEntryRepository.existsById(ArgumentMatchers.any())).thenReturn(true);
//        doThrow(new UserNotFoundWithGivenUserName("")).when(userService).saveEntry(ArgumentMatchers.any());
//        assertThrows(UserNotFoundWithGivenUserName.class, () -> journalEntryService.deleteById(null, ""));
//    }
//
//    @Test
//    void testDeleteByIdException_UserNotFoundWithGivenUserNameInSaveEntryMethod() throws UserNotFoundWithGivenUserName {
//        when(userService.findByUserName(ArgumentMatchers.any())).thenReturn(User.builder().build());
//        when(journalEntryRepository.existsById(ArgumentMatchers.any())).thenReturn(true);
//        doThrow(new UserNotFoundWithGivenUserName("")).when(userService).saveEntry(ArgumentMatchers.any());
//        assertThrows(UserNotFoundWithGivenUserName.class, () -> journalEntryService.deleteById(null, ""));
//    }
//
//    @Test
//    void testDeleteByIdException_UserNotFoundWithGivenUserNameInSaveEntryMethodWhenExistsByIdReturnsFalse() throws UserNotFoundWithGivenUserName {
//        when(userService.findByUserName(ArgumentMatchers.any())).thenReturn(User.builder().build());
//        when(journalEntryRepository.existsById(ArgumentMatchers.any())).thenReturn(false);
//        doThrow(new UserNotFoundWithGivenUserName("")).when(userService).saveEntry(ArgumentMatchers.any());
//        assertThrows(UserNotFoundWithGivenUserName.class, () -> journalEntryService.deleteById(null, ""));
//    }



}
package com.satyabhushan.journalapp.advices;

import com.satyabhushan.journalapp.dtos.ErrorDto;
import com.satyabhushan.journalapp.exceptions.JournalEntryNotFoundException;
import com.satyabhushan.journalapp.exceptions.UserAlreadyExists;
import com.satyabhushan.journalapp.exceptions.UserNotFoundWithGivenUserName;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler
    public ResponseEntity<ErrorDto> handleUserNotFoundWithGivenUserName(
            UserNotFoundWithGivenUserName userNotFoundWithGivenUserName
    ){
        ErrorDto errorDto = new ErrorDto();
        errorDto.setMessage(userNotFoundWithGivenUserName.getMessage());
        return new ResponseEntity<>(errorDto , HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDto> handleUserAlreadyExistsWithGivenUserName(
            UserAlreadyExists userAlreadyExists
    ){
        ErrorDto errorDto = new ErrorDto();
        errorDto.setMessage(userAlreadyExists.getMessage());
        return new ResponseEntity<>(errorDto , HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDto> handleJournalEntryNotFoundException(JournalEntryNotFoundException journalEntryNotFoundException){
        ErrorDto errorDto = new ErrorDto();
        errorDto.setMessage(journalEntryNotFoundException.getMessage());
        return new ResponseEntity<>(errorDto , HttpStatus.NOT_FOUND);
    }
}

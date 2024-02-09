package GiftsBackend.Execptions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@RestControllerAdvice
public class ControllerAdvice {

    AtomicReference<ErrorClass> errorClassAtomicReference = new AtomicReference<>();
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String,String>> handleConstraintViolationException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();

        Map<String,String> errorMap = new HashMap<>();

        constraintViolations.forEach(constraintViolation -> {
            errorMap.put(constraintViolation.getPropertyPath().toString(),
                    constraintViolation.getMessage());
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
    }






    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorClass> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        ErrorClass errorClass = buildErrorResponse(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage(), "400");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorClass);


//        ex.getBindingResult().getAllErrors().forEach((error) -> {
//
//        ErrorClass errorClass = buildErrorResponse(error.getDefaultMessage(),"400");
//        });
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err)
    }



    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public  ResponseEntity<ErrorClass> UserNotFoundException(UserNotFoundException ex){
       ErrorClass errorClass = buildErrorResponse(ex.getMessage(),"404");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorClass);
    }



    @ResponseStatus(HttpStatus.IM_USED)
    @ExceptionHandler(DuplicateUserExeption.class)
    public ResponseEntity<ErrorClass> handleDuplicateUserException(DuplicateUserExeption ex) {
        ErrorClass errorClass = buildErrorResponse(ex.getMessage(), "409");
        return ResponseEntity.status(HttpStatus.IM_USED).body(errorClass);
    }


    private ErrorClass buildErrorResponse(String errorMessage, String errorCode) {
        ErrorClass errorClass = new ErrorClass();
        errorClass.setErrorMessage(errorMessage);
        errorClass.setErrorCode(errorCode);
        return errorClass;
    }

}





//@ResponseStatus(HttpStatus.BAD_REQUEST)
//@ExceptionHandler(MethodArgumentNotValidException.class)
//public Map<String, String> handleValidationExceptions(
//        MethodArgumentNotValidException ex) {
//    Map<String, String> errors = new HashMap<>();
//    ex.getBindingResult().getAllErrors().forEach((error) -> {
//        String fieldName = ((FieldError) error).getField();
//        String errorMessage = error.getDefaultMessage();
//        errors.put(fieldName, errorMessage);
//    });
//    return errors;
//}

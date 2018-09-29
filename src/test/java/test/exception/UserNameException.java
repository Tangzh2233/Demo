package test.exception;

/**
 * Created by Administrator on 2017/8/29.
 */
public class UserNameException extends RuntimeException{
    public UserNameException(String message){
        super(message);
    }
}

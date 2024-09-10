package theChillys.chillys_radio.exception;

public class StationNotFoundException extends RuntimeException{
    public StationNotFoundException(String message) {
        super(message);
    }
}

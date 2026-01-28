package wa.communicationmanager.exceptions.email

class MailNotSentException( cause: Throwable? = null): RuntimeException("Error during message send", cause)
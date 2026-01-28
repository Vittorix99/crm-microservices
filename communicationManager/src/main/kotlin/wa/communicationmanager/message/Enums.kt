package wa.communicationmanager.message

enum class  MessageStatus{
    RECEIVED, READ, PROCESSING, DONE, FAILED, DISCARDED
}

enum class Channel {
    PHONECALL, TEXTMESSAGE, EMAIL
}

enum class Priority {
    LOW, MEDIUM, HIGH
}
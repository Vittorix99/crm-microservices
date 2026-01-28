package wa.communicationmanager.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class EmailDto (
    @JsonProperty()
    var sender : String?,
    @JsonProperty()
    var recipient : String?,
    @JsonProperty()
    var subject: String?,
    @JsonProperty()
    var body: String?,
    )
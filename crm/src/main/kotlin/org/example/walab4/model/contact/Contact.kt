package org.example.walab4.model.contact

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.*
import org.example.walab4.model.contact.*
import org.example.walab4.model.message.Message

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class ContactCategory {
    RECRUITER, CUSTOMER, PROFESSIONAL, UNKNOWN
}
 @Entity
 @Inheritance(strategy = InheritanceType.SINGLE_TABLE)
 @DiscriminatorColumn(name = "category", discriminatorType = DiscriminatorType.INTEGER)
 open class Contact (
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Column(insertable=false, updatable=false)
     open var id: Long = 0,
     open var name: String?,
     open var surname: String?,
     open var ssnCode: String?,
     @Column(insertable=false, updatable=false)
     open var category: ContactCategory = ContactCategory.UNKNOWN,

     @OneToMany(cascade = [CascadeType.ALL], mappedBy = "sender")
     open var messages: MutableList<Message>?,

     @ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE], fetch = FetchType.LAZY)
    @JoinTable(name="contact_email",
               joinColumns = [JoinColumn(name="contact_id")],
               inverseJoinColumns = [JoinColumn(name="email_id")])
    open var emails: MutableList<Email>?,

     @ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE], fetch = FetchType.LAZY)
    @JoinTable(name="contact_address",
               joinColumns = [JoinColumn(name="contact_id")],
               inverseJoinColumns = [JoinColumn(name="address_id")])
    open var addresses: MutableList<Address>?,

     @ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE], fetch = FetchType.LAZY)
    @JoinTable(name="contact_telephone",
               joinColumns = [JoinColumn(name="contact_id")],
               inverseJoinColumns = [JoinColumn(name="telephone_id")])
    open var telephoneNumbers: MutableList<Telephone>?



){



     override fun toString(): String {
         return "Contact(name='$name', surname='$surname', ssnCode='$ssnCode', " +
                 "emails=${emails?.joinToString { it.email }}, " +
                 "telephoneNumbers=${telephoneNumbers?.joinToString { it.number }}"+
                 "address=${addresses?.joinToString { it.name }})"
     }

}
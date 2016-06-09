package pl.com.solution.good.messages.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="messages")
public class MessageEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="type", length=1)
    @NotNull
    private String type; 
    
    @Column(name="status", length=1)
    @NotNull
    private String status; 
    
    @Column(name="address", length=1024)
    @NotNull
    private String address;  
    
    @Column(name="title", length=1024)
    @NotNull
    private String title;
    
    @Lob 
    @Column(name="body", length=4096)
    private String body;
    
    @Version
    private Integer version;
	
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    @Column(name="create_date")
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="modification_date")
    private Date modificationDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModificationDate() {
		return modificationDate;
	}

	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreateDate() {
		return createDate;
	}

	@Override
	public String toString() {
		return "MessageEntity [id=" + id + ", type=" + type + ", status=" + status + ", address=" + address + ", title="
				+ title + ", body=" + body + ", version=" + version + ", createDate=" + createDate
				+ ", modificationDate=" + modificationDate + "]";
	}	
}

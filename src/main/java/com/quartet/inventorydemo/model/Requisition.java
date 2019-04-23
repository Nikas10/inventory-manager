package com.quartet.inventorydemo.model;

import com.quartet.inventorydemo.util.DateIsAfterProvidedDate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@ApiModel(description = "This entity/form represents request of user for providing him some inventory positions")
@DateIsAfterProvidedDate(baseProvidedDateFieldName = "creationDate", testDateFieldName = "dueDate")
@Entity(name = "Requisition")
@Table(name = "quartet_requisition", schema = "public")
public class Requisition {

    @ApiModelProperty(hidden = true)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "pg-uuid")
    @GenericGenerator(name = "pg-uuid", strategy = "uuid2",
            parameters = @org.hibernate.annotations.Parameter(
                    name = "uuid_gen_strategy_class",
                    value = "com.quartet.inventorydemo.repository.PostgreSQLUUIDGenerationStrategy"))
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private UUID id;

    @ApiModelProperty(hidden = true)
    @NotNull(message = "Account should be not null")
    @JoinColumn(name = "account_id")
    @ManyToOne(optional = false)
    private Account account;

    @ApiModelProperty(position = 1, required = false, notes = "Performer to whom this requisition is assign to")
    @JoinColumn(name = "assignedto_account_id")
    @ManyToOne(optional = true)
    private Account assignedtoAccount;

    @ApiModelProperty(position = 2, required = true, notes = "Creation date")
    @NotNull(message = "Status must be not null")
    @Column(name = "status", nullable = false)
    private String status;

    @ApiModelProperty(position = 3, required = true, notes = "Creation date")
    @NotNull(message = "Creation date can not be null")
    @PastOrPresent(message = "Provide correct date. It can not be future then now")
    @Column(name = "creation_date", nullable = false)
    private Date creationDate;

    @ApiModelProperty(position = 4, required = true, notes = "Due date")
    @NotNull(message = "Due date can not be null")
    @Column(name = "due_date", nullable = false)
    private Date dueDate;

    @ApiModelProperty(position = 5, notes = "Request description")
    @NotNull(message = "Description can not be null")
    @Column(name = "description", nullable = false)
    private String description;

    @ApiModelProperty(position = 6, required = true, notes = "Required positions")
    @OneToMany(mappedBy = "requisition", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Requisition_InventoryPosition> requisitionInventoryPositions;

    private Requisition() {
    }

    public Requisition(@NotNull(message = "Account must be not null") Account account,
                       Account assignedtoAccount,
                       @NotNull(message = "Status must be not null") String status,
                       @NotNull(message = "Creation date can not be null") @PastOrPresent(message = "Provide correct date. It can not be future then now") Date creationDate,
                       @NotNull(message = "Due date can not be null") Date dueDate,
                       @NotNull(message = "Description can not be null") String description) {
        this.account = account;
        this.assignedtoAccount = assignedtoAccount;
        this.status = status;
        this.creationDate = creationDate;
        this.dueDate = dueDate;
        this.description = description;
    }

    public Requisition(@NotNull(message = "Account must be not null") Account account,
                       @NotNull(message = "Status must be not null") String status,
                       @NotNull(message = "Creation date can not be null") @PastOrPresent(message = "Provide correct date. It can not be future then now") Date creationDate,
                       @NotNull(message = "Due date can not be null") Date dueDate,
                       @NotNull(message = "Description can not be null") String description) {
        this.account = account;
        this.assignedtoAccount = null;
        this.status = status;
        this.creationDate = creationDate;
        this.dueDate = dueDate;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Requisition)) return false;
        Requisition that = (Requisition) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(account, that.account) &&
                assignedtoAccount.equals(that.assignedtoAccount) &&
                status.equals(that.status) &&
                creationDate.equals(that.creationDate) &&
                dueDate.equals(that.dueDate) &&
                description.equals(that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(account, assignedtoAccount, status, creationDate, dueDate, description);
    }

    public UUID getId() {
        return id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(@NotNull(message = "Account must be not null") Account account) {
        this.account = account;
    }

    public Account getAssignedtoAccount() {
        return assignedtoAccount;
    }

    public void setAssignedtoAccount(Account assignedtoAccount) {
        this.assignedtoAccount = assignedtoAccount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(@NotNull(message = "Status must be not null") String status) {
        this.status = status;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(@NotNull(message = "Creation date can not be null") @PastOrPresent(message = "Provide correct date. It can not be future then now") Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(@NotNull(message = "Due date can not be null") Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(@NotNull(message = "Description can not be null") String description) {
        this.description = description;
    }

    public void setRequisitionInventoryPositions(Set<Requisition_InventoryPosition> requisitionInventoryPositions) {
        this.requisitionInventoryPositions = requisitionInventoryPositions;
    }
}

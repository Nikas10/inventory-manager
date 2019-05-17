<template>
  <c-default-page :storage="storage">
    <b-container>
      <h1>Requisition</h1>

      <b-form>
        <b-form-group label="Description:">
          <b-form-textarea
            id="description"
            :disabled="!changesAllowed"
            v-model="forms.requisition.description"
            required
            placeholder="Description"
          ></b-form-textarea>
        </b-form-group>

        <b-form-group label="Creator:">
          <b-form-input
            id="creator"
            disabled
            v-model="forms.requisition.login"
            required
            placeholder="Creator"
          ></b-form-input>
        </b-form-group>

        <b-form-group label="Holder:">
          <b-form-select
            :disabled="!isNew"
            v-model="forms.requisition.holderUUID"
            :options="holdersOptions"
          ></b-form-select>
        </b-form-group>

        <b-form-group label="Assigned To:">
          <b-form-select
            :disabled="!changeOfAssignedAllowed"
            v-model="forms.requisition.assignedTo"
            :options="assignedToOptions"
          ></b-form-select>
        </b-form-group>

        <b-form-group label="Status:">
          <b-form-input
            id="status"
            disabled
            v-model="forms.requisition.status"
            required
            placeholder="Status"
          ></b-form-input>
        </b-form-group>
        <b-form-group label="Due Date:">
          <b-form-input
            id="dueDate"
            :disabled="!changesAllowed"
            v-model="dueDate"
            type="date"
            required
          ></b-form-input>
        </b-form-group>

        <b-form-group label="Creation Date:">
          <b-form-input id="creationDate" disabled v-model="creationDate" type="date" required></b-form-input>
        </b-form-group>
      </b-form>

      <b-form-group>
        <b-button v-if="approveAllowed" @click="updateRequisition('APPROVED')">Approve</b-button>

        <b-button
          v-if="clarificationAllowed"
          @click="updateRequisition('REQUIRED_CLARIFICATION')"
        >Require Clarification</b-button>

        <b-button v-if="rejectAllowed" @click="updateRequisition('REJECTED')">Reject</b-button>

        <b-button v-if="completeAllowed" @click="updateRequisition('COMPLETED')">Complete</b-button>

        <b-button
          v-if="completeChangeAllowed"
          @click="updateRequisition('REVIEW_NEEDED')"
        >Complete Changes</b-button>

        <b-button v-if="createAllowed" @click="createRequisition">Create</b-button>

        <b-button v-if="updateAllowed" @click="updateRequisition()">Update</b-button>

        <b-button @click="updatePositions">TEST</b-button>
      </b-form-group>
      <h2>Requested Positions</h2>

      <b-form inline>
        <b-form-input
          :disabled="!forms.position.id"
          v-model="forms.position.amount"
          type="number"
          min="1"
        ></b-form-input>
        <b-col>
          <b-form-select
            :disabled="!forms.position.id"
            v-model="forms.position.id"
            :options="positionOptions"
          ></b-form-select>
        </b-col>
        <b-button
          variant="primary"
          :disabled="!forms.position.id"
          @click="addNewPosition"
        >Add position</b-button>
      </b-form>

      <br>

      <b-table small :items="newPositions" :fields="positionsFields">
        <template slot="amount" slot-scope="data">
          <b-form-input
            :disabled="!changesAllowed"
            v-model="data.item.amount"
            size="sm"
            type="number"
            min="1"
          ></b-form-input>
        </template>

        <template slot="actions" slot-scope="data">
          <b-button size="sm" @click="removePosition(data.item)">Remove</b-button>
        </template>
      </b-table>
    </b-container>
  </c-default-page>
</template>

<script>
module.exports = {
  components: {
    "c-default-page": httpVueLoader("js/components/c-default-page.vue")
  },
  props: ["storage"],
  data: function() {
    return {
      forms: {
        requisition: {
          description: "",
          login: "",
          status: "NEW",
          creationDate: Date.now(),
          dueDate: new Date().setDate(new Date().getDate() + 14),
          assigned: "",
          holderName: "",
          holderUUID: ""
        },
        position: {
          amount: 1,
          id: "0000-0001"
        }
      },
      positionsFields: {
        amount: {
          label: "Amount",
          sortable: true
        },
        name: {
          label: "Name",
          sortable: true
        },
        actions: {
          label: "Actions",
          sortable: true
        }
      },
      orig: {
        requisition: {}
      },
      staff: [],
      positions: [],
      newPositions: [],
      availablePositions: [],
      availableHolders: []
    };
  },
  computed: {
    dueDate: {
      get: function() {
        return formatDate(this.forms.requisition.dueDate);
      },
      set: function(value) {
        this.forms.requisition.dueDate = unformatDate(value);
      }
    },
    creationDate: {
      get: function() {
        return formatDate(this.forms.requisition.creationDate);
      },
      set: function(value) {
        this.forms.requisition.creationDate = unformatDate(value);
      }
    },
    isStaff: function() {
      if (!this.storage.user) {
        return false;
      }

      return ["staff", "admin"].includes(this.storage.user.role);
    },
    reviewNeeded: function() {
      return this.forms.requisition.status == "REVIEW_NEEDED";
    },
    requiredClarification: function() {
      return this.forms.requisition.status == "REQUIRED_CLARIFICATION";
    },
    approved: function() {
      return this.forms.requisition.status == "APPROVED";
    },
    isNew: function() {
      return this.forms.requisition.status == "NEW";
    },
    changeOfAssignedAllowed: function() {
      return (
        this.isStaff &&
        (this.reviewNeeded || this.requiredClarification || this.approved)
      );
    },
    needsClarification: function() {
      return this.forms.requisition.status == "REQUIRED_CLARIFICATION";
    },
    changesAllowed: function() {
      if (!this.storage.user) {
        return false;
      }

      return (
        this.isNew ||
        ((this.reviewNeeded || this.needsClarification) && this.isStaff)
      );
    },
    updateAllowed: function() {
      return (this.requiredClarification || this.reviewNeeded) && this.isStaff;
    },
    approveAllowed: function() {
      return this.reviewNeeded && this.isStaff;
    },
    rejectAllowed: function() {
      return this.reviewNeeded && this.isStaff;
    },
    clarificationAllowed: function() {
      return this.reviewNeeded && this.isStaff;
    },
    completeAllowed: function() {
      return this.isStaff && this.approved;
    },
    completeChangeAllowed: function() {
      return this.requiredClarification;
    },
    createAllowed: function() {
      return this.isNew;
    },
    positionOptions: function() {
      return this.availablePositions.map(function(pos) {
        return { value: pos, text: pos.name };
      });
    },
    holdersOptions: function() {
      return this.availableHolders.map(function(holder) {
        return { value: holder.id, text: holder.name };
      });
    },
    assignedToOptions: function() {
      const options = this.staff.map(function(staff) {
        return { value: staff.login, text: staff.login };
      });

      const defaultOption = { value: "", text: "None" };

      return [defaultOption, ...options];
    }
  },
  methods: {
    loadPage: async function() {
      if (this.$route.params.id == "new") {
        this.forms.requisition.login = this.storage.user.login;
      } else {
        this.loadPositions();
        await this.loadRequisition();
      }

      if (this.isStaff) {
        this.loadStaff();
      }

      await this.loadHolders();
      await this.loadAvailablePositions();
    },
    loadRequisition: async function() {
      const self = this;
      const requisitonId = this.$route.params.id;

      return this.$server
        .get("/requisitions/" + requisitonId)
        .then(function(response) {
          self.orig.requisition = response.data;
          self.forms.requisition = deepClone(response.data);
        });
    },
    loadHolders: async function() {
      const self = this;
      const username = this.storage.user.login;

      if (this.$route.params.id == "new") {
        return this.$server
          .get("/accounts/" + username + "/holders/")
          .then(function(response) {
            self.availableHolders = response.data;
            self.forms.requisition.holderUUID = self.availableHolders[0].id;
          });
      } else {
        this.availableHolders = [
          {
            id: this.forms.requisition.holderUUID,
            name: this.forms.requisition.holderName
          }
        ];
        return Promise.resolve();
      }
    },
    loadStaff: async function() {
      const self = this;

      return this.$server.get("/accounts/").then(function(response) {
        //self.positions = response.data;
        //self.newPositions = deepClone(self.positions);
        self.staff = response.data.filter(function(user) {
          return ["staff", "admin"].includes(user.role);
        });
      });
    },
    loadPositions: async function() {
      const self = this;
      const requisitonId = this.$route.params.id;

      await this.$server
        .get("/requisitions/" + requisitonId + "/positions/")
        .then(function(response) {
          self.positions = response.data;
          self.newPositions = deepClone(self.positions);
        });
    },
    loadAvailablePositions: async function() {
      const self = this;
      const holderId = this.forms.requisition.holderUUID;

      return this.$server
        .get("/holders/" + holderId + "/availablePositions/")
        .then(function(response) {
          self.availablePositions = response.data;
          self.forms.position.id = self.availablePositions[0];
        });
    },
    updateRequisition: function(status) {
      const self = this;
      const requisitonId = this.$route.params.id;

      let req = { ...objDiff(this.orig.requisition, this.forms.requisition) };

      if (status) {
        req = { ...req, status: status };
      }

      this.$server
        .patch("/requisitions/" + requisitonId, req)
        .then(function(response) {
          self.$router.go();
        });
    },
    createRequisition: function() {
      const self = this;

      const req = {
        ...this.forms.requisition,
        inventoryPositions: this.newPositions
      };

      this.$server.post("/requisitions/", req).then(function(response) {
        self.$router.push("/requisitions/" + response.data.id);
      });
    },
    addNewPosition: function() {
      // TODO сделать копию positions, чтобы потом по разнице определять какие запросы нужно отправить
      const newPosition = {
        amount: this.forms.position.amount,
        name: this.forms.position.id.name,
        id: this.forms.position.id.id
      };

      this.newPositions = this.newPositions.filter(function(pos) {
        return pos.id != newPosition.id;
      });

      this.newPositions.push(newPosition);
    },
    removePosition: function(position) {
      const index = this.newPositions.indexOf(position);

      this.newPositions.splice(index, 1);
    },
    updatePositions: async function() {
      const self = this;

      const created = this.newPositions.filter(function(newPosition) {
        return (
          self.positions.findIndex(function(oldPosition) {
            return oldPosition.id == newPosition.id;
          }) == -1
        );
      });

      const deleted = this.positions.filter(function(oldPosition) {
        return (
          self.newPositions.findIndex(function(newPosition) {
            return oldPosition.id == newPosition.id;
          }) == -1
        );
      });

      const updated = this.newPositions.filter(function(newPosition) {
        return (
          self.positions.findIndex(function(oldPosition) {
            return (
              newPosition.id == oldPosition.id &&
              newPosition.amount != oldPosition.amount
            );
          }) != -1
        );
      });

      await Promise.all(updated.map(this.changePosition));
      await Promise.all(created.map(this.createPosition));
      await Promise.all(deleted.map(this.deletePosition));

      this.positions = deepClone(this.newPositions);
    },
    deletePosition: async function(position) {
      const requisitionId = this.$route.params.id;

      return this.$server.delete(
        "/requisitions/" + requisitionId + "/positions/" + position.id
      );
    },
    createPosition: async function(position) {
      const requisitionId = this.$route.params.id;

      return this.$server.post(
        "/requisitions/" + requisitionId + "/positions/",
        position
      );
    },
    changePosition: async function(position) {
      const requisitionId = this.$route.params.id;

      return this.$server.patch(
        "/requisitions/" + requisitionId + "/positions/" + position.id,
        position
      );
    }
  },
  mounted: function() {
    this.loadPage();
  },
  watch: {
    $route: function(to, from) {
      this.loadPage();
    },
    "forms.requisition.holderUUID": function(to, from) {
      //this.loadHolders();

      if (this.$route.params.id == "new") {
        this.loadAvailablePositions();
        this.newPositions = [];
      }
    }
  }
};
</script>
